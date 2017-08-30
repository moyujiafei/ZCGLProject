package org.lf.admin.db.pojo;

import java.math.BigDecimal;

public class VZCLX extends PagedPojo {
    private Integer id;

    private Integer appId;

    private String lx;

    private String plx;

    private BigDecimal zjnx;

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

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx == null ? null : lx.trim();
    }

    public String getPlx() {
        return plx;
    }

    public void setPlx(String plx) {
        this.plx = plx == null ? null : plx.trim();
    }

    public BigDecimal getZjnx() {
        return zjnx;
    }

    public void setZjnx(BigDecimal zjnx) {
        this.zjnx = zjnx;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}