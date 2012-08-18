package sm.nntp.client.internal;

import java.io.IOException;

import sm.nntp.client.Article;
import sm.nntp.client.ArticleCursor;
import sm.nntp.client.ArticleId;
import sm.nntp.client.ArticleRetrievalPredicate;
import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.internal.commands.ArticleCommand;
import sm.nntp.client.internal.commands.BodyCommand;
import sm.nntp.client.internal.commands.HeadCommand;
import sm.nntp.client.internal.commands.NextCommand;
import sm.nntp.client.internal.commands.SelectNewsgroupCommand;
import sm.nntp.client.internal.commands.StatCommand;

public class ArticleCursorImplementationForAllArticlesInNewsgroup implements
		ArticleCursor {

	private String name;
	private NntpCommandStream cmdStream;
	private boolean started;
	private boolean endOfArticlesReached;
	private ArticleRetrievalPredicate articleIdPredicate;
	private ArticleId currentArticleId;

	public ArticleCursorImplementationForAllArticlesInNewsgroup(String name, ArticleRetrievalPredicate articleIdPredicate) {
		this.name = name;
		this.articleIdPredicate = articleIdPredicate;
	}
	
	public ArticleCursor startOn(NntpCommandStream cmdStreamToRunOn) throws IOException {
		NewsgroupStatus newsgroupStatus = new SelectNewsgroupCommand(name).executeOn(cmdStreamToRunOn);
		
		endOfArticlesReached = newsgroupStatus.getEstimatedCountOfArticles() == 0;
		cmdStream = cmdStreamToRunOn;
		started = true;
		currentArticleId = null;
		
		return this;
	}

	@Override
	public void close() throws IOException {
		cmdStream = null;
		started = false;
	}

	@Override
	public Article getNextOrNull() throws IOException {
		ensureStarted();
		
		ArticleId articleId = advanceArticleCursorUntilWeMatchPredicateOrNull();
		if (articleId == null)
			return null;
		
		boolean wantHeaders = articleIdPredicate.retrieveHeadersFor(articleId);
		boolean wantBody = articleIdPredicate.retrieveBodyFor(articleId);
		
		if (wantHeaders && wantBody)
			return new ArticleCommand().executeOn(cmdStream);
		else if (wantHeaders)
			return new HeadCommand().executeOn(cmdStream);
		else if (wantBody)
			return new BodyCommand().executeOn(cmdStream);
		else
			return Article.WithJustTheId(articleId);
	}

	private ArticleId advanceArticleCursorUntilWeMatchPredicateOrNull() throws IOException {
		
		if (endOfArticlesReached)
			return null;
		
		do {
			currentArticleId = (currentArticleId == null)
					? new StatCommand().executeOn(cmdStream)
					: new NextCommand().executeOn(cmdStream);
		
			if (currentArticleId == null)
				endOfArticlesReached = true;		
		}
		while (currentArticleId != null
					&& !articleIdPredicate.consider(currentArticleId));
		
		return currentArticleId;
	}

	private void ensureStarted() {
		if (!started)
			throw new IllegalStateException("article cursor is closed");
	}

}
