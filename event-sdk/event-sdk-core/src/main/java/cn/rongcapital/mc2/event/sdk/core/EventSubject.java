package cn.rongcapital.mc2.event.sdk.core;

import com.google.gson.annotations.SerializedName;

/**
 * sdk事件主体数据封装
 * @author 英博
 *
 */
public class EventSubject {

	/**
	 * cookie中存放的身份标识
	 */
	@SerializedName("session_id")
	private String sessionId;

	/**
	 * 用户id
	 */
	@SerializedName("user_id")
	private String userId;

	public EventSubject() {}

	public EventSubject(String sessionId, String userId) {
		this.sessionId = sessionId;
		this.userId = userId;
	}

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
