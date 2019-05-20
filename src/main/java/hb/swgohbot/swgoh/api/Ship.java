package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * A ship character in the game.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
public class Ship extends DescriptiveCharacter {
	
	@JsonProperty("capital_ship")
	private Boolean capitalShip;
	
}
