package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.List;

import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.NntpServerError;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;
import sm.nntp.client.internal.StringUtil;

public class SelectNewsgroupCommand {

	private String name;

	public SelectNewsgroupCommand(String name) {
		this.name = StringUtil.validateNewsgroupNameOrException(name);
	}

	public NewsgroupStatus executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("group " + name);
		
		ResponseHeader header = cmdStream.readResponseHeaderOrExceptionOnError();
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		return parseNewsgroupStatusFromHeader(header.getStatusString());
	}

	private NewsgroupStatus parseNewsgroupStatusFromHeader(String statusString) throws NntpServerError {
		List<String> parts = StringUtil.splitLineIntoWords(statusString);
		if (parts.size() != 4)
			throw new NntpServerError("invalid response for the \"group\" command: " + statusString);
		
		if (!parts.get(3).equals(name))
			throw new NntpServerError("invalid response for the \"group\" command, unexpected newsgroup name received: " + statusString);
		
		@SuppressWarnings("unused")
		int articleCount;
		
		int highArticleNum;
		int lowArticleNum;
		
		try {
			articleCount = Integer.parseInt(parts.get(0));
			lowArticleNum = Integer.parseInt(parts.get(1));
			highArticleNum = Integer.parseInt(parts.get(2));
		}
		catch(NumberFormatException ex)
		{
			throw new NntpServerError("invalid response for the \"group\" command: " + statusString);
		}
		
		return new NewsgroupStatus(parts.get(3), highArticleNum, lowArticleNum, true);
	}

}
