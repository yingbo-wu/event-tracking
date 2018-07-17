package cn.rongcapital.mc2.event.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

@Configuration
@ComponentScan
public class AutoConfiguration {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public MongoClient mongoClient(String servers) {
		MongoClientOptions options = MongoClientOptions.builder().connectTimeout(60000).build();
		List<ServerAddress> addressList = new ArrayList<ServerAddress>();
		Stream.of(servers.split(",")).forEach(server -> {
			try {
				String[] strs = server.split(":");
				ServerAddress address = new ServerAddress(strs[0], Integer.parseInt(strs[1]));
				addressList.add(address);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		});
		return new MongoClient(addressList, options);
	}

	@Bean
	public MongoDbFactory mongoDbFactory(String servers, String database) {
		return new SimpleMongoDbFactory(mongoClient(servers), database);
	}

	@Bean
	public MongoTemplate mongoTemplate(@Value("${event.mongo.servers}") String servers, @Value("${event.mongo.database}") String database) {
		MongoDbFactory mongoDbFactory = mongoDbFactory(servers, database);
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
		return mongoTemplate;
	}

}
