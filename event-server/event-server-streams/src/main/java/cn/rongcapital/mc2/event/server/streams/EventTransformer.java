package cn.rongcapital.mc2.event.server.streams;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventTransformer implements TransformerSupplier<String, String, KeyValue<String, String>>, Transformer<String, String, KeyValue<String, String>> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected ProcessorContext context;

	@Override
	public Transformer<String, String, KeyValue<String, String>> get() {
		return this;
	}

	@Override
	public void init(ProcessorContext context) {
		this.context = context;
		this.context.schedule(1000, PunctuationType.STREAM_TIME, timestamp -> {
			punctuate(timestamp);
		});
	}

	@Override
	public KeyValue<String, String> punctuate(long timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
