package org.lf.wx.user;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户基本信息http://qydev.weixin.qq.com/wiki/index.php?title=%E7%AE%A1%E7%90%86%E6%
 * 88%90%E5%91%98#.E8.8E.B7.E5.8F.96.E6.88.90.E5.91.98
 * 
 * 微信企业号，管理通讯录 --> 管理成员
 * 
 */
public class User extends WXJSON {
	public static final String USERID = "userid";
	public static final String NAME = "name";
	public static final String DEPARTMENT = "department";
	public static final String POSITION = "position";
	public static final String MOBILE = "mobile";
	public static final String GENDER = "gender";
	public static final String EMAIL = "email";
	public static final String WEIXINID = "weixinid";
	public static final String AVATAR = "avatar";
	public static final String STATUS = "status";
	public static final String EXTATTR = "extattr";
	public static final String ENABLE = "enable";    // 启用/禁用成员。1表示启用成员，0表示禁用成员

	private String userid;
	private String name;
	private String department;
	private String position;
	private String mobile;
	private Gender gender;
	private String email;
	private String weixinid;
	private String avatar;
	private UserStatus status;
	private String extattr;
	private UserEnable enable;

	public User(JSONObject json) {
		this.userid = json.getString(USERID);
		this.name = json.getString(NAME);
		this.department = json.getString(DEPARTMENT);
		this.position = json.getString(POSITION);
		this.mobile = json.getString(MOBILE);
		this.gender = Gender.getGender(json.getString(GENDER));
		this.email = json.getString(EMAIL);
		this.weixinid = json.getString(WEIXINID);
		this.avatar = json.getString(AVATAR);
		this.status = UserStatus.getStatus(json.getString(STATUS));
		this.extattr = json.getString(EXTATTR);
		this.enable = UserEnable.getEnable(json.getIntValue(ENABLE));
	}
	
	public User(String userid, String name, String department, String position, 
			String mobile, String gender, String email, String weixinid,
			String avatar, String status, String extattr) {
		super();
		this.userid = userid;
		this.name = name;
		this.department = department;
		this.position = position;
		this.mobile = mobile;
		this.gender = Gender.getGender(gender);
		this.email = email;
		this.weixinid = weixinid;
		this.avatar = avatar;
		this.status = UserStatus.getStatus(status);
		this.extattr = extattr;
	}

	public String getUserid() {
		return userid;
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}

	public String getPosition() {
		return position;
	}

	public String getMobile() {
		return mobile;
	}

	public Gender getGender() {
		return gender;
	}

	public String getEmail() {
		return email;
	}

	public String getWeixinid() {
		return weixinid;
	}

	public String getAvatar() {
		return avatar;
	}

	public UserStatus getStatus() {
		return status;
	}

	public String getExtattr() {
		return extattr;
	}
	
	public UserEnable getEnable() {
		return enable;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(USERID, userid);
		json.put(NAME, name);
		json.put(DEPARTMENT, department);
		json.put(POSITION, position);
		json.put(MOBILE, mobile);
		json.put(GENDER, gender.getValue());
		json.put(EMAIL, email);
		json.put(WEIXINID, weixinid);
		json.put(AVATAR, avatar);
		json.put(STATUS, status.getValue());
		json.put(EXTATTR, extattr);
		json.put(ENABLE, enable.getValue());
		
		return json;
	}

}
