package model;

public class UpdateRequest {
	
	// 업데이트 자바빈
	
	private int articleId;
	private String title;
	private String content;
	private String password;
	
	public int getArticleId() {
		return articleId;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getPassword() {
		return password;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
