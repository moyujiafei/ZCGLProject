package org.lf.wx.media;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

/**
 * 公众号经常有需要用到一些临时性的多媒体素材的场景，例如在使用接口特别是发送消息时，
 * 对多媒体文件、多媒体消息的获取和调用等操作，是通过media_id来进行的。
 * 素材管理接口对所有认证的订阅号和服务号开放（注：自定义菜单接口和素材管理接口向第三方平台旗下未认证订阅号开放）。
 * 
 */
public class TempMedia extends WXJSON {
	// 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
	public static final String TYPE = "type";
	// 媒体文件上传后，获取时的唯一标识
	public static final String MEDIA_ID = "media_id";
	// 媒体文件上传时间戳
	public static final String CREATED_AT = "created_at";

	private String mediaType;
	private String mediaId;
	private String createdAt;
	
	public TempMedia() {}
	
	/**
	 * 公众号经常有需要用到一些临时性的多媒体素材的场景，例如在使用接口特别是发送消息时，
	 * 对多媒体文件、多媒体消息的获取和调用等操作，是通过media_id来进行的。
	 * @param mediaType 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
	 * @param mediaId 媒体文件上传后，获取时的唯一标识
	 * @param createdAt 媒体文件上传时间戳
	 */
	public TempMedia(JSONObject json) {
		super(json);

		this.mediaType = json.getString(TYPE);
		this.mediaId = json.getString(MEDIA_ID);
		this.createdAt = json.getString(CREATED_AT);
	}

	/**
	 * 公众号经常有需要用到一些临时性的多媒体素材的场景，例如在使用接口特别是发送消息时，
	 * 对多媒体文件、多媒体消息的获取和调用等操作，是通过media_id来进行的。
	 * @param mediaType 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
	 * @param mediaId 媒体文件上传后，获取时的唯一标识
	 * @param createdAt 媒体文件上传时间戳
	 */
	public TempMedia(String mediaType, String mediaId, String createdAt) {
		super();
		this.mediaType = mediaType;
		this.mediaId = mediaId;
		this.createdAt = createdAt;
	}

	public String getMediaType() {
		return mediaType;
	}

	public String getMediaId() {
		return mediaId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(TYPE, mediaType);
		json.put(MEDIA_ID, mediaId);
		json.put(CREATED_AT, createdAt);

		return json;
	}

}
