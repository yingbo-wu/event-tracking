package cn.rongcapital.mc2.event.server.dao;

import java.util.ArrayList;
import java.util.List;

public class EventMetadata {

	private String rootType;

	private String rootKey;

	private String type;

	private List<EventEntity> entities = new ArrayList<EventEntity>();

	public void addEntity(EventEntity entity) {
		entities.add(entity);
	}

	public String getRootType() {
		return rootType;
	}

	public void setRootType(String rootType) {
		this.rootType = rootType;
	}

	public String getRootKey() {
		return rootKey;
	}

	public void setRootKey(String rootKey) {
		this.rootKey = rootKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<EventEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<EventEntity> entities) {
		this.entities = entities;
	}

}
