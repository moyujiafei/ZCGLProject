package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.WXChuUserService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/wxchuuser/")
public class WXChuUserController extends WXBaseController {
	@Autowired
	private WXChuUserService wxChuUserService;
	
	/**
	 * 根据部门id返回微信用户列表
	 * 
	 * @param deptId 如果部门id为空则返回全部用户
	 * @param deptId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("getChuWXUserListByDept.do")
	@ResponseBody
	public AjaxResultModel getChuWXUserListByDept(HttpServletRequest request, String deptId, Integer rows, Integer page) {
		try {
			Integer appId = getAppId(request);
			return wxChuUserService.getWXUserListByDeptId(deptId, appId, rows, page);
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
			return wxChuUserService.getUserPicker(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
}
