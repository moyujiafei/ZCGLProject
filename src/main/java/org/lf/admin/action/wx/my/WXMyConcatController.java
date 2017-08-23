package org.lf.admin.action.wx.my;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.my.WXMyConcatService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/myconcat/")
public class WXMyConcatController extends WXBaseController {
	
	@Autowired
	private WXMyConcatService concatService;

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
			return concatService.getWXUserListByDeptId(deptId, appId, rows, page);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
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
			return concatService.getWXDeptPickWithAll(appId);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
}
