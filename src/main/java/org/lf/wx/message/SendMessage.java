package org.lf.wx.message;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 每天可发的数量为：帐号上限数*30人次/天。
 * 
 * 当用户发送消息给公众号时（或某些特定的用户操作引发的事件推送时），会产生一个POST请求，开发者可以在响应包（Get）中返回特定XML结构，
 * 来对该消息进行响应（现支持回复文本、图片、图文、语音、视频、音乐）。严格来说，发送被动响应消息其实并不是一种接口，
 * 而是对微信服务器发过来消息的一次回复。
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E5%8F%91%E6%B6%88%E6%81%AF
 * 
 *
 */
public class SendMessage {
	private SendMessage() {}
	
	public enum SendTarget {all, toUserList, toPartyList, toTagList};
	
	private static String getMessage(List<String> toUserList, List<String> toPartyList, List<String> toTagList,  
			                         String msgType, int agentid, JSONObject content) {
//		回复文本消息模板
		JSONObject json = new JSONObject();
		
		if (toUserList != null && toUserList.size() > 0) {
			json.put("touser", StringUtils.toString(toUserList, '|'));
		}
		if (toPartyList != null && toPartyList.size() > 0) {
			json.put("toparty", StringUtils.toString(toPartyList, '|'));
		}
		if (toTagList != null && toTagList.size() > 0) {
			json.put("totag", StringUtils.toString(toTagList, '|'));
		}
		
		json.put("msgtype", msgType);
		json.put("agentid", agentid);
		json.put(msgType, content);
		
		return json.toJSONString();
	}
	
	/**
	 * 发送消息——被动回复消息——回复文本消息
	 * 消息型应用支持文本、图片、语音、视频、文件、图文等消息类型。主页型应用只支持文本消息类型，且文本长度不超过20个字。
	 * @param toTagList 	标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	 * @param agentid 企业应用的id，整型。可在应用的设置页面查看
	 * @param content 	消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
{
   "touser": "UserID1|UserID2|UserID3",
   "toparty": " PartyID1 | PartyID2 ",
   "totag": " TagID1 | TagID2 ",
   "msgtype": "text",
   "agentid": 1,
   "text": {
       "content": "Holiday Request For Pony(http://xxxxx)"
   },
   "safe":0
}
	 * @return 如果无权限或收件人不存在，则本次发送失败，返回无效的userid列表（注：由于userid不区分大小写，返回的列表都统一转为小写）；如果未关注，发送仍然执行。
{
   "errcode": 0,
   "errmsg": "ok",
   "invaliduser": "UserID1",
   "invalidparty":"PartyID1",
   "invalidtag":"TagID1"
}	 
	 */
	public static String textMessage(int agentid, String content, SendTarget target, List<String> toList) {
		JSONObject json = new JSONObject();
		json.put("content", content);
		
		String msg = null;
		switch (target) {
		case toUserList:
			msg = getMessage(toList, null, null, "text", agentid, json);
			break;
		case toPartyList:
			msg = getMessage(null, toList, null, "text", agentid, json);
			break;
		case toTagList:
			msg = getMessage(null, null, toList, "text", agentid, json);
			break;	
		case all:
			List<String> toUserList = new ArrayList<>();
			toUserList.add("@all");
			msg = getMessage(toUserList, null, null, "text", agentid, json);
			break;
		default:
			
		}
		
		return msg;
	}
	
//	/**
//	 * 图片消息。
//	 * 消息型应用支持文本、图片、语音、视频、文件、图文等消息类型。主页型应用只支持文本消息类型，且文本长度不超过20个字。
//	 * 
//{
//   "touser" : "UserID1|UserID2|UserID3",
//   "toparty" : " PartyID1|PartyID2 ",
//   "totag" : " TagID1 | TagID2 ",
//   "msgtype" : "image",
//   "agentid" : 1,
//   "image" : {
//        "media_id" : "MEDIA_ID"
//   }
//}
//	 * 
//	 * @see org#lf#wx#media#TempMedia
//	 * @see org#lf#wx#media#MediaType
//	 * 
//	 * @return 
//	 * @throws WXException 
//	 */
//	public static String imageMessage(String accessToken, List<String> toTagList, int agentid, File imageFile) throws WXException {
//		TempMedia media = new TempMedia(TempMediaManager.uploadMedia(accessToken, imageFile, MediaType.image));
//		
//		JSONObject json = new JSONObject();
//		json.put("media_id", media.getMediaId());
//		
//		return getMessage(toTagList, "image", agentid, json);
//	}
	
//	/**
//	 * 发送单条图文消息信息
//	 * 消息型应用支持文本、图片、语音、视频、文件、图文等消息类型。主页型应用只支持文本消息类型，且文本长度不超过20个字。
//	 *
//{
//   "touser" : "UserID1|UserID2|UserID3",
//   "toparty" : " PartyID1 | PartyID2 ",
//   "totag" : " TagID1 | TagID2 ",
//   "msgtype" : "news",
//   "agentid" : 1,
//   "news" : {
//       "articles" : [
//           {
//               "title" : "中秋节礼品领取",
//               "description" : "今年中秋节公司有豪礼相送",
//               "url" : "URL",
//               "picurl" : "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"
//           }
//        ]
//   }
//}
//	 *  
//	 * @return
//	 */
//	public static String newsMessage(List<String> toTagList, int agentid, SendArticle article) {
//		List<SendArticle> articleList = new ArrayList<>();
//		articleList.add(article);
//		
//		return newsMessage(toTagList, agentid, articleList);
//	}
//	
//	/**
//	 * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
//	 * 消息型应用支持文本、图片、语音、视频、文件、图文等消息类型。主页型应用只支持文本消息类型，且文本长度不超过20个字。
//	 * 
//{
//   "touser" : "UserID1|UserID2|UserID3",
//   "toparty" : " PartyID1 | PartyID2 ",
//   "totag" : " TagID1 | TagID2 ",
//   "msgtype" : "news",
//   "agentid" : 1,
//   "news" : {
//       "articles" : [
//           {
//               "title" : "中秋节礼品领取",
//               "description" : "今年中秋节公司有豪礼相送",
//               "url" : "URL",
//               "picurl" : "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"
//           }
//        ]
//   }
//}
//	 * @return
//	 */
//	public static String newsMessage(List<String> toTagList, int agentid, List<SendArticle> articleList) {
//		JSONArray array = new JSONArray();
//		for (SendArticle article : articleList) {
//			array.add(article.getJSON());
//		}
//		JSONObject articles = new JSONObject();
//		articles.put("articles", array);
//		
//		JSONObject news = new JSONObject();
//		news.put("news", articles);
//		
//		return getMessage(toTagList, "news", agentid, news);
//	}
}
