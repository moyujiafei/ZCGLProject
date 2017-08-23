package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接受消息——接收普通消息——图片消息
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */
public class ImageMessage extends NormalMessage {
	public final String PIC_URL = "PicUrl";
	public final String MEDIA_ID = "MediaId";

	private String picUrl; // 图片链接
	private String mediaId; // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

//	接收图片消息模板
	private static final String RECEIVE_TEMPLATE = "<xml>" 
	        + " <ToUserName><![CDATA[%s]]></ToUserName>" 
			+ " <FromUserName><![CDATA[%s]]></FromUserName> "
			+ " <CreateTime>%s</CreateTime>" 
			+ " <MsgType><![CDATA[image]]></MsgType>" 
			+ " <PicUrl><![CDATA[%s]]></PicUrl>"
			+ " <MediaId><![CDATA[%s]]></MediaId>" 
			+ "<MsgId>%s</MsgId>"  
			+ "<AgentID>%s</AgentID>"  
			+ " </xml>";	

	public ImageMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		picUrl = root.element(PIC_URL).getText();
		mediaId = root.element(MEDIA_ID).getText();
	}

	public String getPicUrl() {
		return picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}
	
	/**
	 * 接受消息——接收普通消息——图片消息
	 * 
	 * @return
 <xml>
 <ToUserName><![CDATA[toUser]]></ToUserName>
 <FromUserName><![CDATA[fromUser]]></FromUserName>
 <CreateTime>1348831860</CreateTime>
 <MsgType><![CDATA[image]]></MsgType>
 <PicUrl><![CDATA[this is a url]]></PicUrl>
 <MediaId><![CDATA[media_id]]></MediaId>
 <MsgId>1234567890123456</MsgId>
 </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), picUrl, mediaId, msgId);
	}

}
