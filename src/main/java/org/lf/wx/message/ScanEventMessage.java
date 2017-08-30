package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者
 * http://mp.weixin.qq.com/wiki/19/a037750e2df0261ab0a84899d16abd33.html
 */
public class ScanEventMessage extends MenuEventMessage {

	public static final String EVENT_KEY = "EventKey";
	public static final String TICKET = "Ticket";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<Ticket><![CDATA[%s]]></Ticket>"
			+ "</xml>";

	private String eventKey; // 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
	private String ticket; 	// 二维码的ticket，可用来换取二维码图片

	/**
	 * 扫码推事件的事件推送
	 */
	public ScanEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
		ticket = root.elementText(TICKET);
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	/**
	 * <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>123456789</CreateTime> <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[SCAN]]></Event>
	 * <EventKey><![CDATA[SCENE_VALUE]]></EventKey>
	 * <Ticket><![CDATA[TICKET]]></Ticket> </xml>
	 * 
	 * 
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), eventKey,
				ticket);
	}
}
