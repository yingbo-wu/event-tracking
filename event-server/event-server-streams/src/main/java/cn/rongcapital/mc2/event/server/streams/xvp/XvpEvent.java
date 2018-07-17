package cn.rongcapital.mc2.event.server.streams.xvp;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.server.streams.Event;
import cn.rongcapital.mc2.event.server.streams.EventBehavior;

public class XvpEvent implements Event {

	@SerializedName("app_id")
	private String appId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("behavior")
	private EventBehavior behavior;

	@SerializedName("subject")
	private XvpEventSubject subject;

	@SerializedName("object")
	private XvpEventObject object;

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public EventBehavior getBehavior() {
		return behavior;
	}

	public void setBehavior(EventBehavior behavior) {
		this.behavior = behavior;
	}

	@SuppressWarnings("unchecked")
	public XvpEventSubject getSubject() {
		return subject;
	}

	public void setSubject(XvpEventSubject subject) {
		this.subject = subject;
	}

	@SuppressWarnings("unchecked")
	public XvpEventObject getObject() {
		return object;
	}

	public void setObject(XvpEventObject object) {
		this.object = object;
	}

}
