package hb.swgohbot.bot.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import hb.swgohbot.bot.ReplySender;
import hb.swgohbot.services.SearchService;
import hb.swgohbot.setup.SpringContextProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CharacterSearchActionTest {
	
	private static final int USER_ID = 123;
	private static final int MSG_ID = 1234;
	private static final long CHAT_ID = 12345L;
	
	@Mock
	ApplicationContext springContext;
	
	@Mock
	SearchService searchService;
	
	@Mock
	ReplySender replier;
	
	
	@BeforeEach
	void setUp() {
		when(springContext.getBean(SearchService.class)).thenReturn(searchService);
		new SpringContextProvider().setApplicationContext(springContext);
	}
	
	
	@Test
	void testCharnameNotFound() throws Exception {
		
		// given
		// we need messageId
		Update update = new ObjectMapper().readValue("{\"message\": {\"message_id\":\""+MSG_ID+"\"}}", Update.class);
		User endUser = new User(USER_ID, "John", false, "Doe", "jDoe1234", Locale.ENGLISH.getLanguage());
		MessageContext context = MessageContext.newContext(update, endUser, CHAT_ID, "vader");
		String expectedMsg = "<pre>It appears that there's no character with name vader</pre>";
		
		when(searchService.suggestCharacterFromQuery(eq("vader"))).thenReturn(Lists.newArrayList());
		when(replier.reply(anyString(), eq(CHAT_ID), eq(MSG_ID))).thenReturn(Optional.of(new Message()));
	
		// when
		new CharacterSearchAction().doAction(context, replier);
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		Assertions.assertEquals(expectedMsg, argument.getValue().getText());
	}
	
}