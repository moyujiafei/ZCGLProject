package org.lf.admin.db.pojo;

public class JYHP {
    private Integer id;

    private Integer appId;

    private Integer lxId;

    private String xh;

    private String ccbh;

    private Integer zcglId;

    private Integer num;

    private String cfdd;

    private Integer leftLimit;

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

    public Integer getLxId() {
        return lxId;
    }

    public void setLxId(Integer lxId) {
        this.lxId = lxId;
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

    public Integer getZcglId() {
        return zcglId;
    }

    public void setZcglId(Integer zcglId) {
        this.zcglId = zcglId;
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

    public Integer getLeftLimit() {
        return leftLimit;
    }

    public void setLeftLimit(Integer leftLimit) {
        this.leftLimit = leftLimit;
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