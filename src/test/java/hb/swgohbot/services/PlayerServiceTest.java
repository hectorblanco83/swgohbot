package hb.swgohbot.services;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Guild;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
	
	@Mock
	ApiClient apiClient;

	
	private Guild makeGildForFindPlayersWtihUnit() {
		Unit vader = new Unit();
		vader.setId("darth-vader");
		vader.setRarity(4);
		
		Player player = new Player();
		player.setName("player");
		player.setUnits(Lists.newArrayList(vader));
		
		Guild guild = new Guild();
		guild.setPlayers(Lists.newArrayList(player));
		return guild;
	}
	
	
	@Test
	void testFindPlayersWithUnit() {
		// given this unit/player/guild
		when(apiClient.getMyGuild()).thenReturn(makeGildForFindPlayersWtihUnit());
		PlayerService playerService = new PlayerService(apiClient);
		
		// when searching players with Vader
		List<Player> players = playerService.findAllWithUnit("darth-vader");
		
		// then find just one
		assert players.size() == 1;
		verify(apiClient, times(1)).getMyGuild();
	}
	
	
	@Test
	void testFindPlayersWithUnitAndRarity() {
		// given this unit/player/guild
		when(apiClient.getMyGuild()).thenReturn(makeGildForFindPlayersWtihUnit());
		PlayerService playerService = new PlayerService(apiClient);
		
		// when searching players with Vader at least 3*
		List<Player> players = playerService.findAllWithUnit("darth-vader", 3);
		
		// then find just one
		assert players.size() == 1;
		verify(apiClient, times(1)).getMyGuild();
	}
	
	
	@Test
	void testFindNonePlayersWithUnit() {
		// given this unit/player/guild
		when(apiClient.getMyGuild()).thenReturn(makeGildForFindPlayersWtihUnit());
		PlayerService playerService = new PlayerService(apiClient);
		
		// when searching players with Vader at least 7*
		List<Player> players = playerService.findAllWithUnit("darth-vader", 7);
		
		// then find none
		assert players.isEmpty();
		verify(apiClient, times(1)).getMyGuild();
	}
	
}