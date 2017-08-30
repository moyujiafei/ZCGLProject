package org.lf.admin.service.yhpgl;

public enum YHPSQStatus {
	未提交(0), 待审批(1), 同意(2), 拒绝(3);
	
	private int value;
	
	private YHPSQStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static YHPSQStatus valueOf(int value) {
		return YHPSQStatus.values()[value];
	}
}
