package sm.nntp.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

public interface NntpConnection extends Closeable {

	public Iterable<NewsgroupStatus> fetchListOfAllNewsgroups()
			throws IOException;

	public Iterable<NewsgroupStatus> fetchListOfNewsgroupsPublishedSince(
			Date since) throws IOException;

	public Iterable<ArticleHeader> fetchArticleHeadersForNewsgroup(
			String newsgroup) throws IOException;

	public Iterable<String> fetchMessageIdsForArticlesInNewsgroupPostedSince(
			String newsgroup, Date since) throws IOException;

	public Iterable<String> fetchMessageIdsForArticlesPostedSince(Date since) throws IOException;

	public ArticleHeader fetchArticleHeaderForMessageId(String messageId) throws IOException;

	public Article fetchArticleByMessageId(String messageId) throws IOException;

}