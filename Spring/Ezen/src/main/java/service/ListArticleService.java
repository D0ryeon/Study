package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.ArticleDao;
import model.Article;
import model.ArticleListModel;
import jdbc.JdbcUtil;
import jdbc.ConnectionProvider;


public class ListArticleService {
	private static ListArticleService instance = new ListArticleService();
	public static ListArticleService getInstance() {
		return instance;
	}
	
	public static final int COUNT_PER_PAGE = 10;
	
	public ArticleListModel getArticleList(int requestPageNumber) {
		if(requestPageNumber<0) {
			throw new IllegalArgumentException("page number < 0 : "+requestPageNumber);
		}
		ArticleDao articleDao = ArticleDao.getInstance();
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			int totalArticleCount = articleDao.selectCount(conn);
		if(totalArticleCount == 0) {
			return new ArticleListModel();
		}
		
		int totalPageCount = calculateTotalPageCount(totalArticleCount);
		
		int firstRow = (requestPageNumber-1)*COUNT_PER_PAGE+1;
		int endRow = firstRow + COUNT_PER_PAGE - 1;
		
		if(endRow>totalArticleCount) {
			endRow = totalArticleCount;
		}
		List<Article> articleList = articleDao.select(conn, firstRow, endRow);
		ArticleListModel articleListView = new ArticleListModel(articleList, requestPageNumber, totalPageCount, firstRow, endRow);
		return articleListView;
	}catch(SQLException e) {
		throw new RuntimeException("DB 에러 발생 :"+e.getMessage(),e);
	}finally {
		JdbcUtil.close(conn);
	}
	}
		
	private int calculateTotalPageCount(int totalArticleCount) {
		if(totalArticleCount==0) {
			return 0;
		}
		int pageCount = totalArticleCount / COUNT_PER_PAGE;
		if(totalArticleCount % COUNT_PER_PAGE > 0) {
			pageCount++;
		}
		return pageCount;
	}
	
	public ArticleListModel getArticleList(int requestPageNumber, int searchn, String search) {
		if(requestPageNumber<0) {
			throw new IllegalArgumentException("pageNumber < 0 " +requestPageNumber);
		}
		ArticleDao articleDao = ArticleDao.getInstance();
		Connection conn = null;
		try {
			conn=ConnectionProvider.getConnection();
			int totalArticleCount = articleDao.selectCount(conn,requestPageNumber,searchn,search);
			
			// 전체 게시글이 0개인 경우 기본 ArticleListModel 객체를 리턴
			if(totalArticleCount == 0) {
				return new ArticleListModel();
			}
			
			// calculateTotalPageCount() 메서드를 호출해서 전체 페이지 개수를 구한다.
			int totalPageCount = calculateTotalPageCount(totalArticleCount);
			
			// 요청한 페이지 번호를 이용해서 읽어올 행의 시작과 끝 번호를 구한다.
			int firstRow = (requestPageNumber-1) * COUNT_PER_PAGE + 1;
			int endRow = firstRow + COUNT_PER_PAGE - 1;
			if(endRow > totalArticleCount) { endRow = totalArticleCount; }
			
			// articleDao.select() 메서드를 이용해서 시작과 끝 행에 속하는 Article 목록을 구한다.
			List<Article> articleList = articleDao.select(conn, firstRow, endRow, searchn, search);
			
			// Article 목록, 요청 페이지 번호, 전체 페이지 개수, 시작행, 끝행을 이용해서 ArticleListModel 객체 리턴
			ArticleListModel articleListView = new ArticleListModel(articleList, requestPageNumber, totalPageCount, firstRow, endRow);
			return articleListView;
		}catch(Exception e) {
			throw new RuntimeException("DB 에러 발생 : "+e.getMessage(),e);
		}finally {
			JdbcUtil.close(conn);
		}
	}
}
