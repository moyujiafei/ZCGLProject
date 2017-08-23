package org.lf.admin.db.pojo;


public class CZCGL extends PagedPojo {
	private Integer id;

	private Integer appId;
	
	private Integer deptNo;

	private String deptName;

	private String fzr;

	private String fzrmc;

	private String glr;

	private String glrmc;
	
	public Integer getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(Integer deptNo) {
		this.deptNo = deptNo;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName == null ? null : deptName.trim();
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr == null ? null : fzr.trim();
	}

	public String getGlr() {
		return glr;
	}

	public void setGlr(String glr) {
		this.glr = glr == null ? null : glr.trim();
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getGlrmc() {
		return glrmc;
	}

	public void setGlrmc(String glrmc) {
		this.glrmc = glrmc;
	}

}