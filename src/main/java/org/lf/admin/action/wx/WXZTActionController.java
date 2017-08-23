package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.admin.service.wx.WXZCService;
import org.lf.admin.service.wx.WXZTActionService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/zczt/")
public class WXZTActionController extends WXBaseController {

	@Autowired
	private WXZTActionService ztService;
	
	@Autowired
	private WXMediaService wxMediaService;

	@Autowired
	private WXZCService wxzcService;
	
	@Autowired
	private ZCDJService zcdjService;
	

	/**
	 * 返回前台poppicker需要的资产管理部门数据
	 * 资产存放地点
	 * 
	 * @return
	 */
	@RequestMapping("getZCGLPicker.do")
	@ResponseBody
	public AjaxResultModel getZCGLPicker(HttpServletRequest request) {

		try {
			Integer appId = getAppId(request);
			return ztService.getZCGLPicker(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}

	/**
	 * 提交资产调拨请求
	 * 
	 * @return
	 */
	@RequestMapping("allocateZC.do")
	@ResponseBody
	public AjaxResultModel allocateZC(HttpServletRequest request, Integer zcId, Integer zcglId, String cfdd) {

		try {
			String djr = getUserId(request);
			return ztService.allocateZC(djr, zcId, zcglId, cfdd);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}

	}

	/**
	 * 返回前台poppicker需要的房间三级联动数据
	 * 资产存放地点
	 * 
	 * @return
	 */
	@RequestMapping("getFJPicker.do")
	@ResponseBody
	public AjaxResultModel getFJPicker(HttpServletRequest request) {
		try {
			Integer appId = getAppId(request);
			return ztService.getFJPicker(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}

	/**
	 * 根据房间id返回存放地点详细信息
	 * 在三级联动选择完毕后，显示这里的信息
	 * 
	 * @return
	 */
	@RequestMapping("getCFDDByFjid.do")
	@ResponseBody
	public AjaxResultModel getCFDDByFjid(Integer fjId) {
		return ztService.getCFDDByFjid(fjId);
	}

	/**
	 * 根据资产id返回存放地点详细信息
	 * 主要用于展示现在的存放地点
	 * 
	 * @return
	 */
	@RequestMapping("getCFDDByZcid.do")
	@ResponseBody
	public AjaxResultModel getCFDDByZcid(Integer zcId) {
		return ztService.getCFDDByZcid(zcId);
	}

	/**
	 * 根据资产id返回管理部门
	 * 
	 * @param zcid
	 * @return
	 */
	@RequestMapping("getGLBMByZcid.do")
	@ResponseBody
	public AjaxResultModel getGLBMByZcid(Integer zcId) {
		return ztService.getGLBMByZcid(zcId);
	}

	/**
	 * 拒绝归还资产
	 * 
	 * @param request
	 * @param zcid
	 * @param refuseRemark
	 * @return
	 */
	@RequestMapping("refuseRevertZC.do")
	@ResponseBody
	public AjaxResultModel refuseRevertZC(HttpServletRequest request, Integer zcId, String refuseRemark) {
		try {
			Integer appId = getAppId(request);
			String cjr = getUserId(request);
			return ztService.refuseRevertZC(appId, cjr, zcId, refuseRemark);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}

	/**
	 * 资产重新调拨
	 * 
	 * @param request
	 * @param zcid
	 * @param zcglId
	 * @param cfdd
	 * @return
	 */
	@RequestMapping("reallocateZC.do")
	@ResponseBody
	public AjaxResultModel reallocateZC(HttpServletRequest request, Integer zcId, Integer zcglId, String cfdd) {
		try {
			String djr = getUserId(request);
			return ztService.reallocateZC(djr, zcId, zcglId, cfdd);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 申请归还资产
	 * 
	 * @param request
	 * @param zcId   资产Id
	 * @param applyReason 申请原因
	 * @return
	 */
	@RequestMapping("revertZC.do")
	@ResponseBody
	public AjaxResultModel revertZC(HttpServletRequest request, Integer zcId, String applyReason){
		try {
			String czr = getUserId(request);
			return ztService.revertZC(zcId,czr,applyReason.trim());
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 获取资产使用人：二级Popup-picker控件
	 * 第一级：部门名
	 * 第二级：用户名
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getUserPicker.do")
	@ResponseBody
	public AjaxResultModel getUserPicker(HttpServletRequest request){
		Integer appId;
		try {
			appId = getAppId(request);
			return ztService.getUserPicker(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	/**
	 * 资产分配
	 * 
	 * @param request
	 * @param zcId
	 * @param syr   使用人 
	 * @return
	 */
	@RequestMapping("assignZC.do")
	@ResponseBody 
	public AjaxResultModel assignZC(HttpServletRequest request,Integer zcId, String syr){
		try {
			String czr = getUserId(request);
			return ztService.assignZC(zcId,czr,syr);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 获取该zcId下原资产使用人
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("getOldZCSYR.do")
	@ResponseBody
	public AjaxResultModel getOldZCSYR(HttpServletRequest request,Integer zcId){
		Integer appId;
		try {
			appId = getAppId(request);
			return ztService.getUserByZcid(appId,zcId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 资产重新分配
	 * 
	 * @param request
	 * @param zcId
	 * @param syr   新的使用人
	 * @param remark  重新分配原因
	 * @return
	 */
	@RequestMapping("reassignZC.do")
	@ResponseBody
	public AjaxResultModel reassignZC(HttpServletRequest request,Integer zcId, String syr, String remark) { 
		try {
			String czr = getUserId(request);
			return ztService.reassignZC(zcId, czr, syr, remark);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 同意上交资产
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("agreeSendbackZC.do")
	@ResponseBody
	public AjaxResultModel agreeSendbackZC(HttpServletRequest request,Integer zcId) {
		try {
			Integer appId = getAppId(request);
			String cjr = getUserId(request);
			return ztService.agreeSendbackZC(appId,zcId,cjr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 拒绝上交资产 
	 * 
	 * @param request
	 * @param refuseRemark   拒绝原因 
	 * @param zcid
	 * @return
	 */
	@RequestMapping("refuseSendbackZC.do")
	@ResponseBody
	public AjaxResultModel refuseSendbackZC(HttpServletRequest request,String refuseRemark,Integer zcId) {
		try {
			Integer appId = getAppId(request);
			String cjr = getUserId(request);
			return ztService.refuseSendbackZC(appId, refuseRemark, cjr, zcId);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 同意领用资产
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("agreeLeadingZC.do")
	@ResponseBody
	public AjaxResultModel agreeLeadingZC(HttpServletRequest request,Integer zcId){
		try {
			String syr = getUserId(request);
			return ztService.agreeLeadingZC(syr,zcId);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 拒绝领用资产
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("refuseLeadingZC.do")
	@ResponseBody
	public AjaxResultModel refuseLeadingZC(HttpServletRequest request,Integer zcId,String refuseRemark) {
		try {
			String syr = getUserId(request);
			return ztService.refuseLeadingZC(syr,zcId,refuseRemark);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 上交资产
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("sendbackZC.do")
	@ResponseBody
	public AjaxResultModel sendbackZC(Integer zcId,String remark) {
		return ztService.sendbackZC(zcId,remark);
	}
	
	/***
	 *  申请维修资产
	 *  
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("zcwxSQ.do")
	@ResponseBody
	public AjaxResultModel zcwxSQ(HttpServletRequest request,Integer zcId,String remark, String imgId,String voiceId) {
		return null;
	}
	
	/*** 
	 * 资产闲置申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("zcxzSQ.do")
	@ResponseBody
	public AjaxResultModel zcxzSQ(HttpServletRequest request,Integer zcId,String remark, String imgId,String voiceId) {
		return null;
	}
	
	/***
	 * 申请报废资产
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("zcbfSQ.do")
	@ResponseBody
	public AjaxResultModel zcbfSQ(HttpServletRequest request,Integer zcId,String remark, String imgId,String voiceId) {
		return null;
	}
	
	/***
	 * 同意资产维修申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("agreeWXSQ.do")
	@ResponseBody
	public AjaxResultModel agreeWXSQ(HttpServletRequest request,Integer zcId) {
		try {
			String cjr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.agreeWXSQ(appId,cjr,zcId);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 拒绝资产维修申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("refuseWXSQ.do")
	@ResponseBody
	public AjaxResultModel refuseWXSQ(HttpServletRequest request,Integer zcId,String refuseRemark) {
		try {
			String cjr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.refuseWXSQ(appId,cjr,zcId,refuseRemark);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 同意资产报废申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("agreeBFSQ.do")
	@ResponseBody
	public AjaxResultModel agreeBFSQ(HttpServletRequest request,Integer zcId,String cfdd) {
		try {
			String spr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.agreeBFSQ(appId,zcId,cfdd,spr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 拒绝资产报废申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("refuseBFSQ.do")
	@ResponseBody
	public AjaxResultModel refuseBFSQ(HttpServletRequest request,Integer zcId,String refuseRemark) {
		try {
			String spr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.refuseBFSQ(appId,zcId,refuseRemark,spr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 同意资产闲置申请
	 * 
	 * @param request
	 * @param zcId
	 * @param cfdd  房间Id号
	 * @return
	 */
	@RequestMapping("agreeXZSQ.do")
	@ResponseBody
	public AjaxResultModel agreeXZSQ(HttpServletRequest request,Integer zcId,String cfdd) {
		try {
			String spr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.agreeXZSQ(appId,zcId,cfdd,spr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 拒绝资产闲置申请
	 * 
	 * @param request
	 * @param zcId
	 * @return
	 */
	@RequestMapping("refuseXZSQ.do")
	@ResponseBody
	public AjaxResultModel refuseXZSQ(HttpServletRequest request,Integer zcId,String refuseRemark) {
		try {
			String spr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.refuseXZSQ(appId,zcId,refuseRemark,spr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/***
	 * 同意报废申请
	 * 
	 * @param request
	 * @param zcId
	 * @param refuseRemark
	 * @return
	 */
	@RequestMapping("agreeBFZC.do")
	@ResponseBody
	public AjaxResultModel agreeBFZC(HttpServletRequest request,Integer zcId,String refuseRemark) {
		try {
			String spr = getUserId(request);
			Integer appId = getAppId(request);
			return ztService.agreeBFZC(appId,zcId,refuseRemark,spr);
		} catch (Exception e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 获取微信前端资产类型poppick
	 * 
	 * @return
	 */
	@RequestMapping("getZCLXPicker.do")
	@ResponseBody
	public AjaxResultModel getZCLXPicker(HttpServletRequest request) {
		try {
			Integer appId = getAppId(request);
			return wxzcService.getZCLXPicker(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 根据资产代码判断是否已登记
	 * 
	 * @return
	 */
	@RequestMapping("isExist.do")
	@ResponseBody
	public AjaxResultModel isExist(HttpServletRequest request, String dm) {
		try {
			Integer appId = getAppId(request);
			return wxzcService.isExist(appId, dm);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 进行资产登记
	 * 
	 * @return
	 */
	@RequestMapping("sumbitZCDJ.do")
	@ResponseBody
	public AjaxResultModel sumbitZCDJ(HttpServletRequest request, String zcdm, String zcmc, Integer zcLx, String cost, Integer num, String xh, String ccbh, String gzsj, String zjnx, String imgId) {
		try {
			Integer appId = getAppId(request);
			String djr = getUserId(request);
			return wxzcService.registerZCDJ(appId, djr, zcdm, zcmc, zcLx, cost, num, xh, ccbh, gzsj, zjnx, imgId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	} 
}
