package hb.swgohbot.swgoh;

import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.*;
import lombok.extern.log4j.Log4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * Singleton class for accessing swgoh.gg/api
 */
@Component
@Log4j
public class ApiClient {
	
	// user-agent property to send in http connections
	private static final String SWGOH_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
	
	// singleton instance
	private static ApiClient instance;
	
	// Api Client attributes.
	// To prevend deserialization overhead, we will keep those in memory while application is running.
	// TODO: not keep those in mem
	private Guild guild;
	private List<Character> characterList;
	private List<Ship> shipList;
	private List<Gear> gearList;
	
	
	/**
	 * @return the unique instace
	 */
	public static ApiClient getInstance() {
		if(instance == null) {
			instance = new ApiClient();
			LOGGER.debug("API Client created");
		}
		return instance;
	}
	
	
	private HttpEntity<String> getHeaderEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("user-agent", SWGOH_USER_AGENT);
		return new HttpEntity<>("parameters", headers);
	}
	
	
	public Guild getGuild() {
		if(guild != null) {
			LOGGER.debug("Returning cached guild...");
			return guild;
		}
		
		LOGGER.info("Refreshing Guild information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info("Calling swgoh.gg");
		ResponseEntity<Guild> response = restTemplate.exchange("https://swgoh.gg/api/guild/8070/", HttpMethod.GET, entity, Guild.class);
		LOGGER.info("swgoh.gg response received!");
		
		guild = response.getBody();
		return guild;
	}
	
	
	public Player getPlayer(String allyCode) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info("Calling swgoh.gg");
		ResponseEntity<Player> response = restTemplate.exchange("https://swgoh.gg/api/player/" + allyCode, HttpMethod.GET, entity, Player.class);
		LOGGER.info("swgoh.gg response received!");
		
		return response.getBody();
	}
	
	
	public Character getCharacter(String charId) {
		List<Character> characterList = getCharacterList();
		Optional<Character> optionalGear = characterList.stream().filter(character -> character.getId().equals(charId)).findFirst();
		return optionalGear.orElse(null);
	}
	
	
	public List<Character> getCharacterList() {
		if(characterList != null) {
			LOGGER.debug("Returning cached character list...");
			return characterList;
		}
		LOGGER.info("Refreshing Characters information");
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info("Calling swgoh.gg");
		ResponseEntity<List<Character>> response = restTemplate.exchange("https://swgoh.gg/api/characters/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Character>>() {
		});
		LOGGER.info("swgoh.gg response received!");
		
		characterList = response.getBody();
		return characterList;
	}
	
	
	public Ship getShip(String shipId) {
		List<Ship> shipList = getShipList();
		Optional<Ship> optionalGear = shipList.stream().filter(ship -> ship.getId().equals(shipId)).findFirst();
		return optionalGear.orElse(null);
	}
	
	
	public List<Ship> getShipList() {
		if(shipList != null) {
			LOGGER.debug("Returning cached ship list...");
			return shipList;
		}
		LOGGER.info("Refreshing Ships information");
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info("Calling swgoh.gg");
		ResponseEntity<List<Ship>> response = restTemplate.exchange("https://swgoh.gg/api/ships/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Ship>>() {
		});
		LOGGER.info("swgoh.gg response received!");
		
		shipList = response.getBody();
		return shipList;
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
	
	
	public List<Gear> getGearList() {
		if(gearList != null) {
			LOGGER.debug("Returning cached gear list...");
			return gearList;
		}
		
		LOGGER.info("Refreshing Gear information");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = getHeaderEntity();
		
		LOGGER.info("Calling swgoh.gg");
		ResponseEntity<List<Gear>> response = restTemplate.exchange("https://swgoh.gg/api/gear/", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Gear>>() {
		});
		LOGGER.info("swgoh.gg response received!");
		
		gearList = response.getBody();
		return gearList;
	}
	
}
