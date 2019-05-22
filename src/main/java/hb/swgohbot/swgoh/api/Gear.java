package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * A piece of gear for {@link Character}s in the game.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
public class Gear implements Serializable {
	
	@JsonProperty("base_id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("recipes")
	private List<Object> parts;
	
	@JsonProperty("tier")
	private Integer tier;
	
	@JsonProperty("required_level")
	private Integer requiredLevel;
	
	@JsonProperty("stats")
	private Object stats;
	
	@JsonProperty("mark")
	private String mark;
	
	@JsonProperty("cost")
	private Integer cost;
	
	@JsonProperty("image")
	private String urlImage;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("ingredients")
	private List<GearIngredient> ingredients;
	
	
	public Gear() {
	}
	
	
	public Gear(String id) {
		this.id = id;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Gear gear = (Gear) o;
		return getId().equals(gear.getId());
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	
	
	@Override
	public String toString() {
		return "Gear{ id='" + id + "', name=" + name + ", tier=" + tier + "}";
	}
}
