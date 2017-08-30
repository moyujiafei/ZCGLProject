package org.lf.admin.db.pojo;

import java.util.Date;

public class JRW extends PagedPojo {
    private Integer id;

    private Integer appId;

    private Integer lx;
    
    private String lxmc;

    private Date kssj;

    private Date jssj;

    private String czr;
    
    private String czrmc;

    private String czRemark;

    private String ysr;
    
    private String ysrmc;

    private String ysRemark;

    private Date yssj;

    private Integer total;

    private Integer finishCount;

    private Integer finish;
    

	public String getCzrmc() {
		return czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc;
	}

	public String getYsrmc() {
		return ysrmc;
	}

	public void setYsrmc(String ysrmc) {
		this.ysrmc = ysrmc;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

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

    public Integer getLx() {
        return lx;
    }

    public void setLx(Integer lx) {
        this.lx = lx;
    }

    public Date getKssj() {
        return kssj;
    }

    public void setKssj(Date kssj) {
        this.kssj = kssj;
    }

    public Date getJssj() {
        return jssj;
    }

    public void setJssj(Date jssj) {
        this.jssj = jssj;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr == null ? null : czr.trim();
    }

    public String getCzRemark() {
        return czRemark;
    }

    public void setCzRemark(String czRemark) {
        this.czRemark = czRemark == null ? null : czRemark.trim();
    }

    public String getYsr() {
        return ysr;
    }

    public void setYsr(String ysr) {
        this.ysr = ysr == null ? null : ysr.trim();
    }

    public String getYsRemark() {
        return ysRemark;
    }

    public void setYsRemark(String ysRemark) {
        this.ysRemark = ysRemark == null ? null : ysRemark.trim();
    }

    public Date getYssj() {
        return yssj;
    }

    public void setYssj(Date yssj) {
        this.yssj = yssj;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Integer getFinish() {
        return finish;
    }

    public void setFinish(Integer finish) {
        this.finish = finish;
    }
}