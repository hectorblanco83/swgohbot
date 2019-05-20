package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


/**
 * A player's unit in the game, be it a {@link Character} or a {@link hb.swgohbot.swgoh.api.Ship}
 *
 * @author Hector Blanco
 */
@Getter
@Setter
public class Unit extends BaseCharacter {
	
	// log
	private static Logger LOGGER = Logger.getLogger(Unit.class);
	
	@JsonProperty("rarity")
	private Integer rarity;
	
	@JsonProperty("level")
	private Integer level;
	
	@JsonProperty("gear_level")
	private Integer gearLevel;
	
	@JsonProperty("gear")
	private List<PlacedGear> placedGears = new ArrayList<>();
	
	@JsonProperty("ability_data")
	private List<Ability> abilities = new ArrayList<>();
	
	@JsonProperty("zeta_abilities")
	private List<String> zetaAbilities = new ArrayList<>();
	
	
	@JsonProperty("data")
	public void parseData(Map<String, Object> data) {
		setId((String) data.get("base_id"));
		setName((String) data.get("name"));
		setPower((Integer) data.get("power"));
		setGearLevel((Integer) data.get("gear_level"));
		setLevel((Integer) data.get("level"));
		setRarity((Integer) data.get("rarity"));
		setUrl((String) data.get("url"));
		setType((Integer) data.get("combat_type"));
		
		
		@SuppressWarnings("unchecked")
		List<LinkedHashMap> abilitiesData = (List<LinkedHashMap>) data.get("ability_data");
		abilitiesData.forEach(map -> abilities.add(new ObjectMapper().convertValue(map, Ability.class)));
		
		@SuppressWarnings("unchecked")
		List<String> zetaAbilities = (List<String>) data.get("zeta_abilities");
		setZetaAbilitiesId(zetaAbilities);
		
		@SuppressWarnings("unchecked")
		List<LinkedHashMap> gearsData = (List<LinkedHashMap>) data.get("gear");
		gearsData.forEach(map -> placedGears.add(new ObjectMapper().convertValue(map, PlacedGear.class)));
		
		LOGGER.debug("Finished parsing " + getName());
	}
	
	
	public Ability getLeaderAbilityZeta() {
		return getZetaAbilities().stream().filter(ability -> ability.getId().startsWith("leader")).findFirst().orElse(null);
	}
	
	
	public List<Ability> getZetaAbilities() {
		return abilities.stream().filter(ability -> zetaAbilities.contains(ability.getId())).collect(Collectors.toList());
	}
	
	
	public List<String> getZetaAbilitiesId() {
		return zetaAbilities;
	}
	
	
	public void setZetaAbilitiesId(List<String> zetaAbilities) {
		this.zetaAbilities = zetaAbilities;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Unit unit = (Unit) o;
		return Objects.equals(getId(), unit.getId());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	
}
