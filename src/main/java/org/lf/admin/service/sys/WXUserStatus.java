package org.lf.admin.service.sys;

public enum WXUserStatus {
	全部成员(0), 已关注(1), 禁用(2), 未关注(4);
	
	private int value;
	
	WXUserStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static WXUserStatus valueOf(int value) {
		switch (value) {
		case 0:
			return 全部成员;
		case 1:
			return 已关注;
		case 2:
			return 禁用;
		case 4:
			return 未关注;
		default:
			return null;
		}
		
	}

}
