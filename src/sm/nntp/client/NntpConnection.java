package sm.nntp.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

public interface NntpConnection extends Closeable {

	public Iterable<NewsgroupStatus> fetchListOfAllNewsgroups()
			throws IOException;

	public Iterable<NewsgroupStatus> fetchListOfNewsgroupsPublishedSince(
			Date since) throws IOException;
	
	/**
	 * Returns article cursor that is used to retrieve articles from a live
	 * connection. You may want to provide an articleIdPredicate to avoid
	 * fetching articles you don't want from the server.
	 * @param name group name
	 * @param articlePredicate article predicate used to check whether to
	 * 			retrieve parts of the article or not. It may be set it to null,
	 * 			to fetch body and headers of all articles.
	 * @return {@link ArticleCursor} object that is used to iterate articles.
	 * 			ArticleCursor is {@link Closeable}.
	 * @throws IOException if there is a server error or connection failure.
	 */
	public ArticleCursor fetchArticlesForNewsgroup(String name, ArticleRetrievalPredicate articlePredicate)
			throws IOException;

}