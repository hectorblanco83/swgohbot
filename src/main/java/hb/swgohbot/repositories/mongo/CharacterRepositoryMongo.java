package hb.swgohbot.repositories.mongo;

import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.swgoh.api.Character;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Repository mongo db for "offline" mode
 *
 * @author Hector Blanco
 */
@Component
@Profile("offline")
public interface CharacterRepositoryMongo extends MongoRepository<Character, String>, CharacterRepository {
	
	
	@Override
	public default List<String> findAllNames() {
		return findAll().stream().map(Character::getName).collect(Collectors.toList());
	}
	
}
