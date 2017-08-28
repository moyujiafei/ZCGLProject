package org.lf.admin.service.logs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import net.coobird.thumbnailator.Thumbnails;

import org.lf.admin.db.dao.LZTMapper;
import org.lf.admin.db.dao.LZTXZMapper;
import org.lf.admin.db.dao.VZTMapper;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.VZT;
import org.lf.admin.db.pojo.VZTXZ;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.utils.DateUtils;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.lf.wx.media.MediaType;
import org.lf.wx.media.TempMediaManager;
import org.lf.wx.utils.WXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 资产状态管理 
 * 
 * @author 杨靖
 */
@Service("ztService")
public class ZTService {
	public static final int WIDTH = 300;
	public static final int HIGHT = 300;
	
	public static final OperErrCode 资产编号不能为空 = new OperErrCode("10201", "资产编号不能为空");
	public static final OperErrCode 记录人不能为空 = new OperErrCode("10202", "记录人不能为空");
	public static final OperErrCode 新状态不能为空 = new OperErrCode("10203", "新状态不能为空");
	
	public static final OperErrCode 仅支持音频和图片素材下载 = new OperErrCode("10210", "仅支持音频和图片素材下载");
	public static final OperErrCode 微信素材下载失败 = new OperErrCode("10211", "微信素材下载失败");
	public static final OperErrCode 微信素材不存在 = new OperErrCode("10212", "微信素材不存在");
	public static final OperErrCode 无法生成缩略图 = new OperErrCode("10212", "无法生成缩略图");
	
	@Autowired
	private LZTMapper ztDao;
	
	@Autowired
	private LZTXZMapper ztxzDao;
	
	@Autowired
	private VZTMapper vztDao;
	
	@Autowired
	private WXAppService appService;
	
	/**
	 * 调用TempMediaManager的downloadMedia，将微信服务器上mediaId对应的文件下载到指定targetDir目录下。命名方式如下：
	 * a) targetDir/<YYYY-MM-DD>/<mediaType>/mediaId.xxx
	 * b) 其中：YYYY-MM-DD为发生时间，mediaType为介质类型（目前仅支持音频mp3）。
	 * c) 下载的文件以mediaId命名，后缀名为音频mp3和图片jpg
	 * 
	 * @param targetDir
	 * @param fssj 发生时间
	 * @param type 介质类型
	 * @param mediaId
	 * 
	 * @see TempMediaManager#downloadMedia(String, File, String)
	 * 
	 * @exception 微信素材下载失败，仅支持音频和视频素材下载
	 */
	public void downloadWXMedia(Integer appId, String accessToken, File targetDir, Date fssj, MediaType type, String mediaId) throws OperException {
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		String fssjStr=sdf.format(fssj);
		String path = targetDir.getAbsolutePath();		//获取指定目录的绝对路径字符串
		File file=null;
		File dir=null;
		//根据发生时间和文件类型生成临时素材文件目录和文件
		switch (type) {
		case image:
			path = path + "/" + appId + "/" + fssjStr +"/" + MediaType.image.name();
			dir = new File(path);
			if (!dir.exists())
				dir.mkdirs();
			file = new File(dir, mediaId + WXMediaService.IMAGE_SUFFIX);
			break;
		case voice:
			path = path + "/" + appId  + "/" + fssjStr + "/" + MediaType.voice.name();
			dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = new File(dir, mediaId + WXMediaService.VOICE_SUFFIX);
			break;
		default:
			throw new OperException(仅支持音频和图片素材下载);
		}

		// 从微信服务器下载微信素材
		try {
			TempMediaManager.downloadMedia(accessToken, file, mediaId);
			if (type == MediaType.image) {
				getBriefImageURI(appId, fssj, type.name(), mediaId);
			}
		} catch (WXException e) {
			throw new OperException(微信素材下载失败);
		}
	}
	
	/**
	 * 根据mediaId提供的素材下载命名规范，获取素材的相当位置。素材命名规范为：
	 * a) targetDir/<appId>/<YYYY-MM-DD>/<mediaType>/mediaId.xxx
	 * b) 其中：YYYY-MM-DD为发生时间，mediaType为介质类型（目前仅支持音频mp3和视频mp4）。
	 * c) 下载的文件以mediaId命名，后缀名为音频mp3和图片jpg
	 * @param targetDir 目标目录。读取环境表中相关参数来设置
	 * @param jlsj 记录时间
	 * @param type 介质类型
	 * @param mediaId
	 * @return
	 * 
	 * @see #downloadWXMedia(String, File, Date, MediaType, String)
	 * 
	 * @exception 微信素材不存在
	 */
	public String getWXMedia(Integer appId, Date jlsj, String type, String mediaId) throws OperException {
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		String jlsjStr=sdf.format(jlsj);
		try {
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			String realPath = servletContext.getRealPath("");
			String filePath = realPath + "/" + ZCGLProperties.URL_MEDIA_TARGET_DIR + "/" + appId;
			if(type.equals(MediaType.image.name())){
				filePath += "/" + jlsjStr + "/" + type + "/" + mediaId + WXMediaService.IMAGE_SUFFIX;
			}else if(type.equals(MediaType.voice.name())){
				filePath += "/" + jlsjStr + "/" + type + "/" + mediaId + WXMediaService.VOICE_SUFFIX;
			}else{
				throw new Exception();
			}
			return filePath;
		} catch (Exception e) {
			throw new OperException(微信素材不存在);
		}
		
	}
	
