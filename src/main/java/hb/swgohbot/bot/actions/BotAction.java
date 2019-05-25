package hb.swgohbot.bot.actions;

import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;


public interface BotAction {
	
	void doAction(MessageContext context, SilentSender sender);
}
