package hb.swgohbot.repositories.mongo;

import hb.swgohbot.repositories.PlayerRepository;
import hb.swgohbot.swgoh.api.Player;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Repository mongo db for "offline" mode
 *
 * @author Hector Blanco
 */
@Component
@Profile("offline")
public interface PlayerRepositoryMongo extends MongoRepository<Player, String>, PlayerRepository {
	
	
	@Query(value = "{ " +
			"units: { " +
			"$elemMatch: { " +
			"_id:    { $in : ?1 }, " +
			"rarity: { $gte: ?0 }" +
			"}" +
			"}" +
			"}",
			fields = "{name: 1, last_updated: 1, 'units.$': 1}"
	)
	@Override
	List<Player> findAllWithUnitsAndRarity(int rarity, String... unitsId);
	
}
