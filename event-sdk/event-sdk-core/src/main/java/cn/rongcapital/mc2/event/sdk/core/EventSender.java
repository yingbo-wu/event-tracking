package cn.rongcapital.mc2.event.sdk.core;

import reactor.core.publisher.Mono;

/**
 * sdk事件发送接口定义
 * @author 英博
 *
 */
public interface EventSender {

	Mono<Boolean> send(String balanceKey, String dataJson);

}
