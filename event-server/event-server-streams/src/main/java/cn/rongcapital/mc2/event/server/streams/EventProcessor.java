package cn.rongcapital.mc2.event.server.streams;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventProcessor implements ProcessorSupplier<String, String>, Processor<String, String> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Processor<String, String> get() {
		return this;
	}

}
