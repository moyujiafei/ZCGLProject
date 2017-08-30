package org.lf.admin.service.zcgl;

public enum ZCZT {
	未使用(0), 使用中(1), 维修中(2), 闲置(3), 申请维修(4), 申请报废(5), 申请闲置(6), 巡检中(7), 报废(8), 已登记(9), 领用中(10),
	归还中(11), 上交中(12), 未审核(13);
	
	private int value;
	
	private ZCZT(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ZCZT valueOf(int value) {
		return ZCZT.values()[value];
	}
}
