package cn.rongcapital.mc2.event.sdk.core;

import com.google.gson.annotations.SerializedName;

/**
 * sdk默认事件服务实现
 * @author 英博
 *
 */
public class DefaultEvent implements Event {

	/**
	 * 事件组标识
	 */
	@SerializedName("group_id")
	private String groupId;

	/**
	 * 事件所映射的行为标识
	 */
	@SerializedName("behavior")
	private EventBehavior behavior;


	/**
	 * 事件主体标识
	 */
	@SerializedName("subject")
	private EventSubject subject;

	/**
	 * 事件所承载的客体
	 */
	@SerializedName("object")
	private EventObject object;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public EventBehavior getBehavior() {
		return behavior;
	}

	public void setBehavior(EventBehavior behavior) {
		this.behavior = behavior;
	}

	@Override
	public EventSubject getSubject() {
		return subject;
	}

	public void setSubject(EventSubject subject) {
		this.subject = subject;
	}

	@Override
	public EventObject getObject() {
		return object;
	}

	public void setObject(EventObject object) {
		this.object = object;
	}

}
