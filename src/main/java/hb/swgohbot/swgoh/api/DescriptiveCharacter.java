package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.List;


/**
 * A {@link Character} with additional descriptive properties
 *
 * @author Hector Blanco
 */
@Getter
@Setter
public class DescriptiveCharacter extends BaseCharacter {
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("image")
	private String image;
	
	@JsonProperty("alignment")
	private String alignment;
	
	@JsonProperty("role")
	private String role;
	
	@JsonProperty("categories")
	private List<String> categories;
	
	@JsonProperty("ability_classes")
	private List<String> abilityClasses;
	
}
