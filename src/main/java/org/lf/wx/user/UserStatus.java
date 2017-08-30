package org.lf.wx.user;

public enum UserStatus {
	全部成员("0"), 已关注("1"), 禁用("2"), 未关注("4");
	
	private String value;
	
	private UserStatus(String value) {
		if (! value.equals("0") && ! value.equals("1") && ! value.equals("2") && ! value.equals("4")) {
			throw new IllegalArgumentException();
		}
		
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static UserStatus getStatus(String value) {
		for (UserStatus type : UserStatus.values()) {
			if (type.value.equals(value)) {
				return type;
			}
		}

		return null;
	}
}
