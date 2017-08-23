package org.lf.admin.action.console;

import javax.servlet.http.HttpSession;

import org.lf.admin.db.pojo.ChuUser;
import org.lf.utils.servlet.LoginInterceptor;

/**
 * 控制层公共方法类
 *
 */
public class BaseController {
	/**
	 * 操作成功时的返回值
	 */
	public static final String SUCCESS = "success";

	/**
	 * 获得当前用户的appid
	 * 
	 * @param session
	 * @return
	 */
	public Integer getAppId(HttpSession session) {
		ChuUser user = getCurrUser(session);
		return user == null ? null : user.getAppId();
	}

	/**
	 * 获得当前用户信息
	 * 
	 * @param session
	 * @return
	 */
	public ChuUser getCurrUser(HttpSession session) {
		return (ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
	}

}
