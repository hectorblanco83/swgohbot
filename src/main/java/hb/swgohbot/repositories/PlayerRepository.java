package hb.swgohbot.repositories;

import hb.swgohbot.swgoh.api.Player;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * Player's repository
 *
 * @author Hector Blanco
 */
@Component
public interface PlayerRepository {
	
	Flux<Player> findAllWithUnitsAndRarity(int rarity, String... unitsId);
}
