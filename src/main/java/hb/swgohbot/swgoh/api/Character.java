package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;


/**
 * A Character in the game.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
@Document(collection = "characters")
public class Character extends DescriptiveCharacter {
	
	@JsonProperty("pk")
	private Integer primaryKey;
	
	@JsonProperty("gear_levels")
	private List<GearLevel> gearLevels;
	
	@JsonProperty("ship")
	private String shipId;
	
	@JsonProperty("ship_slot")
	private Integer shipSlot;
	
	@JsonProperty("activate_shard_count")
	private Integer shardsToActivate;
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		Character character = (Character) o;
		return getPrimaryKey().equals(character.getPrimaryKey());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getPrimaryKey());
	}
	
}
