package org.lf.admin.service.wx.vue.panel;

import org.lf.admin.service.wx.vue.VueObject;

public class VuePanelList extends VueObject{
	private String title;
	private String desc;
	private String src;
	private VuePanelMeta meta;
	private VuePanelUrl url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public VuePanelMeta getMeta() {
		return meta;
	}

	public void setMeta(VuePanelMeta meta) {
		this.meta = meta;
	}

	public VuePanelUrl getUrl() {
		return url;
	}

	public void setUrl(VuePanelUrl url) {
		this.url = url;
	}

}
