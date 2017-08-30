package org.lf.wx.message;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

public class SendArticle extends WXJSON {
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String URL = "url";
	public static final String PIC_URL = "picurl";
	
	private String title;
	private String description;
	private String picUrl;
	private String url;
	
	public SendArticle(JSONObject json) {
		super(json);
		
		this.title = json.getString(TITLE);
		this.description = json.getString(DESCRIPTION);
		this.picUrl = json.getString(PIC_URL);
		this.url = json.getString(URL);
	}

	public SendArticle(String title, String description, String picUrl, String url) {
		super();
		this.title = title;
		this.description = description;
		this.picUrl = picUrl;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(TITLE, title);
		json.put(DESCRIPTION, description);
		json.put(PIC_URL, picUrl);
		json.put(URL, url);
		
		return json;
	}

}
