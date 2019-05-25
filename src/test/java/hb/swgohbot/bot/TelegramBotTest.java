package hb.swgohbot.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class TelegramBotTest {
	
	private static final int USER_ID = 1234;
	private static final long CHAT_ID = 1234L;
	
	private TelegramBot bot;
	private MessageSender sender;
	
	
	@BeforeEach
	void setUp() {
		bot = new TelegramBot("1", "1");
		sender = mock(MessageSender.class);
		bot.setSender(sender);
	}
	
	
	@Test
	void testSearchCharacterAbility() throws Exception {
		// given
		Update upd = new Update();
		User endUser = new User(USER_ID, "John", false, "Doe", "jDoe1234", Locale.ENGLISH.getLanguage());
		MessageContext context = MessageContext.newContext(upd, endUser, CHAT_ID);
		
		// when
		bot.searchCharacter().action().accept(context);
		
		// Then
		Mockito.verify(sender, times(1)).execute(any());
	}
	
}