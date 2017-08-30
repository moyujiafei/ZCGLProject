package org.lf.admin.db.pojo;

import java.util.Date;

public class LMsg extends PagedPojo {
    private Integer id;

    private Integer appId;

    private Date fssj;

    private String jsr;

    private Integer lx;

    private String nr;

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

    public Date getFssj() {
        return fssj;
    }

    public void setFssj(Date fssj) {
        this.fssj = fssj;
    }

    public String getJsr() {
        return jsr;
    }

    public void setJsr(String jsr) {
        this.jsr = jsr == null ? null : jsr.trim();
    }

    public Integer getLx() {
        return lx;
    }

    public void setLx(Integer lx) {
        this.lx = lx;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr == null ? null : nr.trim();
    }
}