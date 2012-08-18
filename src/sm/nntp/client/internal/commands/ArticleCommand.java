package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.internal.HeadersAndBodyLineOutputStreamMux;
import sm.nntp.client.internal.LineOutputStream;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;
import sm.nntp.client.internal.ResponseStatusType;

public class ArticleCommand {

	public void executeOn(
			NntpCommandStream cmdStream,
			final LineOutputStream headerOutput,
			final LineOutputStream bodyOutput
			) throws IOException {
		cmdStream.writeCommand("article");
		
		ResponseHeader header = cmdStream.readResponseHeader();
		
		// return null if we are trying to receive article from the cursor and we get
		// "423 Bad article number"
		if (header.getStatusCode() == 423)
			return;
		
		header.ensureStatusCodeIsOfType(ResponseStatusType.CommandOK);
		
		cmdStream.readResponseTextContentAsLinesInto(
			new HeadersAndBodyLineOutputStreamMux(headerOutput, bodyOutput)
		);
		
	}

}
