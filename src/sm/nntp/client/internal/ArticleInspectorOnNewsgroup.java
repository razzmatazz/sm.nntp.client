package sm.nntp.client.internal;

import java.io.IOException;


import sm.nntp.client.ArticleId;
import sm.nntp.client.ArticleVisitor;
import sm.nntp.client.ArticleVisitorResponse;
import sm.nntp.client.NewsgroupStatus;
import sm.nntp.client.internal.commands.ArticleCommand;
import sm.nntp.client.internal.commands.BodyCommand;
import sm.nntp.client.internal.commands.HeadCommand;
import sm.nntp.client.internal.commands.NextCommand;
import sm.nntp.client.internal.commands.SelectNewsgroupCommand;
import sm.nntp.client.internal.commands.StatCommand;

public class ArticleInspectorOnNewsgroup {

	private String name;

	public ArticleInspectorOnNewsgroup(String name) {
		this.name = name;
	}

	public void inspectWithVisitorOn(NntpCommandStream cmdStream,
			ArticleVisitor visitor) throws IOException {
		NewsgroupStatus newsgroupStatus = new SelectNewsgroupCommand(name).executeOn(cmdStream);
		
		if (visitor.inArticleListing())
		{			
			if (newsgroupStatus.getEstimatedCountOfArticles() > 0)
			{
				ArticleId currentArticleId = null;
				
				do {
					currentArticleId = (currentArticleId == null)
							? new StatCommand().executeOn(cmdStream)
							: new NextCommand().executeOn(cmdStream);
				
					if (currentArticleId == null)
						break;
					
					if (!inspectSelectedArticleWith(cmdStream, currentArticleId, visitor))
						break;
				}
				while (currentArticleId != null);
			}
		}
		
		visitor.outOfArticleListing();
	}
	
	private boolean inspectSelectedArticleWith(
			NntpCommandStream cmdStream,
			ArticleId articleId,
			ArticleVisitor visitor) throws IOException {
		ArticleVisitorResponse resp = visitor.inArticle(articleId);
		
		boolean continueVisiting = true;
		boolean wantHeaders = false;
		boolean wantBody = false;
		
		switch(resp) {
		case TERMINATE:
			continueVisiting = false;
			break;
		case WANT_BODY:
			wantBody = true;
			break;
		case WANT_BODY_AND_HEADERS:
			wantBody = true;
			wantHeaders = true;
			break;
		case WANT_HEADERS:
			wantHeaders = true;
			break;
		default:
			break;
		}
		
		if (continueVisiting)
		{
			if (wantBody && wantHeaders)
				new ArticleCommand().executeOn(cmdStream, mapHeaderLineOutputTo(articleId, visitor), mapBodyLineOutputTo(articleId, visitor));
			else if (wantBody)
				new HeadCommand().executeOn(cmdStream, mapBodyLineOutputTo(articleId, visitor));
			else if (wantHeaders)
				new BodyCommand().executeOn(cmdStream, mapHeaderLineOutputTo(articleId, visitor));
		}
		
		return continueVisiting && visitor.outOfArticle(articleId);
	}

	private LineOutputStream mapBodyLineOutputTo(final ArticleId articleId,
			final ArticleVisitor visitor) {
		return new LineOutputStream() {
			@Override
			public void onLine(String line) {
				visitor.onArticleBodyLine(articleId, line);
			}
			
			@Override
			public void onFinish() {
				visitor.outOfArticleBody(articleId);
			}
			
			@Override
			public void onBegin() {
				visitor.inArticleBody(articleId);
			}
		};
	}

	private LineOutputStream mapHeaderLineOutputTo(final ArticleId articleId,
			final ArticleVisitor visitor) {
		return new LineOutputStream() {
			@Override
			public void onLine(String line) {
				visitor.onArticleHeadersLine(articleId, line);
			}
			
			@Override
			public void onFinish() {
				visitor.outOfArticleHeaders(articleId);
			}
			
			@Override
			public void onBegin() {
				visitor.inArticleHeaders(articleId);
			}
		};
	}

}
