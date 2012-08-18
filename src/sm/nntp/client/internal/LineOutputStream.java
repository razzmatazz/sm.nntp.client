package sm.nntp.client.internal;

public interface LineOutputStream {
	public void onBegin();
	public void onLine(String line);
	public void onFinish();
}
