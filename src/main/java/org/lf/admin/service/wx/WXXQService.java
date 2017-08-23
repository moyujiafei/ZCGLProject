package org.lf.admin.service.wx;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.LZTXZMapper;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.logs.ZTService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.zcgl.RWLX;
import org.lf.admin.service.zcgl.RWService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXXQService {
	@Autowired
	private LZTXZMapper lztxzDao;
	
	@Autowired
	private WXUserService userService;
	@Autowired
	private RWService rwService;
	@Autowired
	private ZCService zcService;
	@Autowired
	private WXZCService wxzcService;
	@Autowired
	private ZTService ztService;

	/**
	 * 用户详情
	 * 
	 * @param appId
	 * @param userId
	 * @return
	 */
	public AjaxResultModel userXQ(String appIdStr, String userId) {
		AjaxResultModel result = new AjaxResultModel();
		if (StringUtils.isEmpty(appIdStr) || StringUtils.isEmpty(userId)) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
			return result;
		}
		try {
			Integer appId = Integer.parseInt(appIdStr);
			WXUser user = userService.getWXUser(appId, userId);
			if (user != null) {
				result.setCode(WXResultCode.SUCCESS.getCode());
				result.setMsg(WXResultCode.SUCCESS.getMsg());
				result.setData(user);
			}
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(WXResultCode.ERROR.getMsg());
		}
		return result;
	}
	
	/**
	 * 根据id获得任务信息（微信端）
	 * @param rwid
	 * @return
	 */
	public AjaxResultModel getWXRW(Integer rwid) {
		AjaxResultModel result = new AjaxResultModel();
		if(rwid != null){
			JRW jrw = rwService.getRW(rwid);
			if (jrw != null) {
				Integer lx = jrw.getLx();
				if (lx != null) {
					jrw.setLxmc(RWLX.valueOf(lx).name());
				}
				result.setCode(WXResultCode.SUCCESS.getCode());
				result.setMsg(WXResultCode.SUCCESS.getMsg());
				result.setData(jrw);
				return result;
			}
		}
		result.setCode(WXResultCode.ERROR.getCode());
		result.setMsg(WXResultCode.ERROR.getMsg());
		return result;
	}
	
	/**
	 * 查询任务详情
	 * @param rwid
	 * @param rows
	 * @param page
	 * @param finish
	 * @return
	 */
	public AjaxResultModel getWXRWZC(Integer rwid, Integer rows, Integer page, Integer finish) {
		AjaxResultModel result = new AjaxResultModel();
		String errorMsg = "未查询到相关资产信息";
		if (rwid == null || rows == null || page == null) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(errorMsg);
			return result;
		}
		return wxzcService.getZCList(zcService.getRWZCList(rwid, rows, page, finish), null);
	}

	/**
	 * 根据状态id和文件类型获得文件完整路径
	 * 
	 * @param ztid
	 * @param mediaType
	 * @return
	 */
	public AjaxResultModel getWXZCXZ(Integer ztid, String mediaType) {
		AjaxResultModel result = new AjaxResultModel();
		if (ztid != null && !StringUtils.isEmpty(mediaType)) {
			List<LZTXZ> list = lztxzDao.selectListByZt(ztid, mediaType);
			List<String> data = new ArrayList<String>();
			for (LZTXZ xz : list) {
				String mediaPath = null;
				try {
					// 根据状态细则获得媒体文件完整路径
					mediaPath = ztService.getWXMediaUrl(xz.getAppId(), xz.getJlsj(), xz.getMediaType(), xz.getWxMediaId());
				} catch (OperException e) {
				}
				if (!StringUtils.isEmpty(mediaPath)) {
					data.add(mediaPath);
				}
			}
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setData(data);
			return result;
		}
		result.setCode(WXResultCode.ERROR.getCode());
		result.setMsg(WXResultCode.ERROR.getMsg());
		return result;
	}

	/**
	 * 获取资产详情
	 * @param zcid
	 * @param pageUrl
	 * @return
	 */
	public AjaxResultModel zcXQ(Integer zcid, String pageUrl) {
		// 通过消息查看资产详情
		return wxzcService.getZC(null, null, zcid, pageUrl);
	}

}
