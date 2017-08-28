package org.lf.admin.service.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.coobird.thumbnailator.Thumbnails;

import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.logs.ZTService;
import org.lf.utils.DateUtils;
import org.lf.utils.StringUtils;
import org.lf.utils.servlet.LoginInterceptor;
import org.lf.wx.media.MediaType;
import org.lf.wx.media.TempMediaManager;
import org.lf.wx.utils.WXException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

/**
 * 微信素材上传下载服务
 */
@Service("wxMediaService")
public class WXMediaService {
	/**
	 * 图片格式
	 */
	public static final String IMAGE_FORMAT = "jpg";
	/**
	 * 图片后缀
	 */
	public static final String IMAGE_SUFFIX = "." + IMAGE_FORMAT;
	/**
	 * 缩略图后缀
	 */
	public static final String THUMBNAIL_SUFFIX = "_m" + IMAGE_SUFFIX;
	/**
	 * 音频文件后缀，企业微信只支持amr格式
	 */
	public static final String VOICE_SUFFIX = ".amr";

	public static final long MAX_IMAGE_SIZE = 1 * 1024 * 1024; // 1M
	public static final long MAX_VOICE_SIZE = 2 * 1024 * 1024; // 2M
	public static final long MAX_LOGO_SIZE = 256 * 1024; // 企业logo大小256k

	public Map<String, MediaType> uploadMediaList(HttpSession session, @RequestParam(value = "image_upload", required = false) MultipartFile[] imageFileList,
			@RequestParam(value = "voice_upload", required = false) MultipartFile[] voiceFileList) throws OperException {
		// 通过session获取appid
		ChuUser user = (ChuUser) session.getAttribute(LoginInterceptor.LOGIN_INFO);
		Integer appid = user == null ? null : user.getAppId();

		String mediaPath = session.getServletContext().getRealPath("") + ZCGLProperties.URL_MEDIA_TARGET_DIR + "/" + appid;

		String today = DateUtils.toString("yyyy-MM-dd", new Date());
		String images = MediaType.image.name();
		String voices = MediaType.voice.name();
		File imagesDir = new File(mediaPath + "/" + today + "/" + images);
		if (!imagesDir.exists()) {
			imagesDir.mkdirs();
		}
		File voicesDir = new File(mediaPath + "/" + today + "/" + voices);
		if (!voicesDir.exists()) {
			voicesDir.mkdirs();
		}

		// 管理控制台上传图片，使用uuid作为mediaId。
		String mediaId;
		Map<String, MediaType> mediaList = new HashMap<>();
		if (imageFileList != null) {
			for (MultipartFile imageFile : imageFileList) {
				mediaId = StringUtils.getUUID();
				try {
					if (imageFile.getSize() > MAX_IMAGE_SIZE) {
						throw new OperException("100002", "上传图片文件超过1M");
					}

					imageFile.transferTo(new File(imagesDir, mediaId + IMAGE_SUFFIX));
				} catch (IllegalStateException | IOException e) {
					throw new OperException("100001", "上传文件出错");
				}
				mediaList.put(mediaId, MediaType.image);

				// 生成缩略图
				try {
					Thumbnails.of(new File(imagesDir, mediaId + IMAGE_SUFFIX)).size(ZTService.WIDTH, ZTService.HIGHT)
					// 缩略图大小
							.outputFormat(IMAGE_FORMAT)
							// 缩略图格式
							// .outputQuality(0.8f)//缩略图质量
							.toFile(new File(imagesDir, mediaId + THUMBNAIL_SUFFIX));
				} catch (IOException e) {
					throw new OperException(ZTService.无法生成缩略图);
				}
			}
		}
		if (voiceFileList != null) {
			for (MultipartFile voiceFile : voiceFileList) {
				mediaId = StringUtils.getUUID();
				try {
					if (voiceFile.getSize() > MAX_VOICE_SIZE) {
						throw new OperException("100003", "上传音频文件超过2M");
					}

					voiceFile.transferTo(new File(voicesDir, mediaId + VOICE_SUFFIX));
				} catch (IllegalStateException | IOException e) {
					throw new OperException("100001", "上传文件出错");
				}
				mediaList.put(mediaId, MediaType.voice);
			}
		}
		return mediaList;
	}

	/**
	 * 上传文件到指定路径
	 * 
	 * @param session
	 * @param prePath
	 *            上传文件路径
	 * @param maxSize
	 *            上传文件最大值
	 * @param fileType
	 *            上传文件类型，这里指文件后缀名，例如".jpg"
	 * @param FileList
	 *            上传的文件
	 * @return 如果上传成功，返回文件的路径。 如果上传失败，返回null。
	 * @throws OperException
	 */
	public String uploadMediaListToPath(HttpSession session, String prePath, long maxSize, String fileType, MultipartFile[] FileList) throws OperException {
		String filePath = session.getServletContext().getRealPath("") + prePath;
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		String fileName = null;
		if (FileList != null) {
			for (MultipartFile file : FileList) {
				try {
					if (file.getSize() > maxSize) {
						throw new OperException("100002", "上传文件超过限定大小");
					}
					String mediaId = StringUtils.getUUID();
					fileName = mediaId;
					// 下载原图
					file.transferTo(new File(fileDir, fileName + fileType));

					if (fileType.equals(IMAGE_SUFFIX)) {
						// 生成缩略图
						Thumbnails.of(new File(fileDir, fileName + fileType)).size(ZTService.WIDTH, ZTService.HIGHT)
						// 缩略图大小
								.outputFormat(IMAGE_FORMAT)
								// 缩略图格式
								// .outputQuality(0.8f)//缩略图质量
								.toFile(new File(fileDir, fileName + THUMBNAIL_SUFFIX));
					}

				} catch (IllegalStateException | IOException e) {
					throw new OperException("100001", "上传文件出错");
				}
			}
		}
		if (fileName != null) {
			return prePath + "/" + fileName;
		} else {
			return null;
		}
	}

	/**
	 * 下载微信素材到本地
	 * 
	 * @param prePath
	 *            上传文件路径
	 * @param fileType
	 *            上传文件类型，这里指文件后缀名，例如".jpg"
	 * @param FileList
	 *            上传的文件
	 * @return 如果上传成功，返回文件的路径。 如果上传失败，返回null。
	 * @throws OperException
	 */
	public String downloadMediaToPath(String path, String accessToken, String fileType, String mediaId) throws OperException {
		ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		String realPath = servletContext.getRealPath("");
		String filePath = realPath + "/" + path;
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		String fileName = null;
		if (mediaId != null) {
			fileName = mediaId;
			// 下载原图
			try {
				TempMediaManager.downloadMedia(accessToken, new File(filePath + "/" + mediaId + fileType), mediaId);
				if (fileType.equals(IMAGE_SUFFIX)) {
					// 生成缩略图
					Thumbnails.of(new File(fileDir, fileName + fileType)).size(ZTService.WIDTH, ZTService.HIGHT)
					// 缩略图大小
							.outputFormat(IMAGE_FORMAT)
							// 缩略图格式
							// .outputQuality(0.8f)//缩略图质量
							.toFile(new File(fileDir, fileName + THUMBNAIL_SUFFIX));
				}
			} catch (WXException | IOException e) {
				throw new OperException("100002", "下载文件出错");
			}

		}
		if (fileName != null) {
			return path + "/" + fileName;
		} else {
			return null;
		}
	}
}
