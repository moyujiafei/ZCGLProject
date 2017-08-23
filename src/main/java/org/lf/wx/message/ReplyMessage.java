package org.lf.wx.message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lf.utils.StringUtils;
import org.lf.wx.WXProperties;
import org.lf.wx.media.MediaType;
import org.lf.wx.media.TempMedia;
import org.lf.wx.media.TempMediaManager;
import org.lf.wx.message.security.AESException;
import org.lf.wx.message.security.WXEncryptUtils;
import org.lf.wx.utils.WXException;

/**
 * 当用户发送消息给公众号时（或某些特定的用户操作引发的事件推送时），会产生一个POST请求，开发者可以在响应包（Get）中返回特定XML结构，
 * 来对该消息进行响应（现支持回复文本、图片、图文、语音、视频、音乐）。严格来说，发送被动响应消息其实并不是一种接口，
 * 而是对微信服务器发过来消息的一次回复。
 * https://mp.weixin.qq.com/wiki/1/6239b44c206cab9145b1d52c67e6c551.html
 * 
 *
 */
public class ReplyMessage {
	private static Logger logger = Logger.getLogger(ReplyMessage.class);
	
	private ReplyMessage() {}
	
	/**
	 * 检查三个参数是否为空
	 */
	private static void check(String toUserName, String fromUserName, String createTime) {
		if (StringUtils.isEmpty(toUserName) || StringUtils.isEmpty(fromUserName) || StringUtils.isEmpty(createTime)) {
			throw new NullPointerException();
		}
	}
	
	/**
	 * 如果系统处于加密模式，则返回加密后的消息。否则返回原消息。
	 * @param message
	 * @return
	 */
	private static String getMessage(String message) {
		String reply = message;
		
		if (WXProperties.WX_ENCRYPT) {
			// 如果是加密模式，需要对被动回复消息进行加密
			try {
				reply = WXEncryptUtils.encrypt(message);
			} catch (AESException e) {
				logger.error(e.getMessage(), e);
			}
		} 
		
		return reply;
	}
	
	/**
	 * 微信服务器在将用户的消息发给公众号的开发者服务器地址（开发者中心处配置）后，微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
	 * 假如服务器无法保证在五秒内处理并回复，必须做出下述回复，这样微信服务器才不会对此作任何处理，并且不会发起重试。
	 * 在这种情况下，（推荐方式）直接回复success。
	 */
	public static String sendSuccessMessage() {
		return "success";
	}
	
	/**
	 * 以文本消息方式回复成功信息。
	 */
	public static String sendSuccessMessage(String toUserName, String fromUserName, String createTime, String msg) {
		return textMessage(toUserName, fromUserName, createTime, msg);
	}
	
	/**
	 * 发送消息——被动回复消息——回复文本消息
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName 开发者微信号 
	 * @param createTime 消息创建时间 （整型）
	 * @param content 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示） 
	 * @return
	 */
	public static String textMessage(String toUserName, String fromUserName, String createTime, String content) {
		check(toUserName, fromUserName, createTime);
		
//		回复文本消息模板
		String SEND_TEMPLATE =  "<xml>" +
	            "<ToUserName><![CDATA[%s]]></ToUserName>" +
	            "<FromUserName><![CDATA[%s]]></FromUserName>" +
	            "<CreateTime>%s</CreateTime>" +
	            "<MsgType><![CDATA[text]]></MsgType>" +
	            "<Content><![CDATA[%s]]></Content>" +
	            "</xml>";
		
		return getMessage(String.format(SEND_TEMPLATE, toUserName, fromUserName, createTime, content));
	}
	
	/**
	 * 给用户回复一个图片消息。这里的图片是本地图片。
	 * 我们先将本地的mediaFile以临时素材（TempMedia）方式上传到微信服务器，并得到mediaId，
	 * 再回复给用户。
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime 消息创建时间 （整型） 
	 * @param mediaId  	通过素材管理接口上传多媒体文件，得到的id。
	 * 
	 * @see org#lf#wx#media#TempMedia
	 * @see org#lf#wx#media#MediaType
	 * 
	 * @return 
	 * @throws WXException 
	 */
	public static String imageMessage(String accessToken, String toUserName, String fromUserName, String createTime, File imageFile) throws WXException {
		TempMedia media = new TempMedia(TempMediaManager.uploadMedia(accessToken, imageFile, MediaType.image));
		
		return getMessage(imageMessage(toUserName, fromUserName, createTime, media.getMediaId()));
	}
	
