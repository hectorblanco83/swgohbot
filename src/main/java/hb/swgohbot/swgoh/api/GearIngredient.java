package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * Ingredient to craft a piece {@link Gear}
 *
 * @author Hector Blanco
 */
@Data
public class GearIngredient implements Serializable {
	
	@JsonProperty("gear")
	private String gearId;
	
	@JsonProperty("amount")
	private int amount = 0;
	
}