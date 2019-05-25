package hb.swgohbot.services;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
	
	@Mock
	private ApiClient apiClient;
	private SearchService service;
	
	
	@BeforeEach
	void setUp() {
		service = new SearchService(apiClient);
	}
	
	
	@Test
	void testSuggestCharacterFromQuery() {
		// given
		Character rjt = new Character();
		rjt.setName("Rey Jedi Training");
		Character rsc = new Character();
		rsc.setName("Rey (Scavenger)");
		when(apiClient.getCharAndShipList()).thenReturn(Lists.newArrayList(rjt,rsc));
		
		// when
		List<BaseCharacter> characters = service.suggestCharacterFromQuery("rey");
		
		// then
		assertEquals(2, characters.size());
		assertEquals("Rey (Scavenger)", characters.get(1).getName());
		verify(apiClient, times(1)).getCharAndShipList();
	}
	
	
	@Test
	void testSuggestExactlyCharacter() {
		// given
		Character vader = new Character();
		vader.setName("Darth Vader");
		when(apiClient.getCharAndShipList()).thenReturn(Lists.newArrayList(vader));
		
		// when
		List<BaseCharacter> characters = service.suggestCharacterFromQuery("Darth Vader");
		
		// then
		assertEquals(1, characters.size());
		assertEquals("Darth Vader", characters.get(0).getName());
		verify(apiClient, times(1)).getCharAndShipList();
	}
}