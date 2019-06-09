package hb.swgohbot.repositories.rest;

import com.google.common.collect.Lists;
import hb.swgohbot.swgoh.ApiClient;
import hb.swgohbot.swgoh.api.Character;
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
class CharacterRepositoryRestTest {
	
	
	@Mock
	private ApiClient apiClient;
	
	private CharacterRepositoryRest charRepo;
	
	
	@BeforeEach
	void setUp() {
		charRepo = new CharacterRepositoryRest(apiClient);
	}
	
	
	@Test
	void findAllNames() {
		// given
		ArrayList<Character> list = Lists.newArrayList(
				Character.builder().id("1").name("Char1").build(),
				Character.builder().id("2").name("Char2").build()
		);
		when(apiClient.getCharacterList()).thenReturn(list);
		
		// when
		List<String> names = charRepo.findAllNames();
		
		// then
		assertEquals(2, names.size());
		assertEquals("Char1", names.get(0));
		assertEquals("Char2", names.get(1));
	}
}