package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.WXChuDeptService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/wxchudept/")
public class WXChuDeptController extends WXBaseController {
	
	@Autowired
	private WXChuDeptService wxChuDeptService;

	
	/**
	 * 获取微信部门列表(包含全部)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("getWXDeptPickWithAll.do")
	@ResponseBody
	public AjaxResultModel getWXDeptPickWithAll(HttpServletRequest request) {

		try {
			Integer appId = getAppId(request);
			return wxChuDeptService.getWXDeptPickWithAll(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 获取微信部门列表(不包含全部)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("getWXDeptPick.do")
	@ResponseBody
	public AjaxResultModel getWXDeptPick(HttpServletRequest request) {

		try {
			Integer appId = getAppId(request);
			return wxChuDeptService.getWXDeptPick(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	

}
