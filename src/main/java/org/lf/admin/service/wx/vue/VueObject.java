package org.lf.admin.service.wx.vue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class VueObject {
	
	public JSONObject toJson() {
		return (JSONObject) JSON.toJSON(this);
	}
}
