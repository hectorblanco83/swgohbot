package hb.swgohbot.swgoh.api;

import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GearLevelTest {
	
	
	@Mock
	ApiClient apiClient;
	
	@Mock
	ApplicationContext springContext;
	
	
	@BeforeEach
	void setUp() {
		when(springContext.getBean(ApiClient.class)).thenReturn(apiClient);
		new SpringContextProvider().setApplicationContext(springContext);
	}
	
	
	@Test
	void testParseData() {
		// given swgoh's all gear list
		ArrayList<Gear> allSwgohGears = new ArrayList<>();
		allSwgohGears.add(Gear.builder().id("1").build());
		allSwgohGears.add(Gear.builder().id("2").build());
		allSwgohGears.add(Gear.builder().id("3").build());
		when(apiClient.getGearList()).thenReturn(allSwgohGears);
		
		// given those gears during GearLevel import
		ArrayList<String> data = new ArrayList<>();
		data.add("2");
		
		// given converting this GearLevel
		GearLevel gearLevel = new GearLevel();
		gearLevel.setTier(1);
		
		// when
		gearLevel.parseGears(data);
		
		// then
		assert gearLevel.getGears().size() == 1;
		assert "2".equals(gearLevel.getGears().get(0).getId());
		verify(apiClient, times(1)).getGearList();
	}
	
}