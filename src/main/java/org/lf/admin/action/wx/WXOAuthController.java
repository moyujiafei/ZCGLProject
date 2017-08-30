package org.lf.admin.action.wx;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.wx.WXOAuthService;
import org.lf.utils.AjaxResultModel;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信网页授权控制器
 * 
 * @author sunwill
 *
 */
@Controller
@RequestMapping("/wx/oauth/")
public class WXOAuthController {
	/**
	 * session中存放用户信息的key值
	 */
	public static final String WX_LOGIN_USER = "wx_login_user";

	@Autowired
	private WXOAuthService wXOAuthService;

	/**
	 * 验证token
	 * 
	 * @param rwid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("checkToken.do")
	@ResponseBody
	public AjaxResultModel checkToken(HttpServletRequest request, String appId) {
		return wXOAuthService.checkToken(request, appId);
	}

	/**
	 * 初始化授权界面
	 * 
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("toOAuth.do")
	@ResponseBody
	public void toOAuth(String appId, HttpServletResponse response) throws IOException {
		wXOAuthService.getCode(appId, response, "/wx/oauth/getOAuthInfo.do");
	}

	/**
	 * 网络授权完成后，生成token
	 */
	@RequestMapping("getOAuthInfo.do")
	public String toTodayUI(String code, String state, Integer appId) {
		return wXOAuthService.getRedirectUrl(code, state, appId);
	}

	@RequestMapping("toOAuthTest.do")
	public String toOAuthTest(Integer appId, String testToken) {
		// TODO 阿里云测试用 正式上线删除
		String toWxClient = "redirect:" + ZCGLProperties.URL_WX_CLIENT;//
		// 授权完成后,重定向到前端界面
		if (!StringUtils.isEmpty(testToken)) {
			toWxClient += ("?doOauth=1&token=" + testToken + "&appId=" + appId);
		}
		return toWxClient;
	}
}
