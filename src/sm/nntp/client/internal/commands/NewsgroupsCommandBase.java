package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.NntpConnectionError;
import sm.nntp.client.NntpServerError;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.StringUtil;

public abstract class NewsgroupsCommandBase {

	protected Iterable<NewsgroupStatus> parseNewsgroupListResponseFrom(
			NntpCommandStream cmdStream
			) throws IOException {
		List<NewsgroupStatus> newsgroups = new ArrayList<NewsgroupStatus>();
		
		for(String line: cmdStream.readResponseTextContentAsLines()) {
			newsgroups.add(parseNewsgroupStatusLine(line));
		}
		
		return newsgroups;
	}

	private NewsgroupStatus parseNewsgroupStatusLine(String line) throws NntpServerError {
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
