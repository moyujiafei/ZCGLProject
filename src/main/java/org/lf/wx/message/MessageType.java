package org.lf.wx.message;

public enum MessageType {
	/**
	 * 文本消息
	 */
	text,
	/**
	 * 图片消息
	 */
	image, 
	/**
	 * 语音消息
	 */
	voice,  
	/**
	 * 视频消息
	 */
	video, 
	/**
	 * 小视频消息
	 */
	shortvideo, 
	/**
	 * 地理位置消息
	 */
	location, 
	/**
	 * 链接消息
	 */
	link, 
	/**
	 * 事件消息
	 */
	event, 
	/**
	 * 消息转发到多客服
	 */
	transfer_customer_service,
}
