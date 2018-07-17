package cn.rongcapital.mc2.event.server.streams;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class Config {

	@Bean
	public StreamsConfig streamsConfig(@Value("${event.kstream.servers}") String servers, @Value("${event.kstream.app.id}") String appId) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, Runtime.getRuntime().availableProcessors());
		return new StreamsConfig(props);
	}

	@Bean
	public StoreBuilder<KeyValueStore<String, String>> storeBuilder(@Value("${event.kstream.store.name}") String storeName) {
		KeyValueBytesStoreSupplier bytesStoreSupplier = Stores.persistentKeyValueStore(storeName);
		return Stores.keyValueStoreBuilder(bytesStoreSupplier, Serdes.String(), Serdes.String());
	}

	@Bean
	public Map<String, Object> producerConfigs(String servers) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory(String servers) {
		return new DefaultKafkaProducerFactory<String, String>(producerConfigs(servers));
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(@Value("${azeroth.kafka.servers}") String servers) {
		return new KafkaTemplate<String, String>(producerFactory(servers));
	}

}
