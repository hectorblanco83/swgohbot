package hb.swgohbot.repositories.mongo;

import hb.swgohbot.rules.PopulateMongoWithPlayers;
import hb.swgohbot.swgoh.api.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataMongoTest
@ExtendWith({SpringExtension.class, PopulateMongoWithPlayers.class})
@ActiveProfiles(profiles = "offline")
class PlayerRepositoryMongoIT {
	
	@Autowired
	private PlayerRepositoryMongo playerRepository;
	
	
	@Test
	void findAllWithUnitsAndRarity() {
		List<Player> players = playerRepository.findAllWithUnitsAndRarity(7, "1").collectList().block();
		assertEquals(1, players.size());
		assertEquals("1", players.get(0).getUnits().get(0).getId());
	}
	
}