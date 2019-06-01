package hb.swgohbot.services;

import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.repositories.ShipRepository;
import hb.swgohbot.swgoh.api.BaseCharacter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
	
	
	@Autowired
	public SearchService(CharacterRepository characterRepository, ShipRepository shipRepository) {
		this.characterRepository = characterRepository;
		this.shipRepository = shipRepository;
	}
	
	
	public List<BaseCharacter> suggestCharacterFromQuery(String... query) {
		List<BaseCharacter> allChars = new ArrayList<>();
		allChars.addAll(characterRepository.findAll());
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
				String[] splited = replaced.split(" ");
				return ArrayUtils.contains(splited, s);
			});
		}).collect(Collectors.toList());
	}
	
}
