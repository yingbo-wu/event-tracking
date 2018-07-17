package cn.rongcapital.mc2.event.server.streams;

import javax.annotation.PostConstruct;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class EventStreams {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${event.kstream.source.topic}")
	private String sourceTopic;

	@Value("${event.kstream.destination.topic}")
	private String destinationTopic;

	@Autowired
	private StreamsConfig streamsConfig;

	private StreamsBuilder streamsBuilder;

	private KafkaStreams kafkaStreams;

	private KStream<String, String> kStream;

	@PostConstruct
	public void start() {
		this.streamsBuilder = new StreamsBuilder();
		// 创建源流
		this.kStream = this.streamsBuilder.stream(this.sourceTopic);
		// 去除重复数据
		KStream<Windowed<String>, String> distinctDataStream = distinctData(this.kStream);
		// 切分数据为事件粒度
		KStream<String, String> splitEventStream = splitEvent(distinctDataStream);
		// 切分为字段粒度
		KStream<String, String> splitColumnStream = splitColumn(splitEventStream);
		// 根据时间窗口进行数据字段去重合并
		KStream<Windowed<String>, String> mergeColumnStream = mergeColumn(splitColumnStream);
		// 聚合字段到客体中
		KStream<Windowed<String>, String> mergeObjectStream = mergeObject(mergeColumnStream);
		// 聚合客体到事件中
		KStream<String, String> mergeEventStream = mergeEvent(mergeObjectStream);
		// 备份事件
		mergeEventStream.to(this.destinationTopic);
		// 创建流容器
		this.kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsConfig);
		// 启动流容器
		this.kafkaStreams.start();
	}

	/**
	 * 去除重复数据
	 * @param stream
	 * @return
	 */
	protected abstract KStream<Windowed<String>, String> distinctData(KStream<String, String> stream);

	/**
	 * 分离事件:将事件数据中的对个事件消息切分开
	 * @param stream
	 * @return
	 */
	protected abstract KStream<String, String> splitEvent(KStream<Windowed<String>, String> stream);

	/**
	 * 分离客体字段:将事件客体数据按字段切分
	 * @param stream
	 * @return
	 */
	protected abstract KStream<String, String> splitColumn(KStream<String, String> stream);

	/**
	 * 合并客体字段:在时间窗口内字段去重合并
	 * @param stream
	 * @return
	 */
	protected abstract KStream<Windowed<String>, String> mergeColumn(KStream<String, String> stream);

	/**
	 * 合并客体内容:将去重后字段重新聚合到一个客体实体中
	 * @param stream
	 * @return
	 */
	protected abstract KStream<Windowed<String>, String> mergeObject(KStream<Windowed<String>, String> stream);

	/**
	 * 合并消息事件:将客体合并到事件中
	 * @param stream
	 * @return
	 */
	protected abstract KStream<String, String> mergeEvent(KStream<Windowed<String>, String> stream);

}
