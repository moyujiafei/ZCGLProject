package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 接收消息——接收普通消息——地理位置消息
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
 */

public class LocationMessage extends NormalMessage {
	
	public final String LOCATION_X = "LocationX";
	public final String LOCATION_Y = "LocationY";
	public final String SCALE ="Scale";
	public final String LABEL ="Label";
	
	private String locationX ; 	//地理位置维度 
	private String locationY; 	//地理位置经度
	private String scale;       //地图缩放大小 
	private String label;       //地理位置信息 
	
	//接收图片消息模板,接收格式
	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[location]]></MsgType>"
			+ "<Location_X>%s</Location_X>"
			+ "<Location_Y>%s</Location_Y>"
			+ "<Scale>%s</Scale>"
			+ "<Label><![CDATA[%s]]></Label>"
			+ "<MsgId>%s</MsgId>"  
			+ "<AgentID>%s</AgentID>" 
			+ "</xml>";
	
	public LocationMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		locationX = root.elementText(LOCATION_X);   
		locationY = root.elementText(LOCATION_Y);
		scale = root.elementText(SCALE);
		label = root.elementText(LABEL);
	}

	public String getLocationX() {
		return locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public String getScale() {
		return scale;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * 接收消息——接收普通消息——地理位置消息
	 * <xml>
	 *<ToUserName><![CDATA[toUser]]></ToUserName>
	 *<FromUserName><![CDATA[fromUser]]></FromUserName>
	 *<CreateTime>1351776360</CreateTime>
	 *<MsgType><![CDATA[location]]></MsgType>
	 *<Location_X>23.134521</Location_X>
	 *<Location_Y>113.358803</Location_Y>
	 *<Scale>20</Scale>
	 *<Label><![CDATA[位置信息]]></Label>
	 *<MsgId>1234567890123456</MsgId>	
	 *</xml> 
	 * @return
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), locationX, locationY,scale,label,msgId, agentID);
	}

}
