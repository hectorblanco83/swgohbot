package hb.swgohbot.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ResponseUtilsTest {
	
	@Test
	@DisplayName("Test format of messages sent by this bot")
	void testPreFormatText() {
		assertEquals("<pre>test</pre>", ResponseUtils.preFormatText("test"));
	}
	
	
	@Test
	@DisplayName("Test wait message")
	void testWaitMessage() {
		String expected = "<pre>Wait a moment while your guild's information is being updated...</pre>";
		assertEquals(expected, ResponseUtils.getWaitMessage());
	}
}