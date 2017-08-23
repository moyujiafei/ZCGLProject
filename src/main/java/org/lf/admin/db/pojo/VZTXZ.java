package org.lf.admin.db.pojo;

import java.util.Date;

/**
 * 用于页面中状态细则的显示 
 */
public class VZTXZ {
	private Integer id;

    private Integer appId;

    private Integer jlId;

    private String mediaType;

    private String wxMediaId;
    
    private Date jlsj;   // 记录时间
    
    private String briefImageURI;   // 页面中显示的缩略图链接地址
    
    private String mediaURI;   // 图片、音频链接地址

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
		this.mediaType = mediaType;
	}

	public String getWxMediaId() {
		return wxMediaId;
	}

	public void setWxMediaId(String wxMediaId) {
		this.wxMediaId = wxMediaId;
	}

	public Date getJlsj() {
		return jlsj;
	}

	public void setJlsj(Date jlsj) {
		this.jlsj = jlsj;
	}

	public String getBriefImageURI() {
		return briefImageURI;
	}

	public void setBriefImageURI(String briefImageURI) {
		this.briefImageURI = briefImageURI;
	}

	public String getMediaURI() {
		return mediaURI;
	}

	public void setMediaURI(String mediaURI) {
		this.mediaURI = mediaURI;
	}

    
}
