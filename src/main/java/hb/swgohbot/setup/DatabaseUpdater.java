package hb.swgohbot.setup;

import hb.swgohbot.repositories.mongo.CharacterRepositoryMongo;
import hb.swgohbot.repositories.mongo.ShipRepositoryMongo;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.Ship;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * An "scheduler starter" to start all scheduled operations that will update the database when in "offline" mode.
 * Will periodically fetch info from swgoh.gg/api to refresh it's own database.
 *
 * @author Hector Blanco
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
@Profile("offline")
@Log4j
public class DatabaseUpdater {
	
	// field injection to prevent super massive param list on constructor
	@Autowired
	private ApiClient apiClient;
	@Autowired
	private CharacterRepositoryMongo charRepo;
	@Autowired
	private ShipRepositoryMongo shipRepo;
	
	
	/**
	 * 2 minutes after initialization, will update {@link Character} list on db, and repeat itself after 24h
	 */
	@Scheduled(fixedRateString = "P1D", initialDelayString = "PT2M")
	public void updateCharacters() {
		LOGGER.info("Updating database's characters list...");
		
		List<Character> characterList = apiClient.getCharacterList();
		LOGGER.debug("Received " + characterList.size() + " characters");
		
		charRepo.saveAll(characterList);
		LOGGER.info("Database's character list saved!");
	}
	
	
	/**
	 * 2 minutes after initialization, will update {@link Ship} list on db, and repeat itself after 24h
	 */
	@Scheduled(fixedRateString = "P1D", initialDelayString = "PT2M")
	public void updateShips() {
		LOGGER.info("Updating database's ships list...");
		
		List<Ship> shipList = apiClient.getShipList();
		LOGGER.debug("Received " + shipList.size() + " ships");
		
		shipRepo.saveAll(shipList);
		LOGGER.info("Database's ships list saved!");
	}
	
}
