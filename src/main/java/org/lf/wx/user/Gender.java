package org.lf.wx.user;

public enum Gender {
	男("1"), 女("2");
	
	private String value;
	
	private Gender(String value) {
		if (! value.equals("1") && ! value.equals("2")) {
			throw new IllegalArgumentException();
		}
		
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Gender getGender(String value) {
		for (Gender type : Gender.values()) {
			if (type.value.equals(value)) {
				return type;
			}
		}

		return null;
	}
}
