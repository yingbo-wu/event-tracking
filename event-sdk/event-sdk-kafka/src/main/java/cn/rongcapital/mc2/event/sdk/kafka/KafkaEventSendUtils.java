package cn.rongcapital.mc2.event.sdk.kafka;

import cn.rongcapital.mc2.event.sdk.core.EventData;
import cn.rongcapital.mc2.event.sdk.service.ServiceFactory;
import reactor.core.publisher.Mono;

/**
 * @Author:lhz
 * @Description:
 * @Date:16:10 2018-3-16
 */
public class KafkaEventSendUtils {

	// 默认实现
	public static void sendEvent(EventData eventData) {
		if (eventData == null) {
			System.out.println("error data is null");
		} else {
			ServiceFactory.createEventService().send(eventData);
		}
	}

	// 默认实现
	public static void sendEvents(EventData...eventDatas) {
		if (eventDatas == null) {
			System.out.println("error data is null");
		} else {
			ServiceFactory.createEventService().sendMulti(eventDatas);
		}
	}

	public static Mono<Boolean> publishEvent(EventData eventData) {
		if (eventData == null) {
			System.out.println("error data is null");
			return Mono.just(false);
		} else {
			return ServiceFactory.createEventService().publish(eventData);
		}
	}

	public static Mono<Boolean> publishEvents(EventData...eventDatas) {
		if (eventDatas == null) {
			System.out.println("error data is null");
			return Mono.just(false);
		} else {
			return ServiceFactory.createEventService().publishMulti(eventDatas);
		}
	}

}
