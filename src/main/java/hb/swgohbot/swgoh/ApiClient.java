package hb.swgohbot.swgoh;

import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.*;
import lombok.extern.log4j.Log4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Service for accessing swgoh.gg/api
 *
 * @author Hector Blanco
 */
@Service
@CacheConfig(cacheNames = {"swgoh"})
@Log4j
public class ApiClient {
	
	// LOG MSG
	private static final String LOG_CALLING_SWGOH_GG = "Calling swgoh.gg";
	private static final String LOG_SWGOH_GG_RESPONSE_RECEIVED = "swgoh.gg response received!";
	
	// user-agent property to send in http connections
	private static final String SWGOH_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
	
	
	private HttpEntity<String> getHeaderEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("user-agent", SWGOH_USER_AGENT);
		return new HttpEntity<>("parameters", headers);
	}
	

	@Cacheable
	public Guild getGuild() {
		LOGGER.info("Refreshing Guild information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info(LOG_CALLING_SWGOH_GG);
		ResponseEntity<Guild> response = restTemplate.exchange("https://swgoh.gg/api/guild/8070/", HttpMethod.GET, entity, Guild.class);
		LOGGER.info(LOG_SWGOH_GG_RESPONSE_RECEIVED);
		return response.getBody();
	}
	
	
	@Cacheable
	public Player getPlayer(String allyCode) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info(LOG_CALLING_SWGOH_GG);
		ResponseEntity<Player> response = restTemplate.exchange("https://swgoh.gg/api/player/" + allyCode, HttpMethod.GET, entity, Player.class);
		LOGGER.info(LOG_SWGOH_GG_RESPONSE_RECEIVED);
		
		return response.getBody();
	}
	
	
	public Character getCharacter(String charId) {
		List<Character> characterList = getCharacterList();
		Optional<Character> optionalGear = characterList.stream().filter(character -> character.getId().equals(charId)).findFirst();
		return optionalGear.orElse(null);
	}
	
	
	@Cacheable
	public List<Character> getCharacterList() {
		LOGGER.info("Refreshing Characters information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info(LOG_CALLING_SWGOH_GG);
		ResponseEntity<List<Character>> response = restTemplate.exchange("https://swgoh.gg/api/characters/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Character>>() {});
		LOGGER.info(LOG_SWGOH_GG_RESPONSE_RECEIVED);
		
		return response.getBody();
	}
	
	
	public Ship getShip(String shipId) {
		List<Ship> shipList = getShipList();
		Optional<Ship> optionalGear = shipList.stream().filter(ship -> ship.getId().equals(shipId)).findFirst();
		return optionalGear.orElse(null);
	}
	
	
	@Cacheable
	public List<Ship> getShipList() {
		LOGGER.info("Refreshing Ships information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info(LOG_CALLING_SWGOH_GG);
		ResponseEntity<List<Ship>> response = restTemplate.exchange("https://swgoh.gg/api/ships/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Ship>>() {});
		LOGGER.info(LOG_SWGOH_GG_RESPONSE_RECEIVED);
		return response.getBody();
	}
	
	
	public List<DescriptiveCharacter> getCharAndShipList() {
		ArrayList<DescriptiveCharacter> allCharsAndShips = new ArrayList<>();
		allCharsAndShips.addAll(getCharacterList());
		allCharsAndShips.addAll(getShipList());
		return allCharsAndShips;
	}
	
	
	public Gear getGear(String gearId) {
		List<Gear> gearList = getGearList();
		Optional<Gear> optionalGear = gearList.stream().filter(gear -> gear.getId().equals(gearId)).findFirst();
		return optionalGear.orElse(null);
	}
	
	
	@Cacheable
	public List<Gear> getGearList() {
		LOGGER.info("Refreshing Gear information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info(LOG_CALLING_SWGOH_GG);
		ResponseEntity<List<Gear>> response = restTemplate.exchange("https://swgoh.gg/api/gear/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Gear>>() {});
		LOGGER.debug("gear list received: " + response.getBody());
		LOGGER.info(LOG_SWGOH_GG_RESPONSE_RECEIVED);
		return response.getBody();
	}
	
}
