package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.extern.log4j.Log4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A player within the game.
 *
 * @author Hector Blanco
 */
@Getter
@Setter
@Log4j
@Document(collection = "players")
public class Player implements Comparable, Serializable {
	
	@Id
	@JsonProperty("ally_code")
	private Integer allyCode;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("galactic_power")
	private Integer galacticPower;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("last_updated")
	private Long lastUpdated;
	
	@JsonProperty("units")
	private List<Unit> units = new ArrayList<>();
	
	
	@JsonProperty("data")
	public void parseData(Map<String, Object> data) {
		setAllyCode((Integer) data.get("ally_code"));
		setName((String) data.get("name"));
		setUrl((String) data.get("url"));
		setGalacticPower((Integer) data.get("galactic_power"));
		
		Integer lastUpdated = (Integer) data.get("last_updated");
		Instant instant = Instant.ofEpochMilli(lastUpdated.longValue() * 1000);
		setLastUpdated(instant.toEpochMilli());
		
		LOGGER.debug("Finished parsing " + getName());
	}
	
	
	public Date getLastUpdated() {
		return new Date(lastUpdated);
	}
	
	
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated.getTime();
	}
	
	
	public List<Unit> getCharacters() {
		return units.stream().filter(unit -> unit.getType() == 1).collect(Collectors.toList());
	}
	
	
	public List<Unit> getShips() {
		return units.stream().filter(unit -> unit.getType() == 2).collect(Collectors.toList());
	}
	
	
	@Nullable
	public Unit getCharacterById(String id) {
		return units.stream().filter(unit -> unit.getId().equals(id)).findFirst().orElse(null);
	}
	
	
	@Override
	public int compareTo(@Nonnull Object o) {
		if(!(o instanceof hb.swgohbot.swgoh.api.Player)) {
			throw new ClassCastException("Object " + o.getClass().getSimpleName() + " is not a Player");
		}
		Player otherPlayer = (Player) o;
		return this.allyCode.compareTo(otherPlayer.getAllyCode());
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return allyCode.equals(player.allyCode);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(allyCode);
	}
	
	
	@Builder(builderMethodName = "builder")
	public static Player newPlayer(int allyCode, String name, @Singular List<Unit> units) {
		Player player = new Player();
		player.setAllyCode(allyCode);
		player.setName(name);
		player.setUnits(units);
		return player;
	}
	
}