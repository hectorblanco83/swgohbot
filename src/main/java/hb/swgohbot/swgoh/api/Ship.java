package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


/**
 * A ship character in the game.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
@Document(collection = "ships")
public class Ship extends DescriptiveCharacter {
	
	@JsonProperty("capital_ship")
	private Boolean capitalShip;
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Ship that = (Ship) o;
		return getId().equals(that.getId());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	
}
