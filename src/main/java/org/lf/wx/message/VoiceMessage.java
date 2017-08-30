package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收消息——接收普通消息——普通声音消息
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */
public class VoiceMessage extends NormalMessage {

	public final String MEDIA_ID = "MediaId";  
	public final String FORMAT = "Format";

	private String mediaId;   //媒体ID,可以调用多媒体文件下载接口拉取数据。
	private String format;	 //格式

	//接收声音消息模板
	private static final String RECEIVE_TEMPLATE = "<xml>"
					+ "<ToUserName><![CDATA[%s]]></ToUserName>"
					+ "<FromUserName><![CDATA[%s]]></FromUserName>"
					+ "<CreateTime>%s</CreateTime>"
					+ "<MsgType><![CDATA[voice]]></MsgType>"
					+ "<MediaId><![CDATA[%s]]></MediaId>"
					+ "<Format><![CDATA[%s]]></Format>"
					+ "<MsgId>%s</MsgId>"  
					+ "<AgentID>%s</AgentID>" 
					+ "</xml>";

	public VoiceMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		mediaId = root.element(MEDIA_ID).getText();
		format = root.element(FORMAT).getText();
	}

	public String getMediaId() {
		return mediaId;
	}

	public String getFormat() {
		return format;
	}

	/**
	 * 接受消息——接收普通消息——声音消息
	 * 
	 * @return
	 * 
	 * <xml>
	 *<ToUserName><![CDATA[toUser]]></ToUserName>
	 *<FromUserName><![CDATA[fromUser]]></FromUserName>
	 *<CreateTime>1357290913</CreateTime>
	 *<MsgType><![CDATA[voice]]></MsgType>
	 *<MediaId><![CDATA[media_id]]></MediaId>
	 *<Format><![CDATA[Format]]></Format>
	 *<MsgId>1234567890123456</MsgId>
	 *</xml>
	 * 
	 */
	
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), mediaId, format, msgId, agentID);
	}

}
