package hb.swgohbot.repositories;

import hb.swgohbot.swgoh.api.Character;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Character's repository
 *
 * @author Hector Blanco
 */
@Component
public interface CharacterRepository {

	List<String> findAllNames();
	
	List<Character> findAll();

}
