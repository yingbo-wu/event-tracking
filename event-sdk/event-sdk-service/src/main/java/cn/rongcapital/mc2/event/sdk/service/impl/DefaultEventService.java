package cn.rongcapital.mc2.event.sdk.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.mc2.event.sdk.core.EventData;
import cn.rongcapital.mc2.event.sdk.core.EventObject;
import cn.rongcapital.mc2.event.sdk.core.EventSender;
import cn.rongcapital.mc2.event.sdk.core.EventSenderBuilder;
import cn.rongcapital.mc2.event.sdk.service.EventService;
import cn.rongcapital.mc2.event.sdk.util.GsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * sdk默认事件服务实现
 * @author 英博
 *
 */
public class DefaultEventService implements EventService {

	private Logger logger = LoggerFactory.getLogger(DefaultEventService.class);

	@Override
	public void send(EventData eventData) {
		sendMulti(eventData);
	}

	@Override
	public void sendMulti(EventData... eventDatas) {
		publishMulti(eventDatas).subscribe();
	}

	@Override
	public Mono<Boolean> publish(EventData eventData) {
		return publishMulti(eventData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Mono<Boolean> publishMulti(EventData... eventDatas) {
		Mono<Boolean> mono = Mono.create(callback -> {
			try {
				EventSender sender = EventSenderBuilder.build();
				Flux<Boolean> flux = Flux.create(emitter -> {
					Stream.of(eventDatas).forEach(eventData -> {
						// 过滤嵌套对象
						eventData.getEvents().stream().forEach(data -> {
							try {
								EventObject object = data.getObject();
								Object value = object.getValue();
								if (null != value) {
									String valueJson = GsonUtils.create().toJson(value);
									Map<String, Object> valueMap = GsonUtils.create().fromJson(valueJson, Map.class);
									if (valueMap.containsKey("attrs")) {
										// 适配xvp
										valueMap = (Map<String, Object>) valueMap.get("attrs");
									}
									Map<String, Object> newValueMap = valueMap.entrySet().stream().filter(entry -> {
										Object val = entry.getValue();
										if (null != val) {
											if (val instanceof Map || val instanceof Collection) {
												return false;
											}
										}
										return true;
									}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
									object.setValue(newValueMap);
								}
							} catch (Exception e) {
								emitter.next(false);
							}
						});
						String dataJson = GsonUtils.create().toJson(eventData);
						logger.info("the filter event data json is {}", dataJson);
						sender.send(eventData.getPartitionKey(), dataJson).publishOn(Schedulers.parallel()).subscribe(ok -> {
							emitter.next(ok);
						});
					});
				});
				flux.buffer(eventDatas.length).publishOn(Schedulers.parallel()).subscribe(result -> {
					long size = result.stream().filter(ok -> ok).count();
					if (size == eventDatas.length) {
						callback.success(true);
					} else {
						callback.success(false);
					}
				});
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				callback.success(false);
			}
		});
		return mono.publishOn(Schedulers.parallel());
	}

}
