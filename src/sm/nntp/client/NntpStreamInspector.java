package sm.nntp.client;

public interface NntpStreamInspector {
	public void onOutputLine(String line);
	public void onInputLine(String line);
}
