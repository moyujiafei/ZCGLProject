package org.lf.admin.db.pojo;

public class ChuRole {

	private Integer id;

	private String name;

	private Integer tybz;

	private String privList;// 权限列表

	public String getPrivList() {
		return privList;
	}

	public void setPrivList(String privList) {
		this.privList = privList == null ? null : privList.trim();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getTybz() {
		return tybz;
	}

	public void setTybz(Integer tybz) {
		this.tybz = tybz;
	}
}