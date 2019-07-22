package hb.swgohbot.repositories.mongo;

import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.swgoh.api.Character;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Repository mongo db for "offline" mode
 *
 * @author Hector Blanco
 */
@Component
@Profile("offline")
public interface CharacterRepositoryMongo extends ReactiveMongoRepository<Character, String>, CharacterRepository {
	
	
	@Override
	public default Flux<String> findAllNames() {
		return findAll().map(Character::getName);
	}
	
}
