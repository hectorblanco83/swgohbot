package hb.swgohbot.repositories;

import hb.swgohbot.swgoh.api.Ship;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * Ship's repository
 *
 * @author Hector Blanco
 */
@Component
public interface ShipRepository {

	Flux<String> findAllNames();

	Flux<Ship> findAll();
}
