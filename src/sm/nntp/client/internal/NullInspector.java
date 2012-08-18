package sm.nntp.client.internal;

import sm.nntp.client.NntpStreamInspector;

public class NullInspector implements NntpStreamInspector {

	@Override
	public void onOutputLine(String line) {
	}

	@Override
	public void onInputLine(String line) {
	}

}
