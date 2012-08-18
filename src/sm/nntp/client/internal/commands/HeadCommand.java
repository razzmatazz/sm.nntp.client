package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.internal.LineOutputStream;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class HeadCommand {

	public void executeOn(NntpCommandStream cmdStream, LineOutputStream headerOutput) throws IOException {
		cmdStream.writeCommand("head");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		if (header.getStatusCode() == 423)
			return;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		cmdStream.readResponseTextContentAsLinesInto(headerOutput);
	}

}
