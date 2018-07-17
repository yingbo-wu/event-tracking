package cn.rongcapital.mc2.event.server.streams.xvp;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.server.streams.EventColumn;

public class XvpEventColumn extends EventColumn {

	@SerializedName("app_id")
	private String appId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("behavior_id")
	private String behaviorId;

	@SerializedName("object_type")
	private String objectType;

	@SerializedName("object_id")
	private String objectId;

	@SerializedName("column_name")
	private String columnName;

	@SerializedName("column_value")
	private Object columnValue;

	@SerializedName("timestamp")
	private Long timestamp;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
