package hb.swgohbot.swgoh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.ApiClient;
import lombok.Data;
import lombok.extern.log4j.Log4j;

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
@Log4j
public class GearLevel implements Serializable {
	
	@JsonProperty("tier")
	private Integer tier;
	private List<Gear> gears = new ArrayList<>();
	
	
	
	@JsonProperty("gear")
	void parseGears(List<String> data) {
		ApiClient apiClient = SpringContextProvider.getContext().getBean(ApiClient.class);
		List<Gear> gearList = apiClient.getGearList();
		LOGGER.debug("Gear List from SwGoh.api: " + gearList);
		
		for(String gearId : data) {
			Optional<Gear> optionalGear = gearList.stream().filter(gear -> gear.getId().equals(gearId)).findFirst();
			optionalGear.ifPresent(gear -> gears.add(gear));
			LOGGER.debug("Gear founded in allGearList: " + optionalGear.orElse(null));
		}
	}
	
}
