package cn.rongcapital.mc2.event.sdk.service;

import cn.rongcapital.mc2.event.sdk.core.EventData;
import reactor.core.publisher.Mono;

/**
 * sdk事件服务接口
 * @author 英博
 *
 */
public interface EventService {

	void send(EventData eventData);

	void sendMulti(EventData... eventDatas);

	Mono<Boolean> publish(EventData eventData);

	Mono<Boolean> publishMulti(EventData... eventDatas);

}
