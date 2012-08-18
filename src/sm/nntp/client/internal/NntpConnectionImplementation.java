package sm.nntp.client.internal;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import sm.nntp.client.Article;
import sm.nntp.client.ArticleHeader;
import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.NntpConnection;
import sm.nntp.client.NntpStreamInspector;
import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.commands.ListNewNewsgroupsCommand;
import sm.nntp.client.internal.commands.ListNewsgroupsCommand;
import sm.nntp.client.internal.commands.ServerHelloCommand;

public class NntpConnectionImplementation implements NntpConnection {
	
	private Socket socket;
	private boolean setupDone;
	private NntpCommandStream cmdStream;
	private ServerHelloCommand helloCommand;
	private NntpStreamInspector nntpInspector;

	public NntpConnectionImplementation(NntpStreamInspector inspector) {
		nntpInspector = inspector;
	}
	
	public void setupOnSocket(Socket socket) throws IOException {
		if (setupDone)
			throw new IllegalStateException("connection is already set up");
		
		this.socket = socket;
		cmdStream = new NntpCommandStream(socket.getInputStream(), socket.getOutputStream(), nntpInspector);
		setupDone = true;
		
		helloCommand = new ServerHelloCommand();
		helloCommand.executeOn(cmdStream);
	}
	
	public void close() {
		ensureSetupDone();
		
		try {
			socket.close();
		} catch (IOException e) {
			// ignore any errors when closing connections
		}
		
		setupDone = false;
	}
	
	private void ensureSetupDone() {
		if (!setupDone)
			throw new IllegalStateException("not connected");
	}

	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchListOfAllNewsgroups()
	 */
	@Override
	public Iterable<NewsgroupStatus> fetchListOfAllNewsgroups() throws IOException {
		ensureSetupDone();
		return new ListNewsgroupsCommand().executeOn(cmdStream);
	}
	
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchListOfNewsgroupsPublishedSince(java.util.Date)
	 */
	@Override
	public Iterable<NewsgroupStatus> fetchListOfNewsgroupsPublishedSince(Date since) throws IOException {
		ensureSetupDone();		
		return new ListNewNewsgroupsCommand(since).executeOn(cmdStream);
	}
	
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchArticleHeadersForNewsgroup(java.lang.String)
	 */
	@Override
	public Iterable<ArticleHeader> fetchArticleHeadersForNewsgroup(String newsgroup) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchMessageIdsForArticlesInNewsgroupPostedSince(java.lang.String, java.util.Date)
	 */
	@Override
	public Iterable<String> fetchMessageIdsForArticlesInNewsgroupPostedSince(
			String newsgroup,
			Date since) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchMessageIdsForArticlesPostedSince(java.util.Date)
	 */
	@Override
	public Iterable<String> fetchMessageIdsForArticlesPostedSince(Date since) {
		throw new UnsupportedOperationException();
	}
		
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchArticleHeaderForMessageId(java.lang.String)
	 */
	@Override
	public ArticleHeader fetchArticleHeaderForMessageId(String messageId) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see sm.nntp.client.NntpConnection#fetchArticleByMessageId(java.lang.String)
	 */
	@Override
	public Article fetchArticleByMessageId(String messageId) {
		throw new UnsupportedOperationException();
	}
}
