package org.lf.utils.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lf.admin.db.pojo.ChuUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录验证拦截器
 * 
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	/**
	 * 用于在session中存储登录用户信息的key值
	 */
	public static final String LOGIN_INFO = "loginInfo";
	/**
	 * 用于判断当前登录用户是否为超级管理员
	 */
	public static final String IS_SUPER_ADMIN = "is_super_admin";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		// session.getServletContext().getAttribute(request.getRemoteAddr());
		if (session == null) {
			toLoginUI(request, response);
			return false;
		}
		if (session != null) {
			ChuUser user = (ChuUser) session.getAttribute(LOGIN_INFO);
			if (user == null) {
				toLoginUI(request, response);
				return false;
			}
		}
		return true;
	}

	private void toLoginUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 如果是ajax请求响应头会有，x-requested-with；
		if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
			response.getWriter().print("timeout"); // 打印一个返回值，没这一行，无法跳出
		} else {
			response.sendRedirect(request.getContextPath() + "/console/user/loginUI.do");
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
