package org.lf.admin.db.pojo;

import java.math.BigDecimal;
import java.util.Date;

import org.lf.admin.service.zcgl.ZCZT;

public class VZC extends PagedPojo {
	private Integer zcid;

	private Integer appId;

	private String zcdm;

	private String zc;
	
	private String zclxId;

	private String zclx;

	private String xh;

	private String ccbh;
	
	private BigDecimal cost;

	private Integer num;

	private String cfdd;

	private String syr;

	private String syrmc;
	
	private Integer deptNo;

	private String deptName;

	private String fzr;

	private String fzrmc;

	private String glr;

	private String glrmc;

	private String picUrl;

	private Integer imgVersion;

	private Date gzsj;

	private BigDecimal zjnx;

	private Integer zczt;

	private String zcztmc;
	
	public String getZclxId() {
		return zclxId;
	}

	public void setZclxId(String zclxId) {
		this.zclxId = zclxId;
	}

	public Integer getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(Integer deptNo) {
		this.deptNo = deptNo;
	}

	public Integer getImgVersion() {
		return imgVersion;
	}

	public void setImgVersion(Integer imgVersion) {
		this.imgVersion = imgVersion;
	}

	public String getZcztmc() {
		return zcztmc;
	}

	public void setZcztmc(String zcztmc) {
		this.zcztmc = zcztmc == null ? null : zcztmc.trim();
	}

	public String getSyrmc() {
		return syrmc;
	}

	public void setSyrmc(String syrmc) {
		this.syrmc = syrmc == null ? null : syrmc.trim();
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc == null ? null : fzrmc.trim();
	}

	public String getGlrmc() {
		return glrmc;
	}

	public void setGlrmc(String glrmc) {
		this.glrmc = glrmc == null ? null : glrmc.trim();
	}

	public Integer getZcid() {
		return zcid;
	}

	public void setZcid(Integer zcid) {
		this.zcid = zcid;
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

	public String getZc() {
		return zc;
	}

	public void setZc(String zc) {
		this.zc = zc == null ? null : zc.trim();
	}

	public String getZclx() {
		return zclx;
	}

	public void setZclx(String zclx) {
		this.zclx = zclx == null ? null : zclx.trim();
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh == null ? null : xh.trim();
	}

	public String getCcbh() {
		return ccbh;
	}

	public void setCcbh(String ccbh) {
		this.ccbh = ccbh == null ? null : ccbh.trim();
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCfdd() {
		return cfdd;
	}

	public void setCfdd(String cfdd) {
		this.cfdd = cfdd == null ? null : cfdd.trim();
	}

	public String getSyr() {
		return syr;
	}

	public void setSyr(String syr) {
		this.syr = syr == null ? null : syr.trim();
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName == null ? null : deptName.trim();
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr == null ? null : fzr.trim();
	}

	public String getGlr() {
		return glr;
	}

	public void setGlr(String glr) {
		this.glr = glr == null ? null : glr.trim();
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl == null ? null : picUrl.trim();
	}

	public Date getGzsj() {
		return gzsj;
	}

	public void setGzsj(Date gzsj) {
		this.gzsj = gzsj;
	}

	public BigDecimal getZjnx() {
		return zjnx;
	}

	public void setZjnx(BigDecimal zjnx) {
		this.zjnx = zjnx;
	}

	public Integer getZczt() {
		return zczt;
	}

	public void setZczt(Integer zczt) {
		if (zczt != null) {
			this.zcztmc = ZCZT.valueOf(zczt).name();
		}
		this.zczt = zczt;
	}
}