package service;

import java.sql.Connection;
import java.sql.SQLException;
import dao.ArticleDao;
import model.Article;

public class ArticleCheckHelper {
	public Article checkExistsAndPassword(Connection conn, int articleId, String password)throws SQLException,ArticleNotFoundException,InvalidPasswordException{
		
		// 게시글 체크
		
		ArticleDao articleDao = ArticleDao.getInstance();
		Article article = articleDao.selectById(conn, articleId);
		// articleId 가 null이면
		if(article==null) {
			throw new ArticleNotFoundException("게시글이 존재하지 않습니다 : "+articleId);
		}// password 확인 틀리면 아래 구문이 나온다.
		if(!checkPassword(article.getPassword(),password)){
			throw new InvalidPasswordException("잘못된 암호");
		}// 맞으면 내보냄
		return article;
	}
	
	// 패스워드 체크 메소드
	// 유저가 입력한 패스워드와 게시글에 등록되어있는 비밀번호를 비교해서 맞는지 비교
	private boolean checkPassword(String realPassword,String userInputPassword) {
		// 게시글에 등록되어 있는 비밀번호가 null 경우 false
		if(realPassword==null) {
			return false;
		}
		// 게시글에 등록되어 있는 비밀번호의 length가 0일경우 false
		if(realPassword.length()==0) {
			return false;
		}
		// 둘을 비교해서 true false로 답을 리턴함
		return realPassword.equals(userInputPassword);
	}
}
