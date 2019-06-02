package hb.swgohbot.repositories.rest;

import hb.swgohbot.repositories.PlayerRepository;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;


/**
 * "Repository" that base it's queries on swgoh.gg/api for "online" mode.
 * This repository will not have persist capabilities.
 *
 * @author Hector Blanco
 */
@Component
@Profile("online")
public class PlayerRepositoryRest implements PlayerRepository {
	
	
	private final ApiClient apiClient;
	
	
	@Autowired
	public PlayerRepositoryRest(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	
	@Override
	public List<Player> findAllWithUnitsAndRarity(int rarity, String... unitsId) {
		Guild guild = apiClient.getMyGuild();
		List<Player> players = guild.getPlayers();
		Iterator<Player> playerIterator = players.iterator();
		while(playerIterator.hasNext()) {
			Player player = playerIterator.next();
			player.getCharacters().removeIf(unit -> !(ArrayUtils.contains(unitsId, unit.getId()) && unit.getRarity().equals(rarity)));
			if(player.getCharacters().isEmpty()) {
				playerIterator.remove();
			}
		}
		return players;
	}
	
}
