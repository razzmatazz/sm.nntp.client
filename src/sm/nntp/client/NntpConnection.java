package sm.nntp.client;

import java.io.Closeable;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import sm.nntp.client.internal.NntpCommandStream;
import sm.nntp.client.internal.commands.ListNewsgroupsCommand;
import sm.nntp.client.internal.commands.ServerHelloCommand;

public class NntpConnection implements Closeable {
	
	private Socket socket;
	private boolean setupDone;
	private NntpCommandStream cmdStream;
	private ServerHelloCommand helloCommand;
	private NntpStreamInspector nntpInspector;

	public NntpConnection(NntpStreamInspector inspector) {
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

	public Iterable<NewsgroupStatus> fetchListOfAllNewsgroups() throws IOException {
		ensureSetupDone();
		
		ListNewsgroupsCommand listNewsGroupsCmd = new ListNewsgroupsCommand();
		return listNewsGroupsCmd.executeOn(cmdStream);
	}
	
	public Iterable<ArticleHeader> fetchArticleHeadersForNewsgroup(String newsgroup) {
		throw new UnsupportedOperationException();
	}
	
	public Iterable<String> fetchMessageIdsForArticlesInNewsgroupPostedSince(
			String newsgroup,
			Date since) {
		throw new UnsupportedOperationException();
	}
	
	public Iterable<String> fetchMessageIdsForArticlesPostedSince(Date since) {
		throw new UnsupportedOperationException();
	}
		
	public ArticleHeader fetchArticleHeaderForMessageId(String messageId) {
		throw new UnsupportedOperationException();
	}
	
	public Article fetchArticleByMessageId(String messageId) {
		throw new UnsupportedOperationException();
	}
}
