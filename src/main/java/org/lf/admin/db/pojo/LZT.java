package org.lf.admin.db.pojo;

import java.util.Date;

public class LZT extends PagedPojo {
    private Integer id;

    private Integer appId;

    private String zcdm;

    private Date jlsj;
    
    private String jlr;
    
    private String jlrmc;

    private String poi;

    private Integer oldZt;

    private Integer newZt;

    private String remark;

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

	public String getJlrmc() {
		return jlrmc;
	}

	public void setJlrmc(String jlrmc) {
		this.jlrmc = jlrmc;
	}

}