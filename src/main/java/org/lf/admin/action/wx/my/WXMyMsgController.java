package org.lf.admin.action.wx.my;

import javax.servlet.http.HttpServletRequest;

import org.lf.admin.action.wx.WXBaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.wx.my.WXMyMsgService;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/wx/mymsg/")
public class WXMyMsgController extends WXBaseController {
	
	@Autowired
	private WXMyMsgService wxMyMsgService;
	
	/**
	 * 获取消息列表
	 * 
	 * @param request
	 * @param msgLx 消息类型 空表示全部 其他参数是消息类型对应的
	 * @return
	 */
	@RequestMapping("getMsgList.do")
	@ResponseBody
	public AjaxResultModel getMsgList(HttpServletRequest request, Integer msgLx) {
		try {
			Integer appId = getAppId(request);
			String userid = getUserId(request);
			return wxMyMsgService.getMsgList(appId, msgLx, userid);
		} catch (OperException e) {
			return INVALID_TOKEN;
		}
	}
	
	/**
	 * 获取消息类型poppick列表
	 * 
	 * @return
	 */
	@RequestMapping("getLxPickListWithAll.do")
	@ResponseBody
	public AjaxResultModel getLxPickListWithAll() {
		return wxMyMsgService.getLxPickListWithAll();
	}
}
