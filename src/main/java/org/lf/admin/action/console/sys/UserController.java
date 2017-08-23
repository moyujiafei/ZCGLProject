package org.lf.admin.action.console.sys;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuRole;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.RoleService;
import org.lf.admin.service.sys.UserService;
import org.lf.admin.service.sys.WXAppService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.lf.utils.servlet.LoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理控制层
 * 
 * @author sunwill
 *
 */
@Controller
@RequestMapping("/console/user/")
public class UserController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String ROOT = "/console/sys";
	private static final String IS_WX_LOGIN = "is_wx_login";
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private WXAppService wXAppService;

	/* ===============================用户登录==================================== */
	/**
	 * 登录界面初始化
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping("loginUI.do")
	public String loginUI(HttpSession session,Model m) {
		session.removeAttribute(LoginInterceptor.IS_SUPER_ADMIN);
		m.addAttribute("appTreeInfo", wXAppService.getWxAppCombo());
		session.setAttribute(IS_WX_LOGIN, IS_WX_LOGIN);
		return ROOT + "/loginUI";
	}
	
	/**
	 * 超级管理员登录界面初始化
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping("adminLoginUI.do")
	public String superAdminloginUI(HttpSession session) {
		session.setAttribute(LoginInterceptor.IS_SUPER_ADMIN, true);
		session.setAttribute(IS_WX_LOGIN, IS_WX_LOGIN);
		return ROOT + "/adminLoginUI";
	}
	
	/**
	 * 获取微信登录二维码所需的信息
	 * @param session
	 * @param appId
	 * @return
	 */
	@RequestMapping("getQrcodeImg.do")
	@ResponseBody
	public JSONObject getQrcodeImg(HttpSession session, Integer appId) {
		String flag = (String) session.getAttribute(IS_WX_LOGIN);
		String state = Math.random() + "";
		session.setAttribute(UserService.WX_LOGIN_STATE, state);
		return userService.getCorpInfo(flag, appId, state);
	}
	
	/**
	 * 企业微信登录，成功后重定向到home
	 * @param response
	 * @param session
	 * @param code
	 * @param state
	 * @param appId
	 */
	@RequestMapping("wxLogin.do")
	@ResponseBody
	public void wxLogin(HttpServletResponse response,HttpSession session, String code, String state, Integer appId) {
		userService.wxLogin(response,session,code,state,appId);
	}
	
	@RequestMapping("bindWxUserUI.do")
	public String bindWxUserUI(HttpSession session, Model m) {
		Integer validFlag = 0;
		if (!StringUtils.isEmpty((String) session.getAttribute(UserService.BIND_WXUSER_NAME))) {
			validFlag = 1;// 有效用户
		}
		m.addAttribute("validFlag", validFlag);
		return ROOT + "/bindWxUserUI";
	}
	
	/**
	 * 绑定微信账号到对应的管理后台账号
	 * @param session
	 * @param uname
	 * @param upw
	 * @return
	 */
	@RequestMapping("bindWxUser.do")
	@ResponseBody
	public String bindWxUser(HttpSession session, String uname, String upw) {
		String result = null;
		try {
			userService.bindWxUser(session, uname, upw);
			result = SUCCESS;
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}
	
	//=======================================================================
	

	/**
	 * 登录验证
	 * 
	 * @param uname
	 * @param upw
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("login.do")
	@ResponseBody
	public String login(String uname, String upw, Integer appId, String isSuperAdmin, HttpSession session) {
		if ("true".equals(isSuperAdmin)) {
			session.setAttribute(LoginInterceptor.IS_SUPER_ADMIN, Boolean.parseBoolean(isSuperAdmin));
		}
		String result = null;
		try {
			ChuUser user = userService.getUser(uname, upw, appId, (Boolean) session.getAttribute(LoginInterceptor.IS_SUPER_ADMIN));
			userService.saveLoginInfo(session, user);
			result = SUCCESS;
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}

	/**
	 * 修改密码页面初始化
	 */
	@RequestMapping("updatePwdUI.do")
	public String updatePwdUI() {
		return ROOT + "/updatePwd";
	}

	/**
	 * 进行修改用户密码操作
	 */
	@RequestMapping("updatePwd.do")
	@ResponseBody
	public String updatePwd(Integer uid, String oldpwd, String newpwd) {
		try {
			userService.changePasswdByUid(uid, oldpwd, newpwd);
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}

	/**
	 * 返回显示用户信息的对话框
	 */
	@RequestMapping("currUser.do")
	public String curruser() {
		return ROOT + "/currUser";
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("quit.do")
	public String quit(HttpSession session) {
		// 重定向到登录界面
		Boolean is_super_admin = (Boolean) session.getAttribute(LoginInterceptor.IS_SUPER_ADMIN);
		session.invalidate();
		if (is_super_admin == null || !is_super_admin) {
			return "redirect:loginUI.do";
		} else {
			return "redirect:adminLoginUI.do";
		}
	}

	/* ===============================用户管理==================================== */

	/**
	 * 初始化用户列表
	 * 
	 * @return
	 */
	@RequestMapping("userListUI.do")
	public String userListUI() {
		return ROOT + "/user_manage/userListUI";
	}

	/**
	 * 查找所有的用户信息
	 * 
	 * @param rows
	 * @param page
	 * @return 一个EasyuiDatagrid的类地对象，改对象有page,rows这两个属性
	 */
	@RequestMapping("userList.do")
	@ResponseBody
	public EasyuiDatagrid<ChuUser> userList(int rows, int page) {
		return userService.getPageUserList(rows, page);
	}

	/* ===========================新增用户=========================== */
	/**
	 * 新增用户
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("insertUserUI.do")
	public String insertUserUI(Model m) {
		m.addAttribute("default_passwd4Admin", UserService.DEFAULT_PASSWD);
		return ROOT + "/user_manage/insertUserUI";
	}

	/**
	 * 获得角色下拉框
	 * 
	 * @return
	 */
	@RequestMapping("getRoleCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getRoleCombo() {
		ChuRole role = new ChuRole();
		role.setTybz(0);// 停用标识为0
		return roleService.getRoleCombo(role);
	}

	/**
	 * 获得企业号信息下拉框
	 * 
	 * @return
	 */
	@RequestMapping("getCorpInfoCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getCorpInfoCombo() {
		return wXAppService.getWxAppCombo();
	}

	/**
	 * 检验用户的用户名是否重复,返回一个页面参数success(json类型) success :1表示验证通过 success :0表示验证失败
	 * 
	 * @param newName
	 * @param originalName
	 */
	@RequestMapping("checkUname.do")
	@ResponseBody
	public Boolean checkUname(String newName, String originalName) {
		return userService.checkUname(newName, originalName);
	}

	/**
	 * 新增用户表单提交
	 * 
	 * @param newuser
	 */
	@RequestMapping("insertUserInfo.do")
	@ResponseBody
	public String insertUserInfo(ChuUser newUser, HttpSession session) {
		try {
			Boolean isSuperAdmin = (Boolean) session.getAttribute(LoginInterceptor.IS_SUPER_ADMIN);
			ChuUser loginUser = (ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
			userService.insertUser(newUser, isSuperAdmin, loginUser == null ? null : loginUser.getAppId());
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}

	/* =============================编辑用户================================== */
	/**
	 * 编辑用户初始化界面
	 * 
	 * @param uid
	 * @param m
	 * @return
	 */
	@RequestMapping("updateUserUI.do")
	public String updateUserUI(Integer uid, Model m) {
		try {
			m.addAttribute("currUser4Edit", userService.getUser(uid));
		} catch (OperException e) {
			logger.error("读取用户信息失败", e);
		}
		return ROOT + "/user_manage/updateUserUI";
	}

	/**
	 * 编辑用户表单提交
	 * 
	 * @param newuser
	 */
	@RequestMapping("updateUserInfo.do")
	@ResponseBody
	public String updateUserInfo(ChuUser newUser, String originalUname, HttpSession session) {
		try {
			Boolean isSuperAdmin = (Boolean) session.getAttribute(LoginInterceptor.IS_SUPER_ADMIN);
			ChuUser loginUser = (ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
			userService.updateUser(newUser, originalUname, isSuperAdmin, loginUser == null ? null : loginUser.getAppId());
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}

	/* ==============================删除用户==================================== */
	/**
	 * 删除用户
	 * 
	 * @return
	 */
	@RequestMapping("deleteUserInfo.do")
	@ResponseBody
	public String deleteUserInfo(Integer uid) {
		try {
			userService.delUser(uid);
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}

	/* ===================================重置密码================================= */

	/**
	 * 重置密码
	 * 
	 * @param uname
	 * @return
	 */

	@RequestMapping("resetPassword.do")
	@ResponseBody
	public String resetPassword(Integer uid) {
		try {
			userService.resetPasswd(uid);
			return SUCCESS;
		} catch (OperException e) {
			return e.getErrCode().getMsg();
		}
	}
}
