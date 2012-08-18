
package sm.nntp.client;

import java.io.IOException;
import java.net.Socket;

import sm.nntp.client.internal.NntpConnectionImplementation;
import sm.nntp.client.internal.NullInspector;

public class NntpClient {
	private NntpStreamInspector inspectorToUse;

	public NntpClient() {
		inspectorToUse = new NullInspector();
	}
	
	public void setNntpStreamInspectorToUse(NntpStreamInspector inspector) {
		this.inspectorToUse = inspector;
	}
	
	public NntpConnection connectTo(String hostname) throws IOException {
		return connectTo(hostname, 119);
	}
	
	public NntpConnection connectTo(String hostname, int port) throws IOException {
		NntpConnectionImplementation connection = new NntpConnectionImplementation(inspectorToUse);
		connection.setupOnSocket(new Socket(hostname, port));
		return connection;
	}
}
