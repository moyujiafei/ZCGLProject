package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 弹出地理位置选择器的事件推送
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class LocationSelectEventMessage extends MenuEventMessage {

	public static final String EVENT_KEY = "EventKey";
	public static final String SEND_LOCATION_INFO = "SendLocationInfo";
	public static final String LOCATION_X = "Location_X";
	public static final String LOCATION_Y = "Location_Y";
	public static final String SCALE = "Scale";
	public static final String LABLE = "Lable";
	public static final String POINAME = "Poiname";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<SendLocationInfo><Location_X><![CDATA[23]]></Location_X>"
			+ "<Location_Y><![CDATA[%s]]></Location_Y>"
			+ "<Scale><![CDATA[%s]]></Scale>" + "<Label><![CDATA[%s]]></Label>"
			+ "<Poiname><![CDATA[%s]]></Poiname>" + "</SendLocationInfo>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	private String eventKey; // 事件KEY值，与自定义菜单接口中KEY值对应
	private String sendLocationInfo; // 发送的位置信息
	private String location_X; // X坐标信息
	private String location_Y; // Y坐标信息
	private String scale; // 精度，可理解为精度或者比例尺、越精细的话 scale越高
	private String lable; // 地理位置的字符串信息
	private String poiname; // 朋友圈POI的名字，可能为空

	/**
	 * 扫码推事件且弹出“消息接收中”提示框的事件推送
	 */
	public LocationSelectEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
		sendLocationInfo = root.elementText(SEND_LOCATION_INFO);
		location_X = root.elementText(LOCATION_X);
		location_Y = root.elementText(LOCATION_Y);
		scale = root.elementText(SCALE);
		lable = root.elementText(LABLE);
		if(root.elementText(POINAME) == null){   //非空判断
			poiname = "";
		}else{
			
		}
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getSendLocationInfo() {
		return sendLocationInfo;
	}

	public String getLocation_X() {
		return location_X;
	}

	public String getLocation_Y() {
		return location_Y;
	}

	public String getScale() {
		return scale;
	}

	public String getLable() {
		return lable;
	}

	public String getPoiname() {
		return poiname;
	}

	/**
	 * <xml><ToUserName><![CDATA[gh_e136c6e50636]]></ToUserName>
	 * <FromUserName><![CDATA[oMgHVjngRipVsoxg6TuX3vz6glDg]]></FromUserName>
	 * <CreateTime>1408091189</CreateTime> <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[location_select]]></Event>
	 * <EventKey><![CDATA[6]]></EventKey>
	 * <SendLocationInfo><Location_X><![CDATA[23]]></Location_X>
	 * <Location_Y><![CDATA[113]]></Location_Y> <Scale><![CDATA[15]]></Scale>
	 * <Label><![CDATA[ 广州市海珠区客村艺苑路 106号]]></Label>
	 * <Poiname><![CDATA[]]></Poiname> </SendLocationInfo> 
	 * <AgentID>1</AgentID>
	 * </xml>
	 */

	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(), eventKey,sendLocationInfo,location_X,location_Y,scale,lable,poiname,agentID);
	}
}
