package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.MessageDao;
import dao.MessageDaoProvider;
import model.Message;
import jdbc.JdbcUtil;
import jdbc.ConnectionProvider;

public class WriteMessageService {

	private static WriteMessageService instance = new WriteMessageService();
	
	public static WriteMessageService getInstance() {
		return instance;
	}
	
	public void write(Message message) throws ServiceException{
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			MessageDao messageDao = MessageDaoProvider.getInstance().getMessageDao();
			messageDao.insert(conn, message);
		}catch(SQLException e) {
			throw new ServiceException("메시지 등록 실패 : "+e.getMessage(),e);
		}finally {
			loader.JdbcUtil.close(conn);
		}
	}

}
