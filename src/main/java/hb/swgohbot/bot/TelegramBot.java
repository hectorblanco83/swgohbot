package hb.swgohbot.bot;

import hb.swgohbot.bot.actions.CharacterSearchAction;
import hb.swgohbot.bot.actions.PlatoonSearchAction;
import hb.swgohbot.bot.actions.team.TeamTemplateConfigurator;
import hb.swgohbot.bot.actions.team.TeamTemplateConfiguratorStarter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;

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
	
	
	public Ability teamTemplateConfigurator() {
		return Ability
				.builder()
				.name("team")
				.info("")
				.locality(ALL)
				.privacy(PUBLIC)
				.input(0)
				.action(new TeamTemplateConfiguratorStarter())
				.reply(
						new TeamTemplateConfigurator(),
						CALLBACK_QUERY, new TeamTemplateConfigurator()
				)
				.build();
	}
	
	
//	public Ability manageTeamConfigurator() {
//		return Ability
//				.builder()
//				.name("ma")
//				.locality(ALL)
//				.privacy(PUBLIC)
//				.action(ctx -> {
//					System.out.println("managing team configurator");
//				})
//				.reply(
//						update -> {
//							silent.send("bye", update.getCallbackQuery().getMessage().getChatId());
//						},
//						CALLBACK_QUERY,
//						update -> {
//							String[] data = update.getCallbackQuery().getData().split(":");
//							return data[0].equals("team");
//
//						}
//				)
//				.build();
//
//	}
	
	
	
	
	
}