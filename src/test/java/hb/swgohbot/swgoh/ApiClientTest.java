package hb.swgohbot.swgoh;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApiClientTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	
	private ApiClient apiClient;
	
	
	@BeforeEach
	void setUp() {
		apiClient = new ApiClient(restTemplate);
	}
	
	
	@Test
	void testGetGuild() {
		
		// given
		Guild g = new Guild();
		g.setId(1234);
		when(restTemplate.exchange(eq("null/guild/null/"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Guild.class))).thenReturn(new ResponseEntity<>(g, HttpStatus.OK));
		
		// when
		Guild myGuild = apiClient.getMyGuild();
		
		// then
		assertEquals(1234, myGuild.getId().intValue());
		verify(restTemplate, times(1)).exchange(eq("null/guild/null/"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Guild.class));
	}
	
	
	@Test
	void testGetPlayer() {
		// given
		Player p = new Player();
		p.setAllyCode(1234);
		p.setName("Player");
		when(restTemplate.exchange(eq("null/player/1234"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Player.class))).thenReturn(new ResponseEntity<>(p, HttpStatus.OK));
		
		// when
		Player player = apiClient.getPlayer("1234");
		
		// then
		assertEquals("Player", player.getName());
		verify(restTemplate, times(1)).exchange(eq("null/player/1234"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Player.class));
	}
	
	
	@Test
	void testGetCharacterList() {
		// given
		Character c = new Character();
		c.setId("1234");
		c.setName("Unit");
		Character c2 = new Character();
		c2.setId("12345");
		c2.setName("Unit2");
		when(restTemplate.exchange(eq("null/characters/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Character>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(c, c2), HttpStatus.OK));
		
		// when
		List<Character> units = apiClient.getCharacterList();
		
		// then
		assertEquals(2, units.size());
		assertEquals("Unit2", units.get(1).getName());
		verify(restTemplate, times(1)).exchange(eq("null/characters/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Character>>>any());
	}
	
	
	@Test
	void testGetCharacter() {
		// given
		Character c = new Character();
		c.setId("1234");
		c.setName("Unit");
		when(restTemplate.exchange(eq("null/characters/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Character>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(c), HttpStatus.OK));
		
		// when
		Character character = apiClient.getCharacter("1234");
		
		// then
		assertNotNull(character);
		assertEquals("Unit", character.getName());
	}
	
	
	@Test
	void testGetCharacterNotFound() {
		// given
		Character c = new Character();
		c.setId("1234");
		c.setName("Unit");
		when(restTemplate.exchange(eq("null/characters/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Character>>>any())).thenReturn(new ResponseEntity<>(Lists.newArrayList(c), HttpStatus.OK));
		
		// when
		Character character = apiClient.getCharacter("12345");
		
		// then
		assertNull(character);
	}
	
	
	@Test
	void testGetShipList() {
		// given
		Ship s = new Ship();
		s.setId("1234");
		s.setName("Unit");
		Ship s2 = new Ship();
		s2.setId("1234");
		s2.setName("Unit2");
		when(restTemplate.exchange(eq("null/ships/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Ship>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(s, s2), HttpStatus.OK));
		
		// when
		List<Ship> units = apiClient.getShipList();
		
		// then
		assertEquals(2, units.size());
		assertEquals("Unit2", units.get(1).getName());
		verify(restTemplate, times(1)).exchange(eq("null/ships/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Ship>>>any());
	}
	
	
	@Test
	void testGetShip() {
		// given
		Ship s = new Ship();
		s.setId("1234");
		s.setName("Unit");
		when(restTemplate.exchange(eq("null/ships/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Ship>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(s), HttpStatus.OK));
		
		// when
		Ship ship = apiClient.getShip("1234");
		
		// then
		assertNotNull(ship);
		assertEquals("Unit", ship.getName());
	}
	
	
	@Test
	void testGetShipNotFound() {
		// given
		Ship s = new Ship();
		s.setId("1234");
		s.setName("Unit");
		when(restTemplate.exchange(eq("null/ships/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Ship>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(s), HttpStatus.OK));
		
		// when
		Ship unit = apiClient.getShip("12345");
		
		// then
		assertNull(unit);
	}
	
	
	@Test
	void testGetGearList() {
		// given
		Gear g = new Gear();
		g.setId("1234");
		g.setName("Gear");
		Gear g2 = new Gear();
		g2.setId("1234");
		g2.setName("Gear2");
		when(restTemplate.exchange(eq("null/gear/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Gear>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(g, g2), HttpStatus.OK));
		
		// when
		List<Gear> gears = apiClient.getGearList();
		
		// then
		assertEquals(2, gears.size());
		assertEquals("Gear2", gears.get(1).getName());
		verify(restTemplate, times(1)).exchange(eq("null/gear/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Gear>>>any());
	}
	
	
	@Test
	void testGetGear() {
		// given
		Gear g = new Gear();
		g.setId("1234");
		g.setName("Gear");
		when(restTemplate.exchange(eq("null/gear/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Gear>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(g), HttpStatus.OK));
		
		// when
		Gear gear = apiClient.getGear("1234");
		
		// then
		
		
		assertNotNull(gear);
		assertEquals("Gear", gear.getName());
	}
	
	
	@Test
	void testGetGearNotFound() {
		// given
		Gear g = new Gear();
		g.setId("1234");
		g.setName("Gear");
		when(restTemplate.exchange(eq("null/gear/"), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<Gear>>>any()))
				.thenReturn(new ResponseEntity<>(Lists.newArrayList(g), HttpStatus.OK));
		
		// when
		Gear gear = apiClient.getGear("12345");
		
		// then
		assertNull(gear);
	}
	
}