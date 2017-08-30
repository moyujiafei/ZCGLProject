package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 点击菜单跳转链接的事件推送
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class ViewEventMessage extends MenuEventMessage {
	public static final String EVENT_KEY = "EventKey";
	
	private static final String RECEIVE_TEMPLATE =  "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";	
	
	private String eventKey;   //事件KEY值，设置的跳转URL 

	/**
	 * 点击菜单跳转链接时的事件推送 
	 */
	public ViewEventMessage(Message msg) {
		super(msg);
	}
	
	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
	}
	
	/**
	 * <xml>
	 * <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>123456789</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[view]]></Event>
	 * <EventKey><![CDATA[www.qq.com]]></EventKey>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), event.toString(), eventKey, agentID);
	}
}
