package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收事件推送
 * https://mp.weixin.qq.com/wiki/7/9f89d962eba4c5924ed95b513ba69d9b.html
 * 在微信用户和公众号产生交互的过程中，用户的某些操作会使得微信服务器通过事件推送的形式通知到开发者在开发者中心处设置的服务器地址，
 * 从而开发者可以获取到该信息。其中，某些事件推送在发生后，是允许开发者回复用户的，某些则不允许。
 * 
 */
public class EventMessage {
	protected Element root;
	
	private static final String RECEIVE_TEMPLATE =  "<xml>" +
            "<ToUserName><![CDATA[%s]]></ToUserName>" +
            "<FromUserName><![CDATA[%s]]></FromUserName>" +
            "<CreateTime>%s</CreateTime>" +
            "<MsgType><![CDATA[event]]></MsgType>" +
            "<Event><![CDATA[%s]]></Event>" +
            "<AgentID><![CDATA[%s]]></AgentID>" +
            "</xml>";	
	
	protected EventMessageType event;
	protected String agentID;
	
	public final String EVENT = "Event";
	public final String AGENTID = "AgentID";
	
	private Message msg;
	
	public EventMessage(Message msg) {
		root = msg.getRoot();

		bindBaseElement(root);
		bindSpecalElement(root);
		
		this.msg = msg;
	}

	private void bindBaseElement(Element root) {
		event = EventMessageType.valueOf(root.elementText(EVENT));
		agentID = root.elementText(AGENTID);
	}

	/**
	 * 读取XML中与特定消息相关的元素信息。
	 */
	protected void bindSpecalElement(Element root) {
		
	}
	
	public String getToUserName() {
		return msg.getToUserName();
	}

	public String getFromUserName() {
		return msg.getFromUserName();
	}

	public String getCreateTime() {
		return msg.getCreateTime();
	}

	public MessageType getMsgType() {
		return msg.getMsgType();
	}

	public EventMessageType getEvent() {
		return event;
	}
	
	public String getAgentID() {
		return agentID;
	}
	
	public Message getMsg() {
		return msg;
	}

	protected String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), event.toString(), agentID);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
