package org.lf.wx.utils;

import com.alibaba.fastjson.JSONObject;

public class WXException extends Exception {
	private static final long serialVersionUID = -5347398660575526556L;
	
	private WXErrCode errCode;

	public WXException(WXErrCode errCode) {
		super(errCode.getCode() + ": " + errCode.getMsg());
		this.errCode = errCode;
	}
	
	public WXException(String code, String msg) {
		super(code + ": " + msg);
		this.errCode = new WXErrCode(code, msg);
	}
	
	public WXException(JSONObject json) {
		this(json.getString(WXErrCode.ERR_CODE), json.getString(WXErrCode.ERR_MSG));
	}

	public WXErrCode getErrCode() {
		return errCode;
	}
}
