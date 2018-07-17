package cn.rongcapital.mc2.event.server.sourcing;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.event.server.common.util.GsonUtils;

@Component
public class Listener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "#{sourceTopic}")
	@SendTo("#{destinationTopic}")
	public String listen(String in) {
		Map<String, Object> map = GsonUtils.create().fromJson(in, Map.class);
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
			String out = GsonUtils.create().toJson(map);
			logger.info("out message is {}", out);
			return out;
		} catch (Exception e) {
			logger.error("error message is {}", ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

}
