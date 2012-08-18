package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sm.nntp.client.Article;
import sm.nntp.client.ArticleId;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class ArticleCommand {

	public Article executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("article");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		
		// return null if we are trying to receive article from the cursor and we get
		// "423 Bad article number"
		if (header.getStatusCode() == 423)
			return null;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		ArticleId articleId = CommandResponseHelpers.parseArticleIdFromHeaderStatusString(header.getStatusString());
		
		List<String> headerLines = new ArrayList<String>();
		ArrayList<String> bodyLines = new ArrayList<String>();
		
		// TODO: need to do refactor readResponseTextContentAsLines not to buffer the response in memory
		// TODO: and we shouldn't probably buffer it here as well?!
		boolean inBody = false;
		
		for(String line : cmdStream.readResponseTextContentAsLines())
		{
			if (line.equals("")) {
				inBody = true;
			} else {
				if (!inBody)
					headerLines.add(line);
				else
					bodyLines.add(line);
			}
		}
		
		return Article.WithHeaderAndBody(articleId, headerLines, bodyLines);
	}

}
