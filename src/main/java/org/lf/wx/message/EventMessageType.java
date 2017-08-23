package org.lf.wx.message;

public enum EventMessageType {
	/**
	 * 用户未关注时，进行关注后的事件推送
	 */
	subscribe,     
	/**
	 * 取消关注
	 */
	unsubscribe,
	/**
	 * 成员进入应用的事件推送
	 */
	enter_agent,
	
	/**
	 * 上报地理位置事件
	 */
	LOCATION,
	
	/**
	 * 点击菜单拉取消息的事件推送
	 */
	click,
	/**
	 * 点击菜单跳转链接的事件推送
	 */
	view,
	/**
	 * 扫码推事件的事件推送
	 */
	scancode_push,
	/**
	 * 扫码推事件且弹出“消息接收中”提示框的事件推送
	 */
	scancode_waitmsg,
	/**
	 * 弹出系统拍照发图的事件推送 
	 */
	pic_sysphoto,
	/**
	 * 弹出拍照或者相册发图的事件推送 
	 */
	pic_photo_or_album,
	/**
	 * 扫码推事件的事件推送 
	 */
	pic_weixin,
	/**
	 * 弹出地理位置选择器的事件推送 
	 */
	location_select,
	/**
	 * 异步任务完成事件推送
	 */
	batch_job_result,
}