	/**
	 * 接受消息——接收普通消息——图片消息
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime 消息创建时间 （整型） 
	 * @param mediaId  	通过素材管理接口上传多媒体文件，得到的id。
	 * 
	 * @return 
	 */
	public static String imageMessage(String toUserName, String fromUserName, String createTime, String mediaId) {
		check(toUserName, fromUserName, createTime);
		
//		回复图片消息模板
		String SEND_TEMPLATE = "<xml>" 
		        + " <ToUserName><![CDATA[%s]]></ToUserName>" 
				+ " <FromUserName><![CDATA[%s]]></FromUserName> "
				+ " <CreateTime>%s</CreateTime>" 
				+ " <MsgType><![CDATA[image]]></MsgType>" 
				+ " <Image><MediaId><![CDATA[%s]]></MediaId></Image>"
				+ " </xml>";
		
		return getMessage(String.format(SEND_TEMPLATE, toUserName, fromUserName, createTime, mediaId));
	}
	
	/**
	 * 给用户回复一个语音消息。这里的本地素材
	 * 我们先将本地的mediaFile以临时素材（TempMedia）方式上传到微信服务器，并得到mediaId，
	 * 再回复给用户。
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime 消息创建时间 （整型） 
	 * @param mediaId  	通过素材管理接口上传多媒体文件，得到的id。
	 * 
	 * @see org#lf#wx#media#TempMedia
	 * @see org#lf#wx#media#MediaType
	 * 
	 * @return 
	 * @throws WXException 
	 */
	public static String voiceMessage(String accessToken, String toUserName, String fromUserName, String createTime, File voiceFile, String format) throws WXException {
		TempMedia media = new TempMedia(TempMediaManager.uploadMedia(accessToken, voiceFile, MediaType.voice));
		
		return voiceMessage(toUserName, fromUserName, createTime, media.getMediaId(), format);
	}
	
	/**
	 * 
	 * @param toUserName     接收方帐号（收到的OpenID）
	 * @param fromUserName   开发者微信号
	 * @param createTime	   消息创建时间 （整型）
	 * @param mediaId		   通过素材管理接口上传多媒体文件，得到的id。
	 * @param format  		  语音格式，如amr，speex等 
	 * @return
	 */
	public static String voiceMessage(String toUserName, String fromUserName, String createTime, String mediaId, String format) {
		String SEND_TEMPLATEN = "<xml>"
				+ "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[voice]]></MsgType>"
				+ "<MediaId><![CDATA[%s]]></MediaId>"
				+ "<Format><![CDATA[%s]]></Format>"
				+ "</xml>";
		return getMessage(String.format(SEND_TEMPLATEN, toUserName, fromUserName,createTime,mediaId,format));
	}
	
	/**
	 * 给用户回复一个视频消息。这里的本地素材
	 * 我们先将本地的mediaFile以临时素材（TempMedia）方式上传到微信服务器，并得到mediaId，
	 * 再回复给用户。
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime 消息创建时间 （整型） 
	 * @param mediaId  	通过素材管理接口上传多媒体文件，得到的id。
	 * 
	 * @see org#lf#wx#media#TempMedia
	 * @see org#lf#wx#media#MediaType
	 * 
	 * @return 
	 * @throws WXException 
	 */
	public static String videoMessage(String accessToken, String toUserName, String fromUserName, String createTime, File videoFile, String title,String thumbMediaId) throws WXException {
		TempMedia media = new TempMedia(TempMediaManager.uploadMedia(accessToken, videoFile, MediaType.video));
		
		return videoMessage(toUserName, fromUserName, createTime, media.getMediaId(), title, thumbMediaId);
	}
	
