package org.lf.wx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.lf.wx.utils.WXUtils;

public class WXProperties {
	private static Logger logger = Logger.getLogger(WXUtils.class);
	private static Properties p;

	static {
		InputStream inputStream = WXProperties.class.getClassLoader().getResourceAsStream("wx.properties");
		p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e) {
			logger.error("读取配置文件出错", e);
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("读取配置文件出错", e);
				}
			}
		}
	}

	private static String getProperty(String key) {
		String value = p.getProperty(key);
		if (value == null) {
			return null;
		} else {
			return value.trim();
		}
	}

	public static final int AGENT_ID = Integer.parseInt(getProperty("wx.agentid"));
	public static final String APP_ID = getProperty("wx.app_id");
	public static final String APP_SECRET = getProperty("wx.app_secret");
	public static final String TOKEN = getProperty("wx.token");
	public static final String WX_SERVER_URL = getProperty("wx.server.url");

	/**
	 * 是否开启“加密模式”
	 */
	public static final boolean WX_ENCRYPT = getProperty("wx.encrypt") == null ? false : Boolean.parseBoolean(getProperty("wx.encrypt"));

	/**
	 * EncodingAESKey(消息加解密密钥)
	 */
	public static final String WX_AES_KEY = getProperty("wx.aes_key") == null ? "" : getProperty("wx.aes_key");

	/**
	 * 开发者标识
	 */
	public static final String CAMPUS_OPENAPPID = getProperty("campus.openAppID");
	/**
	 * 开发者标识
	 */
	public static final String CAMPUS_APPSECRET = getProperty("campus.AppSecret");
	/**
	 * 开发者标识
	 */
	public static final String CAMPUS_DEVCODE = getProperty("campus.devCode");
	/**
	 * 开发者类型 2-学校，3-合作伙伴，5-第三方开发者
	 */
	public static final String CAMPUS_DEVTYPE = getProperty("campus.devType");
	/**
	 * 学校ID有2种获取方式： 1.由学校提供。可以在学校后台==》学校管理==》open_objectid中获取到，仅学校创建者可以看到；
	 * 2.已创建第三方应用，且学校已安装和使用该应用时，可由腾讯智慧校园在url中返回
	 */
	public static final String CAMPUS_OBJECTID = getProperty("campus.objectid");
	/**
	 * object类型（1：上级单位，2：学校）
	 */
	public static final String CAMPUS_OBJTYPE = getProperty("campus.objType");
	/**
	 * 学校接口授权密钥标号。当学校自己开发API时，可不传。当学校授权给第三方开发者时，需要给第三方开发者授权密钥的同时，给出授权密钥标号，且为必传参数
	 * 。 获取方式：学校后台==》接口授权==》授权密钥标号
	 */
	public static final String CAMPUS_KEY = getProperty("campus.key");
}
