package hb.swgohbot.services;

import com.google.common.collect.Lists;
import hb.swgohbot.repositories.CharacterRepository;
import hb.swgohbot.repositories.PlayerRepository;
import hb.swgohbot.repositories.ShipRepository;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
	
	public static final String DARTH_VADER_NAME = "Darth Vader";
	public static final String UNIT_1_NAME = "unit1";
	public static final String UNIT_2_NAME = "unit2";
	private SearchService service;
	
	@Mock
	private ShipRepository shipRepository;
	
	@Mock
	private CharacterRepository characterRepository;
	
	@Mock
	private PlayerRepository playerRepository;
	
	
	@BeforeEach
	void setUp() {
		service = new SearchService(characterRepository, shipRepository, playerRepository);
	}
	
	
	@Test
	void testSuggestCharacterFromQuery() {
		// given
		Character rjt = new Character();
		rjt.setName("Rey Jedi Training");
		Character rsc = new Character();
		rsc.setName("Rey (Scavenger)");
		when(characterRepository.findAll()).thenReturn(Lists.newArrayList(rjt,rsc));
		when(shipRepository.findAll()).thenReturn(Lists.newArrayList());
		
		// when
		List<BaseCharacter> characters = service.suggestCharacterFromQuery("rey");
		
		// then
		assertEquals(2, characters.size());
		assertEquals("Rey (Scavenger)", characters.get(1).getName());
		verify(characterRepository, times(1)).findAll();
		verify(shipRepository, times(1)).findAll();
	}
	
	
	@Test
	void testSuggestExactlyCharacter() {
		// given
		Character vader = new Character();
		vader.setName(DARTH_VADER_NAME);
		when(characterRepository.findAll()).thenReturn(Lists.newArrayList(vader));
		when(shipRepository.findAll()).thenReturn(Lists.newArrayList());
		
		// when
		List<BaseCharacter> characters = service.suggestCharacterFromQuery(DARTH_VADER_NAME);
		
		// then
		assertEquals(1, characters.size());
		assertEquals(DARTH_VADER_NAME, characters.get(0).getName());
		verify(characterRepository, times(1)).findAll();
		verify(shipRepository, times(1)).findAll();
	}
	
	
	@Test
	void testFindPlayersWithChar() {
		// given
		Unit u1 = Unit.builder().id("1").name(UNIT_1_NAME).rarity(7).build();
		Unit u2 = Unit.builder().id("2").name(UNIT_2_NAME).rarity(7).build();
		Player p1 = Player.builder().allyCode(1).name("player 1").unit(u1).build();
		Player p2 = Player.builder().allyCode(2).name("player 2").unit(u2).build();
		Player p3 = Player.builder().allyCode(3).name("player 3").unit(u1).build();
		
		when(playerRepository.findAllWithUnitsAndRarity(7, "1")).thenReturn(Lists.newArrayList(p1, p3));
		when(playerRepository.findAllWithUnitsAndRarity(7, "2")).thenReturn(Lists.newArrayList(p2));
		
		// when
		ArrayList<BaseCharacter> chars = Lists.newArrayList(
				Unit.builder().id("1").name(UNIT_1_NAME).build(),
				Unit.builder().id("2").name(UNIT_2_NAME).build()
		);
		Map<String, List<Player>> players = service.findPlayersWithChar(chars, 7);
		
		// then
		assertEquals(2, players.keySet().size());
		assertEquals(2, players.get(UNIT_1_NAME).size());
		assertEquals("player 3", players.get(UNIT_1_NAME).get(1).getName());
		assertEquals(1, players.get(UNIT_2_NAME).size());
		assertEquals("player 2", players.get(UNIT_2_NAME).get(0).getName());
	}
	
}