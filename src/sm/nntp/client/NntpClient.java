
package sm.nntp.client;

import java.io.IOException;
import java.net.Socket;

public class NntpClient {
	public NntpClient() {
	}
	
	public NntpConnection connectTo(String hostname) throws IOException {
		return connectTo(hostname, 119);
	}
	
	public NntpConnection connectTo(String hostname, int port) throws IOException {
		NntpConnection connection = new NntpConnection();
		connection.setupOnSocket(new Socket(hostname, port));
		return connection;
	}
}
