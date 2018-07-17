package cn.rongcapital.mc2.event.server.dao;

import java.util.List;

public interface EventDao {

	EventEntity findOne(String type, String key);

	List<EventEntity> findRelationships(String type, String key);

	void merge(EventMetadata eventMetadata);

}
