package sm.nntp.client;

import java.security.InvalidParameterException;

public class ArticleId {

	private String id;

	private ArticleId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public static ArticleId parseFromStringOrException(String string) {
		if (string.length() < 3)
			throw new InvalidParameterException("invalid article id: " + string);
		
		String articleIdInnerPart = string.substring(1, string.length() - 1);
		if (!string.startsWith("<")
				|| !string.endsWith(">")
				|| articleIdInnerPart.contains(">")
				|| articleIdInnerPart.contains("<")
			)
		{
			throw new InvalidParameterException("invalid article id: " + string);
		}
		
		// TODO: check article id in a more detailed way
		
		return new ArticleId(articleIdInnerPart);
	}
	
	@Override
	public String toString() {
		return "<" + id + ">";
	}
}
