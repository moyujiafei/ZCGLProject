package org.lf.admin.db.pojo;

public class CJZW extends PagedPojo {
    private Integer id;

    private Integer appId;

    private Integer xqId;

    private Integer lxId;

    private String mc;

    private String dz;

    private String poi;

    private Integer tybz;

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

    public Integer getXqId() {
        return xqId;
    }

    public void setXqId(Integer xqId) {
        this.xqId = xqId;
    }

    public Integer getLxId() {
        return lxId;
    }

    public void setLxId(Integer lxId) {
        this.lxId = lxId;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc == null ? null : mc.trim();
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
}