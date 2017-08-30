package org.lf.admin.db.pojo;

import java.util.Date;

public class LYHP {
    private Integer id;

    private Integer yhpId;

    private String jlr;

    private Date jlsj;

    private String poi;

    private String czr;

    private Integer czbmId;

    private Integer czlx;

    private Integer num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYhpId() {
        return yhpId;
    }

    public void setYhpId(Integer yhpId) {
        this.yhpId = yhpId;
    }

    public String getJlr() {
        return jlr;
    }

    public void setJlr(String jlr) {
        this.jlr = jlr == null ? null : jlr.trim();
    }

    public Date getJlsj() {
        return jlsj;
    }

    public void setJlsj(Date jlsj) {
        this.jlsj = jlsj;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi == null ? null : poi.trim();
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr == null ? null : czr.trim();
    }

    public Integer getCzbmId() {
        return czbmId;
    }

    public void setCzbmId(Integer czbmId) {
        this.czbmId = czbmId;
    }

    public Integer getCzlx() {
        return czlx;
    }

    public void setCzlx(Integer czlx) {
        this.czlx = czlx;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}