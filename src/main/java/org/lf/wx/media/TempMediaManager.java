package org.lf.wx.media;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.log4j.Logger;
import org.lf.wx.utils.WXErrCode;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E4%B8%8A%E4%BC%A0%E4%B8%B4%E6%97%B6%E7%B4%A0%E6%9D%90%E6%96%87%E4%BB%B6
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96%E4%B8%B4%E6%97%B6%E7%B4%A0%E6%9D%90%E6%96%87%E4%BB%B6
 * 
 * 1、对于临时素材，每个素材（media_id）会在开发者上传或粉丝发送到微信服务器3天后自动删除。
 * 2、素材的格式大小等要求与公众平台官网一致。具体是：
 *   a) 图片（image）: 1M, 支持bmp/png/jpeg/jpg/gif格式，
 *   b) 音（voice）：2M，长度不超过60秒，支持mp3/wma/wav/amr格式
 *   c) 视频（video）：10MB，支持MP4格式 
 *   d) 缩略图（thumb）：64KB，支持JPG格式 
 */
public class TempMediaManager {
	private static Logger logger = Logger.getLogger(TempMediaManager.class);
	
	/**
	 * 上传临时素材
	 */
	public static final String UPLOAD_MEDIA = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
	/**
	 * 下载临时素材
	 */
	public static final String GET_MEDIA = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
	
	/**
	 * 上传临时素材文件.
	 * 用于上传图片、语音、视频等媒体资源文件以及普通文件（如doc，ppt），接口返回媒体资源标识ID：media_id。请注意，media_id是可复用的，
	 * 同一个media_id可用于消息的多次发送(3天内有效)。该接口即原"上传多媒体文件"接口。
	 * 
	 * @param mediaFile 要上传的临时素材文件
	 * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和文件（file）
	 * @param accessToken 
	 * 
	 * @return 
{
   "type": "image",
   "media_id": "1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0",
   "created_at": "1380000000"
}
	 * 
	 * @throws WXException  错误情况下的返回JSON数据包示例如下{"errcode":40004,"errmsg":"invalid media type"}
	 */
	public static JSONObject uploadMedia(String accessToken, File mediaFile, MediaType type) throws WXException {
		if (mediaFile == null || type == null) {
			throw new NullPointerException();
		}
		if (! (type == MediaType.image || type == MediaType.voice || type == MediaType.video || type == MediaType.file)) {
			throw new IllegalArgumentException("媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和文件（file）");
		}
		
		FilePart media = null;
		try {
			media = new FilePart("media", mediaFile);
		} catch (FileNotFoundException e) {
			throw new WXException(WXErrCode.MEDIA_NOT_FOUND);
		}
		
		String url = String.format(UPLOAD_MEDIA, accessToken, type.toString());
		Map<String, String> params = new HashMap<>();
		JSONObject json = WXUtils.uploadFile(url, params, media);
		
		// 首先判断下载是否出错，例如下{"errcode":40004,"errmsg":"invalid media type"}
		if (json.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(json);
		}

		return json;
	}
	
	/**
	 * 获取临时素材文件
	 * 通过media_id获取图片、语音、视频等文件，协议和普通的http文件下载完全相同。该接口即原"下载多媒体文件"接口。
	 * 
	 * @param mediaFile 素材存储文件路径
	 * @param mediaId 素材ID（对应上传后获取到的ID）
	 * 
	 * @return 素材文件
	 * @comment 不支持视频文件的下载
	 */
	public static void downloadMedia(String accessToken, File mediaFile, String mediaId) throws WXException {
		String url = String.format(GET_MEDIA, accessToken, mediaId);
		
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(mediaFile));
			WXUtils.downloadFile(url, "GET", null, out);
		} catch (FileNotFoundException e) {
			logger.error("下载临时素材失败，文件未找到。mediaFile=" + mediaFile.getAbsolutePath(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
	}
}
