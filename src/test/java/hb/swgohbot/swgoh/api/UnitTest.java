package hb.swgohbot.swgoh.api;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnitTest {
	
	
	@Test
	void testParseData() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("base_id", "1234");
		data.put("name", "Unit 1");
		data.put("power", 1);
		data.put("gear_level", 1);
		data.put("level", 1);
		data.put("rarity", 1);
		data.put("url", "www.test.com");
		data.put("combat_type", 1);
		
		LinkedHashMap<String, Object> ability1 = new LinkedHashMap<>();
		ability1.put("id", "1");
		ability1.put("name", "1");
		ability1.put("ability_tier", 1);
		ability1.put("tier_max", 1);
		ability1.put("is_omega", false);
		ability1.put("is_zeta", false);
		LinkedHashMap<String, Object> ability2 = new LinkedHashMap<>();
		ability2.put("id", "2");
		ability2.put("name", "2");
		ability2.put("ability_tier", 1);
		ability2.put("tier_max", 1);
		ability2.put("is_omega", false);
		ability2.put("is_zeta", false);
		data.put("ability_data", Lists.newArrayList(ability1, ability2));
		
		data.put("zeta_abilities",  Lists.newArrayList("1", "2"));
		
		LinkedHashMap<String, Object> gear1 = new LinkedHashMap<>();
		gear1.put("slot", "1");
		gear1.put("base_id", "1");
		gear1.put("is_obtained", false);
		LinkedHashMap<String, Object> gear2 = new LinkedHashMap<>();
		gear2.put("slot", "2");
		gear2.put("base_id", "2");
		gear2.put("is_obtained", false);
		data.put("gear", Lists.newArrayList(gear1, gear2));
		
		
		Unit unit = new Unit();
		unit.parseData(data);
		
		
		assertEquals("1234", unit.getId());
		assertEquals(new Integer(1), unit.getType());
		assertEquals(2, unit.getZetaAbilities().size());
		assertEquals(2, unit.getPlacedGears().size());
		assertEquals("2", unit.getPlacedGears().get(1).getGearId());
		
	}
	
	
	@Test
	void testGetZetaAbilities() {
		// given
		Unit unit = new Unit();
		Ability ability1 = new Ability();
		ability1.setId("uniqueskill_1");
		ability1.setName("Unique Ability");
		ability1.setZeta(true);
		Ability ability2 = new Ability();
		ability2.setId("leader_1");
		ability2.setName("Leader Ability");
		ability2.setZeta(true);
		unit.setAbilities(Lists.newArrayList(ability1, ability2));
		unit.setZetaAbilitiesId(Lists.newArrayList("uniqueskill_1", "leader_1"));
		
		// when
		Ability leaderAbilityZeta = unit.getLeaderAbilityZeta();
		
		// then
		assertNotNull(leaderAbilityZeta);
		assertEquals("Leader Ability", leaderAbilityZeta.getName());
		
		
		// when
		List<Ability> zetaAbilities = unit.getZetaAbilities();
		
		// then
		assertEquals(2, zetaAbilities.size());
		assertEquals("uniqueskill_1", zetaAbilities.get(0).getId());
	}
	
}