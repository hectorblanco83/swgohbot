package hb.swgohbot.repositories.mongo;

import hb.swgohbot.repositories.ShipRepository;
import hb.swgohbot.swgoh.api.Ship;
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
public interface ShipRepositoryMongo extends MongoRepository<Ship, String>, ShipRepository {
	
	
	@Override
	public default List<String> findAllNames() {
		return findAll().stream().map(Ship::getName).collect(Collectors.toList());
	}
	
}
