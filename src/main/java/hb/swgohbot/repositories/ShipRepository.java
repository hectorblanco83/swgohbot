package hb.swgohbot.repositories;

import hb.swgohbot.swgoh.api.Ship;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Ship's repository
 *
 * @author Hector Blanco
 */
@Component
public interface ShipRepository {

	List<String> findAllNames();

	List<Ship> findAll();
}
