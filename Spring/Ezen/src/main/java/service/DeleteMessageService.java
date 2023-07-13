package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.MessageDao;
import dao.MessageDaoProvider;
import model.Message;
import jdbc.JdbcUtil;
import jdbc.ConnectionProvider;

public class DeleteMessageService {
	// 객체생성 싱글톤
	private static DeleteMessageService instance = new DeleteMessageService();
	
	// 객체 호출
	public static DeleteMessageService getInstance() {
		return instance;
	}
	// 생성자
	private DeleteMessageService() {}
	
	// 삭제 메세지 메소드
	public void deleteMessage(int messageId, String password)throws ServiceException,InvalidMessagePassowrdException,MessageNotFoundException{
		Connection conn = null;
		try {
			// db연결
			conn = ConnectionProvider.getConnection();
			// 오토커밋 off
			conn.setAutoCommit(false);
			
			// MessageDao 객체를 생성
			MessageDao messageDao = MessageDaoProvider.getInstance().getMessageDao();
			Message message = messageDao.select(conn, messageId);
			if(message == null) {
				throw new MessageNotFoundException("메시지가 없습니다 : "+messageId);
			}
			if(!message.hasPassword()) {
				throw new InvalidMessagePassowrdException();
			}
			if(!message.getPassword().equals(password)) {
				throw new InvalidMessagePassowrdException();
			}
			messageDao.delete(conn, messageId);
			
			conn.commit();
		}catch(SQLException ex) {
			JdbcUtil.rollback(conn);
			throw new ServiceException("삭제 처리 중 에러가 발생했습니다 : " + ex.getMessage(),ex);
		}catch(InvalidMessagePassowrdException ex) {
			JdbcUtil.rollback(conn);
			throw ex;
		}catch(MessageNotFoundException ex) {
			JdbcUtil.close(conn);
			throw ex;
		} finally {
			if(conn != null) {
				try {
					conn.setAutoCommit(false);
					
				}catch(SQLException e) {}
				JdbcUtil.close(conn);
			}
		}
	}
}