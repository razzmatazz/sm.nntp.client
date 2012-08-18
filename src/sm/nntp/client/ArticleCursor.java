package sm.nntp.client;

import java.io.Closeable;
import java.io.IOException;

public interface ArticleCursor extends Closeable {
	public Article getNextOrNull() throws IOException;
}
