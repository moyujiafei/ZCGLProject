package org.lf.admin.service.wx;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.CZCGLMapper;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.VFJ;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.JZWService;
import org.lf.admin.service.catalog.XQService;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
import org.lf.admin.service.zcgl.GZWXService;
import org.lf.admin.service.zcgl.RCXJService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.admin.service.zcgl.ZCFPService;
import org.lf.admin.service.zcgl.ZCSYService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCYWService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 专门用于提供微信端与资产分配相关的数据展示
 * 
 * @author 
 *
 */

/**
 * @author Administrator
 *
 */
@Service("wxztActionService")
public class WXZTActionService {
	@Autowired
	private ZCFPService zcfpService;

	@Autowired
	private ZCService zcService;

	@Autowired
	private ZCGLService zcglService;

	@Autowired
	private ZCDJService zcdjService;

	@Autowired
	private XQService xqService;

	@Autowired
	private JZWService jzwService;

	@Autowired
	private FJService fjService;

	@Autowired
	private GZWXService gzwxService;

	@Autowired
	private WXDeptService wxDeptService;

	@Autowired
	private WXUserService wxUserService;

	@Autowired
	private ZCSYService zcsyService;

	@Autowired
	private RCXJService rcxjService;

	@Autowired
	private ZCYWService zcywService;

	@Autowired
	private CZCGLMapper czcglDao;
	

	/**
	 * 完成资产调拨
	 * 
	 * @param request
	 * @param zcid
	 * @param deptNo
	 * @param cfdd
	 * @return
	 */
	public AjaxResultModel allocateZC(Integer appId, String djr, Integer zcid, Integer deptNo, String cfdd) {
		AjaxResultModel result = new AjaxResultModel();
		if (cfdd.contains("null")) {
			cfdd = "";
		}
		try {
			CZCGL param = new CZCGL();
			param.setDeptNo(deptNo);
			param.setAppId(appId);
			param = czcglDao.select(param);
			zcdjService.allocateZC(zcid, param.getId(), cfdd, djr);

			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}

		return result;
	}

