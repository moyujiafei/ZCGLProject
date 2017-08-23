package org.lf.admin.service.zcgl;

public enum RWLX {
	日常巡检(0), 故障维修(1);
	
	private int value;
	
	private RWLX(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static RWLX valueOf(int value) {
		return RWLX.values()[value];
	}
}
