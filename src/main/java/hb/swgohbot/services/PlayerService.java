package hb.swgohbot.services;

import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;


/**
 * Service for player issues
 *
 * @author Blanco Hector
 */
@Log4j
@Service
public class PlayerService {
	
	// swgoh api's client
	private final ApiClient apiClient;
	
	
	/**
	 * Default constructor
	 */
	public PlayerService(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	
	/**
	 * Find all players in your guild that have the unit in input
	 *
	 * @param unitId Id of the unit to search in player's rosters
	 * @return The list of player's that have the unit searched
	 */
	public List<Player> findAllWithUnit(String unitId) {
		return findAllWithUnit(unitId, null);
	}
	
	
	/**
	 * Find all players in your guild that have the unit in input
	 *
	 * @param unitId Id of the unit to search in player's rosters
	 * @param rarity the minimum rarity of the unit to search
	 * @return The list of player's that have the unit searched
	 */
	public List<Player> findAllWithUnit(String unitId, Integer rarity) {
		if(rarity == null) {
			rarity = 1;
		}
		int finalRarity = rarity;
		
		Guild guild = apiClient.getMyGuild();
		List<Player> players = guild.getPlayers();
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext()) {
			Player player = iterator.next();
			Optional<Unit> optional = player.getUnits().stream().filter(character -> character.getId().equals(unitId) && character.getRarity() >= finalRarity).findFirst();
			if(!optional.isPresent()) {
				iterator.remove();
			}
		}
		
		return players;
	}
	
	
}