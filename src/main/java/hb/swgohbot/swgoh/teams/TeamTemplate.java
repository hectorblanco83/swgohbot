package hb.swgohbot.swgoh.teams;

import hb.swgohbot.swgoh.api.Unit;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;


/**
 * A configurable team template that will be used to collect teams from player's rosters.
 *
 * @author Hector Blanco
 */
@Data
public class TeamTemplate {
	
	private String name;
	private List<Unit> units = new ArrayList<>();
	
	
	@Builder(builderMethodName = "builder")
	public static TeamTemplate newTemplate(String name, @Singular List<Unit> units) {
		TeamTemplate template = new TeamTemplate();
		template.setName(name);
		template.setUnits(units);
		return template;
	}
	
}
