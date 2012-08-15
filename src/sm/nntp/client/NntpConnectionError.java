package sm.nntp.client;

import java.io.IOException;

public class NntpConnectionError extends IOException {
	public NntpConnectionError(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
