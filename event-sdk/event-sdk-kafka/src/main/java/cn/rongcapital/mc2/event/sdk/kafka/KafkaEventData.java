package cn.rongcapital.mc2.event.sdk.kafka;

/**
 * sdk kafka事件数据操作类
 * @author 英博
 *
 */
public class KafkaEventData {

	public static KafkaEventDataBuilder builder() {
		return new KafkaEventDataBuilder();
	}

}
