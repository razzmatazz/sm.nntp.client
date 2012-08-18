package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.Article;
import sm.nntp.client.ArticleId;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class BodyCommand {

	public Article executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("body");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		if (header.getStatusCode() == 423)
			return null;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		ArticleId articleId = CommandResponseHelpers.parseArticleIdFromHeaderStatusString(header.getStatusString());
		return Article.WithBodyOnly(articleId, cmdStream.readResponseTextContentAsLines());
	}

}
