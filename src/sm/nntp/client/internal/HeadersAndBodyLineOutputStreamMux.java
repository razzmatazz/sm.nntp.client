package sm.nntp.client.internal;

public class HeadersAndBodyLineOutputStreamMux implements LineOutputStream {

	private boolean inHeaders = true;
	private boolean inBody = false;
	private LineOutputStream headerOutput;
	private LineOutputStream bodyOutput;

	public HeadersAndBodyLineOutputStreamMux(LineOutputStream headerOutput,
			LineOutputStream bodyOutput) {
		this.headerOutput = headerOutput;
		this.bodyOutput = bodyOutput;
	}

	@Override
	public void onLine(String line) {
		// empty line separates headers and body
		if (line.equals("") && inHeaders) {
			headerOutput.onFinish();
			inHeaders = false;
			
			bodyOutput.onBegin();
			inBody = true;
		} else {
			if (inHeaders)
				headerOutput.onLine(line);
			else if (inBody)
				bodyOutput.onLine(line);
		}
	}

	@Override
	public void onBegin() {
		headerOutput.onBegin();
	}

	@Override
	public void onFinish() {
		if (inHeaders)
			headerOutput.onFinish();
		else if (inBody)
			bodyOutput.onFinish();
	}
}