	/**
	 * 
	 * @param toUserName     	接收方帐号（收到的OpenID）
	 * @param fromUserName   	开发者微信号
	 * @param createTime	   	消息创建时间 （整型）
	 * @param mediaId           视频消息媒体id，可以调用多媒体文件下载接口拉取数据。 
	 * @param thumbMediaId      视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	 * @return
	 */
	public static String videoMessage(String toUserName, String fromUserName, String createTime, 
			                          String mediaId, String title,String thumbMediaId) {
		String SEND_TEMPLATEN = "<xml>"
				+ "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[video]]></MsgType>"
				+ "<MediaId><![CDATA[%s]]></MediaId>"
				+ "<ThumbMediaId><![CDATA[%s]]></ThumbMediaId>"
				+ "</xml>";
		return getMessage(String.format(SEND_TEMPLATEN, toUserName, fromUserName,createTime,mediaId,thumbMediaId));
	}
	
	/**
	 * 回复音乐消息
	 * 
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime  	消息创建时间 （整型） 
	 * @param title  	音乐标题 
	 * @param description  	音乐描述 
	 * @param musicURL  	音乐链接
	 * @param hqMusicURL 高质量音乐链接，WIFI环境优先使用该链接播放音乐 
	 * @param thumbMediaId  	缩略图的媒体id，通过素材管理接口上传多媒体文件，得到的id 
	 * @return
	 */
	public static String musicMessage(String toUserName, String fromUserName, String createTime, 
			                          String title, String description, String musicURL, String hqMusicURL, String thumbMediaId) {
		String SEND_TEMPLATEN = "<xml>"
				+ "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[music]]></MsgType>"
				+ "<Music>"
				+ "<Title><![CDATA[%s]]></Title>"
				+ "<Description><![CDATA[%s]]></Description>"
				+ "<MusicUrl><![CDATA[%s]]></MusicUrl>"
				+ "<HQMusicUrl><![CDATA[%s]]></HQMusicUrl>"
				+ "<ThumbMediaId><![CDATA[%s]]></ThumbMediaId>"
				+ "</Music>"
				+ "</xml>";
		return getMessage(String.format(SEND_TEMPLATEN, toUserName, fromUserName,createTime,title, description, musicURL, hqMusicURL, thumbMediaId));
	}
	
	/**
	 * 发送单条图文消息信息，默认第一个item为大图
	 * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200 
	 *  
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime  	消息创建时间 （整型） 
	 * @param articleList 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	 * @return
	 */
	public static String newsMessage(String toUserName, String fromUserName, String createTime, SendArticle article) {
		List<SendArticle> articleList = new ArrayList<>();
		articleList.add(article);
		
		return newsMessage(toUserName, fromUserName, createTime, articleList);
	}
	
	/**
	 * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	 * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200 
	 *  
	 * @param toUserName 接收方帐号（收到的OpenID） 
	 * @param fromUserName  	开发者微信号 
	 * @param createTime  	消息创建时间 （整型） 
	 * @param articleList 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	 * @return
	 */
	public static String newsMessage(String toUserName, String fromUserName, String createTime, List<SendArticle> articleList) {
		String FONT = "<xml>"
				+ "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[news]]></MsgType>"
		        + "<ArticleCount>%s</ArticleCount>"
		        + "<Articles>";
		String MIDDLE = "<item>"
				+ "<Title><![CDATA[%s]]></Title>"
				+ "<Description><![CDATA[%s]]></Description>"
				+ "<PicUrl><![CDATA[%s]]></PicUrl>"
				+ "<Url><![CDATA[%s]]></Url>"
				+ "</item>";
		String TAIL = "</Articles>"
				+ "</xml>";
		
		StringBuilder sb = new StringBuilder();
		for (SendArticle article : articleList) {
			sb.append(String.format(MIDDLE, article.getTitle(), article.getDescription(), article.getPicUrl(), article.getUrl()));
		}
		
		return getMessage(String.format(FONT, toUserName, fromUserName,createTime, articleList.size()) + sb.toString() + TAIL);
	}
}
