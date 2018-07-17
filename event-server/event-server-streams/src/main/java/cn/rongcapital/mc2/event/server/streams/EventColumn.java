package cn.rongcapital.mc2.event.server.streams;

public abstract class EventColumn {

	public abstract String getObjectId();

	public abstract String getObjectType();

	public abstract String getColumnName();

	public abstract Object getColumnValue();

}
