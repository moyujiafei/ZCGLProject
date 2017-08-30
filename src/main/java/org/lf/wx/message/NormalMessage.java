package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 当普通微信用户向公众账号发消息时，微信服务器将POST消息的XML数据包到开发者填写的URL上。
 * https://mp.weixin.qq.com/wiki/17/f298879f8fb29ab98b2f2971d42552fd.html
 */
public abstract class NormalMessage {
	protected Element root;
	
	protected int msgId; // 消息id，64位整型
	protected int agentID; // 	企业应用的id

	public final String MSG_ID = "MsgId";
	public final String AGENT_ID = "AgentID";
	
	private Message msg;

	public NormalMessage(Message msg) {
		root = msg.getRoot();
		bindBaseElement(root);
		bindSpecalElement(root);
		
		this.msg = msg;
	}

	private void bindBaseElement(Element root) {
		msgId = Integer.parseInt(root.elementText(MSG_ID));
		agentID = Integer.parseInt(root.elementText(AGENT_ID));
	}

	/**
	 * 读取XML中与特定消息相关的元素信息。
	 */
	protected abstract void bindSpecalElement(Element root);
	
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

	public int getMsgId() {
		return msgId;
	}
	
	public int getAgentID() {
		return agentID;
	}
	
	public abstract String getMessage();
	
	@Override
	public String toString() {
		return getMessage();
	}
}
