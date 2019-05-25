package hb.swgohbot.bot;

public class ResponseConstants {
	
	private static final String TAG_PRE = "<pre>";
	private static final String TAG_CLOSE_PRE = "</pre>";
	
	
	private ResponseConstants() {
	}
	
	
	public static String preFormatText(String text) {
		return TAG_PRE + text + TAG_CLOSE_PRE;
	}
}
