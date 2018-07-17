package cn.rongcapital.mc2.event.sdk.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.jsonzou.jmockdata.JMockData;

import cn.rongcapital.mc2.event.sdk.core.EventData;
import cn.rongcapital.mc2.event.sdk.core.EventObjectType;
import reactor.core.publisher.Mono;

public class EventSendTest {

	public static void main(String[] args) {
		// 构造事件埋点数据
		EventData data_01 = createEventData_01();
		EventData data_02 = createEventData_02();
		EventData data_03 = createEventData_03();
		// 发送事件埋点数据
		Mono<Boolean> mono_01 = KafkaEventSendUtils.publishEvent(data_01);
		Mono<Boolean> mono_02 = KafkaEventSendUtils.publishEvent(data_02);
		Mono<Boolean> mono_03 = KafkaEventSendUtils.publishEvent(data_03);
		mono_01.then(mono_02).then(mono_03).subscribe();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static EventData createEventData_01() {
		String sessionId = "BnU8OpHVmkq78f9HN%2FLyQ9QH8";
		Map<String, Object> orderMap_10 = mockOrderMap("10");
		Map<String, Object> orderItemMap_1010 = mockOrderItemMap("1010");
		Map<String, Object> productMap_101010 = mockProductMap("101010");
		Map<String, Object> productSkuMap_10101010 = mockProductSkuMap("10101010");

		String groupId_01 = UUID.randomUUID().toString();

		EventData eventData = KafkaEventData.builder()
											.setPartitionKey(sessionId) // 可以使用cookie中的sessionId, 也可是其他内容
											.setStoreId("S_01")
											.setGroupId(groupId_01)
											.setBehaviorId("B_01") // 使用预置的行为id
											.setTimestamp(System.currentTimeMillis()) // 设置时间发生时间
											.setSessionId(sessionId) // 使用cookie中的sessionId
											.setUserId("1") // 使用用户id
											/**
											 * appendObject方法参数说明
											 * @param rootType 聚合根类型
											 * @param rootId 聚合根id
											 * @param objectType 客体类型:xvp业务实体表名抽象枚举
											 * @param objectId 客体标识:xvp业务实体id
											 * @param value 业务实体
											 * @param fillMode 补齐方式 0:不进行补齐、1:补齐自身、2:补齐自身+关联实体
											 * @return
											 */
											.appendObject(EventObjectType.ORDER, "10", orderMap_10, 1) // 可以分类、客体业务标识、客体业务数据、是否要补充字段
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.ORDER_ITEM,  "1010",     orderItemMap_1010,      0)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT,     "101010",   productMap_101010,      0)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT_SKU, "10101010", productSkuMap_10101010, 0)
											.build();
		return eventData;
	}

	private static EventData createEventData_02() {
		String sessionId = "BnU8OpHVmkq78f9HN%2FLyQ9QH8";
		Map<String, Object> orderMap_10 = mockOrderMap("10");
		Map<String, Object> orderItemMap_1010 = mockOrderItemMap("1010");
		Map<String, Object> productMap_101020 = mockProductMap("101020");
		Map<String, Object> productSkuMap_10102010 = mockProductSkuMap("10102010");

		String groupId_02 = UUID.randomUUID().toString();

		EventData eventData = KafkaEventData.builder()
											.setPartitionKey(sessionId) // 可以使用cookie中的sessionId, 也可是其他内容
											.setStoreId("S_01")
											.setGroupId(groupId_02)
											.setBehaviorId("B_01") // 使用预置的行为id
											.setTimestamp(System.currentTimeMillis()) // 设置时间发生时间
											.setSessionId(sessionId) // 使用cookie中的sessionId
											.setUserId("1") // 使用用户id
											/**
											 * appendObject方法参数说明
											 * @param rootType 聚合根类型
											 * @param rootId 聚合根id
											 * @param objectType 客体类型:xvp业务实体表名抽象枚举
											 * @param objectId 客体标识:xvp业务实体id
											 * @param value 业务实体
											 * @param fillMode 补齐方式 0:不进行补齐、1:补齐自身、2:补齐自身+关联实体
											 * @return
											 */
											.appendObject(EventObjectType.ORDER, "10", orderMap_10, 0) // 可以分类、客体业务标识、客体业务数据、是否要补充字段
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.ORDER_ITEM,  "1010",     orderItemMap_1010,      1)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT,     "101020",   productMap_101020,      0)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT_SKU, "10102010", productSkuMap_10102010, 0)
											.build();
		return eventData;
	}

	private static EventData createEventData_03() {
		String sessionId = "BnU8OpHVmkq78f9HN%2FLyQ9QH8";
		Map<String, Object> orderMap_10 = mockOrderMap("10");
		Map<String, Object> orderItemMap_1020 = mockOrderItemMap("1020");
		Map<String, Object> productMap_102010 = mockProductMap("102010");
		Map<String, Object> productSkuMap_10201010 = mockProductSkuMap("10201010");

		String groupId_03 = UUID.randomUUID().toString();

		EventData eventData = KafkaEventData.builder()
											.setPartitionKey(sessionId) // 可以使用cookie中的sessionId, 也可是其他内容
											.setStoreId("S_01")
											.setGroupId(groupId_03)
											.setBehaviorId("B_01") // 使用预置的行为id
											.setTimestamp(System.currentTimeMillis()) // 设置时间发生时间
											.setSessionId(sessionId) // 使用cookie中的sessionId
											.setUserId("1") // 使用用户id
											/**
											 * appendObject方法参数说明
											 * @param rootType 聚合根类型
											 * @param rootId 聚合根id
											 * @param objectType 客体类型:xvp业务实体表名抽象枚举
											 * @param objectId 客体标识:xvp业务实体id
											 * @param value 业务实体
											 * @param fillMode 补齐方式 0:不进行补齐、1:补齐自身、2:补齐自身+关联实体
											 * @return
											 */
											.appendObject(EventObjectType.ORDER, "10", orderMap_10, 2) // 可以分类、客体业务标识、客体业务数据、是否要补充字段
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.ORDER_ITEM,  "1020",     orderItemMap_1020,      0)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT,     "102010",   productMap_102010,      0)
											.appendObject(EventObjectType.ORDER, "10", EventObjectType.PRODUCT_SKU, "10201010", productSkuMap_10201010, 0)
											.build();
		return eventData;
	}

	static Map<String, Object> mockOrderMap(String id) {
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("id", id);
		orderMap.put("name", JMockData.mock(String.class));
		return orderMap;
	}

	static Map<String, Object> mockOrderItemMap(String id) {
		Map<String, Object> orderItemMap = new HashMap<String, Object>();
		orderItemMap.put("id", id);
		orderItemMap.put("amount", JMockData.mock(Integer.class));
		return orderItemMap;
	}

	static Map<String, Object> mockProductMap(String id) {
		Map<String, Object> productMap = new HashMap<String, Object>();
		productMap.put("id", id);
		productMap.put("name", JMockData.mock(String.class));
		return productMap;
	}

	static Map<String, Object> mockProductSkuMap(String id) {
		Map<String, Object> productSkuMap = new HashMap<String, Object>();
		productSkuMap.put("id", id);
		productSkuMap.put("name", JMockData.mock(String.class));
		productSkuMap.put("price", JMockData.mock(Integer.class));
		return productSkuMap;
	}

}