	/**
	 * 返回前台poppicker需要的房间三级联动数据
	 * 资产存放地点
	 * 联动具体数据格式看官网api
	 * 三级联动第一层加一个空数据 这个空实现需要三个节点
	 * 其他如果最后一级房间没有数据，就不显示给用户看
	 * [[{ 
	 * 	 name: xxx
	 * 	 value: xxx
	 * 	 parent: xxx
	 * }]]
	 * 
	 * @return
	 */
	public AjaxResultModel getFJPicker(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			CZCGL param = new CZCGL();
			param.setAppId(appId);

			List<PickerDataElement> pickList = new ArrayList<PickerDataElement>();

			PickerDataElement firstPickers = new PickerDataElement();
			firstPickers.setName("");
			firstPickers.setParent("0");
			firstPickers.setValue("null1");

			PickerDataElement sencondPickers = new PickerDataElement();
			sencondPickers.setName("");
			sencondPickers.setParent("null1");
			sencondPickers.setValue("null2");

			PickerDataElement thirdPickers = new PickerDataElement();
			thirdPickers.setName("");
			thirdPickers.setParent("null2");
			thirdPickers.setValue("null3");

			pickList.add(firstPickers);
			pickList.add(sencondPickers);
			pickList.add(thirdPickers);

			List<CXQ> xqList = xqService.getXQList(appId);
			if (xqList.size() == 0 || xqList == null) {

			} else {
				for (CXQ cxq : xqList) {
					PickerDataElement cxqPickers = new PickerDataElement();
					cxqPickers.setName(cxq.getXqmc());
					cxqPickers.setValue(cxq.getXqmc());
					cxqPickers.setParent("0");

					CJZW cjzwParam = new CJZW();
					cjzwParam.setXqId(cxq.getId());
					List<CJZW> jzwList = jzwService.getJZWList(cjzwParam);
					if (jzwList.size() == 0 || jzwList == null) {
					} else {
						pickList.add(cxqPickers);
						for (CJZW cjzw : jzwList) {
							PickerDataElement cjzwPickers = new PickerDataElement();
							cjzwPickers.setName(cjzw.getMc());
							cjzwPickers.setValue(cjzw.getMc());
							cjzwPickers.setParent(cxqPickers.getValue());

							VFJ fjParam = new VFJ();
							fjParam.setJzwId(cjzw.getId());
							List<VFJ> fjList = fjService.getFJList(fjParam);
							if (fjList.size() == 0 || fjList == null) {
							} else {
								pickList.add(cjzwPickers);
								for (VFJ vfj : fjList) {
									PickerDataElement vfjPickers = new PickerDataElement();
									vfjPickers.setName(vfj.getRoom());
									vfjPickers.setValue(vfj.getFjId() + "");
									vfjPickers.setParent(cjzwPickers.getValue());
									pickList.add(vfjPickers);
								}
							}
						}
					}
				}
			}

			result.setData(pickList);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据房间id返回存放地点详细信息
	 * 在三级联动选择完毕后，显示这里的信息
	 * 
	 * @param fjid
	 * @return
	 */
	public AjaxResultModel getCFDDByFjid(Integer fjid) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			String cfdd = "";
			if (fjid != null) {
				cfdd = fjService.getCFDD(fjid);
			}
			result.setData(cfdd);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据资产id返回存放地点详细信息
	 * 主要用于展示现在的存放地点
	 * 
	 * @param zcid
	 * @return
	 */
	public AjaxResultModel getCFDDByZcid(Integer zcid) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			VZC param = new VZC();
			param.setZcid(zcid);
			param = zcService.getZC(param);
			String cfdd = "";
			if (param.getCfdd() != null) {
				cfdd = fjService.getCFDD(Integer.parseInt(param.getCfdd()));
			}

			result.setData(cfdd);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据资产id返回管理部门
	 * 
	 * @param zcid
	 * @return
	 */
	public AjaxResultModel getGLBMByZcid(Integer zcid) {

		AjaxResultModel result = new AjaxResultModel();
		try {
			VZC param = new VZC();
			param.setZcid(zcid);
			param = zcService.getZC(param);

			result.setData(param.getDeptName());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}

		return result;
	}

	/**
	 * 完成资产重新调拨
	 * 
	 * @param request
	 * @param zcid
	 * @param zcglId
	 * @param cfdd
	 * @return
	 */
	public AjaxResultModel reallocateZC(Integer appId, String djr, Integer zcid, Integer deptNo, String cfdd) {
		AjaxResultModel result = new AjaxResultModel();
		if (cfdd.contains("null")) {
			cfdd = "";
		}
		try {
			CZCGL param = new CZCGL();
			param.setDeptNo(deptNo);
			param.setAppId(appId);
			param = czcglDao.select(param);
			zcdjService.reallocateZC(zcid, param.getId(), cfdd, djr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}

		return result;
	}

	/**
	 * 拒绝归还资产
	 * 
	 * @param request
	 * @param zcid
	 * @param refuseRemark
	 * @return
	 */
	public AjaxResultModel refuseRevertZC(Integer appId, String cjr, Integer zcid, String refuseReason) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<>();
		zcidList.add(zcid);

		try {
			zcdjService.refuseRevertSQ(appId, refuseReason, cjr, zcidList);

			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}

		return result;
	}

	/**
	 * 提交申请归还资产
	 * 
	 * @param sqyy 申请原因
	 * @return
	 */
	public AjaxResultModel revertZC(Integer zcId, String czr, String applyReason) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcfpService.revertZC(zcId, czr, applyReason);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 提交资产分配
	 * 
	 * @param request
	 * @param userId
	 * @param zcid 资产id
	 * @return
	 */
	public AjaxResultModel assignZC(Integer zcId, String czr, String syr) {
		AjaxResultModel result = null;
		try {
			result = new AjaxResultModel();
			zcfpService.assignZC(zcId, czr, syr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 资产重新分配
	 *
	 * @param request
	 * @param zcid
	 * @param applyReason
	 * @return
	 */
	public AjaxResultModel reassignZC(Integer zcId, String czr, String syr, String remark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcfpService.reassignZC(zcId, czr, syr, remark);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			return result;
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
			return result;
		}

	}

	/**
	 * 根据资产id返回资产使用人
	 * 
	 * @param zcid
	 * @return
	 */
	public AjaxResultModel getUserByZcid(Integer appId, Integer zcid) {
		AjaxResultModel result = new AjaxResultModel();
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZcid(zcid);
		VZC zc = zcService.getZC(param);
		result.setData(zc.getSyrmc());
		result.setCode(WXResultCode.SUCCESS.getCode());
		result.setMsg(WXResultCode.SUCCESS.getMsg());
		return result;
	}

	/**
	 * 拒绝资产上交
	 * 
	 * @param zcid
	 * @param refuseReason
	 * @return
	 */
	public AjaxResultModel refuseSendbackZC(Integer appId, String refuseRemark, String cjr, Integer zcId) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcfpService.refuseSendbackSQ(appId, refuseRemark, cjr, zcidList);
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setData("success");
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 同意上交资产
	 * 
	 * @param zcid
	 * @param refuseReason
	 * @return
	 */
	public AjaxResultModel agreeSendbackZC(Integer appId, Integer zcid, String cjr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcid);
		try {
			zcfpService.agreeSendbackSQ(appId, cjr, zcidList);
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setData("success");
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 同意领用资产
	 * @param syr
	 * @param zcId
	 * @return
	 */
	public AjaxResultModel agreeLeadingZC(String syr, Integer zcId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcsyService.agreeLeadingZC(zcId, syr);
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setData("success");
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 *  拒绝领用资产 
	 * @param syr
	 * @param zcId
	 * @param refuseRemark
	 * @return
	 */
	public AjaxResultModel refuseLeadingZC(String syr, Integer zcId, String refuseRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcsyService.refuseLeadingZC(zcId, syr, refuseRemark);
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setData("success");
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/*** 
	 * 上交资产
	 * @param appId
	 * @param zcId
	 * @param remark
	 */
	public AjaxResultModel sendbackZC(Integer zcId, String remark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcsyService.sendbackZC(zcId, remark);
			result.setMsg(WXResultCode.SUCCESS.getMsg());
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setData("success");
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 同意资产维修申请
	 * @param syr
	 * @param zcId
	 * @param refuseRemark
	 * @return
	 */
	public AjaxResultModel agreeWXSQ(Integer appId, String cjr, Integer zcId) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.agreeWXSQ(appId, cjr, zcidList);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 拒绝资产维修申请
	 * @param syr
	 * @param zcId
	 * @param refuseRemark
	 * @return
	 */
	public AjaxResultModel refuseWXSQ(Integer appId, String cjr, Integer zcId, String refuseRemark) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.refuseWXSQ(appId, refuseRemark, cjr, zcidList);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 同意资产报废申请
	 * @param appId
	 * @param zcId
	 * @param cfdd
	 * @param spr
	 * @return
	 */
	public AjaxResultModel agreeBFSQ(Integer appId, Integer zcId, String cfdd, String spr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.agreeBFSQ(appId, zcidList, cfdd, spr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 拒绝资产报废申请
	 * @param appId
	 * @param zcId
	 * @param refuseRemark
	 * @param spr
	 * @return
	 */
	public AjaxResultModel refuseBFSQ(Integer appId, Integer zcId, String refuseRemark, String spr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.refuseBFSQ(appId, zcidList, refuseRemark, spr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 同意闲置资产
	 * @param appId
	 * @param zcId
	 * @param cfdd   房间Id号
	 * @param spr
	 * @return
	 */
	public AjaxResultModel agreeXZSQ(Integer appId, Integer zcId, String cfdd, String spr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.agreeXZSQ(appId, zcidList, cfdd, spr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 拒绝资产闲置申请 
	 * @param appId
	 * @param zcId
	 * @param refuseRemark
	 * @param spr
	 * @return
	 */
	public AjaxResultModel refuseXZSQ(Integer appId, Integer zcId, String refuseRemark, String spr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.refuseXZSQ(appId, zcidList, refuseRemark, spr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/***
	 * 同意报废资产
	 * @param appId
	 * @param zcId
	 * @param refuseRemark
	 * @param spr 审批人
	 * @return
	 */
	public AjaxResultModel agreeBFZC(Integer appId, Integer zcId, String refuseRemark, String spr) {
		AjaxResultModel result = new AjaxResultModel();
		List<Integer> zcidList = new ArrayList<Integer>();
		zcidList.add(zcId);
		try {
			zcywService.agreeBFZC(appId, zcidList, refuseRemark, spr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
}
