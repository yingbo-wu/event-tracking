package cn.rongcapital.mc2.event.sdk.core;

import com.google.gson.annotations.SerializedName;

import cn.rongcapital.mc2.event.sdk.util.PropertiesUtils;

/**
 * sdk事件客体数据封装
 * @author 英博
 *
 */
public class EventObject {

	public final static String TYPE_PREFIX = PropertiesUtils.getProperty("sdk.object.type.prefix");

	/**
	 * 聚合根类型
	 */
	@SerializedName("root_type")
	private String rootType;

	/**
	 * 聚合根id
	 */
	@SerializedName("root_id")
	private String rootId;

	/**
	 * 客体类型:商品、订单、购物车
	 */
	@SerializedName("object_type")
	private String objectType;

	/**
	 * 客体标识:商品id、订单id、购物车id
	 */
	@SerializedName("object_id")
	private String objectId;

	/**
	 * 客体内容
	 */
	@SerializedName("value")
	private Object value;

	/**
	 * 客体内容补充模式
	 */
	@SerializedName("fill_mode")
	private int fillMode;

	public EventObject() {}

	public EventObject(EventObjectType objectType) {
		this.objectType = TYPE_PREFIX + objectType.name().toLowerCase();
	}

	public EventObject(EventObjectType objectType, String objectId, Object value) {
		this(objectType);
		this.objectId = objectId;
		this.value = value;
	}

	public EventObject(EventObjectType objectType, String objectId, Object value, int fillMode) {
		this(objectType);
		this.objectId = objectId;
		this.value = value;
		this.fillMode = fillMode;
	}

	public EventObject(EventObjectType rootType, String rootId, EventObjectType objectType, String objectId, Object value, int fillMode) {
		this(objectType);
		this.rootType = TYPE_PREFIX + rootType.name().toLowerCase();
		this.rootId = rootId;
		this.objectId = objectId;
		this.value = value;
		this.fillMode = fillMode;
	}

	public String getRootType() {
		return rootType;
	}

	public void setRootType(String rootType) {
		this.rootType = rootType;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getFillMode() {
		return fillMode;
	}

	public void setFillMode(int fillMode) {
		this.fillMode = fillMode;
	}

}
