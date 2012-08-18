package sm.nntp.client;

public class ArticleVisitorDefaultImpl implements ArticleVisitor {

	@Override
	public boolean inArticleListing() {
		return true;
	}

	@Override
	public ArticleVisitorResponse inArticle(ArticleId articleId) {
		return ArticleVisitorResponse.WANT_BODY_AND_HEADERS;
	}

	@Override
	public boolean inArticleBody(ArticleId articleId) {
		return true;
	}

	@Override
	public boolean onArticleBodyLine(ArticleId articleId, String bodyLine) {
		return true;
	}

	@Override
	public boolean outOfArticleBody(ArticleId articleId) {
		return true;
	}

	@Override
	public boolean inArticleHeaders(ArticleId articleId) {
		return true;
	}

	@Override
	public boolean onArticleHeadersLine(ArticleId articleId, String headersLine) {
		return true;
	}

	@Override
	public boolean outOfArticleHeaders(ArticleId articleId) {
		return true;
	}

	@Override
	public boolean outOfArticle(ArticleId articleId) {
		return true;
	}

	@Override
	public boolean outOfArticleListing() {
		return true;
	}

}
