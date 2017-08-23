package org.lf.wx.utils;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

/**
 * 用于网页OAuth2.0授权的时候进行安全验证(企业微信)
 */
public class QyAccessToken4OAuth extends WXJSON {
	public static final String ERRCODE = "errcode";
	public static final String ERRMSG = "errmsg";
	public static final String USERID = "UserId";
	public static final String DEVICEID = "DeviceId";
	public static final String USER_TICKET = "user_ticket";
	public static final String EXPIRES_IN = "expires_in";
	public static final String OPENID = "OpenId";
	/**
	 * 成员票据，最大为512字节。
	 * scope为snsapi_userinfo或snsapi_privateinfo，且用户在应用可见范围之内时返回此参数。
	 * 后续利用该参数可以获取用户信息或敏感信息。
	 */
	private String user_ticket;
	/**
	 * user_token的有效时间（秒），随user_ticket一起返回
	 */
	private int expiresin;
	/**
	 * 返回码
	 */
	private String errcode;
	/**
	 *	对返回码的文本描述内容
	 */
	private String errmsg;
	/**
	 * 成员UserID
	 */
	private String userId;
	/**
	 * 手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
	 */
	private String deviceId;
	/**
	 * 非企业成员的标识，对当前企业唯一(非企业成员授权时返回)
	 */
	private String openId;

	public QyAccessToken4OAuth(JSONObject json) {
		this.errcode = json.getString(ERRCODE);
		this.errmsg = json.getString(ERRMSG);
		this.userId = json.getString(USERID);
		this.deviceId = json.getString(DEVICEID);
		this.user_ticket = json.getString(USER_TICKET);
		this.expiresin = json.getIntValue(EXPIRES_IN);
		this.openId = json.getString(OPENID);
	}

	public QyAccessToken4OAuth(String errcode, String errmsg, String userId, String deviceId, String user_ticket,int expiresin,String openId) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
		this.userId = userId;
		this.deviceId = deviceId;
		this.user_ticket = user_ticket;
		this.expiresin = expiresin;
		this.openId = openId;
	}
	
	
	

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();

		json.put(ERRCODE, errcode);
		json.put(ERRMSG, errmsg);
		json.put(USERID, userId);
		json.put(DEVICEID, deviceId);
		json.put(USER_TICKET, user_ticket);
		json.put(EXPIRES_IN, expiresin);
		json.put(OPENID, openId);

		return json;
	}

	public String getUser_ticket() {
		return user_ticket;
	}

	public void setUser_ticket(String user_ticket) {
		this.user_ticket = user_ticket;
	}

	public int getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(int expiresin) {
		this.expiresin = expiresin;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
