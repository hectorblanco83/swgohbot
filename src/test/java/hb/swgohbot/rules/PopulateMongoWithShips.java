package hb.swgohbot.rules;

import hb.swgohbot.swgoh.api.Ship;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * @author Hector Blanco
 */
public class PopulateMongoWithShips implements BeforeEachCallback, AfterEachCallback {
	
	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		
		mongoTemplate.save(Ship.builder().id("1").name("Ship 1").build());
		mongoTemplate.save(Ship.builder().id("2").name("Ship 2").build());
	}
	
	
	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection(Ship.class);
	}
}
