package hb.swgohbot.setup;

import com.google.common.collect.Lists;
import hb.swgohbot.repositories.mongo.CharacterRepositoryMongo;
import hb.swgohbot.repositories.mongo.PlayerRepositoryMongo;
import hb.swgohbot.repositories.mongo.ShipRepositoryMongo;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@ExtendWith({SpringExtension.class,MockitoExtension.class})
@ActiveProfiles(profiles = "offline")
class DatabaseUpdaterTest {
	
	@Mock
	private ApiClient apiClient;
	
	@Autowired
	private CharacterRepositoryMongo charRepo;
	@Autowired
	private ShipRepositoryMongo shipRepo;
	@Autowired
	private PlayerRepositoryMongo playerRepo;
	
	private DatabaseUpdater dbUpdater;
	
	
	@BeforeEach
	void setUp() {
		dbUpdater = new DatabaseUpdater(apiClient, charRepo, shipRepo, playerRepo);
	}
	
	
	@Test
	void updateCharacters() {
		// given
		ArrayList<Character> list = Lists.newArrayList(Character.builder().id("1").name("Char1").build());
		Mockito.when(apiClient.getCharacterList()).thenReturn(list);
		
		// when
		dbUpdater.updateCharacters();
		
		// then
		//assertEquals(1, charRepo.findAll().size());
	}
	
	
	@Test
	void updateShips() {
		// given
		ArrayList<Ship> list = Lists.newArrayList(Ship.builder().id("1").name("Ship1").build());
		Mockito.when(apiClient.getShipList()).thenReturn(list);
		
		// when
		dbUpdater.updateShips();
		
		// then
		assertEquals(1, shipRepo.findAll().collectList().block().size());
	}
	
	
	@Test
	void updatePlayers() {
		// given
		Guild guild = new Guild();
		guild.setPlayers(Lists.newArrayList(Player.builder().allyCode(1).name("Player1").build()));
		Mockito.when(apiClient.getMyGuild()).thenReturn(guild);
		
		// when
		dbUpdater.updatePlayers();
		
		// then
		assertEquals(1, playerRepo.findAll().collectList().block().size());
	}

}