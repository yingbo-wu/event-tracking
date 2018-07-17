package cn.rongcapital.mc2.event.server.dao;

import java.util.Map;

public class EventEntity {

	private String type;

	private String key;

	private Map<String, Object> value;

	private Map<String, String> relationship;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> getValue() {
		return value;
	}

	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

	public Map<String, String> getRelationship() {
		return relationship;
	}

	public void setRelationship(Map<String, String> relationship) {
		this.relationship = relationship;
	}

}
