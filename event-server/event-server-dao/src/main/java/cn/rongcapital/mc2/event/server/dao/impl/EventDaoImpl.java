package cn.rongcapital.mc2.event.server.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.event.server.dao.EventDao;
import cn.rongcapital.mc2.event.server.dao.EventEntity;
import cn.rongcapital.mc2.event.server.dao.EventMetadata;

@Component
public class EventDaoImpl implements EventDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public EventEntity findOne(String type, String key) {
		Query query = new Query(Criteria.where("key").is(key));
		EventEntity document = mongoTemplate.findOne(query, EventEntity.class, type);
		if (null != document) {
			document.setType(type);
		}
		return document;
	}

	@Override
	public List<EventEntity> findRelationships(String type, String key) {
		List<EventEntity> entities = new ArrayList<EventEntity>();
		EventEntity rootDocument = findOne(type, key);
		if (null != rootDocument) {
			Map<String, String> relationship = rootDocument.getRelationship();
			if (null != relationship) {
				relationship.entrySet().stream().forEach(kv -> {
					String relationType = kv.getKey();
					String relationKey = kv.getValue();
					Query relationQuery = new Query(Criteria.where("key").is(relationKey));
					EventEntity relationDocument = mongoTemplate.findOne(relationQuery, EventEntity.class, relationType);
					if (null != relationDocument) {
						relationDocument.setType(relationType);
						entities.add(relationDocument);
					}
				});
			}
		}
		return entities;
	}

	@Override
	public void merge(EventMetadata eventMetadata) {
		String type = eventMetadata.getType();
		String rootType = eventMetadata.getRootType();
		String rootKey = eventMetadata.getRootKey();
		boolean exists = false;
		if (StringUtils.isNoneEmpty(rootType) && StringUtils.isNoneEmpty(rootKey)) {
			// 判断根表是否存在
			exists = mongoTemplate.collectionExists(rootType);
			// 不存在则创建
			if (!exists) {
				mongoTemplate.createCollection(rootType);
				TextIndexDefinition textIndex = new TextIndexDefinitionBuilder().onField("key").build();
				mongoTemplate.indexOps(rootType).ensureIndex(textIndex);
			}
			BulkOperations rootBulkOperations = mongoTemplate.bulkOps(BulkMode.ORDERED, rootType);
			List<Pair<Query, Update>> rootUpdates = eventMetadata.getEntities().stream().map(entity -> {
				// 查询并更新或插入根表数据
				Query rootQuery = new Query(Criteria.where("key").is(rootKey));
				Update rootUpdate = new Update();
				rootUpdate.set("relationship." + type, entity.getKey());
				return Pair.of(rootQuery, rootUpdate);
			}).collect(Collectors.toList());
			rootBulkOperations.upsert(rootUpdates);
			rootBulkOperations.execute();
		}
		// 判断本表是否存在
		exists = mongoTemplate.collectionExists(type);
		// 不存在则创建
		if (!exists) {
			mongoTemplate.createCollection(type);
			TextIndexDefinition textIndex = new TextIndexDefinitionBuilder().onField("key").build();
			mongoTemplate.indexOps(type).ensureIndex(textIndex);
		}
		// 批量更新信息
		BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.ORDERED, type);
		List<Pair<Query, Update>> updates = eventMetadata.getEntities().stream().map(entity -> {
			Query query = new Query(Criteria.where("key").is(entity.getKey()));
			Update update = new Update();
			Map<String, Object> map = entity.getValue();
			if (null != map) {
				map.entrySet().stream().forEach(kv -> {
					String key = String.format("value.%s", kv.getKey());
					Object value  = kv.getValue();
					update.set(key, value);
				});
			}
			update.set("root_type", rootType);
			update.set("root_key", rootKey);
			return Pair.of(query, update);
		}).collect(Collectors.toList());
		bulkOperations.upsert(updates);
		bulkOperations.execute();
	}

}
