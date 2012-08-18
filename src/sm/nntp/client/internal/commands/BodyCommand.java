package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.internal.LineOutputStream;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class BodyCommand {

	public void executeOn(NntpCommandStream cmdStream, LineOutputStream bodyOutputStream) throws IOException {
		cmdStream.writeCommand("body");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		if (header.getStatusCode() == 423)
			return;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		cmdStream.readResponseTextContentAsLinesInto(bodyOutputStream);
	}

}
