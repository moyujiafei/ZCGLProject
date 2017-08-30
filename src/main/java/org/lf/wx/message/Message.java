package org.lf.wx.message;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lf.wx.WXProperties;
import org.lf.wx.message.security.AESException;
import org.lf.wx.message.security.WXEncryptUtils;

/**
 * 接收消息与事件
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%B6%88%E6%81%AF%E4%B8%8E%E4%BA%8B%E4%BB%B6
 * 
 * 将消息型应用设置在回调模式时，企业可以通过回调URL接收成员回复的消息，以及成员关注、点击菜单、上报地理位置等事件。主页型应用不能接收消息及事件.
 * 在接收到事件后，企业可以发送被动响应消息，实现成员与企业的互动。
 *
 */
public class Message {
	private static Logger logger = Logger.getLogger(Message.class);
	
	private Element root = null;
	private HttpServletRequest request;
	
	protected String encrypt; //加密消息   

	protected String toUserName; // 开发者微信号
	protected String fromUserName; // 发送方帐号（一个OpenID）
	protected String createTime; // 消息创建时间 （整型）
	protected MessageType msgType; // 普通消息类型

	public final String ENCRYPT = "Encrypt";
	
	public final String TO_USER_NAME = "ToUserName";
	public final String FROM_USER_NAME = "FromUserName";
	public final String CREATE_TIME = "CreateTime";
	public final String MSG_TYPE = "MsgType";

	public Message(HttpServletRequest request) {
		this.request = request;
		
		// 解析request中发送过来的XML文件内容。
		SAXReader reader = new SAXReader();

		Document document;
		InputStream in = null;
		
		try {
			in = request.getInputStream();
			document = reader.read(in);
			// 得到xml根元素
			root = document.getRootElement();
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

		bindBaseElement(root);
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	private void bindBaseElement(Element root) {
		if (WXProperties.WX_ENCRYPT) {
			// 如果是加密模式，首先要解密
			encrypt = root.elementText(ENCRYPT);
			try {
				String xml = WXEncryptUtils.decrypt(encrypt);
				Document document =  DocumentHelper.parseText(xml); // 将字符串转为XML  
				this.root = document.getRootElement();
			} catch (AESException e) {
				logger.error("微信消息解密错误", e);
			} catch (DocumentException e) {
				logger.error("解析XML文件错误", e);
			} 
		} 
		
		toUserName = this.root.elementText(TO_USER_NAME);
		fromUserName = this.root.elementText(FROM_USER_NAME);
		createTime = this.root.elementText(CREATE_TIME);
		msgType = MessageType.valueOf(this.root.elementText(MSG_TYPE));
	}
	
	public Element getRoot() {
		return root;
	}

	public String getToUserName() {
		return toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MessageType getMsgType() {
		return msgType;
	}
}
