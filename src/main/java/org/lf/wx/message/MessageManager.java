package org.lf.wx.message;

import javax.servlet.http.HttpServletRequest;

import org.lf.wx.utils.WXUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageManager {
	public MessageManager() {		
	}
	
	public String load(HttpServletRequest request) {
		Message msg = new Message(request);
		
		String result = null;
		switch (msg.getMsgType()) {
		case text:
			result = handleTextMessage(msg);
			break;
		case image:
			result = handleImageMessage(msg);
			break;
		case voice:
			result = handleVoiceMessage(msg);
			break;
		case video:
			result = handleVideoMessage(msg);
			break;
		case shortvideo:
			result = handleShortVideoMessage(msg);
			break;
		case location:
			result = handleLocationMessage(msg);
			break;
		case link:
			result = handleLinkMessage(msg);
			break;
		case event:
			EventMessage eventMsg = new EventMessage(msg);
			switch (eventMsg.getEvent()) {
			case subscribe:
				result = handleSubscribeEventMessage(msg);
				break;
			case unsubscribe:
				result = handleUnsubscribeEventMessage(msg);
				break;
			case LOCATION:
				result = handleLocationEventMessage(msg);
				break;
			case click:
				result = handleClickEventMessage(msg);
				break;
			case scancode_push:
				result = handleScancodePushEventMessage(msg);
				break;
			case scancode_waitmsg:
				result = handleScancodeWaitMsgEventMessage(msg);
				break;
			case pic_sysphoto:
				result = handlePicSysPhotoEventMessage(msg);
				break;
			case pic_photo_or_album:
				result = handlePicPhotoAlbumEventMessage(msg);
				break;
			case pic_weixin:
				result = handlePicWeixinEventMessage(msg);
				break;
			case location_select:
				result = handleLocationSelectEventMessage(msg);
				break;
			case enter_agent:
				result = handleEnterAgentEventMessage(msg);
				break;
			case batch_job_result:
				result = handleBatchJobResultEventMessage(msg);
				break;
			default:
				
			}
			break;
		default:
			result = sendDefaultMsg(msg);
		}
		
		return result;
	}
	
	private String sendDefaultMsg(Message msg) {
		// 向发送端返回数据，所以要对调toUserName和fromUserName
		String toUserName = msg.getFromUserName();
		String fromUserName = msg.getToUserName();
		StringBuilder sb = new StringBuilder();
		sb.append(msg.getMsgType());
		if (msg.getMsgType() == MessageType.event) {
			EventMessage em = new EventMessage(msg);
			sb.append(":").append(em.getEvent()).append("--");
		}
		sb.append("功能尚未实现");
		String reply = sb.toString();
		return ReplyMessage.textMessage(toUserName, fromUserName, WXUtils.getTime(), reply);
	}

	protected String handleLocationSelectEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}
	
	protected String handleEnterAgentEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}
	
	protected String handleBatchJobResultEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handlePicWeixinEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handlePicPhotoAlbumEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handlePicSysPhotoEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleScancodeWaitMsgEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleScancodePushEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleClickEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleLocationEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleUnsubscribeEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleSubscribeEventMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleLinkMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleLocationMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleShortVideoMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleVideoMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleVoiceMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleImageMessage(Message msg) {
		return sendDefaultMsg(msg);
	}

	protected String handleTextMessage(Message msg) {
		return sendDefaultMsg(msg);
	}
}
