package org.lf.admin.service.yhpgl;

public enum YHPSQType {
	部门申领(0), 个人申领(1), 部门报损(2), 个人报损(3);
	
	private int value;
	
	private YHPSQType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static YHPSQType valueOf(int value) {
		return YHPSQType.values()[value];
	}
}
