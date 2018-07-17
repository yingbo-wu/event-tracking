package cn.rongcapital.mc2.event.sdk.core;

import java.util.ArrayList;
import java.util.List;

/**
 * sdk事件数据构建器(抽象类)
 * @author 英博
 *
 */
public abstract class EventDataBuilder {

	protected EventSubject eventSubject = new EventSubject();

	protected List<EventObject> eventObjects = new ArrayList<EventObject>();

	protected String groupId;

	protected String behaviorId;

	protected Long timestamp;

	protected EventDataBuilder() {}

	public EventDataBuilder setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public EventDataBuilder setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
		return this;
	}

	public EventDataBuilder setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public EventDataBuilder setSessionId(String sessionId) {
		eventSubject.setSessionId(sessionId);
		return this;
	}

	public EventDataBuilder setUserId(String userId) {
		eventSubject.setUserId(userId);
		return this;
	}

	/**
	 * @param objectType 客体类型:xvp业务实体表名抽象枚举
	 * @param objectId 客体标识:xvp业务实体id
	 * @param value 业务实体
	 * @param fillMode 补齐方式 0:不进行补齐、1:补齐自身、2:补齐自身+关联实体
	 * @return
	 */
	public EventDataBuilder appendObject(EventObjectType objectType, String objectId, Object value, int fillMode) {
		EventObject object = new EventObject(objectType, objectId, value, fillMode);
		eventObjects.add(object);
		return this;
	}

	/**
	 * @param rootType 聚合根类型
	 * @param rootId 聚合根id
	 * @param objectType 客体类型:xvp业务实体表名抽象枚举
	 * @param objectId 客体标识:xvp业务实体id
	 * @param value 业务实体
	 * @param fillMode 补齐方式 0:不进行补齐、1:补齐自身、2:补齐自身+关联实体
	 * @return
	 */
	public EventDataBuilder appendObject(EventObjectType rootType, String rootId, EventObjectType objectType, String objectId, Object value, int fillMode) {
		EventObject object = new EventObject(rootType, rootId, objectType, objectId, value, fillMode);
		eventObjects.add(object);
		return this;
	}

	public abstract EventData build();

}
