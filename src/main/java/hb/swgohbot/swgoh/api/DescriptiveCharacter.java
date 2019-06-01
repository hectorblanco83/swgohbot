package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


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
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		DescriptiveCharacter that = (DescriptiveCharacter) o;
		return getId().equals(that.getId());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
