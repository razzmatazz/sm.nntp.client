package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class ListNewsgroupsCommand extends NewsgroupsCommandBase {

	public Iterable<NewsgroupStatus> executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("list");
		
		ResponseHeader header = cmdStream.readResponseHeaderOrExceptionOnError();
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		return parseNewsgroupListResponseFrom(cmdStream);
	}
}
