package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.ArticleDao;
import model.DeleteRequest;
import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;

public class DeleteArticleService {
	// 객체 생성 싱글톤
	private static DeleteArticleService instance = new DeleteArticleService();
	// 생성한 객체를 꺼내쓰는 메소드
	public static DeleteArticleService getInstance() {
		return instance;
	}
	
	// 이 클래스의 생성자
	private DeleteArticleService() {}
	
	
	// 게시글 삭제 메소드
	public void deleteArticle(DeleteRequest deleteRequest)throws ArticleNotFoundException,InvalidPasswordException{
		// 커넥션 변수를 만들어줌
		Connection conn = null;
		try {
			// 커넥션 변수로 ConnectionProvider의 커넥션 메소드를 불러와 db에 접속
			conn = ConnectionProvider.getConnection();
			// 오토커밋 끔
			conn.setAutoCommit(false);
			// 게시글 체크용 객체를 생성
			ArticleCheckHelper checkHelper = new ArticleCheckHelper();
			// 삭제할 게시글을 확인한다 비밀번호가 맞는지 확인
			checkHelper.checkExistsAndPassword(conn, deleteRequest.getArticleId(),deleteRequest.getPassword());
			// articleDao 객체 생성
			ArticleDao articleDao = ArticleDao.getInstance();
			// 게시글을 삭제함
			articleDao.delete(conn, deleteRequest.getArticleId());
			//커밋
			conn.commit();

		} catch(SQLException e) {
			// 오류뜨면 돌아감
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch(ArticleNotFoundException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} catch(InvalidPasswordException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			// 커넥션이 비어있지 않으면
			if(conn != null) {
				try {
					// 오토커밋을 트루로 바꿈
					conn.setAutoCommit(true);
				}catch(SQLException e) {}
			}
			JdbcUtil.close(conn); // 커넥션 객체를 클로즈함
		}
	}

}
