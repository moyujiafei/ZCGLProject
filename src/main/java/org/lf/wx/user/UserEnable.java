package org.lf.wx.user;

public enum UserEnable {
	禁用成员(0), 启用成员(1);
	
	private int value;
	
	private UserEnable(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static UserEnable getEnable(int value) {
		if (value > 1) {
			return null;
		}
		
		return UserEnable.values()[value];
	}
}
