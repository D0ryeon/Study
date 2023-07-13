package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.PdsItemDao;
import model.AddRequest;
import model.PdsItem;
import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;

public class AddPdsItemService {

	private static AddPdsItemService instance = new AddPdsItemService();
	public static AddPdsItemService getInstance() {
		return instance;
	}
	
	private AddPdsItemService() {}
	
	public PdsItem add(AddRequest request) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			PdsItem pdsItem = request.toPdsItem();
			int id = PdsItemDao.getInstance().insert(conn, pdsItem);
			if(id == -1) {
				JdbcUtil.rollback(conn);
				throw new RuntimeException("DB 삽입 안됨");
			}
			pdsItem.setId(id);
			
			conn.commit();
			
			return pdsItem;
		}catch(SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		}finally {
			if(conn!=null) {
				try {conn.setAutoCommit(true);}catch(SQLException e) {}
			}
			JdbcUtil.close(conn);
		}
	}
}
