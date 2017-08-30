package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 上报地理位置事件
 * 
 * 用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，或在进入会话后每5秒上报一次地理位置，
 * 公众号可以在公众平台网站中修改以上设置。上报地理位置时，微信会将上报地理位置事件推送到开发者填写的URL。
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class LocationEventMessage extends EventMessage {
	
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static final String PRECISION = "Precision";
	
	private String latitude;   //地理位置纬度 
	private String longitude;  //地理位置经度 
	private String precision;  //地理位置精度 
	
	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<Latitude>%s</Latitude>"
			+ "<Longitude>%s</Longitude>"
			+ "<Precision>%s</Precision>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	public LocationEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		latitude = root.elementText(LATITUDE);
		longitude = root.elementText(LONGITUDE);
		precision = root.elementText(PRECISION);
	}
	
	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getPrecision() {
		return precision;
	}

	/**
	 * <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>123456789</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[LOCATION]]></Event> 
	 * <Latitude>23.137466</Latitude>
	 * <Longitude>113.352425</Longitude> 
	 * <Precision>119.385040</Precision>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(),latitude,longitude,precision, agentID);
	}
}
