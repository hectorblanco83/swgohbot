package hb.swgohbot.repositories.rest;

import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


/**
 * "Repository" that base it's queries on swgoh.gg/api for "online" mode.
 * This repository will not have persist capabilities.
 *
 * @author Hector Blanco
 */
@Component
@Profile("online")
public class CharacterRepositoryRest implements CharacterRepository {
	
	
	private final ApiClient apiClient;
	
	
	@Autowired
	public CharacterRepositoryRest(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	
	@Override
	public Flux<String> findAllNames() {
		return findAll().map(Character::getName);
	}
	
	
	@Override
	public Flux<Character> findAll() {
		return Flux.fromIterable(apiClient.getCharacterList());
	}
	
}
