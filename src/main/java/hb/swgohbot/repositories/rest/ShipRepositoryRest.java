package hb.swgohbot.repositories.rest;

import hb.swgohbot.repositories.ShipRepository;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
public class ShipRepositoryRest implements ShipRepository {
	
	
	private final ApiClient apiClient;
	
	
	@Autowired
	public ShipRepositoryRest(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	
	@Override
	public List<String> findAllNames() {
		return findAll().stream().map(Ship::getName).collect(Collectors.toList());
	}
	
	
	@Override
	public List<Ship> findAll() {
		return apiClient.getShipList();
	}
}
