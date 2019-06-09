package hb.swgohbot.bot.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import hb.swgohbot.bot.ReplySender;
import hb.swgohbot.bot.TelegramBot;
import hb.swgohbot.services.PlayerService;
import hb.swgohbot.services.SearchService;
import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.api.Character;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Ship;
import hb.swgohbot.swgoh.api.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapdb.DBMaker;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.db.MapDBContext;
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
	private static final String D_VADER = "Darth Vader";
	private static final String VADER = "vader";
	
	@Mock
	ReplySender replier;

	@Mock
	ApplicationContext springContext;
	
	@Mock
	SearchService searchService;

	@Mock
	PlayerService playerService;

	
	
	private MessageContext prepareMessageContext() throws java.io.IOException {
		// we need messageId
		Update update = new ObjectMapper().readValue("{\"message\": {\"message_id\":\"" + MSG_ID + "\"}}", Update.class);
		User endUser = new User(USER_ID, "John", false, "Doe", "jDoe1234", Locale.ENGLISH.getLanguage());
		return MessageContext.newContext(update, endUser, CHAT_ID, VADER);
	}
	
	
	@BeforeEach
	void setUp() {
		TelegramBot bot = spy(new TelegramBot("1", "1", new MapDBContext(DBMaker.memoryDB().make())));
		when(springContext.getBean(TelegramBot.class)).thenReturn(bot);
		when(bot.replier()).thenReturn(replier);
		when(replier.reply(anyString(), eq(CHAT_ID), eq(MSG_ID))).thenReturn(Optional.of(new Message()));
		
		when(springContext.getBean(SearchService.class)).thenReturn(searchService);
		new SpringContextProvider().setApplicationContext(springContext);
	}
	
	
	@Test
	@DisplayName("Test action when search characters found nothing")
	void testCharNameNotFound() throws Exception {
		String expectedMsg = "<pre>It appears that there's no character with name vader</pre>";

		// given
		MessageContext context = prepareMessageContext();
		when(searchService.suggestCharacterFromQuery(eq(VADER))).thenReturn(Lists.newArrayList());

		// when
		new CharacterSearchAction().accept(context);

		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		Assertions.assertEquals(expectedMsg, argument.getValue().getText());
	}
	
	
	@Test
	@DisplayName("Test suggest characters when search find multiple possibilities")
	void testFoundMultipleCharacters() throws Exception {
		String expectedMsg = "<pre>There's more than one character with name vader\n" +
				"Here are some possibilities:\n" +
				"\n" +
				"Darth Vader\n" +
				"Darth Vader 2</pre>";
		
		// given
		// we need messageId
		MessageContext context = prepareMessageContext();
		Character vader1 = Character.builder().id("1").name(D_VADER).build();
		Character vader2 = Character.builder().id("2").name("Darth Vader 2").build();
		when(searchService.suggestCharacterFromQuery(eq(VADER))).thenReturn(Lists.newArrayList(vader1, vader2));
		
		// when
		new CharacterSearchAction().accept(context);
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		Assertions.assertEquals(expectedMsg, argument.getValue().getText());
	}
	
	
	@Test
	@DisplayName("Test action when it find the exact character")
	void testFoundCharacter() throws Exception {
		String expectedMsg =
				"<pre>Darth Vader" +
				"\n" +
				"\n" +
				"| -- | --- | player 3\n" +
				"| 7* | G12 | player 1\n" +
				"| 6* | G10 | player 2\n" +
				"</pre>";
		
		// given
		MessageContext context = prepareMessageContext();
		
		Character vader = Character.builder().id("1").name(D_VADER).build();
		when(searchService.suggestCharacterFromQuery(eq(VADER))).thenReturn(Lists.newArrayList(vader));
		
		when(springContext.getBean(PlayerService.class)).thenReturn(playerService);
		when(playerService.findAllWithUnit(eq("1"))).thenReturn(Lists.newArrayList(
				Player.builder().name("player 1").unit(Unit.builder().id("1").name(D_VADER).rarity(7).gear(12).build()).build(),
				Player.builder().name("player 2").unit(Unit.builder().id("1").name(D_VADER).rarity(6).gear(10).build()).build(),
				Player.builder().name("player 3").build()
		));
		
		// when
		new CharacterSearchAction().accept(context);
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		Assertions.assertEquals(expectedMsg, argument.getValue().getText());
	}
	
	
	@Test
	@DisplayName("Test action when it find the exact ship")
	void testFoundShip() throws Exception {
		String expectedMsg =
				"<pre>Darth Vader" +
				"\n" +
				"\n" +
				"| -- | player 2\n" +
				"| 7* | player 1\n" +
				"</pre>";
		
		// given
		MessageContext context = prepareMessageContext();
		
		Ship vader = Ship.builder().id("1").name(D_VADER).build();
		when(searchService.suggestCharacterFromQuery(eq(VADER))).thenReturn(Lists.newArrayList(vader));
		
		when(springContext.getBean(PlayerService.class)).thenReturn(playerService);
		when(playerService.findAllWithUnit(eq("1"))).thenReturn(Lists.newArrayList(
				Player.builder().name("player 1").unit(Unit.builder().id("1").name(D_VADER).rarity(7).type(2).build()).build(),
				Player.builder().name("player 2").build()
		));
		
		// when
		new CharacterSearchAction().accept(context);
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		Assertions.assertEquals(expectedMsg, argument.getValue().getText());
	}
	
}