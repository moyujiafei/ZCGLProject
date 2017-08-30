package org.lf.admin.action.wx;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.WXCommonService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/common/")
public class WXCommonController extends WXBaseController {
	@Autowired
	private WXCommonService wxCommonService;

	/**
	 * 返回wxconfig配置
	 * 
	 * @param request
	 * @param url
	 * @return
	 */
	@RequestMapping("getWXConfig.do")
	@ResponseBody
	public AjaxResultModel getWXConfig(HttpServletRequest request, String url) {
		try {
			return wxCommonService.getWxConfig(getAppId(request), url);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}

}
