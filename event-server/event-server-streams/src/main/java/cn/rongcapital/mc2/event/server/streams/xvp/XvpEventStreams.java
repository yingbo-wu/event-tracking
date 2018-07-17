package cn.rongcapital.mc2.event.server.streams.xvp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.event.server.common.util.GsonUtils;
import cn.rongcapital.mc2.event.server.streams.EventBehavior;
import cn.rongcapital.mc2.event.server.streams.EventProcessor;
import cn.rongcapital.mc2.event.server.streams.EventStreams;
import cn.rongcapital.mc2.event.server.streams.EventSubject;
import cn.rongcapital.mc2.event.server.streams.EventTransformer;

@Component
public class XvpEventStreams extends EventStreams {

	@Autowired
	private EventTransformer eventTransformer;

	@Autowired
	private EventProcessor eventProcessor;

	@Override
	protected KStream<Windowed<String>, String> distinctData(KStream<String, String> stream) {
		return stream.filter((k, v) -> {
			XvpEventData data = null;
			try {
				data = GsonUtils.create().fromJson(v, XvpEventData.class);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			return null != data && data.getEvents().stream().findFirst().isPresent();
		}).map((k, v) -> {
			XvpEventData data = GsonUtils.create().fromJson(v, XvpEventData.class);
			XvpEvent event = data.getEvents().stream().findFirst().get();
			EventBehavior behavior = event.getBehavior();
			EventSubject subject = event.getSubject();
			Integer dataId = data.getDataId();
			String sessionId = subject.getSessionId();
			String behaviorId = behavior.getBehaviorId();
			String key = String.format("%s:%s:%d", sessionId, behaviorId, dataId);
			return new KeyValue<String, String>(key, v);
		}).groupByKey().windowedBy(SessionWindows.with(TimeUnit.MINUTES.toMillis(5))).aggregate(() -> "", (k, v, a) -> {
			XvpEventData data = GsonUtils.create().fromJson(v, XvpEventData.class);
			XvpEventData aggregationData = null;
			if (StringUtils.isEmpty(a)) {
				aggregationData = data;
			} else {
				aggregationData = GsonUtils.create().fromJson(a, XvpEventData.class);
				aggregationData.getEvents().stream().forEach(aggregationEvent -> {
					EventBehavior aggregationBehavior = aggregationEvent.getBehavior();
					EventSubject aggregationSubject = aggregationEvent.getSubject();
					XvpEventObject aggregationObject = aggregationEvent.getObject();
					Map<String, Object> aggregationValue = aggregationObject.getValue();

					Optional<XvpEvent> optional = data.getEvents().stream().filter(event -> {
						EventBehavior behavior = event.getBehavior();
						EventSubject subject = event.getSubject();
						XvpEventObject object = event.getObject();
						return behavior.equals(aggregationBehavior) && subject.equals(aggregationSubject) && object.equals(aggregationObject);
					}).findFirst();

					if (optional.isPresent()) {
						XvpEvent event = optional.get();
						EventBehavior behavior = event.getBehavior();
						XvpEventObject object = event.getObject();
						Map<String, Object> value = object.getValue();
						if (null != aggregationValue) {
							if (behavior.getTimestamp() > aggregationBehavior.getTimestamp()) {
								aggregationValue.putAll(value);
								aggregationEvent.setGroupId(event.getGroupId());
							} else {
								for (Entry<String, Object> entry : value.entrySet()) {
									aggregationValue.putIfAbsent(entry.getKey(), entry.getValue());
								}
							}
						}
					}
				});
			}
			return GsonUtils.create().toJson(aggregationData);
		}, (k, v1, v2) -> {
			return v2;
		}).toStream();
	}

	@Override
	protected KStream<String, String> splitEvent(KStream<Windowed<String>, String> stream) {
		return stream.filter((k, v) -> {
			return StringUtils.isNotEmpty(v);
		}).flatMap((k, v) -> {
			XvpEventData data = GsonUtils.create().fromJson(v, XvpEventData.class);
			String appId = data.getAppId();
			String storeId = data.getStoreId();
			return data.getEvents().stream().filter(event -> null != event).map(event -> {
				EventBehavior behavior = event.getBehavior();
				EventSubject subject = event.getSubject();
				event.setAppId(appId);
				event.setStoreId(storeId);
				String sessionId = subject.getSessionId();
				String behaviorId = behavior.getBehaviorId();
				String groupId = event.getGroupId();
				String key = String.format("%s:%s:%s", sessionId, behaviorId, groupId);
				return new KeyValue<String, String>(key, GsonUtils.create().toJson(event));
			}).collect(Collectors.toList());
		}).transform(eventTransformer);
	}

	@Override
	protected KStream<String, String> splitColumn(KStream<String, String> stream) {
		return stream.filter((k, v) -> {
			return StringUtils.isNotEmpty(v);
		}).flatMap((k, v) -> {
			XvpEvent event = GsonUtils.create().fromJson(v, XvpEvent.class);
			EventBehavior behavior = event.getBehavior();
			XvpEventSubject subject = event.getSubject();
			XvpEventObject object = event.getObject();
			String appId = event.getAppId();
			String storeId = event.getStoreId();
			String groupId = event.getGroupId();
			String sessionId = subject.getSessionId();
			String userId = subject.getUserId();
			String behaviorId = behavior.getBehaviorId();
			String objectId = object.getObjectId();
			String objectType = object.getObjectType();
			Long timestamp = behavior.getTimestamp();
			return object.getValue().entrySet().stream().filter(entry -> null != entry.getValue()).map(entry -> {
				XvpEventColumn column = new XvpEventColumn();
				String columnName = entry.getKey();
				Object value = entry.getValue();
				if (null != value) {
					String columnValue = null;
					if (value.getClass().isPrimitive()) {
						column.setColumnValue(value);
					} else if (value instanceof String) {
						column.setColumnValue(value);
					} else {
						try {
							columnValue = GsonUtils.create().toJson(value, value.getClass());
							column.setColumnValue(columnValue);
						} catch (Exception e) {
							columnValue = String.valueOf(value);
							column.setColumnValue(columnValue);
						}
					}
				}
				column.setAppId(appId);
				column.setStoreId(storeId);
				column.setSessionId(sessionId);
				column.setUserId(userId);
				column.setGroupId(groupId);
				column.setBehaviorId(behaviorId);
				column.setObjectId(objectId);
				column.setObjectType(objectType);
				column.setColumnName(columnName);
				column.setTimestamp(timestamp);
				String key = String.format("%s:%s:%s:%s:%s:%s", sessionId, behaviorId, groupId, objectType, objectId, columnName);
				return new KeyValue<String, String>(key, GsonUtils.create().toJson(column));
			}).collect(Collectors.toList());
		});
	}

	@Override
	protected KStream<Windowed<String>, String> mergeColumn(KStream<String, String> stream) {
		return stream.filter((k, v) -> {
			return StringUtils.isNotEmpty(v);
		}).groupByKey().windowedBy(SessionWindows.with(TimeUnit.MINUTES.toMillis(5))).aggregate(() -> "", (k, v, a) -> {
			XvpEventColumn column = GsonUtils.create().fromJson(v, XvpEventColumn.class);
			XvpEventColumn aggregationColumn = null;
			if (StringUtils.isEmpty(a)) {
				aggregationColumn = column;
			} else {
				aggregationColumn = GsonUtils.create().fromJson(a, XvpEventColumn.class);
				if (column.getTimestamp() > aggregationColumn.getTimestamp()) {
					aggregationColumn = column;
				}
			}
			return GsonUtils.create().toJson(aggregationColumn);
		}, (k, v1, v2) -> {
			return v2;
		}).toStream();
	}

	@Override
	protected KStream<Windowed<String>, String> mergeObject(KStream<Windowed<String>, String> stream) {
		return stream.filter((k, v) -> {
			return StringUtils.isNotEmpty(v);
		}).map((k, v) -> {
			XvpEventColumn column = GsonUtils.create().fromJson(v, XvpEventColumn.class);
			String sessionId = column.getSessionId();
			String behaviorId = column.getBehaviorId();
			String groupId = column.getGroupId();
			String key = String.format("%s:%s:%s", sessionId, behaviorId, groupId);
			return new KeyValue<String, String>(key, GsonUtils.create().toJson(column));
		}).groupByKey().windowedBy(SessionWindows.with(TimeUnit.MINUTES.toMillis(5))).aggregate(() -> "", (k, v, a) -> {
			XvpEventColumn column = GsonUtils.create().fromJson(v, XvpEventColumn.class);
			String appId = column.getAppId();
			String storeId = column.getStoreId();
			String sessionId = column.getSessionId();
			String userId = column.getUserId();
			String behaviorId = column.getBehaviorId();
			String groupId = column.getGroupId();
			String objectId = column.getObjectId();
			String objectType = column.getObjectType();
			String columnName = column.getColumnName();
			Object columnValue = column.getColumnValue();
			Long timestamp = column.getTimestamp();
			XvpEventObject object = null;
			if (StringUtils.isEmpty(a)) {
				object = new XvpEventObject();
				object.setAppId(appId);
				object.setStoreId(storeId);
				object.setSessionId(sessionId);
				object.setUserId(userId);
				object.setGroupId(groupId);
				object.setBehaviorId(behaviorId);
				object.setObjectId(objectId);
				object.setObjectType(objectType);
				object.setTimestamp(timestamp);
			} else {
				object = GsonUtils.create().fromJson(a, XvpEventObject.class);
			}
			if (null != columnValue) {
				Map<String, Object> map = object.getValue();
				columnName = String.format("%s_%s", objectType, columnName);
				map.putIfAbsent(columnName, columnValue);
			}
			return GsonUtils.create().toJson(object);
		}, (k, v1, v2) -> {
			return v2;
		}).toStream();
	}

	@Override
	protected KStream<String, String> mergeEvent(KStream<Windowed<String>, String> stream) {
		KStream<String, String> destinationStream = stream.filter((k, v) -> {
			return StringUtils.isNotEmpty(v);
		}).map((k, v) -> {
			XvpEventObject inObject = GsonUtils.create().fromJson(v, XvpEventObject.class);
			XvpEventObject outObject = new XvpEventObject();
			XvpEventSubject subject = new XvpEventSubject();
			XvpEvent event = new XvpEvent();
			EventBehavior behavior = new EventBehavior();
			String appId = inObject.getAppId();
			String storeId = inObject.getStoreId();
			String sessionId = inObject.getSessionId();
			String userId = inObject.getUserId();
			String behaviorId = inObject.getBehaviorId();
			Map<String, Object> objectValue = inObject.getValue();
			Long timestamp = inObject.getTimestamp();
			behavior.setBehaviorId(behaviorId);
			behavior.setTimestamp(timestamp);
			outObject.setValue(objectValue);
			subject.setSessionId(sessionId);
			subject.setUserId(userId);
			event.setAppId(appId);
			event.setStoreId(storeId);
			event.setBehavior(behavior);
			event.setSubject(subject);
			event.setObject(outObject);
			String key = inObject.getSessionId();
			return new KeyValue<String, String>(key, GsonUtils.create().toJson(event));
		});
		destinationStream.process(eventProcessor);
		return destinationStream;
	}

}
