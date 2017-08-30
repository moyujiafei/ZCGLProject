package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收消息——接收普通消息——小视频消息
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */

public class ShortVideoMessage extends NormalMessage {
	
	public final String MEDIA_ID = "mediaId"; 
	public final String THUMB_MEDIA_ID = "thumbMediaId";
	
	private String mediaId;        //视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String thumbMediaId;  //视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	
	//接收小视频格式
	private final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[shortvideo]]></MsgType>"
			+ "<MediaId><![CDATA[%s]]></MediaId>"
			+ "<ThumbMediaId><![CDATA[%s]]></ThumbMediaId>"
			+ "<MsgId>%s</MsgId>"  
			+ "<AgentID>%s</AgentID>" 
			+ "</xml>";
	
	public ShortVideoMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		mediaId = root.element(MEDIA_ID).getText();
		thumbMediaId = root.element(THUMB_MEDIA_ID).getText();
	}
	
	public String getMediaId() {
		return mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	/**
	 * 接受消息——接收普通消息——短视频消息
	 *<xml>
	 *<ToUserName><![CDATA[toUser]]></ToUserName>
	 *<FromUserName><![CDATA[fromUser]]></FromUserName>
	 *<CreateTime>1357290913</CreateTime>
	 *<MsgType><![CDATA[shortvideo]]></MsgType>
	 *<MediaId><![CDATA[media_id]]></MediaId>
	 *<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>
	 *<MsgId>1234567890123456</MsgId>
	 *</xml>
	 */
	
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), mediaId, thumbMediaId, msgId, agentID);
	}

}
