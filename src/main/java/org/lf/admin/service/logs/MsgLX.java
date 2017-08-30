package org.lf.admin.service.logs;

/**
 * 消息类型 
 *
 */
public enum MsgLX {
	系统通知(0), 系统预警(1), 消息通告(2), 隐患上报(3);
	
	private int value;
	
	private MsgLX(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static MsgLX valueOf(int value) {
		return MsgLX.values()[value];
	}
}
