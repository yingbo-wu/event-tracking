package cn.rongcapital.mc2.event.server.streams.xvp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.server.streams.EventData;

public class XvpEventData implements EventData {

	@SerializedName("app_id")
	private String appId;

	@SerializedName("store_id")
	private String storeId;

	@SerializedName("data_id")
	private Integer dataId;

	@SerializedName("events")
	private List<XvpEvent> events = new ArrayList<XvpEvent>();

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public List<XvpEvent> getEvents() {
		return events;
	}

	public void setEvents(List<XvpEvent> events) {
		this.events = events;
	}

}
