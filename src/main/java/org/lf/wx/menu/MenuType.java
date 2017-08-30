package org.lf.wx.menu;

/**
 * 自定义菜单接口可实现多种类型按钮
 * 
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E5%88%9B%E5%BB%BA%E5%BA%94%E7%94%A8%E8%8F%9C%E5%8D%95
 */
public enum MenuType {
	/**
	 * 点击推事件
	 */
	click,  
	/**
	 * 跳转URL
	 */
	view,
	/**
	 * 扫码推事件
	 */
	scancode_push,
	/**
	 * 扫码推事件且弹出“消息接收中”提示框
	 */
	scancode_waitmsg,
	/**
	 * 弹出系统拍照发图
	 */
	pic_sysphoto,
	/**
	 * 弹出拍照或者相册发图
	 */
	pic_photo_or_album,
	/**
	 * 弹出微信相册发图器
	 */
	pic_weixin,
	/**
	 * 弹出地理位置选择器
	 */
	location_select,
}
