package hb.swgohbot.repositories;

import hb.swgohbot.swgoh.api.Character;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * Character's repository
 *
 * @author Hector Blanco
 */
@Component
public interface CharacterRepository {

	Flux<String> findAllNames();
	
	Flux<Character> findAll();

}
