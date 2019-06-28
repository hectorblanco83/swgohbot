package hb.swgohbot.swgoh.api;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class PlayerTest {
	
	
	private static final String UNIT_1_NAME = "unit1";
	private static final String UNIT_2_NAME = "unit2";
	private static final String UNIT_3_NAME = "unit3";
	
	
	@DisplayName("Assert correct Player parsing data from swgoh.gg")
	@Test
	void testParseData() throws ParseException {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("ally_code", 1234);
		data.put("name", "player 1");
		data.put("url", "1234.com");
		data.put("galactic_power", 10000);
		data.put("last_updated", 0);
		
		// when
		Player player = new Player();
		player.parseData(data);
		
		// then
		assertEquals(Integer.valueOf(1234), player.getAllyCode());
		assertEquals("player 1", player.getName());
		
		// date from swgoh is UTC
		assertEquals(Instant.ofEpochMilli(0).toEpochMilli(), player.getLastUpdated().getTime());
	}
	
	
	@DisplayName("Assert stream filter correctness when searching from only Characters within player units")
	@Test
	void testGetCharacters() {
		// given
		Player player = new Player();
		player.setUnits(Lists.newArrayList(
				Unit.builder().id("1").name(UNIT_1_NAME).type(1).build(),
				Unit.builder().id("2").name(UNIT_2_NAME).type(2).build(),
				Unit.builder().id("3").name(UNIT_3_NAME).type(1).build()
		));
		
		// when
		List<Unit> characters = player.getCharacters();
		
		// then
		assertEquals(2, characters.size());
		assertEquals(UNIT_3_NAME, characters.get(1).getName());
	}
	
	
	@DisplayName("Assert stream filter correctness when searching from specific Character within player units")
	@Test
	void testGetCharacterById() {
		// given
		Player player = new Player();
		player.setUnits(Lists.newArrayList(
				Unit.builder().id("1").name(UNIT_1_NAME).type(1).build(),
				Unit.builder().id("2").name(UNIT_2_NAME).type(2).build(),
				Unit.builder().id("3").name(UNIT_3_NAME).type(1).build()
		));
		
		// when
		Unit character = player.getCharacterById("3");
		
		
		// then
		assertNotNull(character);
		assertEquals(UNIT_3_NAME, character.getName());
	}
	
	
	@DisplayName("Assert stream filter correctness when searching from only Ships within player units")
	@Test
	void testGetShips() {
		// given
		Player player = new Player();
		player.setUnits(Lists.newArrayList(
				Unit.builder().id("1").name(UNIT_1_NAME).type(1).build(),
				Unit.builder().id("2").name(UNIT_2_NAME).type(2).build(),
				Unit.builder().id("3").name(UNIT_3_NAME).type(1).build()
		));
		
		// when
		List<Unit> ships = player.getShips();
		
		// then
		assertEquals(1, ships.size());
		assertEquals(UNIT_2_NAME, ships.get(0).getName());
	}
	
}