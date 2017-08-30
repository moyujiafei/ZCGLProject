package org.lf.wx.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.log4j.Logger;
import org.lf.utils.StringUtils;
import org.lf.wx.WXProperties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class WXUtils {
	private static Logger logger = Logger.getLogger(WXUtils.class);

	// 企业号token 接口
	private static final String ACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

	private static AccessToken token;
	private static String jsapi_ticket;

	static {
		// 开启一个新的线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// 获取accessToken

						String tockenUrl = String.format(ACCESS_TOKEN, WXProperties.APP_ID, WXProperties.APP_SECRET);
						String response = downloadString(tockenUrl, "GET", null);
						JSONObject json = JSON.parseObject(response);
						if (null != json) {
							try {
								token = new AccessToken(json);
							} catch (JSONException e) {
								token = null;// 获取token失败
							}
						}
						// 获取成功
						if (token != null) {
							// 企业号获取jsapi_ticket
							String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + token.getAccessToken();
							json = JSON.parseObject(downloadString(url, "GET", null));
							jsapi_ticket = json.getString("ticket");
							logger.debug("jsapi_ticket: " + jsapi_ticket);
							logger.debug("access_token: " + token.getAccessToken());
							// 获取到access_token 休眠7000秒,大约2个小时左右
//							Thread.sleep(2 * 60 * 60 * 1000);
							Thread.sleep((token.getExpiresin() - 200) * 1000);//提前200秒获取token
						} else {
							// 获取失败
							Thread.sleep(1000 * 3); // 获取的access_token为空 休眠3秒
						}
					} catch (Exception e) {
						logger.error("发生异常：" + e.getMessage());
						try {
							Thread.sleep(1000 * 10); // 发生异常休眠1秒
						} catch (Exception e1) {
							logger.error("发生异常：" + e1.getMessage());
						}
					}
				}

			}
		}).start();
	}

	/**
	 * 返回当前系统时间对应的长整型数。并以String类型方式返回。
	 */
	public static String getTime() {
		return getTime(new Date());
	}

	public static String getTime(Date date) {
		long dd = date.getTime();
		return String.valueOf(dd);
	}

	/**
	 * 返回指定时间对应的日期。这里，time是微信传过来的createTime，以秒为单位。
	 */
	public static String getDate(String time, SimpleDateFormat format) {
		Long dd = Long.parseLong(time);
		Date date = new Date(dd.longValue() * 1000);
		if (format == null) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return format.format(date);
	}

	public static String getDate(String time) {
		return getDate(time, null);
	}

	/**
	 * 发起Https请求
	 * 
	 * @param reqUrl
	 *            请求的URL地址
	 * @param requestMethod
	 * @return 响应后的字符串
	 */
	public static String getHttpsResponse(String reqUrl, String requestMethod) {
		URL url;
		InputStream is;
		String resultData = "";

		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}
		};

		try {
			url = new URL(reqUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			TrustManager[] tm = { xtm };

			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tm, null);

			con.setSSLSocketFactory(ctx.getSocketFactory());
			con.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});

			con.setDoInput(true); // 允许输入流，即允许下载

			// 在android中必须将此项设置为false
			con.setDoOutput(false); // 允许输出流，即允许上传
			con.setUseCaches(false); // 不使用缓冲
			if (null != requestMethod && !requestMethod.equals("")) {
				con.setRequestMethod(requestMethod); // 使用指定的方式
			} else {
				con.setRequestMethod("GET"); // 使用get请求
			}
			is = con.getInputStream(); // 获取输入流，此时才真正建立链接
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferReader = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = bufferReader.readLine()) != null) {
				resultData += inputLine + "\n";
			}
			logger.debug(resultData);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultData;
	}

	/**
	 * 依据本地Property生成AccessToken通用接口获取Token凭证
	 */
	public static String getLocalToken() {
		if (token == null || token.getAccessToken() == null) {
			String tockenUrl = String.format(ACCESS_TOKEN, WXProperties.APP_ID, WXProperties.APP_SECRET);
			String response = downloadString(tockenUrl, "GET", null);
			JSONObject json = JSON.parseObject(response);
			if (json.containsKey(WXErrCode.ERR_CODE) && json.getIntValue(WXErrCode.ERR_CODE) != 0) {
				logger.error(json.toJSONString());
			}
			token = new AccessToken(json);
		}

		return token.getAccessToken();
	}

	/**
	 * 获取微信的配置信息 其中signature为微信权限签名。详见：《15 附录1-JS-SDK使用权限签名算法》
	 * https://mp.weixin.qq.com/wiki/11/74ad127cc054f6b80759c40f77ec03db.html
	 * 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）,
	 * url（当前网页的URL，不包含#及其后面部分） 。 对所有待签名参数按照字段名的ASCII
	 * 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1。
	 * 这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
	 * 
	 * @param request
	 * @return 说明返回值含义
	 * @throws 说明发生此异常的条件
	 */
	public static Map<String, Object> getWXConfig(HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<String, Object>();

		String appId = WXProperties.APP_ID; // 必填，公众号的唯一标识
		String requestUrl = request.getRequestURL().toString();
		// 拼接请求协议https
		requestUrl = WXProperties.WX_SERVER_URL.split("://")[0] + requestUrl.substring(requestUrl.indexOf("://"));
		String queryParam = request.getQueryString();
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = queryParam.split("#")[0];// 去掉参数末尾#部分
			requestUrl += "?" + queryParam;// 拼接请求参数
		}
		String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
		String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串

		// 获取签名
		String signature = "";


		// 企业号签名
		String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + requestUrl;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(sign.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		ret.put("appId", appId);
		ret.put("timestamp", timestamp);
		ret.put("nonceStr", nonceStr);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;

	}

	/**
	 * 发送请求以https方式发送请求并将请求响应内容以String方式返回
	 * 
	 * @param path
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param body
	 *            请求数据体
	 * @return 请求响应内容转换成字符串信息
	 */
	public static String downloadString(String path, String method, String body) {
		if (path == null || method == null) {
			return null;
		}
		method = method.toUpperCase();
		String response = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpsURLConnection conn = null;
		try {
			TrustManager[] tm = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			logger.info(path);
			URL url = new URL(path);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			response = buffer.toString();
		} catch (Exception e) {

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * 以http方式发送请求,并将请求响应内容输出到文件
	 * 
	 * @param path
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param body
	 *            请求数据
	 * @return 返回响应的存储到文件
	 */
	public static void downloadFile(String path, String method, String body, OutputStream out) throws WXException {
		if (out == null || path == null || method == null) {
			throw new IllegalArgumentException();
		}

		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = conn.getInputStream();
			if (inputStream == null) {
				throw new WXException(WXErrCode.MEDIA_NOT_FOUND);
			}

			// 写入到文件
			int c = inputStream.read();
			while (c != -1) {
				out.write(c);
				c = inputStream.read();
			}
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				conn.disconnect();
			}

			/*
			 * 必须关闭文件流 否则JDK运行时，文件被占用其他进程无法访问
			 */
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private static Part[] toParts(Map<String, String> params, FilePart media) {
		Part[] parts = new Part[params.size() + 1];

		Part part;
		Iterator<String> it = params.keySet().iterator();
		int i = 0;
		String key;
		while (it.hasNext()) {
			key = it.next();
			part = new StringPart(key, params.get(key));
			parts[i] = part;
			i++;
		}
		parts[i] = media;

		return parts;
	}

	public static JSONObject uploadFile(String url, Map<String, String> params, FilePart media) throws WXException {
		if (url == null || params == null || media == null) {
			throw new IllegalArgumentException("input arguments invalidate");
		}

		JSONObject jsonObject = null;
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Cache-Control", "no-cache");
		HttpClient httpClient = new HttpClient();

		// 信任任何类型的证书
		Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		try {
			Part[] parts = toParts(params, media);
			MultipartRequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(entity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String text = post.getResponseBodyAsString();
				jsonObject = JSON.parseObject(text);
			} else {
				logger.error("upload Media failure status is:" + status);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (HttpException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		// 如果上传操作不成功，json中会包含对应的错误信息。
		if (jsonObject.getIntValue(WXErrCode.ERR_CODE) != 0) {
			throw new WXException(jsonObject);
		}

		return jsonObject;
	}

	private static Part[] toParts(Map<String, String> params) {
		Part[] parts = new Part[params.size()];

		Part part;
		Iterator<String> it = params.keySet().iterator();
		int i = 0;
		String key;
		while (it.hasNext()) {
			key = it.next();
			part = new StringPart(key, params.get(key));
			parts[i] = part;
			i++;
		}

		return parts;
	}

	public static JSONObject uploadFile(String url, Map<String, String> params) throws WXException {
		if (url == null || params == null) {
			throw new IllegalArgumentException("input arguments invalidate");
		}

		JSONObject jsonObject = null;
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Cache-Control", "no-cache");
		HttpClient httpClient = new HttpClient();

		// 信任任何类型的证书
		Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		try {
			Part[] parts = toParts(params);
			MultipartRequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(entity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String text = post.getResponseBodyAsString();
				jsonObject = JSON.parseObject(text);
			} else {
				logger.error("upload Media failure status is:" + status);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (HttpException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		// 如果上传操作不成功，json中会包含对应的错误信息。
		if (jsonObject.containsKey(WXErrCode.ERR_CODE)) {
			throw new WXException(jsonObject);
		}

		return jsonObject;
	}

}
