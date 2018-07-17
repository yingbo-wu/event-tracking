package cn.rongcapital.mc2.event.server.streams.xvp;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.server.streams.EventObject;

public class XvpEventObject extends EventObject {

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

	@SerializedName("root_type")
	private String rootType;

	@SerializedName("root_id")
	private String rootId;

	@SerializedName("object_type")
	private String objectType;

	@SerializedName("object_id")
	private String objectId;

	@SerializedName("value")
	private Map<String, Object> value = new HashMap<String, Object>();

	@SerializedName("timestamp")
	private Long timestamp;

	@SerializedName("fill_mode")
	private Integer fillMode;

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

	public String getRootType() {
		return rootType;
	}

	public void setRootType(String rootType) {
		this.rootType = rootType;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
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

	public Map<String, Object> getValue() {
		return value;
	}

	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getFillMode() {
		return fillMode;
	}

	public void setFillMode(Integer fillMode) {
		this.fillMode = fillMode;
	}

}
