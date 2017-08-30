package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 用户扫描带场景值二维码时，可能推送以下两种事件：
 * 如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
 * 如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者。
 */
public class QRCodeEventMessage extends EventMessage {
	private static final String RECEIVE_TEMPLATE =  "<xml>" +
            "<ToUserName><![CDATA[%s]]></ToUserName>" +
            "<FromUserName><![CDATA[%s]]></FromUserName>" +
            "<CreateTime>%s</CreateTime>" +
            "<MsgType><![CDATA[event]]></MsgType>" +
            "<Event><![CDATA[%s]]></Event>" +
            "<EventKey><![CDATA[%s]]></EventKey>" +
            "<Ticket><![CDATA[%s]]></Ticket>" +
            "</xml>";	
	
	public static final String EVENT_KEY = "EventKey";
	public static final String TICKET = "Ticket";
	
	private String eventKey;   //事件KEY值，q
	private String ticket;     //二维码的ticket，可用来换取二维码图片
	
	public QRCodeEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
		ticket = root.elementText(TICKET);
	}

	/**
	 * @return 用户未关注时，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>
<FromUserName><![CDATA[FromUser]]></FromUserName>
<CreateTime>123456789</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[subscribe]]></Event>
<EventKey><![CDATA[qrscene_123123]]></EventKey>
<Ticket><![CDATA[TICKET]]></Ticket>
</xml>
       
       用户关注时：则微信会将带场景值扫描事件推送给开发者。
<xml>
<ToUserName><![CDATA[toUser]]></ToUserName>
<FromUserName><![CDATA[FromUser]]></FromUserName>
<CreateTime>123456789</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[SCAN]]></Event>
<EventKey><![CDATA[SCENE_VALUE]]></EventKey>
<Ticket><![CDATA[TICKET]]></Ticket>
</xml>       
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), event.toString(), eventKey, ticket);
	}

}
