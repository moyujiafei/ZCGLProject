package org.lf.utils.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 统一异常处理，记录日志
 * 
 * @author sunwill
 *
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory
			.getLogger(MyHandlerExceptionResolver.class);

	/**
	 * 记录异常日志
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String ip = getRemortIP(request);
		String msg = "访问ip地址为：" + ip + ": ";
		logger.error(msg, ex);// 把异常信息记入日志
		return null;
	}

	/**
	 * 获取用户ip地址
	 * 
	 * @param request
	 * @return
	 */
	public String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

}
