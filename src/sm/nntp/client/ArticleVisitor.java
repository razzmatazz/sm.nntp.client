package sm.nntp.client;

/**
 * This interface is used when retrieving article contents
 * live off the wire. Visitor can decide if we wants to see body
 * and/or headers of the article, or nothing just when
 * inspecting articles in a newsgroup or when retrieving new articles.
 * 
 * Every method (except {@link #inArticle}) returns true, if it is wanted
 * for visiting to continue, or false to terminate visiting. On termination
 * outOfXXX() methods are invoked.
 */
public interface ArticleVisitor {
	public boolean inArticleListing();
	
	public ArticleVisitorResponse inArticle(ArticleId articleId);
	
	public void inArticleHeaders(ArticleId articleId);
	
	public void onArticleHeadersLine(ArticleId articleId, String headersLine);
	
	/**
	 * It is possible for the visitor to decide whether to check the body
	 * of the message just after inspecting the headers by providing
	 * the corresponding {@link ArticleVisitorResponse} value.
	 * 
	 * However, in case {@link #inArticle} has returned WANT_HEADERS_AND_BODY,
	 * it is already too late and we already have data over the wire (but
	 * {@link #inArticleBody} will not be invoked in this case for
	 * convenience).
	 * 
	 * @param articleId article id
	 * @return ArticleVisitorResponse, that says whether we want to see the
	 * 				body, or not, or whether to terminate it all.
	 */
	public ArticleVisitorResponse outOfArticleHeaders(ArticleId articleId);
	
	public void inArticleBody(ArticleId articleId);
	public void onArticleBodyLine(ArticleId articleId, String bodyLine);
	public boolean outOfArticleBody(ArticleId articleId);
	
	public boolean outOfArticle(ArticleId articleId);
	
	public boolean outOfArticleListing();
}
