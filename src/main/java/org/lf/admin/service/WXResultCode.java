package org.lf.admin.service;
/**
 * 微信ajax返回码
 */
public enum WXResultCode {
	SUCCESS(200, "ok"), ERROR(500, "未查询到相关信息");
	
	private int code;
	private String msg;
	
	WXResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
