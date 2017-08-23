package org.lf.admin.db.pojo;

public class CXQ extends PagedPojo {
    private Integer id;

    private Integer appId;

    private String xqmc;

    private String xqdz;

    private String yb;

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

    public String getXqmc() {
        return xqmc;
    }

    public void setXqmc(String xqmc) {
        this.xqmc = xqmc == null ? null : xqmc.trim();
    }

    public String getXqdz() {
        return xqdz;
    }

    public void setXqdz(String xqdz) {
        this.xqdz = xqdz == null ? null : xqdz.trim();
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb == null ? null : yb.trim();
    }
}