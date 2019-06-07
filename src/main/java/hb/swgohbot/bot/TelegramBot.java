package hb.swgohbot.bot;

import com.google.common.collect.Lists;
import hb.swgohbot.bot.actions.CharacterSearchAction;
import hb.swgohbot.bot.actions.PlatoonSearchAction;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.telegram.abilitybots.api.objects.Flag.CALLBACK_QUERY;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;


/**
 * Telegram's Bot
 *
 * @author Hector Blanco
 */
@Log4j
@Component
// TODO: extend BaseAbilityBot instead of AbilityBot, i think its better
public class TelegramBot extends AbilityBot {
	
	// Bot creator's ID
	@Value("${telegram.bot.creatorId}")
	private String creatorId;
	
	// Reply sender that will handle almost all bot's interactions
	private ReplySender replier;
	
	
	/**
	 * Default constructor
	 */
	@Autowired
	public TelegramBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.username}") String botUsername) {
		super(botToken, botUsername);
		replier = new ReplySender(sender);
	}
	
	
	@Override
	public int creatorId() {
		return Integer.parseInt(creatorId);
	}
	
	
	void setSender(MessageSender newSender) {
		this.sender = newSender;
		this.silent = new SilentSender(sender);
		this.replier = new ReplySender(sender);
	}
	
	
	// Abilities
	
	
	/**
	 * Bot ability to search for characters in gild's players rosters
	 */
	public Ability searchCharacter() {
		return Ability
				.builder()
				.name("cs")
				.info("Search for a character within the guild.")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(1)
				.action(ctx -> new CharacterSearchAction().doAction(ctx, replier))
				.build();
	}
	
	
	public Ability searchForPlatoon() {
		return Ability
				.builder()
				.name("tb")
				.info("Search for characters for TB's platoons.")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(2)
				.action(ctx -> new PlatoonSearchAction().doAction(ctx, replier))
				.build();
	}
	
	
	public Ability startTeamConfigurator() {
		return Ability
				.builder()
				.name("team")
				.info("")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(0)
				.action(ctx -> {
					InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
					List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
					rowsInline.add(Lists.newArrayList(
							new InlineKeyboardButton().setText("Add Team").setCallbackData("team:addTeam"),
							new InlineKeyboardButton().setText("Edit Team").setCallbackData("team:editTeam")
					));
					rowsInline.add(Lists.newArrayList(
							new InlineKeyboardButton().setText("Delete Team").setCallbackData("team:deleteTeam"),
							new InlineKeyboardButton().setText("Close").setCallbackData("team:close")
					));
					markupInline.setKeyboard(rowsInline);
					
					SendMessage message = new SendMessage()
							.setText(ResponseConstants.preFormatText("Let's begin to configure guild's teams"))
							.setChatId(ctx.chatId())
							.setReplyToMessageId(ctx.update().getMessage().getMessageId())
							.setParseMode(ParseMode.HTML);
					message.setReplyMarkup(markupInline);
					silent.execute(message);
				})
				.build();
	}
	
	
	public Ability manageTeamConfigurator() {
		return Ability
				.builder()
				.name("ma")
				.locality(ALL)
				.privacy(PUBLIC)
				.action(ctx -> {
					System.out.println("managing team configurator");
				})
				.reply(
						update -> {
							silent.send("bye", update.getCallbackQuery().getMessage().getChatId());
						},
						CALLBACK_QUERY,
						update -> {
							String[] data = update.getCallbackQuery().getData().split(":");
							return data[0].equals("team");
							
						}
				)
				.build();
		
	}
	
	
	public boolean isTest(Object... args) {
		System.out.println(args);
		return false;
	}
}