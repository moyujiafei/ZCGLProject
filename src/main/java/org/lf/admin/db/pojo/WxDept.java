package org.lf.admin.db.pojo;

/**
 * 用于微信端显示三级部门信息。例如：我的通讯录
 */
public class WxDept extends PagedPojo {
	@Override
	public String toString() {
		return "WxDept [LvOne=" + LvOne + ", LvTwo=" + LvTwo + ", LvThree=" + LvThree + ", LvOneId=" + LvOneId + ", LvTwoId=" + LvTwoId + ", LvThreeId="
				+ LvThreeId + "]";
	}

	private String LvOne;
	private String LvTwo;
	private String LvThree;
	private String LvOneId;
	private String LvTwoId;
	private String LvThreeId;

	public String getLvOne() {
		return LvOne;
	}

	public void setLvOne(String lvOne) {
		LvOne = lvOne;
	}

	public String getLvTwo() {
		return LvTwo;
	}

	public void setLvTwo(String lvTwo) {
		LvTwo = lvTwo;
	}

	public String getLvThree() {
		return LvThree;
	}

	public void setLvThree(String lvThree) {
		LvThree = lvThree;
	}

	public String getLvOneId() {
		return LvOneId;
	}

	public void setLvOneId(String lvOneId) {
		LvOneId = lvOneId;
	}

	public String getLvTwoId() {
		return LvTwoId;
	}

	public void setLvTwoId(String lvTwoId) {
		LvTwoId = lvTwoId;
	}

	public String getLvThreeId() {
		return LvThreeId;
	}

	public void setLvThreeId(String lvThreeId) {
		LvThreeId = lvThreeId;
	}

}
