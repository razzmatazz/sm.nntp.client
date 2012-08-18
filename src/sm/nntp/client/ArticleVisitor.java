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
	
	public boolean inArticleBody(ArticleId articleId);
	public boolean onArticleBodyLine(ArticleId articleId, String bodyLine);
	public boolean outOfArticleBody(ArticleId articleId);
	
	public boolean inArticleHeaders(ArticleId articleId);
	public boolean onArticleHeadersLine(ArticleId articleId, String headersLine);
	public boolean outOfArticleHeaders(ArticleId articleId);
	
	public boolean outOfArticle(ArticleId articleId);
	
	public boolean outOfArticleListing();
}
