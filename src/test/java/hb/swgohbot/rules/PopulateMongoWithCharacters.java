package hb.swgohbot.rules;

import hb.swgohbot.swgoh.api.Character;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * @author Hector Blanco
 */
public class PopulateMongoWithCharacters implements BeforeEachCallback, AfterEachCallback {
	
	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		
		mongoTemplate.save(Character.builder().id("1").name("Char 1").build());
		mongoTemplate.save(Character.builder().id("2").name("Char 2").build());
		
	}
	
	
	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection(Character.class);
		
	}
}
