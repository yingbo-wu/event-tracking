package cn.rongcapital.mc2.event.server.streams;

import java.util.List;

public interface EventData {

	String getAppId();

	String getStoreId();

	List<? extends Event> getEvents();

}
