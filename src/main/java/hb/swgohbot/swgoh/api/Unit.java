package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.stream.Collectors;


/**
 * A player's unit in the game, be it a {@link Character} or a {@link hb.swgohbot.swgoh.api.Ship}
 *
 * @author Hector Blanco
 */
@Getter
@Setter
@Log4j
public class Unit extends BaseCharacter {
	
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
		List<String> dataZetaAbilities = (List<String>) data.get("zeta_abilities");
		setZetaAbilitiesId(dataZetaAbilities);
		
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
	
	
	@Builder(builderMethodName = "builder")
	public static Unit newUnit(String id, String name, int rarity, int gear, int type) {
		Unit unit = new Unit();
		unit.setId(id);
		unit.setName(name);
		unit.setRarity(rarity);
		unit.setGearLevel(gear);
		unit.setType(type);
		return unit;
	}
	
}