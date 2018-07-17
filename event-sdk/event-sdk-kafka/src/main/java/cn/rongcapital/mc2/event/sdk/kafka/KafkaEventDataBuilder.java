package cn.rongcapital.mc2.event.sdk.kafka;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import cn.rongcapital.mc2.event.sdk.core.DefaultEvent;
import cn.rongcapital.mc2.event.sdk.core.DefaultEventData;
import cn.rongcapital.mc2.event.sdk.core.Event;
import cn.rongcapital.mc2.event.sdk.core.EventBehavior;
import cn.rongcapital.mc2.event.sdk.core.EventData;
import cn.rongcapital.mc2.event.sdk.core.EventDataBuilder;
import cn.rongcapital.mc2.event.sdk.util.PropertiesUtils;

/**
 * sdk kafka事件数据构建器
 * @author 英博
 *
 */
public class KafkaEventDataBuilder extends EventDataBuilder {

	private DefaultEventData eventData = new DefaultEventData();

	KafkaEventDataBuilder() {}

	public KafkaEventDataBuilder setStoreId(String storeId) {
		eventData.setStoreId(storeId);
		return this;
	}

	public KafkaEventDataBuilder setPartitionKey(String partitionKey) {
		eventData.setPartitionKey(partitionKey);
		return this;
	}

	public EventData build() {
		Set<String> set = new TreeSet<String>();
		List<Event> events = eventObjects.stream().map(object -> {
			DefaultEvent event = new DefaultEvent();
			EventBehavior behavior = new EventBehavior();
			behavior.setBehaviorId(behaviorId);
			behavior.setTimestamp(timestamp);
			event.setGroupId(groupId);
			event.setBehavior(behavior);
			event.setSubject(eventSubject);
			event.setObject(object);
			String objectType = object.getObjectType();
			String objectId = object.getObjectId();
			set.add(String.format("%s.%s", objectType, objectId));
			return event;
		}).collect(Collectors.toList());
		eventData.setDataId(set.hashCode());
		eventData.setAppId(PropertiesUtils.getProperty("sdk.appId"));
		eventData.setEvents(events);
		return eventData;
	}

}
