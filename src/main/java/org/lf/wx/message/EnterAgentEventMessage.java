package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 成员进入应用的事件推送
 * 本事件在成员进入企业号的应用时触发，如果企业需要接收此事件，请打开应用回调模式中的相应开关。
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class EnterAgentEventMessage extends MenuEventMessage {

	public static final String EVENT_KEY = "EventKey";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	private String eventKey; // 事件KEY值，与自定义菜单接口中KEY值对应

	/**
	 * 扫码推事件且弹出“消息接收中”提示框的事件推送
	 */
	public EnterAgentEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
	}

	public String getEventKey() {
		return eventKey;
	}

	/**
	 * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>1408091189</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[enter_agent]]></Event>
	 * <EventKey><![CDATA[]]></EventKey>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */

	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), eventKey,agentID);
	}
}
