package cn.rongcapital.mc2.event.server.streams;

public interface Event {

	String getGroupId();

	EventBehavior getBehavior();

	<T extends EventSubject> T getSubject();

	<T extends EventObject> T getObject();

}
