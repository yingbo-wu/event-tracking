package cn.rongcapital.mc2.event.server.streams.xvp;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.server.streams.EventSubject;

public class XvpEventSubject extends EventSubject {

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("user_id")
	private String userId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
