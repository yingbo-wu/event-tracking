package cn.rongcapital.mc2.event.sdk.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.mc2.event.sdk.core.EventSender;
import cn.rongcapital.mc2.event.sdk.util.GsonUtils;
import cn.rongcapital.mc2.event.sdk.util.PropertiesUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

/**
 * 封装reactor-kafka KafkaSender 访问
 * 
 * @author 英博
 *
 */
public class KafkaEventSender implements EventSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventSender.class);
	private static final KafkaSender<String, String> KAFKA_SENDER;
	private static final String TOP_NAME = PropertiesUtils.getProperty("kafka.topic");

	static {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertiesUtils.getProperty("kafka.servers"));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		SenderOptions<String, String> senderOptions = SenderOptions.create(props);
		KAFKA_SENDER = KafkaSender.create(senderOptions);
	}

	@Override
	public Mono<Boolean> send(String partitionKey, String dataJson) {
		LOGGER.info("send event data is {}", dataJson);
		return sendMulti(partitionKey, dataJson);
	}

	/**
	 * 
	 * @param partitionKey
	 * @param dataJsons
	 */
	Mono<Boolean> sendMulti(String partitionKey, String... dataJsons) {
		Flux<SenderRecord<String, String, Integer>> outboundFlux = Flux.just(dataJsons).map(dataJson -> {
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOP_NAME, partitionKey, dataJson);
			return SenderRecord.create(record, dataJson.hashCode());
		});
		Mono<Boolean> mono = Mono.create(callback -> {
			KAFKA_SENDER.send(outboundFlux)
						.doOnError(e -> {
							LOGGER.error(e.getMessage());
							callback.success(false);
						})
						.subscribe(result -> {
							LOGGER.info(GsonUtils.create().toJson(result));
							callback.success(true);
						});
		});
		return mono;
	}

}
