package sm.nntp.client;

public enum ArticleVisitorResponse {
	WANT_BODY,
	WANT_HEADERS,
	WANT_BODY_AND_HEADERS,
	WANT_NOTHING,
	TERMINATE
}
