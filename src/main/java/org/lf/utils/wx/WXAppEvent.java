package org.lf.utils.wx;

import org.lf.admin.db.pojo.ChuApp;

public class WXAppEvent {
	private Integer appId;
	private String corpId;
	private Integer agentId;
	private String secret;
	private String token;
	private boolean encrypt;
	private String aesKey;
	private String accessToken;
	private String jsapiTicket;

	private boolean stop = true; // 默认为关闭状态
	private Long threadId;

	public WXAppEvent(ChuApp app) {
		this.appId = app.getAppId();
		this.corpId = app.getCorpId();
		this.agentId = app.getAgentId();
		this.secret = app.getSecret();
		this.token = app.getToken();
		this.encrypt = app.getEncrypt() == 1;
		this.aesKey = app.getAesKey();
	}

	public Integer getAppId() {
		return appId;
	}

	public String getCorpId() {
		return corpId;
	}

	public Integer getAgentId() {
		return agentId;
	}
	public String getSecret() {
		return secret;
	}

	public String getToken() {
		return token;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public String getAesKey() {
		return aesKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

}
