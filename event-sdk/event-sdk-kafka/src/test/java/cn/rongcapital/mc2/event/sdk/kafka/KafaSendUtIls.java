package cn.rongcapital.mc2.event.sdk.kafka;

import cn.rongcapital.mc2.event.sdk.util.PropertiesUtils;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

/**
 * @Author:lhz
 * @Description:
 * @Date:16:12 2018-3-16
 */
public class KafaSendUtIls {

	private static final KafkaSender<Integer, String> sender;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");;
	private static final String topname = PropertiesUtils.getProperty("kafka.topic");

	static {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertiesUtils.getProperty("kafka.address"));
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "SDK");
		props.put(ProducerConfig.ACKS_CONFIG, "1");
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
		props.put(ProducerConfig.RETRIES_CONFIG, 2);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 10);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);
		sender = KafkaSender.create(senderOptions);
	}

	public static void sendOne(String strJson) {

		Random ran = new Random();
		int key = ran.nextInt(100);
		sender.<Integer>send(
				Flux.range(1, 1).map(i -> SenderRecord.create(new ProducerRecord<>(topname, key, strJson), key)))
				.doOnError(e -> System.out.println("send failed :" + e.getMessage())).subscribe(r -> {
					RecordMetadata metadata = r.recordMetadata();
					System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
							r.correlationMetadata(), metadata.topic(), metadata.partition(), metadata.offset(),
							dateFormat.format(new Date(metadata.timestamp())));
				});

	}

	public static void sendMore(List<String> strs) {

		sender.<Integer>send(Flux.fromIterable(strs)
				.map(str -> SenderRecord.create(new ProducerRecord<>(topname, str.hashCode(), str), str.hashCode()))
				.doOnError(e -> System.out.println("send failed" + e.getMessage()))).subscribe(r -> {
					RecordMetadata metadata = r.recordMetadata();
					System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
							r.correlationMetadata(), metadata.topic(), metadata.partition(), metadata.offset(),
							dateFormat.format(new Date(metadata.timestamp())));
				});

	}

	public static String getNowTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS"));
	}

}
