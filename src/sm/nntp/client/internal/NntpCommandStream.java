package sm.nntp.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sm.nntp.client.NntpConnectionError;
import sm.nntp.client.NntpStreamInspector;

public class NntpCommandStream {
	private static final String NntpResponseTerminatorLine = ".\r\n";
	
	private InputStream input;
	private OutputStream output;
	private NntpStreamInspector inspector;

	public NntpCommandStream(InputStream input, OutputStream output, NntpStreamInspector inspector) throws IOException {
		this.input = input;
		this.output = output;
		this.inspector = inspector;
	}

	public ResponseHeader readResponseHeaderOrExceptionOnError() throws IOException {
		String line = readCrLfTerminatedLine();
		String[] parts = StringUtil.splitLineIntoWordAndRemainder(line);
		
		int responseStatusCode = Integer.parseInt(parts[0]);
		String responseStatusString = parts[1];
		
		ResponseHeader header = new ResponseHeader(line, responseStatusCode, responseStatusString);
		header.throwExceptionOnErrorStatusCode();
		return header;
	}
	
	private String readDotTerminatedInput() throws IOException {
		StringBuffer input = new StringBuffer();
		
		while (true) {
			String line = readCrLfTerminatedLine();
			if (line.equals(NntpResponseTerminatorLine))
				break;
			
			if (line.startsWith(".."))
				line = line.substring(1);
			
			input.append(line);
		}
		
		return input.toString();
	}

	private String readCrLfTerminatedLine() throws IOException {
		StringBuffer line = new StringBuffer();
		boolean crSeenAlready = false;
		
		while (true) {
			int code = input.read();
			if (code == -1)
			{
				throw new NntpConnectionError("nntp server has unexpected closed connection");
			}
			if (code == 0x0d)
			{
				if (crSeenAlready)
					throw new NntpConnectionError("nntp server has returned invalid response: double CR w/o line feed in response line");
				
				crSeenAlready = true;
			}
			else if (code == 0x0a)
			{
				if (!crSeenAlready)
					throw new NntpConnectionError("nntp server has returned invalid response: LF on line w/o a preceeding CR");
				
				break;
			}
			else
			{
				if (crSeenAlready)
					throw new NntpConnectionError("nntp server has returned invalid response: CR seen on response line, but now LF");
				
				line.append((char)code);
			}
		}
		
		inspector.onInputLine(line.toString());
		
		line.append((char)0x0d);
		line.append((char)0x0a);
		
		return line.toString();
	}
	
	public void writeCommand(String command) throws IOException {
		if (command.length() > 510)
			throw new UnsupportedOperationException("internal error: nntp command length must be under 510 chars");
		
		byte[] bytes = new byte[512];
		int index = 0;
		
		for(index = 0; index < command.length(); index++)
		{
			int charCode = command.codePointAt(index);
			if (charCode < 32 || charCode > 127)
				throw new UnsupportedOperationException("internal error: nntp command should contain ASCII chars w/o control codes");
			
			bytes[index] = (byte)charCode;
		}
		
		bytes[index++] = 0x0d;
		bytes[index++] = 0x0a;
		
		inspector.onOutputLine(command);
		
		output.write(bytes, 0, index);
	}

	public Iterable<String> readResponseTextContentAsLines() throws IOException {
		String content = readDotTerminatedInput();
		return StringUtil.splitIntoCrLfTerminatedLines(content);
	}

}
