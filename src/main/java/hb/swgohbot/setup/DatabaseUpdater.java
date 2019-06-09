package hb.swgohbot.setup;

import hb.swgohbot.repositories.mongo.CharacterRepositoryMongo;
import hb.swgohbot.repositories.mongo.PlayerRepositoryMongo;
import hb.swgohbot.repositories.mongo.ShipRepositoryMongo;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Ship;
import lombok.extern.log4j.Log4j;
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
@Component
@Profile("offline")
@Log4j
public class DatabaseUpdater {
	
	// fields
	private final ApiClient apiClient;
	private final CharacterRepositoryMongo charRepo;
	private final ShipRepositoryMongo shipRepo;
	private final PlayerRepositoryMongo playerRepo;
	
	
	public DatabaseUpdater(ApiClient apiClient, CharacterRepositoryMongo charRepo, ShipRepositoryMongo shipRepo, PlayerRepositoryMongo playerRepo) {
		this.apiClient = apiClient;
		this.charRepo = charRepo;
		this.shipRepo = shipRepo;
		this.playerRepo = playerRepo;
	}
	
	
	/**
	 * 2 minutes after initialization, will update {@link Character} list on db, and repeat itself after 24h
	 */
	@Scheduled(fixedRateString = "P1D", initialDelayString = "PT2M")
	public void updateCharacters() {
		LOGGER.info("Updating database's characters list...");
		
		List<Character> characterList = apiClient.getCharacterList();
		LOGGER.debug("Received characters: " + characterList.size());
		
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
		LOGGER.debug("Received ships: " + shipList.size());
		
		shipRepo.saveAll(shipList);
		LOGGER.info("Database's ships list saved!");
	}
	
	
	/**
	 * 2 minutes after initialization, will update {@link Player} list on db, and repeat itself after 24h
	 */
	@Scheduled(fixedRateString = "PT4H", initialDelayString = "PT5M")
	public void updatePlayers() {
		LOGGER.info("Updating database's ships list...");
		
		Guild guild = apiClient.getMyGuild();
		List<Player> players = guild.getPlayers();
		LOGGER.debug("Received " + guild.getName() + " guild with " + players.size() + " players");
		
		playerRepo.saveAll(players);
		LOGGER.info("Database's players list saved!");
	}
	
}
