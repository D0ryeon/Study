package service;

import java.sql.Connection;
import java.sql.SQLException;
import dao.PdsItemDao;
import jdbc.JdbcUtil;
import jdbc.ConnectionProvider;

public class IncreaseDownloadCountService {
	private static IncreaseDownloadCountService instace = new IncreaseDownloadCountService();
	
	public static IncreaseDownloadCountService getInstance() {
		return instace;
	}
	
	private IncreaseDownloadCountService() {}
	
	public boolean increaseCount(int id) {
		Connection conn = null;
		try {
			conn=ConnectionProvider.getConnection();
			int updatedCount = PdsItemDao.getInstance().increaseCount(conn, id);
			return updatedCount == 0 ? false : true;
		} catch(SQLException e) {
			throw new RuntimeException("DB처리 에러 발생 : "+e.getMessage(),e);
		}finally {
			JdbcUtil.close(conn);
		}
	}
}
