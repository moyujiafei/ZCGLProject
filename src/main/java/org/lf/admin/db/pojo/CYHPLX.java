package org.lf.admin.db.pojo;

public class CYHPLX {
    private Integer id;

    private Integer appId;

    private String lxId;

    private String lxPid;

    private String mc;

    private String remark;

    private String picUrl;

    private Integer imgVersion;

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

    public String getLxId() {
        return lxId;
    }

    public void setLxId(String lxId) {
        this.lxId = lxId == null ? null : lxId.trim();
    }

    public String getLxPid() {
        return lxPid;
    }

    public void setLxPid(String lxPid) {
        this.lxPid = lxPid == null ? null : lxPid.trim();
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc == null ? null : mc.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Integer getImgVersion() {
        return imgVersion;
    }

    public void setImgVersion(Integer imgVersion) {
        this.imgVersion = imgVersion;
    }
}