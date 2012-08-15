package sm.nntp.client.internal;

import sm.nntp.client.NntpConnectionError;

public class ResponseHeader {

	private int responseCode;
	private String completeResponseLine;
	private String responseStatus;

	public ResponseHeader(String completeResponseLine, int responseCode, String responseStatus) throws NntpConnectionError {
		this.completeResponseLine = completeResponseLine;
		this.responseCode = validateResponseCodeOrException(responseCode);
		this.responseStatus = responseStatus;
	}
	
	private static int validateResponseCodeOrException(int responseCode) throws NntpConnectionError {
		if (responseCode < 100 || responseCode >= 1000) {
			throw new NntpConnectionError(String.format(
					"invalid nntp response code %d from server", responseCode));
		}
		
		return responseCode;
	}

	public int getStatusCode() {
		return responseCode;
	}
	
	public String getStatusString() {
		return responseStatus;
	}
	
	public ResponseStatusType getStatusType() {
		switch(getStatusCode() / 100) {
		case 1: return ResponseStatusType.InformativeMessage;
		case 2: return ResponseStatusType.CommandOK;
		case 3: return ResponseStatusType.CommandOKSoFarSendMore;
		case 4: return ResponseStatusType.CommandCouldNotBePerformed;
		case 5: return ResponseStatusType.CommandErrorOrNotImplemented;
		default:
			return ResponseStatusType.UnknownResponseCode;
		}
	}
	
	public String getResponseLine() {
		return completeResponseLine;
	}

	public void throwExceptionOnErrorStatusCode() throws NntpConnectionError {
		switch(getStatusType()) {
		case CommandCouldNotBePerformed:
		case CommandErrorOrNotImplemented:
		case UnknownResponseCode:
			throw new NntpConnectionError(getResponseLine());
			
		default:
			break;
		}
	}

	public void ensureStatusCodeIsOfType(ResponseStatusType expectedStatusType) throws NntpConnectionError {
		if (getStatusType() != expectedStatusType) {
			throw new NntpConnectionError(String.format(
					"unexpected error code type on command: %s (%d)",
					getStatusType(), getStatusCode()
			));
		}
	}
}
