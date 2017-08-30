package org.lf.admin.db.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class JZC extends PagedPojo{
	 private Integer id;

    private Integer appId;

    private String dm;

    private String mc;

    private Integer lxId;

    private String xh;

    private String ccbh;

    private BigDecimal cost;

    private Integer num;

    private String cfdd;

    private String syr;

    private Integer zcglId;

    private String picUrl;

    private Integer imgVersion;

    private Date gzsj;

    private BigDecimal zjnx;

    private Integer zt;

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

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm == null ? null : dm.trim();
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc == null ? null : mc.trim();
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

    public Integer getZcglId() {
        return zcglId;
    }

    public void setZcglId(Integer zcglId) {
        this.zcglId = zcglId;
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

    public Integer getZt() {
        return zt;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }
}