package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.ArticleId;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class NextCommand {

	public ArticleId executeOn(NntpCommandStream cmdStream) throws IOException {
		
		cmdStream.writeCommand("next");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		
		// return null if we got "421 No next to retrieve"
		if (header.getStatusCode() == 421)
			return null;
		
		// check that we got something valuable out of it
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		return CommandResponseHelpers.parseArticleIdFromHeaderStatusString(header.getStatusString());
	}

}
