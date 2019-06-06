package hb.swgohbot.bot.actions;

import hb.swgohbot.bot.ReplySender;
import hb.swgohbot.bot.ResponseConstants;
import hb.swgohbot.services.PlayerService;
import hb.swgohbot.services.SearchService;
import hb.swgohbot.setup.SpringContextProvider;
import hb.swgohbot.swgoh.api.BaseCharacter;
import hb.swgohbot.swgoh.api.Player;
import hb.swgohbot.swgoh.api.Unit;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j
public class CharacterSearchAction implements BotReplyerAction {
	
	
	@Override
	public void doAction(MessageContext context, ReplySender replyer) {
		Message updateMsg = context.update().getMessage();
		Optional<Message> optionalMsg = replyer.reply(getWaitMessage(), context.chatId(), updateMsg.getMessageId());
		if(optionalMsg.isPresent()) {
			Message waitMsg = optionalMsg.get();
			
			EditMessageText editedMessage = new EditMessageText();
			editedMessage.setMessageId(waitMsg.getMessageId());
			editedMessage.setChatId(context.chatId());
			editedMessage.setParseMode(ParseMode.HTML);
			
			SearchService service = SpringContextProvider.getContext().getBean(SearchService.class);
			List<BaseCharacter> charFounded = service.suggestCharacterFromQuery(context.arguments());
			if(charFounded.isEmpty()) {
				String text = "It appears that there's no character with name " + StringUtils.join(context.arguments(), " ");
				editedMessage.setText(ResponseConstants.preFormatText(text));
				replyer.execute(editedMessage);
				return;
			} else if(charFounded.size() > 1) {
				String text = "There's more than one character with name " + StringUtils.join(context.arguments(), " ") + "\n";
				text += "Here are some possibilities:\n\n";
				text += charFounded.stream().map(BaseCharacter::getName).collect(Collectors.joining("\n"));
				editedMessage.setText(ResponseConstants.preFormatText(text));
				replyer.execute(editedMessage);
				return;
			}
			
			// run query in another thread
			getSearcher(replyer, editedMessage, charFounded.get(0)).run();
		}
	}
	
	
	@Nonnull
	private Runnable getSearcher(ReplySender replyer, EditMessageText editedMessage, BaseCharacter character) {
		return () -> {
			ApplicationContext springContext = SpringContextProvider.getContext();
			PlayerService playerService = springContext.getBean(PlayerService.class);
			
			String unitId = character.getId();
			List<Player> players = playerService.findAllWithUnit(unitId);
			players = players.stream().sorted(getPlayerComparator()).collect(Collectors.toList());
			
			StringBuilder text = new StringBuilder();
			text.append(character.getName()).append("\n\n");
			for(Player player : players) {
				List<Unit> playerUnits = player.getUnits();
				if(CollectionUtils.isEmpty(playerUnits)) {
					text.append("| -- ");
					if(character.getType() == 1) {
						text.append("| --- ");
					}
					text.append("| ").append(player.getName()).append("\n");
				} else {
					Unit playerUnit = playerUnits.get(0);
					text.append("| ").append(playerUnit.getRarity()).append("*");
					if(character.getType() == 1) {
						text.append(" | G").append(StringUtils.rightPad(String.valueOf(playerUnit.getGearLevel()), 2));
					}
					text.append(" | ").append(player.getName()).append("\n");
				}
			}
			
			editedMessage.setText(ResponseConstants.preFormatText(text.toString()));
			editedMessage.setParseMode(ParseMode.HTML);
			replyer.execute(editedMessage);
		};
	}
	
	
	@Nonnull
	private Comparator<Player> getPlayerComparator() {
		return (o1, o2) -> {
			if(CollectionUtils.isEmpty(o1.getUnits()) && CollectionUtils.isEmpty(o2.getUnits())) {
				return 0;
			} else if(CollectionUtils.isEmpty(o1.getUnits())) {
				return -1;
			} else if(CollectionUtils.isEmpty(o2.getUnits())) {
				return 1;
			}
			
			Unit o1Char = o1.getUnits().get(0);
			Unit o2Char = o2.getUnits().get(0);
			int compare = o1Char.getRarity().compareTo(o2Char.getRarity());
			if(compare != 0) {
				return -compare;
			}
			
			compare = o1Char.getGearLevel().compareTo(o2Char.getGearLevel());
			if(compare != 0) {
				return -compare;
			}
			
			return o1.getName().compareTo(o2.getName());
		};
	}
	
}
