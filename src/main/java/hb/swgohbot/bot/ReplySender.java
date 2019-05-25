package hb.swgohbot.bot;

import lombok.extern.log4j.Log4j;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;


/**
 * A sender that reply to user's messages.
 *
 * @author Hector Blanco
 */
@Log4j
public class ReplySender extends SilentSender {
	
	
	/**
	 * Default constructor
	 *
	 * @param sender The {@link MessageSender} used by this sender
	 */
	public ReplySender(MessageSender sender) {
		super(sender);
	}
	
	
	/**
	 * Reply messages
	 *
	 * @param message the content of the reply
	 * @param chatId  the id of the chat which to send the reply
	 * @param msgId   the id of the message which to reply
	 * @return an optional with the result of the message send's execution
	 */
	public Optional<Message> reply(String message, long chatId, int msgId) {
		return doReplyMessage(message, chatId, msgId, false);
	}
	
	
	/**
	 * Reply messages with markdown
	 *
	 * @param message the content of the reply
	 * @param chatId  the id of the chat which to send the reply
	 * @param msgId   the id of the message which to reply
	 * @return an optional with the result of the message send's execution
	 */
	public Optional<Message> replyMd(String message, long chatId, int msgId) {
		return doReplyMessage(message, chatId, msgId, true);
	}
	
	
	/**
	 * Exceute the {@link SendMessage} method
	 *
	 * @param txt      the content of the reply
	 * @param chatId   the id of the chat which to send the reply
	 * @param msgId    the id of the message which to reply
	 * @param markdown indicates if the message should be sent with Markdown enabled
	 * @return an optional with the result of the message send's execution
	 */
	private Optional<Message> doReplyMessage(String txt, long chatId, int msgId, boolean markdown) {
		SendMessage smsg = new SendMessage();
		smsg.setChatId(chatId);
		smsg.setText(txt);
		smsg.setReplyToMessageId(msgId);
		smsg.enableMarkdown(markdown);
		smsg.setParseMode(ParseMode.HTML);
		return execute(smsg);
	}
	
	
}
