package cn.rongcapital.mc2.event.sdk.core;

import java.util.List;

/**
 * sdk事件数据接口定义
 * @author 英博
 *
 */
public interface EventData {

	String getAppId();

	String getPartitionKey();

	Integer getDataId();

	List<Event> getEvents();

}
