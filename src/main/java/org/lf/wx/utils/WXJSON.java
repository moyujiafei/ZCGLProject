package org.lf.wx.utils;

import com.alibaba.fastjson.JSONObject;

public abstract class WXJSON {
	public WXJSON() {}
	
	public WXJSON(JSONObject json) {
	}
	
	public abstract JSONObject getJSON();
	
	@Override
	public String toString() {
		return getJSON().toJSONString();
	}
}
