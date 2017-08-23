package org.lf.wx.campus;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务错误码
 * 开发者每次调用接口时，可能获得正确或错误的返回码，开发者可以根据返回码信息调试接口，排查错误。
 * http://open.campus.qq.com/doc/?item_id=3&page_id=174
 */
public class CampusErrCode {
	public static final String ERR_CODE = "code";
	public static final String ERR_MSG = "msg";

	private String code;
	private String msg;

	public CampusErrCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return getJSON().toJSONString();
	}

	public JSONObject getJSON() {
		JSONObject json = new JSONObject();

		json.put(ERR_CODE, code);
		json.put(ERR_MSG, msg);

		return json;
	}
	
	public static CampusErrCode C_ERR = new CampusErrCode("-1", "错误");
	public static CampusErrCode C_1 = new CampusErrCode("1", "未登录");

}
