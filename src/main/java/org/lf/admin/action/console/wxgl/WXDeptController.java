package org.lf.admin.action.console.wxgl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.utils.EasyuiTree;
import org.lf.utils.servlet.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信应用有关，包括微信用户管理、微信部门管理、微信标签管理。
 * 
 */
@Controller
@RequestMapping("/console/wxgl/wxdept/")
public class WXDeptController {
	private static final String ROOT = "/console/wxgl/wxdept";
	@Autowired
	private WXDeptService deptService;

	/**
	 * 查询微信部门
	 * @return
	 */
	@RequestMapping("queryWXDeptUI.do")
	public String queryWXDeptUI(@RequestParam(required=false,defaultValue="false")boolean isEdit,Model model) {
		model.addAttribute("isEdit", isEdit);
		return ROOT + "/queryWXDeptUI";
	}
	
	/**
	 * 根据appId，查询Chu_WXDept表，构建完成的微信部门树。
	 * @param session
	 * @return
	 */
	@RequestMapping("getWXDeptTree.do")
	@ResponseBody
	public List<EasyuiTree> getWXDeptTree(HttpSession session) {
		ChuUser user=(ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
		return deptService.getWXDeptTree(user.getAppId());
	}
	
	/**
	 * 返回用户选中的部门
	 * @param id
	 * @return
	 */
	@RequestMapping("queryWXDept.do")
	@ResponseBody
	public ChuWXDept queryWXDept(Integer id) {
		return deptService.getChuWXDeptByPrimaryKey(id); 
	}

}
