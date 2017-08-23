package org.lf.admin.service.wx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.ChuZTActionMapper;
import org.lf.admin.db.dao.JZCMapper;
import org.lf.admin.db.dao.VZCMapper;
import org.lf.admin.db.dao.VZTMapper;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.db.pojo.ChuZTAction;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.db.pojo.VZT;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.admin.service.wx.vue.panel.VuePanelList;
import org.lf.admin.service.wx.vue.picker.PickerData;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.admin.service.zcgl.ZCFPService;
import org.lf.admin.service.zcgl.ZCSYService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCYWService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.DateUtils;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 专门用于提供微信端与资产相关的数据展示
 * 
 * @author
 *
 */

@Service("wxzcService")
public class WXZCService {
	@Autowired
	private VZTMapper vztDao;
	
	@Autowired
	private VZCMapper vzcDao;

	@Autowired
	private ChuZTActionMapper actionDao;

	@Autowired
	private ZCService zcService;

	@Autowired
	private WXAppService wxAppService;

	@Autowired
	private FJService fjService;

	@Autowired
	private ZCLXService zclxService;

	@Autowired
	private JZCMapper jzcDao;

	@Autowired
	private ZCSYService zCSYService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private WXMediaService wxMediaService;
	
	@Autowired
	private ZCDJService zcdjService;
	
	@Autowired
	private ZCYWService zcywService;
	
