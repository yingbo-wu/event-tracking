package cn.rongcapital.mc2.event.sdk.core;

import com.google.gson.annotations.SerializedName;

public class EventBehavior {

	/**
	 * 行为标识
	 */
	@SerializedName("behavior_id")
	private String behaviorId;

	/**
	 * 行为时间
	 */
	@SerializedName("timestamp")
	private long timestamp;

	public String getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
