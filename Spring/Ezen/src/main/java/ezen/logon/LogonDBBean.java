package ezen.logon;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class LogonDBBean {

	//SingleTon Pattern : LogonDBBean m = LogonDBBean.getInstance();
	private static LogonDBBean instance = new LogonDBBean();
	
	public static LogonDBBean getInstance() {
		return instance;
	}
	
	private LogonDBBean() {}
	
	
	
	private Connection getConnection() throws Exception {
		String jdbcDriver = "jdbc:apache:commons:dbcp:/pool";
		return DriverManager.getConnection(jdbcDriver);
	}
	
	public void insertMember(LogonDataBean member)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("insert into MEMBERS(id,passwd,name,jumin1,jumin2,email,blog,reg_date,zipcode,address) values (?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getJumin1());
			pstmt.setString(5, member.getJumin2());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getBlog());
		}catch(aa) {
			
		}
	}
	
	
}