	@Autowired
	private ZCFPService zcfpService;
	/**
	 * 用于展示资产列表数据
	 * 
	 * @param countNo
	 * @param map
	 * 
	 * 
	 * @param rwid
	 * @param rows
	 * @param page
	 * @return
	 */
	public AjaxResultModel getZCList(List<VZC> list, Integer countNo) {
		AjaxResultModel result = new AjaxResultModel();

		if (list != null && list.size() > 0) {
			JSONArray zcList = new JSONArray();
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());

			JSONObject jsonObj = null;
			for (VZC vzc : list) {
				VuePanelList vuePanelList = new VuePanelList();
				String picUrl = vzc.getPicUrl();
				if (!StringUtils.isEmpty(picUrl)) {
					vuePanelList.setSrc(zcService.getSmallImgUrl(picUrl, vzc.getImgVersion()));
				}
				vuePanelList.setTitle(StringUtils.nullToEmpty(vzc.getZc()) + " (" + StringUtils.nullToEmpty(vzc.getZcztmc()) + ")");
				String cfddmc = null;
				try {
					cfddmc = fjService.getCFDD(Integer.parseInt(vzc.getCfdd()));
				} catch (Exception e) {
				}
				StringBuilder sb = new StringBuilder();
				sb.append("本资产于").append(DateUtils.toString("yyyy年MM月dd日", vzc.getGzsj()));
				sb.append("购买，目前由").append(StringUtils.nullToEmpty(vzc.getDeptName())).append("的").append(StringUtils.nullToEmpty(vzc.getSyrmc()));
				sb.append("保管，存放在").append(StringUtils.nullToEmpty(cfddmc));
				sb.append("，资产状态为 ").append(StringUtils.nullToEmpty(vzc.getZcztmc()));
				vuePanelList.setDesc(sb.toString());
				jsonObj = vuePanelList.toJson();
				jsonObj.put("zcid", vzc.getZcid());
				zcList.add(jsonObj);
			}
			JSONObject data = new JSONObject();
			data.put("zcList", zcList);// 资产列表
			data.put("countNo", countNo);// 统计数量
			result.setData(data);
		} else {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
		}
		return result;
	}

	/**
	 * 底部按钮依据：用户身份（appId + tagName）和资产状态来决定。  已登记资产（后勤管理员）：删除、调拨 
	 * 未使用资产（后勤管理员）：重新调拨  未使用资产（部门资产管理员）：申请归还、资产分配  归还中资产（后勤管理员）：同意、拒绝 
	 * 领用中资产（部门资产管理员）：重新分配  领用中资产（使用人）：同意、拒绝  上交中资产（部门资产管理员）：同意、拒绝 
	 * 使用中资产（使用人）：上交、申请维修、申请闲置、申请报废  使用中资产（部门资产管理员）：申请维修、申请闲置、申请报废 
	 * 使用中资产（后勤管理员）：创建巡检任务  巡检中资产（巡检员）：设备正常、申请维修、申请闲置、申请报废 
	 * 申请维修资产（后勤管理员）：同意、拒绝  维修中资产（维修人员）：完成维修、申请再次维修、申请闲置、申请报废 
	 * 申请报废资产（后勤管理员）：同意、拒绝  申请闲置资产（后勤管理员）：同意、拒绝  闲置资产（后勤管理员）：报废
	 * 
	 * @param appId
	 * @param tagNo
	 * @param zczt
	 * @return
	 */
	private JSONArray getActionList(Integer appId, String tagName, Integer zcid, Integer zczt) {
		ChuZTAction param = new ChuZTAction();
		param.setAppId(appId);
		param.setTagName(tagName);
		param.setZt(zczt);
		List<ChuZTAction> actionList = actionDao.selectList(param);

		JSONArray array = new JSONArray();
		JSONObject json = null;
		for (ChuZTAction action : actionList) {
			json = new JSONObject();
			json.put("id", action.getId());
			json.put("show-dot", false);
			json.put("badge", "");
			json.put("icon", ZCGLProperties.URL_SERVER + action.getImgUrl());
			json.put("label", action.getActionTitle());
			if (!StringUtils.isEmpty(action.getActionName())) {
				json.put("action", action.getActionName());
			}
			if (!StringUtils.isEmpty(action.getQueryParam())) {
				// 资产编码是页面跳转的主键
				json.put("link", String.format(action.getQueryParam(), zcid));
			}
			array.add(json);
		}

		return array;
	}

	/**
	 * 获取资产详情，包括：资产信息(vzc)，资产状态列表(vztList)，底部操作按钮列表(actionList)
	 * 
	 * @param appId
	 * @param tagName
	 * @param zcid
	 * @param pageUrl
	 * @return
	 */
	public AjaxResultModel getZC(Integer appId, String tagName, Integer zcid, String pageUrl) {
		AjaxResultModel result = new AjaxResultModel();
		if (zcid != null) {
			VZC vzc = vzcDao.selectByZcid(zcid);
			if (vzc != null) {
				if (!StringUtils.isEmpty(vzc.getPicUrl())) {
					vzc.setPicUrl(zcService.getOriginalImgUrl(vzc.getPicUrl(), vzc.getImgVersion()));
				}
				String cfddmc = null;
				try {
					cfddmc = fjService.getCFDD(Integer.parseInt(vzc.getCfdd()));
				} catch (Exception e) {
				}
				vzc.setCfdd(cfddmc);
				// 获得资产状态信息
				JSONObject vzcJson = (JSONObject) JSON.toJSON(vzc);// 转换为json对象
				if (vzc.getAppId() != null && !StringUtils.isEmpty(vzc.getZcdm())) {
					VZT param = new VZT();
					param.setAppId(vzc.getAppId());
					param.setZcdm(vzc.getZcdm());
					List<VZT> ztList = vztDao.selectList(param);
					vzcJson.put("ztList", ztList);
				}
				result.setCode(WXResultCode.SUCCESS.getCode());
				result.setMsg(WXResultCode.SUCCESS.getMsg());
				vzcJson.put("jssdkConfig", wxAppService.getJssdkConfig(vzc.getAppId(), pageUrl));
				if (appId != null && !StringUtils.isEmpty(tagName)) {
					vzcJson.put("actionList", getActionList(appId, tagName, vzc.getZcid(), vzc.getZczt()));
				}
				result.setData(vzcJson);
				return result;
			}
		}
		result.setCode(WXResultCode.ERROR.getCode());
		result.setMsg(WXResultCode.ERROR.getMsg());
		return result;
	}

	/**
	 * 获得我的资产信息
	 * 
	 * @param tagName
	 * @param appId
	 * @param userId
	 * @param pageUrl
	 * @param page
	 * @param rows
	 * @param zcdm
	 * @return
	 */
	public AjaxResultModel getMyZCList(String tagName, Integer appId, String userId, Integer page, Integer rows, String zcdm) {
		AjaxResultModel result = new AjaxResultModel();
		List<VZC> zcList = null;
		try {
			zcList = zcService.getVZCList(appId, zcdm, null, null, null, userId, null, null, new ArrayList<Integer>(), page, rows);
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
			return result;
		}
		return getZCList(zcList, getCountNo(appId, tagName, userId));
	}

	/**
	 * 获取我的工作资产列表
	 * 
	 * @param appId
	 * @param userId
	 * @param tagName
	 * @param page
	 * @param rows
	 * @param zcdm
	 * @param tabFlag
	 * @return
	 */
	public AjaxResultModel getZClist(Integer appId, String userId, String tagName, Integer page, Integer rows, String zcdm,
			String tabFlag) {
		if (StringUtils.isEmpty(zcdm)) {
			zcdm = null;
		}
		AjaxResultModel result = new AjaxResultModel();
		List<VZC> zcList = null;
		try {
			if (TAGNAMES.后勤管理人员组.name().equals(tagName)) {
				List<Integer> zcztList;
				if ("0".equals(tabFlag)) {
					zcztList = getCountZtids(tagName);
				} else {
					zcztList = getQueryZtids(tagName, true);
				}
				zcList = zcService.getVZCList(appId, zcdm, null, null, null, null, null, null, zcztList, page, rows);
			} else if (TAGNAMES.部门资产管理员组.name().equals(tagName)) {
				List<Integer> zcztList;
				if ("0".equals(tabFlag)) {
					zcztList = getCountZtids(tagName);
				} else {
					zcztList = getQueryZtids(tagName, true);
				}
				zcList = zcService.getVZCList(appId, zcdm, null, null, null, null, null, userId, zcztList, page, rows);
			} else if (TAGNAMES.使用人组.name().equals(tagName)) {
				List<Integer> zcztList;
				if ("0".equals(tabFlag)) {
					zcztList = getCountZtids(tagName);
				} else {
					zcztList = getQueryZtids(tagName, true);
				}
				zcList = zcService.getVZCList(appId, zcdm, null, null, null, userId, null, null, zcztList, page, rows);
			} else if (TAGNAMES.巡检组.name().equals(tagName)) {
				Integer zczt = null;
				if ("0".equals(tabFlag)) {
					zczt = ZCZT.巡检中.getValue();
				}
				zcList = zcService.getRWZCList(appId, userId, null, zcdm, null, null, null, null, zczt, null, rows, page);
			} else if (TAGNAMES.维修组.name().equals(tagName)) {
				Integer zczt = null;
				if ("0".equals(tabFlag)) {
					zczt = ZCZT.维修中.getValue();
				}
				zcList = zcService.getRWZCList(appId, userId, null, zcdm, null, null, null, null, zczt, null, rows, page);
			}
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
			return result;
		}
		return getZCList(zcList, getCountNo(appId, tagName, userId));
	}

	/**
	 * 根据标签名 获取统计时的状态id集合
	 * 
	 * @param tagName
	 * @return
	 */
	private List<Integer> getCountZtids(String tagName) {
		return getQueryZtids(tagName, false);
	}

	/**
	 * 根据标签名 获取列表查询时的状态id集合
	 * 
	 * @param tagName
	 *            标签名
	 * @param listFlag
	 *            是否为筛选查询，为false时表示只返回统计时的状态
	 * @return
	 */
	private List<Integer> getQueryZtids(String tagName, boolean listFlag) {
		List<Integer> ztidList = new ArrayList<Integer>();
		if (TAGNAMES.后勤管理人员组.name().equals(tagName)) {
			if (!listFlag) {
				// 统计:已登记、未使用、归还中、申请维修、申请报废、申请闲置
				ztidList.add(ZCZT.已登记.getValue());
				ztidList.add(ZCZT.未使用.getValue());
				ztidList.add(ZCZT.归还中.getValue());
				ztidList.add(ZCZT.申请维修.getValue());
				ztidList.add(ZCZT.申请报废.getValue());
				ztidList.add(ZCZT.申请闲置.getValue());
			}
		} else if (TAGNAMES.部门资产管理员组.name().equals(tagName)) {
			// 统计:资产状态为以下状态的资产数量：未使用、领用中、上交中
			ztidList.add(ZCZT.未使用.getValue());
			ztidList.add(ZCZT.领用中.getValue());
			ztidList.add(ZCZT.上交中.getValue());
			if (listFlag) {
				// 查找v_zc，筛选出管理员为当前用户，资产状态为以下状态的资产列表：未使用、领用中、上交中、归还中、使用中、巡检中、维修中、申请维修、申请闲置、申请报废
				ztidList.add(ZCZT.归还中.getValue());
				ztidList.add(ZCZT.使用中.getValue());
				ztidList.add(ZCZT.巡检中.getValue());
				ztidList.add(ZCZT.维修中.getValue());
				ztidList.add(ZCZT.申请维修.getValue());
				ztidList.add(ZCZT.申请闲置.getValue());
				ztidList.add(ZCZT.申请报废.getValue());
			}
		} else if (TAGNAMES.使用人组.name().equals(tagName)) {
			// 统计出资产状态为以下状态的资产数量：领用中
			if (!listFlag) {
				ztidList.add(ZCZT.领用中.getValue());
			}
		} else {
			throw new IllegalArgumentException("无效的标签名");
		}
		return ztidList;
	}

	/**
	 * 获得待处理工作数量
	 * 
	 * @param appId
	 * @param tagName
	 * @param userId
	 * @return
	 */
	private Integer getCountNo(Integer appId, String tagName, String userId) {
		Integer countNo = null;
		if (TAGNAMES.后勤管理人员组.name().equals(tagName)) {
			countNo = zcService.countVZCList(appId, null, null, null, null, null, null, null, getCountZtids(tagName));
		} else if (TAGNAMES.部门资产管理员组.name().equals(tagName)) {
			countNo = zcService.countVZCList(appId, null, null, null, null, null, null, userId, getCountZtids(tagName));
		} else if (TAGNAMES.使用人组.name().equals(tagName)) {
			countNo = zcService.countVZCList(appId, null, null, null, null, userId, null, null, getCountZtids(tagName));
		} else if (TAGNAMES.巡检组.name().equals(tagName)) {
			countNo = zcService.countRWZCList(appId, userId, null, null, null, null, null, null, ZCZT.巡检中.getValue(), 0);
		} else if (TAGNAMES.维修组.name().equals(tagName)) {
			countNo = zcService.countRWZCList(appId, userId, null, null, null, null, null, null, ZCZT.维修中.getValue(), 0);
		}
		// 如果资产数量大于99，显示99,若为0，则不显示
		if (countNo != null) {
			if (countNo > 99) {
				countNo = 99;
			} else if (countNo == 0) {
				countNo = null;
			}
		}
		return countNo;
	}
	
	/**
	 * 获取微信前端资产类型poppick
	 * 
	 * @return
	 */
	public AjaxResultModel getZCLXPicker(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			CZCLX param = new CZCLX();
			param.setAppId(appId);
			List<CZCLX> zclxLvOneList = zclxService.getLevel1ZCLXList(param);
			List<CZCLX> zclxLvTwoList = zclxService.getLevel2ZCLXList(param);
			PickerData pd = new PickerData();
			List<PickerDataElement> pickList = pd.getElementList();
			for (CZCLX tempLx : zclxLvOneList) {
				PickerDataElement zclxPickers = new PickerDataElement();
				zclxPickers.setName(tempLx.getMc());
				zclxPickers.setValue(tempLx.getLxId());
				zclxPickers.setParent("0");
				pickList.add(zclxPickers);
			}
			for (CZCLX tempLx : zclxLvTwoList) {
				PickerDataElement zclxPickers = new PickerDataElement();
				zclxPickers.setName(tempLx.getMc());
				zclxPickers.setValue(tempLx.getId() + "");
				zclxPickers.setParent(tempLx.getLxPid());
				pickList.add(zclxPickers);
			}
			result.setData(pd.getLinkArray());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据资产代码判断是否已登记
	 * 
	 * @return
	 */
	public AjaxResultModel isExist(Integer appId, String dm) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			JZC param = new JZC();
			param.setAppId(appId);
			param.setDm(dm);
			result.setData(jzcDao.select(param));
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 进行资产登记
	 * 
	 * @return
	 */

	public AjaxResultModel registerZCDJ(Integer appId, String djr, String zcdm, String zcmc, Integer zcLx, String cost, Integer num, String xh, String ccbh,
			String gzsj, String zjnx, String imgId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			String[] ids = imgId.split("###");
			String picUrl = wxMediaService.downloadMediaToPath("/upload/zc_images/" + appId, appService.getAccessToken(appId), ".jpg", ids[0]);
			zcdjService.registZC(appId, zcdm, zcmc, zcLx, new BigDecimal(cost), num, xh, ccbh, new SimpleDateFormat("yyyy-MM-dd").parse(gzsj), new BigDecimal(
					zjnx), djr, picUrl);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 资产详情操作
	 * 
	 * @param appId
	 * @param userId
	 * @param zcid
	 * @param tagName 
	 * @return
	 */
	public AjaxResultModel doAction(Integer appId, String userId, Integer zcid, String action, String tagName) {
		AjaxResultModel result = new AjaxResultModel();
		if (zcid != null && !StringUtils.isEmpty(action)) {
			try {
				if (action.equals("agreeLeadingZC")) {
					zCSYService.agreeLeadingZC(zcid, userId);
				} else if (action.equals("deleteZC")) {
					zcdjService.delRegistedZC(zcid, userId);
				} else if (action.equals("agreeRevertZC")) {
					List<Integer> zcidList = new ArrayList<Integer>();
					zcidList.add(zcid);
					zcdjService.agreeRevertSQ(appId, userId, zcidList);
				} else if (action.equals("agreeWXSQ")) {
					List<Integer> zcidList = new ArrayList<Integer>();
					zcidList.add(zcid);
					zcywService.agreeWXSQ(appId, userId, zcidList);
				} else if (action.equals("agreeSendbackZC")) {
					List<Integer> zcidList = new ArrayList<Integer>();
					zcidList.add(zcid);
					zcfpService.agreeSendbackSQ(appId, userId, zcidList);
				} else {
					throw new IllegalArgumentException("无效的action");
				}
				result.setCode(WXResultCode.SUCCESS.getCode());
				result.setMsg(WXResultCode.SUCCESS.getMsg());
				JSONObject data = new JSONObject();
				data.put("gzCount", getCountNo(appId, tagName, userId));
				result.setData(data);
			} catch (Exception e) {
				result.setCode(WXResultCode.ERROR.getCode());
				result.setMsg(WXResultCode.ERROR.getMsg());
			}
			return result;
		} else {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
			return result;
		}
	}
	

}
