package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 扫码推事件的事件推送
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6
 */
public class ScancodePushEventMessage extends MenuEventMessage {

	public static final String EVENT_KEY = "EventKey";
	public static final String SCAN_CODE_INFO = "ScanCodeInfo";
	public static final String SCAN_TYPE = "ScanType";
	public static final String SCAN_RESULT = "ScanResult";

	private static final String RECEIVE_TEMPLATE = "<xml>"
			+ "<ToUserName><![CDATA[%s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%s]]></FromUserName>"
			+ "<CreateTime>%s</CreateTime>"
			+ "<MsgType><![CDATA[event]]></MsgType>"
			+ "<Event><![CDATA[%s]]></Event>"
			+ "<EventKey><![CDATA[%s]]></EventKey>"
			+ "<ScanCodeInfo><ScanType><![CDATA[%s]]></ScanType>"
			+ "<ScanResult><![CDATA[%s]]></ScanResult>"
			+ "</ScanCodeInfo>"
			+ "<AgentID>%s</AgentID>"
			+ "</xml>";

	private String eventKey; // 事件KEY值，由开发者在创建菜单时设定
	private String scanType; // 扫描类型，一般是qrcode
	private String scanResult; // 扫描结果，即二维码对应的字符串信息

	/**
	 * 扫码推事件的事件推送
	 */
	public ScancodePushEventMessage(Message msg) {
		super(msg);
	}

	@Override
	protected void bindSpecalElement(Element root) {
		eventKey = root.elementText(EVENT_KEY);
		Element scanCodeInfo = root.element(SCAN_CODE_INFO);
		scanType = scanCodeInfo.elementText(SCAN_TYPE);
		scanResult = scanCodeInfo.elementText(SCAN_RESULT);
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getScanType() {
		return scanType;
	}

	public String getScanResult() {
		return scanResult;
	}

	/**
	 * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[FromUser]]></FromUserName>
	 * <CreateTime>1408090502</CreateTime>
	 * <MsgType><![CDATA[event]]></MsgType>
	 * <Event><![CDATA[scancode_push]]></Event>
	 * <EventKey><![CDATA[6]]></EventKey>
	 * <ScanCodeInfo><ScanType><![CDATA[qrcode]]></ScanType>
	 * <ScanResult><![CDATA[1]]></ScanResult>
	 * </ScanCodeInfo>
	 * <AgentID>1</AgentID>
	 * </xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(),
				getFromUserName(), getCreateTime(), event.toString(),eventKey,scanType,scanResult, agentID);
	}
}
