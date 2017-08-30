package org.lf.admin.db.pojo;

public class ChuWXDept {
    private Integer id;

    private Integer appId;

    private Integer deptNo;

    private String deptName;

    private Integer deptPno;

    private Integer deptOrder;

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

    public Integer getDeptPno() {
        return deptPno;
    }

    public void setDeptPno(Integer deptPno) {
        this.deptPno = deptPno;
    }

    public Integer getDeptOrder() {
        return deptOrder;
    }

    public void setDeptOrder(Integer deptOrder) {
        this.deptOrder = deptOrder;
    }
}