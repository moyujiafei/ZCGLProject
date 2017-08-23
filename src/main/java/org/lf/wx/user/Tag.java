package org.lf.wx.user;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

/**
 * 标签 https://work.weixin.qq.com/api/doc#10915
 */
public class Tag extends WXJSON {
	public static final String TAGID = "tagid";
	public static final String TAGNAME = "tagname";

	private String tagid;
	private String tagname;

	public Tag(JSONObject json) {
		this.tagid = json.getString(TAGID);
		this.tagname = json.getString(TAGNAME);
	}

	public Tag(String tagid, String tagname) {
		super();
		this.tagid = tagid;
		this.tagname = tagname;
	}

	public String getTagid() {
		return tagid;
	}

	public String getTagname() {
		return tagname;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();

		json.put(TAGID, tagid);
		json.put(TAGNAME, tagname);

		return json;
	}

}
