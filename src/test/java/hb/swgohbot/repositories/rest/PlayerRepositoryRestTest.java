package hb.swgohbot.repositories.rest;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlayerRepositoryRestTest {
	
	
	@Mock
	private ApiClient apiClient;
	
	private PlayerRepositoryRest playerRepo;
	
	
	@BeforeEach
	void setUp() {
		playerRepo = new PlayerRepositoryRest(apiClient);
	}
	
	
	@Test
	@DisplayName("Test search for players with certain units at certain rarity")
	void findAllPlayersWithUnit() {
		// given
		Unit unit1 = Unit.builder().id("1").name("Unit1").type(1).rarity(7).gear(12).build();
		Unit unit2 = Unit.builder().id("2").name("Unit2").type(1).rarity(7).gear(12).build();
		Unit unit3 = Unit.builder().id("3").name("Unit3").type(1).rarity(6).gear(10).build();
		Guild guild = new Guild();
		guild.setName("Guild");
		guild.setPlayers(Lists.newArrayList(
				Player.builder().allyCode(1).name("Player1").unit(unit1).unit(unit2).build(),
				Player.builder().allyCode(2).name("Player2").unit(unit1).unit(unit2).build(),
				Player.builder().allyCode(3).name("Player3").unit(unit3).build()
		));
		when(apiClient.getMyGuild()).thenReturn(guild);
		
		// when
		List<Player> players = playerRepo.findAllWithUnitsAndRarity(7, "1");
		
		// then
		assertEquals(2, players.size());
		assertEquals("Player1", players.get(0).getName());
		assertEquals("Player2", players.get(1).getName());
	}
}