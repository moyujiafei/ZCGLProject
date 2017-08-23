package org.lf.admin.service.yhpgl;

/**
 * 易耗品出入库操作类型 
 *
 */
public enum YHPCZLX {
	登记(0), 调拨(1), 领用(2), 报损(3), 补货(4);
	
	private int value;
	
	private YHPCZLX(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static YHPCZLX valueOf(int value) {
		return YHPCZLX.values()[value];
	}
}
