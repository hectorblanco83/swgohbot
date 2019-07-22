package hb.swgohbot.setup;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


/**
 * @author Hector Blanco
 */
@Profile("offline")
@Configuration
@EnableReactiveMongoRepositories
@Log4j
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {
	
	
	@Override
	protected String getDatabaseName() {
		return "eternal-flames";
	}
	
	
	@Override
	public MongoClient reactiveMongoClient() {
		return MongoClients.create();
	}
}
