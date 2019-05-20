package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import hb.swgohbot.swgoh.ApiClient;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * A full Gear Level for the {@link Character}s in the game.
 *
 * @author Hector Blanco
 */
@Data
public class GearLevel implements Serializable {
	
	@JsonProperty("tier")
	private Integer tier;
	private List<Gear> gears = new ArrayList<>();
	
	
	@JsonProperty("gear")
	void parseGears(List<String> data) {
		List<Gear> gearList = ApiClient.getInstance().getGearList();
		for(String gearId : data) {
			Optional<Gear> optionalGear = gearList.stream().filter(gear -> gear.getId().equals(gearId)).findFirst();
			optionalGear.ifPresent(gear -> gears.add(gear));
		}
	}
	
}
