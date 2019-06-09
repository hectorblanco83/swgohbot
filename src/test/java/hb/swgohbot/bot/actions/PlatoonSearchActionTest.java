package hb.swgohbot.bot.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import hb.swgohbot.bot.ReplySender;
import hb.swgohbot.bot.TelegramBot;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.db.MapDBContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlatoonSearchActionTest {
	
	private static final int USER_ID = 123;
	private static final int MSG_ID = 1234;
	private static final long CHAT_ID = 12345L;
	public static final String TIE = "tie";
	public static final String TIE_FIGHTER_PILOT = "Tie Fighter Pilot";
	public static final String IMPERIAL_TIE_FIGHTER = "Imperial Tie Fighter";
	
	
	@Mock
	ReplySender replier;
	
	@Mock
	ApplicationContext springContext;
	
	@Mock
	SearchService searchService;
	
	
	private MessageContext prepareMessageContext() throws java.io.IOException {
		// we need messageId
		Update update = new ObjectMapper().readValue("{\"message\": {\"message_id\":\"" + MSG_ID + "\"}}", Update.class);
		User endUser = new User(USER_ID, "John", false, "Doe", "jDoe1234", Locale.ENGLISH.getLanguage());
		return MessageContext.newContext(update, endUser, CHAT_ID, TIE, "7*");
	}
	
	
	@BeforeEach
	void setUp() throws IOException {
		TelegramBot bot = spy(new TelegramBot("1", "1", new MapDBContext(DBMaker.memoryDB().make())));
		when(springContext.getBean(TelegramBot.class)).thenReturn(bot);
		when(bot.replier()).thenReturn(replier);
		
		Message waitMessage = new ObjectMapper().readValue("{\"message_id\":\"" + MSG_ID + "\"}", Message.class);
		when(replier.reply(anyString(), eq(CHAT_ID), eq(MSG_ID))).thenReturn(Optional.of(waitMessage));
		
		when(springContext.getBean(SearchService.class)).thenReturn(searchService);
		new SpringContextProvider().setApplicationContext(springContext);
	}
	
	
	@Test
	@DisplayName("Test action when search characters found nothing")
	void testCharNameNotFound() throws Exception {
		String expectedMsg = "<pre>It appears that there's no character with name tie</pre>";
		
		// given
		when(searchService.suggestCharacterFromQuery(eq(TIE))).thenReturn(Lists.newArrayList());
		
		// when
		new PlatoonSearchAction().accept(prepareMessageContext());
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());

		EditMessageText editMessage = argument.getValue();
		// assert wait message was edited with new text
		Assertions.assertEquals(MSG_ID, editMessage.getMessageId().intValue());
		// assert sent correct information to telegram client
		Assertions.assertEquals(expectedMsg, editMessage.getText());
	}
	
	
	@Test
	@DisplayName("Test action when only one char is founded by no player has it")
	void testNoPlayerHasCharacter() throws Exception {
		String expectedMsg = "<pre>No player has character tie at 7*</pre>";
		
		// given
		Character tiePilot = Character.builder().id("1").name(TIE_FIGHTER_PILOT).build();
		when(searchService.suggestCharacterFromQuery(eq(TIE))).thenReturn(Lists.newArrayList(tiePilot));
		
		// when
		new PlatoonSearchAction().accept(prepareMessageContext());
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		
		EditMessageText editMessage = argument.getValue();
		// assert wait message was edited with new text
		Assertions.assertEquals(MSG_ID, editMessage.getMessageId().intValue());
		// assert sent correct information to telegram client
		Assertions.assertEquals(expectedMsg, editMessage.getText());
	}

	
	@Test
	@DisplayName("Test when multiple units are found for the query received")
	void testCharacterAndUnitFounded() throws Exception {
		String expectedMsg =
				"<pre>" +
				"| 02 | 7* Imperial Tie Fighter\n" +
				"| 02 | 7* Tie Fighter Pilot\n" +
				"</pre>";
		
		// given
		Character tiePilot = Character.builder().id("1").name(TIE_FIGHTER_PILOT).build();
		Ship tieFighter = Ship.builder().id("2").name(IMPERIAL_TIE_FIGHTER).build();
		when(searchService.suggestCharacterFromQuery(eq(TIE))).thenReturn(Lists.newArrayList(tiePilot, tieFighter));
		
		Unit tiePilot7Star = Unit.builder().id("1").type(1).name(TIE_FIGHTER_PILOT).rarity(7).gear(12).build();
		Unit tieFighter7Star = Unit.builder().id("2").type(2).name(IMPERIAL_TIE_FIGHTER).rarity(7).build();
		
		Player player1 = Player.builder().allyCode(1).name("Player1").unit(tiePilot7Star).build();
		Player player2 = Player.builder().allyCode(2).name("Player2").unit(tiePilot7Star).build();
		Player player3 = Player.builder().allyCode(3).name("Player3").unit(tieFighter7Star).build();
		Player player4 = Player.builder().allyCode(1).name("Player1").unit(tieFighter7Star).build();
		
		Map<String, List<Player>> map = new HashMap<>();
		map.put(TIE_FIGHTER_PILOT, Lists.newArrayList(player1, player2));
		map.put(IMPERIAL_TIE_FIGHTER, Lists.newArrayList(player3, player4));
		when(searchService.findPlayersWithChar(ArgumentMatchers.anyList(), ArgumentMatchers.anyInt())).thenReturn(map);
		
		// when
		new PlatoonSearchAction().accept(prepareMessageContext());
		
		// then
		ArgumentCaptor<EditMessageText> argument = ArgumentCaptor.forClass(EditMessageText.class);
		verify(replier, times(1)).execute(argument.capture());
		
		EditMessageText editMessage = argument.getValue();
		// assert wait message was edited with new text
		Assertions.assertEquals(MSG_ID, editMessage.getMessageId().intValue());
		// assert sent correct information to telegram client
		Assertions.assertEquals(expectedMsg, editMessage.getText());
	}
	
}