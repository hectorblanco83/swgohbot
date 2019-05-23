package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * A unit's ability.
 *
 * @author Hector Blanco
 */
@Data
public class Ability implements Serializable {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("ability_tier")
	private Integer tier;
	
	@JsonProperty("tier_max")
	private Integer maxTier;
	
	@JsonProperty("is_omega")
	private Boolean omega;
	
	@JsonProperty("is_zeta")
	private Boolean zeta;
	
}
