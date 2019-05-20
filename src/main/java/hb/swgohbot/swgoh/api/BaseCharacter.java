package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.Objects;


/**
 * Base class of characters within swgoh.gg's API.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
public class BaseCharacter implements Serializable {
	
	@JsonProperty("base_id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("power")
	private Integer power;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("combat_type")
	private Integer type;
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BaseCharacter that = (BaseCharacter) o;
		return getId().equals(that.getId());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	
}