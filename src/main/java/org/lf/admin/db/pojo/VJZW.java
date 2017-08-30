package org.lf.admin.db.pojo;

public class VJZW extends PagedPojo {
    private Integer jzwId;

    private Integer appId;

    private String xqmc;

    private String lxmc;

    private String jzw;

    private String dz;

    private String poi;

    private Integer tybz;
    
    private Integer xqid;
    
    private Integer lxid;

    public Integer getJzwId() {
        return jzwId;
    }

    public void setJzwId(Integer jzwId) {
        this.jzwId = jzwId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getXqmc() {
        return xqmc;
    }

    public void setXqmc(String xqmc) {
        this.xqmc = xqmc == null ? null : xqmc.trim();
    }

    public String getLxmc() {
        return lxmc;
    }

    public void setLxmc(String lxmc) {
        this.lxmc = lxmc == null ? null : lxmc.trim();
    }

    public String getJzw() {
        return jzw;
    }

    public void setJzw(String jzw) {
        this.jzw = jzw == null ? null : jzw.trim();
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz == null ? null : dz.trim();
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi == null ? null : poi.trim();
    }

    public Integer getTybz() {
        return tybz;
    }

    public void setTybz(Integer tybz) {
        this.tybz = tybz;
    }

	public Integer getXqid() {
		return xqid;
	}

	public void setXqid(Integer xqid) {
		this.xqid = xqid;
	}

	public Integer getLxid() {
		return lxid;
	}

	public void setLxid(Integer lxid) {
		this.lxid = lxid;
	}
    
}