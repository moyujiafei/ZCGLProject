package org.lf.admin.action.wx.zcgl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.wx.WXZCService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信端显示：资产登记相关操作。
 * 
 * 
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/wx/zcgl/")
public class WXZCDJController extends WXBaseController {
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCDJService zcdjService;
	
	@Autowired
	private WXZCService wxzcService;
	
	
	/**
	 * 资产调拨
	 * 
	 * @return
	 */
	@RequestMapping("allocateZC.do")
	@ResponseBody
	public AjaxResultModel allocateZC(Integer zcid, Integer zcglId, String cfdd, HttpServletRequest request) {
		AjaxResultModel result = new AjaxResultModel();
		String djr;
		try {
			djr = getUserId(request);
		} catch (OperException e1) {
			return INVALID_TOKEN;
		}
		
		try {
			zcdjService.allocateZC(zcid, zcglId, cfdd, djr);
			
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 资产重新调拨
	 * 
	 * @return
	 */
	@RequestMapping("reallocateZC.do")
	@ResponseBody
	public AjaxResultModel reallocateZC(Integer zcid, Integer zcglId, String cfdd, HttpServletRequest request) {
		AjaxResultModel result = new AjaxResultModel();
		String djr;
		try {
			djr = getUserId(request);
		} catch (OperException e1) {
			return INVALID_TOKEN;
		}
		
		try {
			zcdjService.reallocateZC(zcid, zcglId, cfdd, djr);
			
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
	 * @return
	 */
	@RequestMapping("refuseRevertZC.do")
	@ResponseBody
	public AjaxResultModel refuseRevertZC(Integer zcid, String refuseRemark, HttpServletRequest request) {
		Integer appId;
		String cjr;
		try {
			appId = getAppId(request);
			cjr = getUserId(request);
		} catch (OperException e1) {
			return INVALID_TOKEN;
		}
		
		List<Integer> zcidList = new ArrayList<>();
		zcidList.add(zcid);
		AjaxResultModel result = new AjaxResultModel();
		try {
			zcdjService.refuseRevertSQ(appId, refuseRemark, cjr, zcidList);
			
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (OperException e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		
		return result;
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
}
