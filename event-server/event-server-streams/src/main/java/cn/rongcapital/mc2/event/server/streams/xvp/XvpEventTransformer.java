package cn.rongcapital.mc2.event.server.streams.xvp;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.event.server.common.util.GsonUtils;
import cn.rongcapital.mc2.event.server.dao.EventDao;
import cn.rongcapital.mc2.event.server.dao.EventEntity;
import cn.rongcapital.mc2.event.server.dao.EventMetadata;
import cn.rongcapital.mc2.event.server.streams.EventTransformer;
import reactor.core.publisher.Mono;

@Component
public class XvpEventTransformer extends EventTransformer {

	@Autowired
	private EventDao eventMetadataDao;

	@Override
	public KeyValue<String, String> transform(String key, String value) {
		XvpEvent event = GsonUtils.create().fromJson(value, XvpEvent.class);
		XvpEventObject object = event.getObject();
		String rootType = object.getRootType();
		String rootId = object.getRootId();
		String objectType = object.getObjectType();
		String objectId = object.getObjectId();
		Map<String, Object> objectValue = object.getValue();
		Integer fillMode = object.getFillMode();
		// 判断是否补全数据
		if (null != fillMode && 0 != fillMode) {
			// 补齐自身
			EventEntity entity = eventMetadataDao.findOne(objectType, objectId);
			if (null != entity) {
				Map<String, Object> oldVal = object.getValue();
				Map<String, Object> newVal = entity.getValue();
				if (null == oldVal) {
					object.setValue(newVal);
				} else {
					if (null != newVal) {
						newVal.entrySet().stream().forEach(kv -> {
							oldVal.putIfAbsent(kv.getKey(), kv.getValue());
						});
					}
				}
			}
			this.context.forward(key, GsonUtils.create().toJson(event));
			// 补齐关联客体
			if (2 == fillMode) {
				Set<String> infiniteSet = new HashSet<String>();
				infiniteSet.add(objectType);
				infiniteMerge(key, event, object, objectType, objectId, infiniteSet);
			}
		} else {
			this.context.forward(key, value);
		}

		// 合并数据处理
		Mono.create(callback -> {
			EventMetadata eventMetadata = new EventMetadata();
			if (StringUtils.isNoneEmpty(rootType) && StringUtils.isNoneEmpty(rootId)) {
				eventMetadata.setRootType(rootType);
				eventMetadata.setRootKey(rootId);
			}
			eventMetadata.setType(objectType);
			EventEntity entity = new EventEntity();
			entity.setKey(objectId);
			entity.setValue(objectValue);
			eventMetadata.addEntity(entity);
			eventMetadataDao.merge(eventMetadata);
		}).subscribe();

		this.context.commit();
		return null;
	}

	void infiniteMerge(String key, XvpEvent event, XvpEventObject object, String objectType, String objectId, Set<String> infiniteSet) {
		List<EventEntity> entities = eventMetadataDao.findRelationships(objectType, objectId);
		if (CollectionUtils.isNotEmpty(entities)) {
			entities.stream().forEach(kv -> {
				String t = kv.getType();
				String k = kv.getKey();
				object.setObjectType(t);
				object.setObjectId(k);
				object.setValue(kv.getValue());
				this.context.forward(key, GsonUtils.create().toJson(event));
				if (!infiniteSet.contains(t)) {
					infiniteSet.add(t);
					infiniteMerge(key, event, object, t, k, infiniteSet);
				}
			});
		}
	}

}
