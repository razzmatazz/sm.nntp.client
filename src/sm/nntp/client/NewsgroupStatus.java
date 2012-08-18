package sm.nntp.client;

public class NewsgroupStatus {
	private String newsgroup;
	private int lowArticleNum;
	private int highArticleNum;
	private boolean isPostingAllowedFlag;
	
	public NewsgroupStatus(String name, int highNum, int lowNum, boolean postingIsAllowed) {
		this.newsgroup = name;
		this.lowArticleNum = lowNum;
		this.highArticleNum = highNum;
		this.isPostingAllowedFlag = postingIsAllowed;
	}
	
	public String getName() {
		return newsgroup;
	}
	public int getLowArticleNum() {
		return lowArticleNum;
	}
	public int getHighArticleNum() {
		return highArticleNum;
	}
	public boolean isPostingAllowed() {
		return isPostingAllowedFlag;
	}

	public int getEstimatedCountOfArticles() {
		if (lowArticleNum >= 0 && highArticleNum > lowArticleNum)
			return highArticleNum - lowArticleNum;
		else
			return 0;
	}
}
