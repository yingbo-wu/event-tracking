package cn.rongcapital.mc2.event.server.streams.xvp;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.event.server.common.util.GsonUtils;
import cn.rongcapital.mc2.event.server.streams.EventProcessor;
import cn.rongcapital.mc2.event.server.streams.EventSender;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class XvpEventProcessor extends EventProcessor {

	@Value("${azeroth.kafka.topic}")
	private String topic;

	protected ProcessorContext context;

	@Override
	public void init(ProcessorContext context) {
		this.context = context;
		this.context.schedule(1000, PunctuationType.STREAM_TIME, timestamp -> {
			punctuate(timestamp);
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(String key, String value) {
		logger.info("origin value is {}", value);
		Mono.create(callback -> {
			Map<String, Object> map = GsonUtils.create().fromJson(value, Map.class);
			Map<String, Object> object = (Map<String, Object>) map.get("object");
			if (null != object) {
				Map<String, Object> attribute = (Map<String, Object>) object.get("value");
				if (null != attribute) {
					object.remove("type");
					object.remove("value");
					object.putAll(attribute);
				}
			}
			try {
				String message = GsonUtils.create().toJson(map);
				logger.info("send message is {}", message);
				KafkaTemplate<String, String> kafkaTemplate = EventSender.create();
				kafkaTemplate.send(topic, message);
				callback.success();
			} catch (Exception e) {
				logger.error("send error is {}", ExceptionUtils.getStackTrace(e));
				callback.success();
			}
		}).publishOn(Schedulers.parallel()).subscribe();
	}

	@Override
	public void punctuate(long timestamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
