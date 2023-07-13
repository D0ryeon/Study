package model;

public class ReplyingRequest extends WritingRequest {
	
	
	// 답변시 필요한 자바빈
	
	private int parentArticleId;
	
	public int getParentArticleId() {
		return parentArticleId;
	}
	
	public void setParentArticleId(int parentArticleId) {
		this.parentArticleId = parentArticleId;
	}

}
