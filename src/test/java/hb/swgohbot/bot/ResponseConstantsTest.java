package hb.swgohbot.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ResponseConstantsTest {
	
	@Test
	void preFormatText() {
		assertEquals("<pre>test</pre>", ResponseConstants.preFormatText("test"));
	}
}