package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * A piece of {@link Gear} given to a player's {@link Unit}
 *
 * @author Hector Blanco
 */
@Data
public class PlacedGear implements Serializable {
	
	@JsonProperty("slot")
	private Integer slot;
	
	@JsonProperty("is_obtained")
	private Boolean isObtained;
	
	@JsonProperty("base_id")
	private String gearId;
	
}
