package hb.swgohbot.services;

import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.DescriptiveCharacter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class SearchService {
	
	// the swgoh.gg/api client
	private final ApiClient apiClient;
	
	
	public SearchService(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	
	public List<BaseCharacter> suggestCharacterFromQuery(String... query) {
		List<DescriptiveCharacter> allChars = apiClient.getCharAndShipList();
		
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
