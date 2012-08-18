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
			LineStreamOutputMapperToArticleHeaders headerOutput
				= new LineStreamOutputMapperToArticleHeaders(articleId, visitor, resp);
			
			LineStreamOutputMapperToArticleBody bodyOutput
				= new LineStreamOutputMapperToArticleBody(articleId, visitor, headerOutput);
			
			if (wantBody && wantHeaders) {
				new ArticleCommand().executeOn(cmdStream, headerOutput, bodyOutput);
			}
			else {
				if (wantHeaders) {
					new HeadCommand().executeOn(cmdStream, headerOutput);
					
					// it can be that after visitor.outOfArticleHeaders() we get a wish to
					// get the body, after all 
					wantBody = wantBody || headerOutput.getVisitorResponseSaysItWantsBody();
				}
				
				// it can be that after visitor.outOfArticleHeaders() we get a wish to
				// get the body, after all
				if (wantBody) {
					new BodyCommand().executeOn(cmdStream, bodyOutput);
				}
			}
			
			continueVisiting = continueVisiting
						&& headerOutput.getContinueVisiting()
						&& bodyOutput.getContinueVisiting();
		}
		
		return continueVisiting && visitor.outOfArticle(articleId);
	}

	private class LineStreamOutputMapperToArticleHeaders implements LineOutputStream {
		private ArticleVisitorResponse visitorResponse;
		private ArticleId articleId;
		private ArticleVisitor visitor;

		public LineStreamOutputMapperToArticleHeaders(ArticleId articleId,
				ArticleVisitor visitor,
				ArticleVisitorResponse assumedVisitorResponse) {
			this.articleId = articleId;
			this.visitor = visitor;
			this.visitorResponse = assumedVisitorResponse;
		}

		public boolean getContinueVisiting() {
			return visitorResponse != ArticleVisitorResponse.TERMINATE;
		}

		public boolean getVisitorResponseSaysItWantsBody() {
			return visitorResponse == ArticleVisitorResponse.WANT_BODY
					|| visitorResponse == ArticleVisitorResponse.WANT_BODY_AND_HEADERS;
		}

		@Override
		public void onBegin() {
			visitor.inArticleHeaders(articleId);
		}

		@Override
		public void onLine(String line) {
			visitor.onArticleHeadersLine(articleId, line);
		}
		
		@Override
		public void onFinish() {
			visitorResponse = visitor.outOfArticleHeaders(articleId);
		}
	}

	private class LineStreamOutputMapperToArticleBody implements LineOutputStream {
		private ArticleId articleId;
		private ArticleVisitor visitor;
		private boolean continueVisiting;
		private LineStreamOutputMapperToArticleHeaders headerOutput;

		public LineStreamOutputMapperToArticleBody(
				ArticleId articleId,
				ArticleVisitor visitor,
				LineStreamOutputMapperToArticleHeaders headerOutput) {
			this.articleId = articleId;
			this.visitor = visitor;
			this.continueVisiting = true;
			this.headerOutput = headerOutput;
		}

		public boolean getContinueVisiting() {
			return continueVisiting;
		}

		
		@Override
		public void onBegin() {
			if (headerOutput.getVisitorResponseSaysItWantsBody())
				visitor.inArticleBody(articleId);
		}

		@Override
		public void onLine(String line) {
			if (headerOutput.getVisitorResponseSaysItWantsBody())
				visitor.onArticleBodyLine(articleId, line);
		}
		
		@Override
		public void onFinish() {
			if (headerOutput.getVisitorResponseSaysItWantsBody())
				continueVisiting = visitor.outOfArticleBody(articleId);
		}
	}

}
