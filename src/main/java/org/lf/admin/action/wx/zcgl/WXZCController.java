package org.lf.admin.action.wx.zcgl;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.WXZCService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信端显示：资产列表
 * 
 * 不同的用户查看资产列表。
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/wx/zcgl/")
public class WXZCController extends WXBaseController {

	@Autowired
	private WXZCService wxzcService;

	/**
	 * 获得我的资产信息
	 * 
	 * @param tagName
	 * @param pageUrl
	 * @param page
	 * @param rows
	 * @param request
	 * @param zcdm
	 * @param finish
	 * @param tabFlag
	 * @return
	 */
	@RequestMapping("getMyZCList.do")
	@ResponseBody
	public AjaxResultModel getMyZCList(String tagName, Integer page, Integer rows, HttpServletRequest request, String searchParam, Integer finish) {
		Integer appId;
		String userId;
		try {
			appId = getAppId(request);
			userId = getUserId(request);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
		return wxzcService.getMyZCList(tagName, appId, userId, page, rows, searchParam);
	}

	/**
	 * 获得工作相关资产列表
	 * 
	 * @return
	 */
	@RequestMapping("getZCList.do")
	@ResponseBody
	public AjaxResultModel getZCList(String tagName, Integer page, Integer rows, HttpServletRequest request, String searchParam, String tabFlag) {
		Integer appId;
		String userId;
		try {
			appId = getAppId(request);
			userId = getUserId(request);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
		return wxzcService.getZClist(appId, userId, tagName, page, rows, searchParam, tabFlag);
	}

	/**
	 * 资产详情
	 * 
	 * @return
	 */
	@RequestMapping("getZC.do")
	@ResponseBody
	public AjaxResultModel getZC(Integer zcid, String tagName, String pageUrl, HttpServletRequest request) {
		Integer appId;
		try {
			appId = getAppId(request);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
		return wxzcService.getZC(appId, tagName, zcid, pageUrl);
	}

	@RequestMapping("doAction.do")
	@ResponseBody
	public AjaxResultModel doAction(Integer zcid, String action, String tagName, HttpServletRequest request) {
		Integer appId;
		String userId;
		try {
			userId = getUserId(request);
			appId = getAppId(request);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
		return wxzcService.doAction(appId, userId, zcid, action, tagName);
	}
}
