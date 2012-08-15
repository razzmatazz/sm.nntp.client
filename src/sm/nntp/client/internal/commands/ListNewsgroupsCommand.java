package sm.nntp.client.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class ListNewsgroupsCommand {

	public Iterable<NewsgroupStatus> executeOn(NntpCommandStream cmdStream) throws IOException {
		cmdStream.writeCommand("list");
		
		ResponseHeader header = cmdStream.readResponseHeaderOrExceptionOnError();
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		List<NewsgroupStatus> newsgroups = new ArrayList<NewsgroupStatus>();
		
		for(String line: cmdStream.readResponseTextContentAsLines()) {
			newsgroups.add(new NewsgroupStatus(line, 0, 1, false));
		}
		
		return newsgroups;
	}

}
