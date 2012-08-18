package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sm.nntp.client.ArticleId;
import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.NntpServerError;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.StringUtil;

public class CommandResponseHelpers {

	public static ArticleId parseArticleIdFromHeaderStatusString(String statusString) throws NntpServerError {
		List<String> parts = StringUtil.splitLineIntoWords(statusString);
		if (parts.size() < 2)
			throw new NntpServerError("invalid response from server: " + statusString);
		
		@SuppressWarnings("unused")
		int articleNum;
		
		try {
			articleNum = Integer.parseInt(parts.get(0));
			return ArticleId.parseFromStringOrException(parts.get(1));
		}
		catch(Exception ex)
		{
			throw new NntpServerError("invalid response from server: " + statusString);

		}
	}
	
	protected static Iterable<NewsgroupStatus> parseNewsgroupListResponseFrom(
			NntpCommandStream cmdStream
			) throws IOException {
		List<NewsgroupStatus> newsgroups = new ArrayList<NewsgroupStatus>();
		
		for(String line: cmdStream.readResponseTextContentAsLines()) {
			newsgroups.add(parseNewsgroupStatusLine(line));
		}
		
		return newsgroups;
	}

	private static NewsgroupStatus parseNewsgroupStatusLine(String line) throws NntpServerError {
		List<String> parts = StringUtil.splitLineIntoWords(line);
		if (parts.size() != 4)
			throw new NntpServerError("invalid nntp group list response");
		
		String publishingIsAllowedStr = parts.get(3).toLowerCase();
		if (!publishingIsAllowedStr.equals("y") && !publishingIsAllowedStr.equals("n"))
			throw new NntpServerError("invalid nntp group list response");
		
		boolean publishingIsAllowed = publishingIsAllowedStr.equals("y");
		
		int highArticleNum;
		int lowArticleNum;
		
		try {
			highArticleNum = Integer.parseInt(parts.get(1));
			lowArticleNum = Integer.parseInt(parts.get(2));
		}
		catch(NumberFormatException ex)
		{
			throw new NntpServerError("invalid nntp group list response");
		}
		
		if (lowArticleNum < 0 || highArticleNum < lowArticleNum)
		{
			lowArticleNum = -1;
			highArticleNum = -1;
		}
		
		return new NewsgroupStatus(parts.get(0), lowArticleNum, highArticleNum, publishingIsAllowed);
	}

}
