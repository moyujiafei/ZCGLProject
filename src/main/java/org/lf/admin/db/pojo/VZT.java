package org.lf.admin.db.pojo;

import java.util.Date;

import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.utils.WXMediaService;

public class VZT extends PagedPojo {
	private Integer ztid;

	private Integer appId;

	private String zcdm;

	private Date jlsj;

	private String jlr;

	private String jlrmc;

	private String poi;

	private Integer oldZt;

	private Integer newZt;

	private String remark;

	private String mediaType;

	private String imageUrl;

	private String smallImageUrl;// 完整的图片路径

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getJlrmc() {
		return jlrmc;
	}

	public void setJlrmc(String jlrmc) {
		this.jlrmc = jlrmc == null ? null : jlrmc.trim();
	}

	public Integer getZtid() {
		return ztid;
	}

	public void setZtid(Integer ztid) {
		this.ztid = ztid;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getZcdm() {
		return zcdm;
	}

	public void setZcdm(String zcdm) {
		this.zcdm = zcdm == null ? null : zcdm.trim();
	}

	public Date getJlsj() {
		return jlsj;
	}

	public void setJlsj(Date jlsj) {
		this.jlsj = jlsj;
	}

	public String getJlr() {
		return jlr;
	}

	public void setJlr(String jlr) {
		this.jlr = jlr == null ? null : jlr.trim();
	}

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		this.poi = poi == null ? null : poi.trim();
	}

	public Integer getOldZt() {
		return oldZt;
	}

	public void setOldZt(Integer oldZt) {
		this.oldZt = oldZt;
	}

	public Integer getNewZt() {
		return newZt;
	}

	public void setNewZt(Integer newZt) {
		this.newZt = newZt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		if (imageUrl == null) {
			this.imageUrl = null;
		} else {
			this.imageUrl = imageUrl.trim();
			this.smallImageUrl = ZCGLProperties.URL_SERVER + ZCGLProperties.URL_MEDIA_TARGET_DIR + imageUrl + WXMediaService.THUMBNAIL_SUFFIX;
		}
	}
}