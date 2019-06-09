package hb.swgohbot.bot.actions;

import hb.swgohbot.bot.ReplySender;
import hb.swgohbot.bot.ResponseUtils;
import hb.swgohbot.bot.TelegramBot;
import hb.swgohbot.services.SearchService;
import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.Player;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static hb.swgohbot.bot.ResponseUtils.getWaitMessage;


@Log4j
public class PlatoonSearchAction implements Consumer<MessageContext> {
	
	
	@Override
	public void accept(MessageContext context) {
		TelegramBot bot = SpringContextProvider.getContext().getBean(TelegramBot.class);
		Message updateMsg = context.update().getMessage();
		Optional<Message> optionalMsg = bot.replier().reply(getWaitMessage(), context.chatId(), updateMsg.getMessageId());
		if(optionalMsg.isPresent()) {
			Message waitMsg = optionalMsg.get();
			
			EditMessageText editedMessage = new EditMessageText();
			editedMessage.setMessageId(waitMsg.getMessageId());
			editedMessage.setChatId(context.chatId());
			editedMessage.setParseMode(ParseMode.HTML);
			
			String[] arguments = context.arguments();
			
			String rarity = "1";
			for(int i = 0; i < arguments.length; i++) {
				if(arguments[i].endsWith("*")) {
					rarity = arguments[i].replace("*", "");
					arguments = ArrayUtils.remove(arguments, i);
				}
			}
			String[] charQuery = arguments;
			Integer rarityQuery = Integer.valueOf(rarity);
			
			SearchService service = SpringContextProvider.getContext().getBean(SearchService.class);
			List<BaseCharacter> charFounded = service.suggestCharacterFromQuery(charQuery);
			if(charFounded.isEmpty()) {
				String text = "It appears that there's no character with name " + StringUtils.join(charQuery, " ");
				editedMessage.setText(ResponseUtils.preFormatText(text));
				bot.replier().execute(editedMessage);
				return;
			}
			
			// run query in another thread
			getSearcher(bot.replier(), editedMessage, charFounded, charQuery, rarityQuery).run();
		}
	}
	
	
	@Nonnull
	private Runnable getSearcher(ReplySender replier, EditMessageText editedMessage, List<BaseCharacter> charFounded,
								 String[] charQuery, Integer rarityQuery) {
		return () -> {
			ApplicationContext springContext = SpringContextProvider.getContext();
			SearchService searchService = springContext.getBean(SearchService.class);
			Map<String, List<Player>> queryResults = searchService.findPlayersWithChar(charFounded, rarityQuery);
			if(queryResults.isEmpty()) {
				String response = "No player has character " + StringUtils.join(charQuery, " ") + " at " + rarityQuery + "*";
				editedMessage.setText(ResponseUtils.preFormatText(response));
			} else {
				// 30 chars per row
				StringBuilder sb = new StringBuilder();
				queryResults.forEach((charName, players) -> {
					// 2 chars
					sb.append("| ");
					
					// 2 chars maximum
					sb.append(StringUtils.leftPad(String.valueOf(players.size()), 2, "0"));
					
					// 3 chars
					sb.append(" | ");
					
					// 2 chars
					sb.append(rarityQuery).append("* ").append(charName).append("\n");
				});
				editedMessage.setText(ResponseUtils.preFormatText(sb.toString()));
			}
			replier.execute(editedMessage);
		};
	}
}