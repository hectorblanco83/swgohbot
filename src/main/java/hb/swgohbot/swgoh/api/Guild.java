package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Map;


/**
 * A guild of players within the game.
 *
 * @author Hector Blanco
 */
@Data
@Log4j
public class Guild {
	
	private Integer id;
	private String name;
	private Integer rank;
	private Integer memberCount;
	private Integer galaticPower;
	private Integer profileCount;
	
	@JsonProperty("players")
	private List<Player> players;
	
	
	@JsonProperty("data")
	public void parseData(Map<String, Object> data) {
		setId((Integer) data.get("id"));
		setName((String) data.get("name"));
		setMemberCount((Integer) data.get("member_count"));
		setGalaticPower((Integer) data.get("galactic_power"));
		setRank((Integer) data.get("rank"));
		setProfileCount((Integer) data.get("profile_count"));
		LOGGER.debug("Finished parsing " + getName());
	}
	
}
