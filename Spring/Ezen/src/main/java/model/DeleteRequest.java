package model;

public class DeleteRequest {
	
	
	// 삭제시 필요한 자바 빈
	
	private int articleId;
	private String password;
	
	public int getArticleId() {
		return articleId;
	}
	public String getPassword() {
		return password;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
