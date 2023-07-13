package model;

import java.util.ArrayList;
import java.util.List;

public class ArticleListModel {

	// 게시판 리스트 자바빈
	
	private List<Article> articleList;
	private int requestPage;
	private int totalPageCount;
	private int startRow;
	private int endRow;
	
	public ArticleListModel() {
		this(new ArrayList<Article>(),0,0,0,0);
	}
	
	
	// 모든 필드변수를 가진 생성자
	
	public ArticleListModel(List<Article> articleList, int requestPage, int totalPageCount, int startRow, int endRow) {
		this.articleList = articleList;
		this.requestPage = requestPage;
		this.totalPageCount = totalPageCount;
		this.startRow = startRow;
		this.endRow = endRow;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public int getRequestPage() {
		return requestPage;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	// 게시글 목록이 비어있는지 확인하는 메소드
	public boolean isHasArticle() {
		return ! articleList.isEmpty();
	}
	
}
