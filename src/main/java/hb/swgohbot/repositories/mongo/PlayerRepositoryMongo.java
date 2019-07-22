package hb.swgohbot.repositories.mongo;

import hb.swgohbot.repositories.PlayerRepository;
import hb.swgohbot.swgoh.api.Player;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * Repository mongo db for "offline" mode
 *
 * @author Hector Blanco
 */
@Component
@Profile("offline")
public interface PlayerRepositoryMongo extends ReactiveMongoRepository<Player, String>, PlayerRepository {
	
	
	@Query(value = 	"{ " +
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
	Flux<Player> findAllWithUnitsAndRarity(int rarity, String... unitsId);
	
}
