package cn.rongcapital.mc2.event.server.streams;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventSender implements InitializingBean, ApplicationContextAware {

	private static KafkaTemplate<String, String> kafkaTemplate;

	private ApplicationContext applicationContext;

	public static KafkaTemplate<String, String> create() {
		return kafkaTemplate;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		kafkaTemplate = applicationContext.getBean(KafkaTemplate.class);
	}

}
