package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收消息——接收普通消息——文本消息
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */
public class TextMessage extends NormalMessage {
	public final String CONTENT = "Content";

	private String content; // 文本消息内容

//	接收文本消息模板
	private static final String RECEIVE_TEMPLATE =  "<xml>" +
            "<ToUserName><![CDATA[%s]]></ToUserName>" +
            "<FromUserName><![CDATA[%s]]></FromUserName>" +
            "<CreateTime>%s</CreateTime>" +
            "<MsgType><![CDATA[text]]></MsgType>" +
            "<Content><![CDATA[%s]]></Content>" +
            "<MsgId>%s</MsgId>" +
            "<AgentID>%s</AgentID>" +
            "</xml>";	
	
	public TextMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		content = root.element(CONTENT).getText();
	}

	public String getContent() {
		return content;
	}

	/**
	 * 接受消息——接收普通消息——文本消息
	 * 
	 * @return  
	<xml>
 	<ToUserName><![CDATA[toUser]]></ToUserName>
 	<FromUserName><![CDATA[fromUser]]></FromUserName> 
 	<CreateTime>1348831860</CreateTime>
 	<MsgType><![CDATA[text]]></MsgType>
 	<Content><![CDATA[this is a test]]></Content>
 	<MsgId>1234567890123456</MsgId>
 	</xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), content, msgId, agentID);
	}
	
	
}
