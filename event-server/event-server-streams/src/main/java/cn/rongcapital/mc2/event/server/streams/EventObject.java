package cn.rongcapital.mc2.event.server.streams;

import java.util.Map;

public abstract class EventObject {

	public abstract String getRootType();

	public abstract String getRootId();

	public abstract String getObjectType();

	public abstract String getObjectId();

	public abstract Map<String, Object> getValue();

	public abstract Long getTimestamp();

	public abstract Integer getFillMode();

	@Override
	public boolean equals(Object obj) {
		if (null != getObjectId() && null != getObjectType()) {
			try {
				EventObject object = (EventObject) obj;
				return getObjectId().equals(object.getObjectId()) && getObjectType().equals(object.getObjectType());
			} catch (Exception e) {
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (null != getObjectId() && null != getObjectType()) {
			return super.hashCode() + getObjectId().hashCode() + getObjectType().hashCode();
		}
		return super.hashCode();
	}

}
