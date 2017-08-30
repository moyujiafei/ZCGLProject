package org.lf.admin.db.pojo;

import java.util.Date;

public class LZTXZ extends PagedPojo {
	private Integer id;

	private Integer appId;

	private Integer jlId;

	private String mediaType;

	private String wxMediaId;

	private Date jlsj;// 记录时间


	public Date getJlsj() {
		return jlsj;
	}

	public void setJlsj(Date jlsj) {
		this.jlsj = jlsj;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getJlId() {
		return jlId;
	}

	public void setJlId(Integer jlId) {
		this.jlId = jlId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType == null ? null : mediaType.trim();
	}

	public String getWxMediaId() {
		return wxMediaId;
	}

	public void setWxMediaId(String wxMediaId) {
		this.wxMediaId = wxMediaId == null ? null : wxMediaId.trim();
	}
}