package org.lf.admin.action.console.rwgl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.service.OperException;
import org.lf.admin.service.logs.MsgLX;
import org.lf.admin.service.logs.MsgService;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.admin.service.sys.WXTagService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/rwgl/msg/")
public class MsgController extends BaseController {
	private final String ROOT = "/console/rwgl/msg";
	
	@Autowired
	private WXDeptService deptService;
	
	@Autowired
	private WXTagService tagService;
	
	@Autowired
	private MsgService msgService;

	@RequestMapping("sendMsgUI.do")
	public String sendMsgUI() {
		return ROOT + "/sendMsgUI";
	}

	/**
	 * 选择部门：下拉列表框，默认为空。
	 * 
	 * @return
	 */
	@RequestMapping("getWXDeptCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getWXDeptCombo(HttpSession session) {
		return deptService.getWXDeptCombo(getAppId(session));
	}
	
	/**
	 * 选择标签：下拉列表框，默认为空。
	 * 
	 * @return
	 */
	@RequestMapping("getWXTagCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getWXTagCombo(HttpSession session) {
		return tagService.getWXTagCombo(getAppId(session));
	}
	
	@RequestMapping("sendMsg.do")
	@ResponseBody
	public String sendMsg(HttpSession session, String deptNo, String tagNo, String nr) throws OperException {
		MsgLX lx = MsgLX.消息通告;
		if(StringUtils.isEmpty(deptNo) && StringUtils.isEmpty(tagNo)){
			return "部门和标签不能同时为空";
		}
		if(!StringUtils.isEmpty(deptNo)){
			msgService.sendDeptMsg(getAppId(session), lx, Integer.parseInt(deptNo), nr);
		}
		if(!StringUtils.isEmpty(tagNo)){
			msgService.sendTagMsg(getAppId(session), lx, Integer.parseInt(tagNo), nr);
		}
		return SUCCESS;
	}
}
