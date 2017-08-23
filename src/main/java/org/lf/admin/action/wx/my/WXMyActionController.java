package org.lf.admin.action.wx.my;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.my.WXMyActionService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/action/")
public class WXMyActionController extends WXBaseController {

	@Autowired
	private WXMyActionService actionService;

	/**
	 * 获取用户状态列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("getVZTList.do")
	@ResponseBody
	public AjaxResultModel getVZTList(HttpServletRequest request, Integer rows, Integer page) {

		try {
			Integer appId = getAppId(request);
			String jlr = getUserId(request);
			return actionService.getVZTList(appId, jlr, rows, page);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
}
