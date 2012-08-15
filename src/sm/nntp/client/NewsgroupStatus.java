package sm.nntp.client;

public class NewsgroupStatus {
	private String newsgroup;
	private int firstArticleNum;
	private int lastArticleNum;
	private boolean isPostingAllowedFlag;
	
	public NewsgroupStatus(String name, int firstNum, int lastNum, boolean postingIsAllowed) {
		this.newsgroup = name;
		this.firstArticleNum = firstNum;
		this.lastArticleNum = lastNum;
		this.isPostingAllowedFlag = postingIsAllowed;
	}
	
	public String getName() {
		return newsgroup;
	}
	public int getFirstArticleNum() {
		return firstArticleNum;
	}
	public int getLastArticleNum() {
		return lastArticleNum;
	}
	public boolean isPostingAllowed() {
		return isPostingAllowedFlag;
	}
}
