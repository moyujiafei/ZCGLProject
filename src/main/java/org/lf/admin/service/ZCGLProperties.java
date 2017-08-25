package org.lf.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.lf.wx.utils.WXUtils;

public class ZCGLProperties {
	private static Logger logger = Logger.getLogger(WXUtils.class);
	private static Properties p;

	static {
		InputStream inputStream = ZCGLProperties.class.getClassLoader().getResourceAsStream("zcgl.properties");
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

	/**
	 * 服务器地址
	 */
	public static final String URL_SERVER = getProperty("URL_SERVER");
	/**
	 * 微信前端页面地址
	 */
	public static final String URL_WX_CLIENT = getProperty("URL_WX_CLIENT");
	/**
	 * 微信素材存放位置
	 */
	public static final String URL_MEDIA_TARGET_DIR = getProperty("URL_MEDIA_TARGET_DIR");
	/**
	 * 资产类型图片存放位置
	 */
	public static final String URL_ZCLX_TARGET_DIR = getProperty("URL_ZCLX_TARGET_DIR");
	/**
	 * 易耗品类型图片存放位置
	 */
	public static final String URL_YHPLX_TARGET_DIR = getProperty("URL_YHPLX_TARGET_DIR");
	/**
	 * 资产图片存放位置
	 */
	public static final String URL_ZC_TARGET_DIR = getProperty("URL_ZC_TARGET_DIR");
	/**
	 * 易耗品图片存放位置
	 */
	public static final String URL_YHP_TARGET_DIR = getProperty("URL_YHP_TARGET_DIR");
	/**
	 * 在微信端查看用户信息
	 */
	public static final String URL_USER_INFO = getProperty("URL_USER_INFO");
	/**
	 * 在微信端查看资产信息
	 */
	public static final String URL_ZC_INFO = getProperty("URL_ZC_INFO");
	/**
	 * 在微信端查看任务信息
	 */
	public static final String URL_RW_INFO = getProperty("URL_RW_INFO");
	/**
	 * 在微信端查看资产状态信息
	 */
	public static final String URL_ZT_INFO = getProperty("URL_ZT_INFO");
	/**
	 * 数据库备份路径
	 */
	public static final String URL_BACKUP_PATH = getProperty("URL_BACKUP_PATH");
	
	
	/**
	 * 数据库相关配置信息
	 */
	public static final String DB_IP = getProperty("DB_IP");
	public static final String DB_PORT = getProperty("DB_PORT");
	public static final String DB_USERNAME = getProperty("DB_USERNAME");
	public static final String DB_PASSWORD = getProperty("DB_PASSWORD");
	public static final String DB_NAME = getProperty("DB_NAME");
	
	/**
	 * 模板微信应用编号
	 */
	public static final String TEMPLATE_APPID = getProperty("TEMPLATE_APPID");

}
