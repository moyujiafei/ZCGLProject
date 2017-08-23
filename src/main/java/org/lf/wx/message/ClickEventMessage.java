package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 点击菜单拉取消息的事件推送
 * 成员点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报。
 * 另外，扫码、拍照及地理位置的菜单事件，仅支持微信iPhone5.4.1/Android5.4以上版本，
 * 旧版本微信成员点击后将没有回应，开发者也不能正常接收到事件推送。
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class ClickEventMessage extends MenuEventMessage {
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

	private String eventKey; // 事件KEY值，设置的跳转URL

	/**
	 * 点击菜单拉取消息时的事件推送
	 */
	
	public ClickEventMessage(Message msg) {
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
	 * <xml>
	 * <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>123456789</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[click]]></Event>
	 * <EventKey><![CDATA[EVENTKEY]]></EventKey>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), eventKey, agentID);
	}
}
