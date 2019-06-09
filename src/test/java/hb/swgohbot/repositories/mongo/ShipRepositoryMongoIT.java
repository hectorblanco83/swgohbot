package hb.swgohbot.repositories.mongo;

import hb.swgohbot.rules.PopulateMongoWithShips;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataMongoTest
@ExtendWith({SpringExtension.class, PopulateMongoWithShips.class})
@ActiveProfiles(profiles = "offline")
class ShipRepositoryMongoIT {
	
	@Autowired
	private ShipRepositoryMongo shipRepository;
	
	
	@Test
	void findAllWithUnitsAndRarity() {
		
		List<String> names = shipRepository.findAllNames();
		assertEquals(2, names.size());
		assertEquals("Ship 1", names.get(0));
		assertEquals("Ship 2", names.get(1));
	}
	
}