package org.lf.wx.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token，
 * 开发者需要妥善保存access_token的存储至少要保留512个字符空间。
 * access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
 * 并且每天调用获取access_token接口的上限是2000次。
 */
public class AccessToken extends WXJSON {
	public static final String ACCESS_TOKEN = "access_token";
	public static final String EXPIRES_IN = "expires_in";

	// 获取到的凭证
	private String accessToken;
	// 凭证有效时间，单位：秒
	private int expiresin;
	
	public AccessToken(JSONObject json) {
		this.accessToken = json.getString(ACCESS_TOKEN);
		this.expiresin = json.getIntValue(EXPIRES_IN);
	}

	public AccessToken(String accessToken, int expiresin) {
		super();
		this.accessToken = accessToken;
		this.expiresin = expiresin;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(int expiresin) {
		this.expiresin = expiresin;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(ACCESS_TOKEN, accessToken);
		json.put(EXPIRES_IN, expiresin);
		
		return json;
	}
}
