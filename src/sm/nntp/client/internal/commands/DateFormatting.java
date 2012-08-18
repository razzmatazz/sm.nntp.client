package sm.nntp.client.internal.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatting {
	
	private static SimpleDateFormat nntpCommandDateFormat = new SimpleDateFormat("yyyyMMdd hhmmss");

	public static String formatDateForNntpCommand(Date since) {
		return nntpCommandDateFormat.format(since);
	}

}
