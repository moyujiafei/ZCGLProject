package org.lf.admin.db.pojo;

import java.util.Date;

public class ChuBackup extends PagedPojo{
    private Integer id;

    private Integer appId;

    private Date bfsj;

    private String czr;

	private String czrmc; 
    
    private String backupset;
    
    private String backuptype;
    
    private Integer status;

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

    public Date getBfsj() {
        return bfsj;
    }

    public void setBfsj(Date bfsj) {
        this.bfsj = bfsj;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr == null ? null : czr.trim();
    }

    public String getCzrmc() {
		return czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc == null ? null : czrmc.trim();
	}
	
    public String getBackupset() {
        return backupset;
    }
    
    public String getBackuptype() {
		return backuptype;
	}

	public void setBackuptype(String backuptype) {
		this.backuptype = backuptype == null ? null : backuptype.trim();
	}

	public void setBackupset(String backupset) {
        this.backupset = backupset == null ? null : backupset.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}