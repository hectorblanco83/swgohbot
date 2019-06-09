package hb.swgohbot.repositories.mongo;

import hb.swgohbot.rules.PopulateMongoWithCharacters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataMongoTest
@ExtendWith({SpringExtension.class, PopulateMongoWithCharacters.class})
@ActiveProfiles(profiles = "offline")
class CharacterRepositoryMongoIT {
	
	@Autowired
	private CharacterRepositoryMongo charRepository;
	
	
	@Test
	void findAllWithUnitsAndRarity() {
		List<String> names = charRepository.findAllNames();
		assertEquals(2, names.size());
		assertEquals("Char 1", names.get(0));
		assertEquals("Char 2", names.get(1));
	}
	
}