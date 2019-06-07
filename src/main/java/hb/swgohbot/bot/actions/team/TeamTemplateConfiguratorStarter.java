package hb.swgohbot.bot.actions.team;

import com.google.common.collect.Lists;
import hb.swgohbot.bot.ResponseConstants;
import hb.swgohbot.bot.TelegramBot;
import hb.swgohbot.setup.SpringContextProvider;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class TeamTemplateConfiguratorStarter implements Consumer<MessageContext> {
	
	// the bot where this action will be exceuted
	private TelegramBot thisBot;
	
	public TeamTemplateConfiguratorStarter() {
		thisBot = SpringContextProvider.getContext().getBean(TelegramBot.class);
	}
	
	
	@Override
	public void accept(MessageContext ctx) {
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
		thisBot.replier().execute(message);
	}
	
	
}
