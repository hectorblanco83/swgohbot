package hb.swgohbot.swgoh.api;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GuildTest {
	
	
	@Test
	void parseData() {
		// given
		Map<String, Object> data = new HashMap<>();
		data.put("id", 1);
		data.put("name", "guild");
		data.put("member_count", 50);
		data.put("galactic_power", 2000);
		data.put("rank", 1);
		data.put("profile_count", 49);
		
		// when
		Guild guild = new Guild();
		guild.parseData(data);
		
		// then
		assertEquals(Integer.valueOf(50), guild.getMemberCount());
		assertEquals(Integer.valueOf(1), guild.getId());
	}
	
}