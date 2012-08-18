package sm.nntp.client;

public interface ArticleRetrievalPredicate {
	public boolean consider(ArticleId articleId);
	public boolean retrieveHeadersFor(ArticleId articleId);
	public boolean retrieveBodyFor(ArticleId articleId);
}
