package sm.nntp.client.internal.commands;

import java.io.IOException;

import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.ResponseHeader;

public class ServerHelloCommand {
	public ResponseHeader executeOn(NntpCommandStream cmdStream) throws IOException {
		return cmdStream.readResponseHeaderOrExceptionOnError();
	}
}
