package hb.swgohbot.bot.actions;

import hb.swgohbot.bot.ReplySender;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;


public interface BotReplyerAction extends BotAction {
	
	void doAction(MessageContext context, ReplySender replySender);
	
	
	@Override
	default void doAction(MessageContext context, SilentSender sender) {
		doAction(context, (ReplySender) sender);
	}
	
	default String getWaitMessage() {
		return "<pre>Wait a moment while your guild's information is being updated...</pre>";
	}
}