	/**
	 *获得素材在服务器的地址
	 * 
	 * @see #downloadWXMedia(String, File, Date, MediaType, String)
	 * 
	 * @exception 微信素材不存在
	 */
	public String getWXMediaUrl(Integer appId, Date jlsj, String type, String mediaId) throws OperException {
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		String jlsjStr=sdf.format(jlsj);
		try {
			String serverPath = ZCGLProperties.URL_SERVER;
			String filePath = serverPath + ZCGLProperties.URL_MEDIA_TARGET_DIR + "/" + appId;
			if(type.equals(MediaType.image.name())){
				filePath += "/" + jlsjStr + "/" + type + "/" + mediaId + WXMediaService.IMAGE_SUFFIX;
			}else if(type.equals(MediaType.voice.name())){
				filePath += "/" + jlsjStr + "/" + type + "/" + mediaId + WXMediaService.VOICE_SUFFIX;
			}else{
				throw new Exception();
			}
			return filePath;
		} catch (Exception e) {
			throw new OperException(微信素材不存在);
		}
		
	}
	
	
	public int countZTList(VZT param) {
		return vztDao.countVZTList(param);
	}
	
	/**
	 * 查找资产状态日志列表.
	 * 注意：这里允许OLD_ZT条件为空。如新购资产时日志记录，OLD_ZT为空，NEW_ZT为“未使用”
	 * @param param
	 * @return
	 */
	public List<VZT> getZTList(VZT param) {
		List<VZT> ztList=vztDao.selectList(param);
		return ztList;
	}
	
	public List<LZT> getZTList(LZT param) {
		return ztDao.selectList(param);
	}
	
	public LZT getZT(Integer ztid) {
		return ztDao.selectByPrimaryKey(ztid);
	}
	
	public List<LZTXZ> getZTXZList(Integer ztid, MediaType mediaType) {
		LZTXZ param = new LZTXZ();
		param.setJlId(ztid);
		param.setMediaType(mediaType.name());
		return ztxzDao.selectList(param);
	}
	
	/**
	 * 根据mediaId提供的素材下载命名规范，获取素材对应的缩略图。
	 * 
	 * 如果是图片素材，缩略图命名规范为：
	 * a) targetDir/<YYYY-MM-DD>/<mediaType>/mediaId_m.xxx
	 * b) 其中：YYYY-MM-DD为发生时间
	 * c) 下载的文件以mediaId_m命名，后缀名为jpg
	 * d) 如果图片素材不存在
	 * @param targetDir 目标目录。读取环境表中相关参数来设置
	 * @param jlsj 记录时间
	 * @param type 介质类型
	 * @param mediaId
	 * @return
	 * 
	 * @see #downloadWXMedia(String, File, Date, MediaType, String)
	 * 
	 * @exception 微信素材不存在
	 */
	private String getBriefImageURI(Integer appId, Date jlsj, String type, String mediaId) throws OperException {
		if (MediaType.image.name().equals(type)) {

			// 得到文件绝对路径
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			String realPath = servletContext.getRealPath("");
			String filePath = realPath + ZCGLProperties.URL_MEDIA_TARGET_DIR + "/" + appId;
			filePath += "/" + DateUtils.toString("YYYY-MM-dd", jlsj) + "/" + type + "/" + mediaId + WXMediaService.THUMBNAIL_SUFFIX;
			
			File file = new File(filePath);
			String imgUrl = ZCGLProperties.URL_SERVER + ZCGLProperties.URL_MEDIA_TARGET_DIR + "/" + appId + "/" + DateUtils.toString("YYYY-MM-dd", jlsj) + "/" + type + "/" + mediaId + WXMediaService.THUMBNAIL_SUFFIX;
			if (file.exists()) {
				// 如果图片素材缩略图存在，直接返回。
				return imgUrl;
			} else {
				// 缩略图不存在，依据原图生成一个。
				// 生成缩略图
				try {
					Thumbnails
							.of(new File(getWXMedia(appId, jlsj, type, mediaId)))
							.size(WIDTH, HIGHT)
							// 缩略图大小
							.outputFormat(WXMediaService.IMAGE_FORMAT)
							// 缩略图格式
							// .outputQuality(0.8f)//缩略图质量
							.toFile(file);
				} catch (IOException e) {
					e.printStackTrace();
					throw new OperException(无法生成缩略图);
				}
				
				return imgUrl;
			}
			
		} else {
			// 音频素材，统一使用缺省的缩略图
			return ZCGLProperties.URL_SERVER + "/images/briefimage_voice" +WXMediaService.IMAGE_SUFFIX;
		}
	}
	
