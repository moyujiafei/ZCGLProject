package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 取消关注企业号的事件，会推送到每个应用在管理端设置的URL
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class UnsubscribeEventMessage extends EventMessage {
	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	public UnsubscribeEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
	}

	/**
	 * @return <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
	 *<FromUserName><![CDATA[FromUser]]></FromUserName>
	 *<CreateTime>123456789</CreateTime>
	 *<MsgType><![CDATA[event]]></MsgType>
	 *<Event><![CDATA[unsubscribe]]></Event>
	 *<AgentID>1</AgentID>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), agentID);
	}

}
