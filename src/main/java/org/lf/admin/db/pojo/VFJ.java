package org.lf.admin.db.pojo;

public class VFJ extends PagedPojo {
	private Integer fjId;

	private Integer appId;

	private String floor;

	private String room;

	private String deptName;

	private String glr;

	private String glrmc;

	private String jzw;

	private String dz;

	private String poi;

	private Integer tybz;

	private Integer jzwId;

	private Integer xqId;

	private String xqmc;

	public String getXqmc() {
		return xqmc;
	}

	public void setXqmc(String xqmc) {
		this.xqmc = xqmc;
	}

	public Integer getFjId() {
		return fjId;
	}

	public void setFjId(Integer fjId) {
		this.fjId = fjId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor == null ? null : floor.trim();
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room == null ? null : room.trim();
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName == null ? null : deptName.trim();
	}

	public String getGlr() {
		return glr;
	}

	public void setGlr(String glr) {
		this.glr = glr == null ? null : glr.trim();
	}

	public String getGlrmc() {
		return glrmc;
	}

	public void setGlrmc(String glrmc) {
		this.glrmc = glrmc;
	}

	public String getJzw() {
		return jzw;
	}

	public void setJzw(String jzw) {
		this.jzw = jzw == null ? null : jzw.trim();
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz == null ? null : dz.trim();
	}

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		this.poi = poi == null ? null : poi.trim();
	}

	public Integer getTybz() {
		return tybz;
	}

	public void setTybz(Integer tybz) {
		this.tybz = tybz;
	}

	public Integer getJzwId() {
		return jzwId;
	}

	public void setJzwId(Integer jzwId) {
		this.jzwId = jzwId;
	}

	public Integer getXqId() {
		return xqId;
	}

	public void setXqId(Integer xqId) {
		this.xqId = xqId;
	}
}