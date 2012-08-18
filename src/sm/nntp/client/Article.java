package sm.nntp.client;

public class Article {
	
	private ArticleId id;
	private Iterable<String> headerLines;
	private Iterable<String> bodyLines;

	private Article(ArticleId articleId, Iterable<String> headerLines,
			Iterable<String> bodyLines) {
		this.id = articleId;
		this.headerLines = headerLines;
		this.bodyLines = bodyLines;
	}

	public static Article WithHeaderAndBody(ArticleId articleId,
			Iterable<String> headerLines, Iterable<String> bodyLines) {
		
		return new Article(articleId, headerLines, bodyLines);
	}

	public ArticleId getId() {
		return id;
	}

	public Iterable<String> getHeaderLines() {
		return headerLines;
	}

	public Iterable<String> getBodyLines() {
		return bodyLines;
	}

	public static Article WithJustTheId(ArticleId articleId) {
		return new Article(articleId, null, null);
	}

	public static Article WithBodyOnly(ArticleId articleId,
			Iterable<String> bodyLines) {
		return new Article(articleId, null, bodyLines);
	}

	public static Article WithHeadersOnly(ArticleId articleId,
			Iterable<String> headerLines) {
		return new Article(articleId, headerLines, null);
	}
	
}
