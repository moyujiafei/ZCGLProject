package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 弹出微信相册发图器的事件推送
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class PicWeixinEventMessage extends MenuEventMessage {

	public static final String EVENT_KEY = "EventKey";
	public static final String SEND_PICS_INFO = "SendPicsInfo";
	public static final String COUNT = "Count";
	public static final String PIC_LIST = "PicList";
	public static final String PIC_MD5_SUM = "PicMd5Sum";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<SendPicsInfo><Count>%s</Count>"
			+ "<PicList><item><PicMd5Sum><![CDATA[%s]]></PicMd5Sum>"
			+ "</item>" + "</PicList>" + "</SendPicsInfo>" 
			+ "<AgentID>%s</AgentID>" 
			+ "</xml>";

	private String eventKey; // 事件KEY值，与自定义菜单接口中KEY值对应
	private String sendPicsInfo; // 发送的图片信息
	private String count; // 发送的图片数量
	private String picList; // 图片列表
	private String picMd5Sum; // 图片的MD5值，开发者若需要，可用于验证接收到图片

	/**
	 * 扫码推事件且弹出“消息接收中”提示框的事件推送
	 */
	public PicWeixinEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
		sendPicsInfo = root.elementText(SEND_PICS_INFO);
		count = root.elementText(COUNT);
		picList = root.elementText(PIC_LIST);
		picMd5Sum = root.elementText(PIC_MD5_SUM);
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getSendPicsInfo() {
		return sendPicsInfo;
	}

	public String getCount() {
		return count;
	}

	public String getPicList() {
		return picList;
	}

	public String getPicMd5Sum() {
		return picMd5Sum;
	}

	/**
	 * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>1408090816</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[pic_weixin]]></Event>
	 * <EventKey><![CDATA[6]]></EventKey>
	 * <SendPicsInfo><Count>1</Count>
	 * <PicList><item><PicMd5Sum><![CDATA[5a75aaca956d97be686719218f275c6b]]></PicMd5Sum>
	 * </item>
	 * </PicList>
	 * </SendPicsInfo>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), eventKey,sendPicsInfo,count,picList,picMd5Sum,agentID);
	}
}
