package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.ArticleId;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class StatCommand {

	public ArticleId executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("stat");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		
		// return null if no articles are there on this group
		if (header.getStatusCode() == 423)
			return null;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		return CommandResponseHelpers.parseArticleIdFromHeaderStatusString(header.getStatusString());
	}


}
