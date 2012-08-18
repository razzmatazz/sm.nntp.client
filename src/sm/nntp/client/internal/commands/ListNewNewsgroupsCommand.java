package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.Date;

import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class ListNewNewsgroupsCommand {
	
	private Date since;

	public ListNewNewsgroupsCommand(Date since)
	{
		this.since = since;
	}

	public Iterable<NewsgroupStatus> executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("newgroups " + DateFormatting.formatDateForNntpCommand(since));
		
		ResponseHeader responseHdr = cmdStream.readResponseHeaderOrExceptionOnError();
		responseHdr.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		return CommandResponseHelpers.parseNewsgroupListResponseFrom(cmdStream);
	}

}
