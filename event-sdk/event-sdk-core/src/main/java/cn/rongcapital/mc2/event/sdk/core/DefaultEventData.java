package cn.rongcapital.mc2.event.sdk.core;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * sdk默认事件数据对象
 * @author 英博
 *
 */
public class DefaultEventData implements EventData {

	/**
	 * 事件应用源标识
	 */
	@SerializedName("app_id")
	private String appId;

	/**
	 * 时间数据均衡key, 用于对应支持数据平衡, 例如kafka的partition
	 */
	@SerializedName("partition_key")
	private String partitionKey;

	/**
	 * 事件地点标识
	 */
	@SerializedName("store_id")
	private String storeId;

	/**
	 * 数据标识
	 */
	@SerializedName("data_id")
	private Integer dataId;

	/**
	 * 事件信息集合
	 */
	@SerializedName("events")
	private List<Event> events;

	@Override
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	@Override
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
