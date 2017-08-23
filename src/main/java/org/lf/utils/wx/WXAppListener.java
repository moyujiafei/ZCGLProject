package org.lf.utils.wx;

import org.apache.log4j.Logger;
import org.lf.wx.utils.AccessToken;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class WXAppListener {
	private static Logger logger = Logger.getLogger(WXAppListener.class);
	
	private WXAppEvent event;
	
	// 企业号token 接口
	private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
	
	private static final String JSSDK_URL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=%s";
	
	public WXAppListener(WXAppEvent event) {
		this.event = event;
	}
	
	/**
	 * 注意：这里stop标记为true时，系统并不会中断对应的线程。
	 */
	public void handleEvent() {
		if (event.getThreadId() == null) {
			// 如果stop为false， 但该应用没有运行，启动应用线程
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							if (Thread.interrupted() == true) {
								logger.debug("appId (" + event.getAppId() + ") 线程终止");
								break;
							}
							// 获取accessToken
							AccessToken token = null;
							
							String tokenUrl = String.format(ACCESS_TOKEN_URL, event.getCorpId(), event.getSecret());
							String response = WXUtils.downloadString(tokenUrl, "GET", null);
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
								String apiUrl = String.format(JSSDK_URL, token.getAccessToken());
								json = JSON.parseObject(WXUtils.downloadString(apiUrl, "GET", null));
								// 更新accessToken和ticket
								event.setAccessToken(token.getAccessToken());
								event.setJsapiTicket(json.getString("ticket"));
								logger.debug("appId (" + event.getAppId() + ") get jsapi_ticket: " + json.getString("ticket"));
								logger.debug("appId (" + event.getAppId() + ") get access_token: " + token.getAccessToken());
								// 获取到access_token 休眠7000秒,大约2个小时左右
								Thread.sleep((token.getExpiresin() - 200) * 1000);// 提前200秒获取token
							} else {
								// 获取失败
								Thread.sleep(1000 * 10); // 获取的access_token为空 休眠3秒
							}
						} catch(InterruptedException eI) {
							Thread.currentThread().interrupt(); // 把中断向量再次置为true  
							logger.error("appId (" + event.getAppId() + ") 发生异常：" + eI.getMessage());
						}
						catch (Exception e) {
							logger.error("appId (" + event.getAppId() + ") 发生异常：" + e.getMessage());
							try {
								Thread.sleep(1000 * 10); // 发生异常休眠1秒
							} catch (Exception e1) {
								logger.error("appId (" + event.getAppId() + ") 发生异常：" + e1.getMessage());
							}
						}
					}
				}
			});
			thread.start();
			event.setThreadId(thread.getId());
		} 
	}

	public WXAppEvent getEvent() {
		return event;
	}
	
}
