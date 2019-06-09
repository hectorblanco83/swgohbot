package hb.swgohbot.bot;

public class ResponseUtils {
	
	private static final String TAG_PRE = "<pre>";
	private static final String TAG_CLOSE_PRE = "</pre>";
	
	
	private ResponseUtils() {
	}
	
	
	public static String getWaitMessage() {
		return preFormatText("Wait a moment while your guild's information is being updated...");
	}
	
	public static String preFormatText(String text) {
		return TAG_PRE + text + TAG_CLOSE_PRE;
	}
}
