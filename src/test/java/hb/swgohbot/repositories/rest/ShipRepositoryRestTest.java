package hb.swgohbot.repositories.rest;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ShipRepositoryRestTest {
	
	
	@Mock
	private ApiClient apiClient;
	
	private ShipRepositoryRest shipRepo;
	
	
	@BeforeEach
	void setUp() {
		shipRepo = new ShipRepositoryRest(apiClient);
	}
	
	
	@Test
	void findAllNames() {
		// given
		ArrayList<Ship> list = Lists.newArrayList(
				Ship.builder().id("1").name("Ship1").build(),
				Ship.builder().id("2").name("Ship2").build()
		);
		when(apiClient.getShipList()).thenReturn(list);
		
		// when
		List<String> names = shipRepo.findAllNames();
		
		// then
		assertEquals(2, names.size());
		assertEquals("Ship1", names.get(0));
		assertEquals("Ship2", names.get(1));
	}
}