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
	public void inArticleBody(ArticleId articleId) {
	}

	@Override
	public void onArticleBodyLine(ArticleId articleId, String bodyLine) {
	}

	@Override
	public boolean outOfArticleBody(ArticleId articleId) {
		return true;
	}

	@Override
	public void inArticleHeaders(ArticleId articleId) {
	}

	@Override
	public void onArticleHeadersLine(ArticleId articleId, String headersLine) {
	}

	@Override
	public ArticleVisitorResponse outOfArticleHeaders(ArticleId articleId) {
		return ArticleVisitorResponse.WANT_BODY;
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
