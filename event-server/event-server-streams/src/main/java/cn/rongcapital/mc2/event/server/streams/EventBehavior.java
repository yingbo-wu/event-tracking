package cn.rongcapital.mc2.event.server.streams;

import com.google.gson.annotations.SerializedName;

public class EventBehavior {

	@SerializedName("behavior_id")
	private String behaviorId;

	@SerializedName("timestamp")
	private Long timestamp;

	public String getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if (null != behaviorId) {
			try {
				EventBehavior behavior = (EventBehavior) obj;
				return behaviorId.equals(behavior.behaviorId);
			} catch (Exception e) {
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (null != behaviorId) {
			return super.hashCode() + behaviorId.hashCode();
		}
		return super.hashCode();
	}

}
