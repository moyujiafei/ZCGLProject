package org.lf.utils.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.lf.admin.service.sys.WXAppService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet(loadOnStartup = 2, urlPatterns = { "/wxAppStartServlet" })
public class WXAppStartServlet extends HttpServlet {
	private static final long serialVersionUID = -7350418304971495440L;

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext wac = null;
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		WXAppService appService = (WXAppService) wac.getBean("appService");
		appService.startAppList();
	}

}
