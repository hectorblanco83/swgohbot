package hb.swgohbot.bot;

import hb.swgohbot.bot.actions.CharacterSearchAction;
import hb.swgohbot.bot.actions.PlatoonSearchAction;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;

import static org.telegram.abilitybots.api.db.MapDBContext.offlineInstance;
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
	private ReplySender replier;
	
	
	/**
	 * Default constructor
	 */
	@Autowired
	public TelegramBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.username}") String botUsername) {
		this(botToken, botUsername, offlineInstance(botUsername));
	}
	
	
	public TelegramBot(String botToken, String botUsername, DBContext db) {
		super(botToken, botUsername, db);
		replier = new ReplySender(sender);
	}
	
	
	@Override
	public int creatorId() {
		return Integer.parseInt(creatorId);
	}
	
	
	public void setSender(MessageSender newSender) {
		this.sender = newSender;
		this.silent = new SilentSender(sender);
		this.replier = new ReplySender(sender);
	}
	
	
	public ReplySender replier() {
		return replier;
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
				.action(new CharacterSearchAction())
				.build();
	}
	
	
	public Ability tbPlatoon() {
		return Ability
				.builder()
				.name("tb")
				.info("Search for characters for TB's platoons.")
				.locality(ALL)
				.privacy(PUBLIC)
				.action(new PlatoonSearchAction())
				.build();
	}
	
}