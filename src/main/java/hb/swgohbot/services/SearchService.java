package hb.swgohbot.services;

import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.repositories.PlayerRepository;
import hb.swgohbot.repositories.ShipRepository;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Service for search related issues
 *
 * @author Hector Blanco
 */
@Service
public class SearchService {
	
	private final CharacterRepository characterRepository;
	private final ShipRepository shipRepository;
	private final PlayerRepository playerRepository;
	
	
	@Autowired
	public SearchService(CharacterRepository characterRepository, ShipRepository shipRepository,
						 PlayerRepository playerRepository) {
		this.characterRepository = characterRepository;
		this.shipRepository = shipRepository;
		this.playerRepository = playerRepository;
	}
	
	
	public List<BaseCharacter> suggestCharacterFromQuery(String... query) {
		List<BaseCharacter> allChars = new ArrayList<>();
//		allChars.addAll(characterRepository.findAll().);
		allChars.addAll(shipRepository.findAll());
		
		// Search by name equals
		String joinedQuery = StringUtils.join(query, " ");
		List<BaseCharacter> byNameEquals = allChars
				.stream()
				.filter(character -> joinedQuery.equalsIgnoreCase(character.getName().replace("(", "").replace(")", "")))
				.collect(Collectors.toList());
		if(byNameEquals.size() == 1) {
			return byNameEquals;
		}
		
		// Otherwise search similar names
		return allChars.stream().filter(character -> {
			String lowerCaseCharName = character.getName().toLowerCase();
			Stream<String> queryStream = Stream.of(query).map(String::toLowerCase);
			return queryStream.allMatch(s -> {
				String replaced = lowerCaseCharName.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'s", "");
				String[] split = replaced.split(" ");
				return ArrayUtils.contains(split, s);
			});
		}).collect(Collectors.toList());
	}
	
	
	public Map<String, List<Player>> findPlayersWithChar(List<BaseCharacter> chars, Integer queryRarity) {
		HashMap<String, List<Player>> founded = new HashMap<>();
		chars.forEach(bChar -> founded.put(bChar.getName(), playerRepository.findAllWithUnitsAndRarity(queryRarity, bChar.getId())));
		return founded;
	}
	
}