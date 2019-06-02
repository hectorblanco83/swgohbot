package hb.swgohbot.rules;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * @author Hector Blanco
 */
public class PopulateMongoWithPlayers implements BeforeEachCallback, AfterEachCallback {
	
	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		
		// Player 1
		Player player = new Player();
		player.setAllyCode(1);
		player.setName("Player 1");
		player.setUnits(Lists.newArrayList());
		player.getUnits().add(Unit.builder().id("1").name("Unit 1").rarity(6).build());
		player.getUnits().add(Unit.builder().id("2").name("Unit 2").rarity(7).build());
		mongoTemplate.save(player);
		
		// Player 2
		player = new Player();
		player.setAllyCode(2);
		player.setName("Player 2");
		player.setUnits(Lists.newArrayList());
		player.getUnits().add(Unit.builder().id("1").name("Unit 1").rarity(7).build());
		player.getUnits().add(Unit.builder().id("2").name("Unit 2").rarity(7).build());
		mongoTemplate.save(player);
	}
	
	
	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
		MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection(Player.class);
	}
	
}
