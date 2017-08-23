package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收消息——接收普通消息——url消息
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */
public class LinkMessage extends NormalMessage {
	
	public final String TITLE = "Title";
	public final String DESCRIPTION = "Description";
	public final String PICURL = "PicUrl";
	
	private String title;   //消息标题
	private String description;   //消息描述
	private String picUrl;			 //封面缩略图的url
	
	//接收url格式
	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[link]]></MsgType>"
			+ "<Title><![CDATA[%s]]></Title>"
			+ "<Description><![CDATA[%s]]></Description>"
			+ "<PicUrl><![CDATA[%s]]></PicUrl>"
			+ "<MsgId>%s</MsgId>"  
			+ "<AgentID>%s</AgentID>" 
			+ "</xml> ";
	
	public LinkMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		title = root.element(TITLE).getText();
		description = root.element(DESCRIPTION).getText();
		picUrl = root.element(PICURL).getText();
	}
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	/***
	 * 接收消息——接收普通消息——链接消息
	 * <xml>
	 *<ToUserName><![CDATA[toUser]]></ToUserName>
	 *<FromUserName><![CDATA[fromUser]]></FromUserName>
	 *<CreateTime>1351776360</CreateTime>
	 *<MsgType><![CDATA[link]]></MsgType>
	 *<Title><![CDATA[公众平台官网链接]]></Title>
	 *<Description><![CDATA[公众平台官网链接]]></Description>
	 *<Url><![CDATA[url]]></Url>
	 *<MsgId>1234567890123456</MsgId>
	 *</xml> 
	 * 
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), title, description,picUrl,msgId, agentID);
	}

}