	/**
	 * 将LZTXZ转换为能够在网页中显示的VZTXZ。
	 * @param ztxz
	 * @return
	 */
	public VZTXZ translateZTXZ(LZT zt, LZTXZ lxz) throws OperException {
		VZTXZ vxz = new VZTXZ();
		
		// 基本信息
		vxz.setId(lxz.getId());
		vxz.setAppId(lxz.getAppId());
		vxz.setJlId(lxz.getJlId());
		vxz.setMediaType(lxz.getMediaType());
		vxz.setWxMediaId(lxz.getWxMediaId());
		
		// 状态细则中图片、音频的连接信息。
		vxz.setJlsj(zt.getJlsj());
		vxz.setMediaURI(getWXMediaUrl(zt.getAppId(), zt.getJlsj(), lxz.getMediaType(), lxz.getWxMediaId()));
		
		// 状态细则用于在页面中显示的缩略图。通常以mediaURI
		vxz.setBriefImageURI(getBriefImageURI(zt.getAppId(), zt.getJlsj(), lxz.getMediaType(), lxz.getWxMediaId()));
		
		return vxz;
	}
	
	/**
	 * 获取指定页的资产状态日志列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 * @throws OperException
	 */
	public List<VZT> getZTList(VZT param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());

		return getZTList(param);
	}
	
	/**
	 * 获取指定页的资产状态日志列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 * @throws OperException
	 */
	public EasyuiDatagrid<VZT> getPageZTList(VZT param, int rows, int page) {
		int total = countZTList(null);

		EasyuiDatagrid<VZT> pageDatas = new EasyuiDatagrid<VZT>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZT>());
		} else {
			List<VZT> ztList = getZTList(param,rows, page);
			pageDatas.setRows(ztList);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
	/**
	 * 插入一个状态日志.
	 * @param zt
	 * @throws OperException 资产编号不能为空，记录人不能为空，新状态不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZT(LZT zt) throws OperException {
		insertZT(zt, new ArrayList<LZTXZ>());
	}
	
	/**
	 * 插入一个状态日志，和一组日志细则。在插入日志细则的过程中，调用downloadWXMedia函数将用户上传的微信临时素材下载到WEB服务器上。
	 * @param zt
	 * @param xzList 为空时，不插入日志细则
	 * @throws OperException 资产编号不能为空，记录人不能为空，新状态不能为空
	 * 
	 * @see #downloadWXMedia(String, File, Date, MediaType, String)
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertZT(LZT zt, List<LZTXZ> xzList) throws OperException {
		if(StringUtils.isEmpty(zt.getZcdm())){
			throw new OperException(资产编号不能为空);
		}
		if(StringUtils.isEmpty(zt.getJlr())){
			throw new OperException(记录人不能为空);
		}
		if(zt.getNewZt() == null) {
			throw new OperException(新状态不能为空);
		}
		//插入一个资产状态日志
		ztDao.insertSelective(zt);
		//插入一组日志细则
		if(xzList==null || xzList.size()==0){
			return;	//为空时，不插入日志细则
		}
		String accessToken=appService.getAccessToken(zt.getAppId());
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        // 得到文件绝对路径
        String realPath = servletContext.getRealPath("/");
		File targetDir=new File(realPath + "/" +  ZCGLProperties.URL_MEDIA_TARGET_DIR);
		int jlId=zt.getId();
		int appId=zt.getAppId();
		for (LZTXZ xz : xzList) {
			xz.setJlId(jlId);
			xz.setAppId(appId);
			if (xz.getWxMediaId().length() != 36) {
				// mediaId如果长度为36，表明他是uuid，即由控制台上传的图片（音频）文件，无需再从微信服务器上下载
				downloadWXMedia(zt.getAppId(), accessToken, targetDir, new Date(), MediaType.valueOf(xz.getMediaType()), xz.getWxMediaId());
			}
			
			ztxzDao.insertSelective(xz);
		}
	}
	
	
}
