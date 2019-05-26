package hb.swgohbot.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import hb.swgohbot.services.SearchService;
import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {
	
	private static final int USER_ID = 1234;
	private static final long CHAT_ID = 1234L;
	
	private TelegramBot bot;
	private MessageSender sender;
	
	@Mock
	ApplicationContext springContext;
	
	@Mock
	ApiClient apiClient;
	
	@Mock
	SearchService searchService;
	
	
	@BeforeEach
	void setUp() {
		bot = new TelegramBot("1", "1");
		sender = mock(MessageSender.class);
		bot.setSender(sender);
		
		searchService = new SearchService(apiClient);
		when(springContext.getBean(SearchService.class)).thenReturn(searchService);
		new SpringContextProvider().setApplicationContext(springContext);
	}
	
	
	@Test
	void testSearchCharacterAbility() throws Exception {
		// given
		
		// we need messageId
		Update update = new ObjectMapper().readValue("{\"message\": {\"message_id\":\"1234\"}}", Update.class);
		User endUser = new User(USER_ID, "John", false, "Doe", "jDoe1234", Locale.ENGLISH.getLanguage());
		MessageContext context = MessageContext.newContext(update, endUser, CHAT_ID, "vader");
		
		when(sender.execute(any())).thenReturn(update.getMessage());
		when(apiClient.getCharAndShipList()).thenReturn(new ArrayList<>());
		
		// when
		bot.searchCharacter().action().accept(context);
		
		// Then we expect 2 messages, one for user wait until call swgoh.gg
		// and another with the actual search result
		Mockito.verify(sender, times(2)).execute(any());
	}
	
}