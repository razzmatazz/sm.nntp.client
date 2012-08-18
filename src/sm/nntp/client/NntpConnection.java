package sm.nntp.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

public interface NntpConnection extends Closeable {

	public Iterable<NewsgroupStatus> fetchListOfAllNewsgroups()
			throws IOException;

	public Iterable<NewsgroupStatus> fetchListOfNewsgroupsPublishedSince(
			Date since) throws IOException;

}