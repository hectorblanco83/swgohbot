package hb.swgohbot.bot.actions.team;

import hb.swgohbot.bot.TelegramBot;
import hb.swgohbot.setup.SpringContextProvider;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;
import java.util.function.Predicate;


public class TeamTemplateConfigurator implements Consumer<Update>, Predicate<Update> {
	
	// the bot where this action will be exceuted
	private TelegramBot thisBot;
	
	public TeamTemplateConfigurator() {
		thisBot = SpringContextProvider.getContext().getBean(TelegramBot.class);
	}
	
	
	@Override
	public void accept(Update upd) {
//		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//		rowsInline.add(Lists.newArrayList(
//				new InlineKeyboardButton().setText("Add Team").setCallbackData("team:addTeam"),
//				new InlineKeyboardButton().setText("Edit Team").setCallbackData("team:editTeam")
//		));
//		rowsInline.add(Lists.newArrayList(
//				new InlineKeyboardButton().setText("Delete Team").setCallbackData("team:deleteTeam"),
//				new InlineKeyboardButton().setText("Close").setCallbackData("team:close")
//		));
//		markupInline.setKeyboard(rowsInline);
//
//		SendMessage message = new SendMessage()
//				.setText(ResponseConstants.preFormatText("Let's begin to configure guild's teams"))
//				.setChatId(upd.chatId())
//				.setReplyToMessageId(upd.update().getMessage().getMessageId())
//				.setParseMode(ParseMode.HTML);
//		message.setReplyMarkup(markupInline);
//		thisBot.replier().execute(message);
	}
	
	
	@Override
	public boolean test(Update update) {
		String[] data = update.getCallbackQuery().getData().split(":");
		return data[0].equals("team");
	}
}
