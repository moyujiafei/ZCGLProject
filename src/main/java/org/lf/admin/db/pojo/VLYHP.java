package org.lf.admin.db.pojo;

import java.util.Date;

public class VLYHP {
    private Integer id;

    private Integer appId;

    private Integer yhpId;

    private String jlr;

    private Date jlsj;

    private String czr;

    private Integer czlx;

    private Integer num;

    private Integer lxId;

    private String lx;

    private String xh;

    private String ccbh;

    private Integer zcglId;

    private Integer deptNo;

    private String deptName;

    private Integer czbmId;

    private Integer czbmDeptNo;

    private String czbmDeptName;

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

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr == null ? null : czr.trim();
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

    public Integer getLxId() {
        return lxId;
    }

    public void setLxId(Integer lxId) {
        this.lxId = lxId;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx == null ? null : lx.trim();
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

    public Integer getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Integer deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public Integer getCzbmId() {
        return czbmId;
    }

    public void setCzbmId(Integer czbmId) {
        this.czbmId = czbmId;
    }

    public Integer getCzbmDeptNo() {
        return czbmDeptNo;
    }

    public void setCzbmDeptNo(Integer czbmDeptNo) {
        this.czbmDeptNo = czbmDeptNo;
    }

    public String getCzbmDeptName() {
        return czbmDeptName;
    }

    public void setCzbmDeptName(String czbmDeptName) {
        this.czbmDeptName = czbmDeptName == null ? null : czbmDeptName.trim();
    }
}