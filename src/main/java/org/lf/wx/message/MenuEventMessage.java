package org.lf.wx.message;

import org.dom4j.Element;

/**
 * 自定义菜单事件
 * 
 * 用户点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报。
 * 1）点击菜单拉取消息时的事件推送
 * 2）点击菜单跳转链接时的事件推送
 */
public class MenuEventMessage extends EventMessage {
	private static final String RECEIVE_TEMPLATE =  "<xml>" +
            "<ToUserName><![CDATA[%s]]></ToUserName>" +
            "<FromUserName><![CDATA[%s]]></FromUserName>" +
            "<CreateTime>%s</CreateTime>" +
            "<MsgType><![CDATA[event]]></MsgType>" +
            "<Event><![CDATA[%s]]></Event>" +
            "</xml>";	
	
	public MenuEventMessage(Message msg) {
		super(msg);
	}

	/**
	 * 没有需要特殊绑定的组件。
	 */
	@Override
	protected void bindSpecalElement(Element root) {

	}

	/**
	 * @return <xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[FromUser]]></FromUserName>
	<CreateTime>123456789</CreateTime>
	<MsgType><![CDATA[event]]></MsgType>
	<Event><![CDATA[subscribe]]></Event>
	</xml>
	 */
	@Override
	public String getMessage() {
		return String.format(RECEIVE_TEMPLATE, getToUserName(), getFromUserName(), getCreateTime(), event.toString());
	}
}
