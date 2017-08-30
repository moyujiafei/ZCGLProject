package org.lf.admin.service.sys;


public enum WXZT {
	启用(0), 停用(1);
	
	private int value;
	
	private WXZT(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static WXZT valueOf(int value) {
		return WXZT.values()[value];
	}
}
