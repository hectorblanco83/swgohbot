package hb.swgohbot.bot;

import hb.swgohbot.bot.actions.CharacterSearchAction;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;


/**
 * Telegram's Bot
 *
 * @author Hector Blanco
 */
@Log4j
@Component
public class TelegramBot extends AbilityBot {
	
	// Bot creator's ID
	@Value("${telegram.bot.creatorId}")
	private String creatorId;
	
	// Reply sender that will handle almost all bot's interactions
	private ReplySender replyer;
	
	
	/**
	 * Default constructor
	 */
	@Autowired
	public TelegramBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.username}") String botUsername) {
		super(botToken, botUsername);
		replyer = new ReplySender(sender);
	}
	
	
	@Override
	public int creatorId() {
		return Integer.parseInt(creatorId);
	}
	
	
	void setSender(MessageSender newSender) {
		this.sender = newSender;
		this.silent = new SilentSender(sender);
		this.replyer = new ReplySender(sender);
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
				.action(ctx -> new CharacterSearchAction().doAction(ctx, replyer))
				.build();
	}
	
}
