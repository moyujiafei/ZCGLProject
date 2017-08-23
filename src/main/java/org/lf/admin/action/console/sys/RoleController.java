package org.lf.admin.action.console.sys;

import java.util.List;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuRole;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.RoleService;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/role/")
public class RoleController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String ROOT = "/console/sys/role_manage";
	@Autowired
	private RoleService roleService;

	/**
	 * 初始化角色管理界面
	 * 
	 * @return
	 */
	@RequestMapping("roleListUI.do")
	public String roleListUI() {
		return ROOT + "/roleListUI";
	}

	/**
	 * 获得角色列表信息
	 * 
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("roleDatagrid.do")
	@ResponseBody
	public EasyuiDatagrid<ChuRole> roleDatagrid(int rows, int page) {
		return roleService.getPageRoleList(rows, page);
	}

	/**
	 * 初始化添加角色界面
	 * 
	 * @return
	 */
	@RequestMapping("insertRoleUI.do")
	public String insertRoleUI() {
		return ROOT + "/insertRoleUI";
	}

	/**
	 * 获得角色权限菜单书
	 * 
	 * @param role_id
	 * @return
	 */
	@RequestMapping("roleMeunTree.do")
	@ResponseBody
	public List<EasyuiTree> roleMeunTree(Integer role_id) {
		return roleService.getTreeRoleList(role_id, 0);
	}

	/**
	 * 检查角色名称是否重复
	 * 
	 * @param newName
	 * @param originalName
	 * @return
	 */
	@RequestMapping("checkRoleName.do")
	@ResponseBody
	public Boolean checkRoleName(String newName, String originalName) {
		return roleService.checkRoleName(newName,originalName);
	}

	/**
	 * 添加角色
	 * 
	 * @param role
	 */
	@RequestMapping("insertRoleInfo.do")
	@ResponseBody
	public String insertRoleInfo(ChuRole role) {
		try {
			roleService.insertRole(role);
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}

	/**
	 * 初始化编辑角色界面
	 * 
	 * @param currRoleId
	 * @param m
	 * @return
	 */
	@RequestMapping("updateRoleUI.do")
	public String updateRoleUI(Integer currRoleId, Model m) {
		try {
			m.addAttribute("currRoleInfo", roleService.getRole(currRoleId));
		} catch (OperException e) {
			logger.error("获取角色信息出错", e);
		}
		return ROOT + "/updateRoleUI";
	}
	
	/**
	 * 编辑角色
	 * @param role
	 * @return
	 */
	@RequestMapping("updateRoleInfo.do")
	@ResponseBody
	public String updateRoleInfo(ChuRole role,String originalName) {
		try {
			roleService.updateRoleInfo(role,originalName);
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}
}
