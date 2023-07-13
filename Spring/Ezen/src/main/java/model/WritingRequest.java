package model;

public class WritingRequest {

	//글쓰기 자바빈
	
	private String writerName;
	private String password;
	private String title;
	private String content;
	public String getWriterName() {
		return writerName;
	}
	public String getPassword() {
		return password;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	// WritingRequest객체를 article 객체에 담기 위한 메소드
	
	public Article toArticle() {
		Article article = new Article();
		article.setWriterName(writerName);
		article.setPassword(password);
		article.setTitle(title);
		article.setContent(content);
		return article;
	}
	
}
