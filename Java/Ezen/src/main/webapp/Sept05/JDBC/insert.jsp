<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.DriverManager" %>  
<%@ page import = "java.sql.Connection" %>  
<%@ page import = "java.sql.PreparedStatement" %>  
<%@ page import = "java.sql.SQLException" %> 
<%
	request.setCharacterEncoding("UTF-8");
	
	String id=request.getParameter("memberID");
	String pw=request.getParameter("password");
	String name=request.getParameter("name");
	String email=request.getParameter("email");
	
	Class.forName("oracle.jdbc.driver.OracleDriver");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	try {
		String jdbcDriver = "jdbc:oracle:thin:@localhost:1521:XE";
		String dbUser = "EZ";
		String dbPass = "oracle";
		
	conn = DriverManager.getConnection(jdbcDriver,dbUser,dbPass);
	pstmt = conn.prepareStatement("insert into MEMBER values(?,?,?,?)");
	pstmt.setString(1,id);
	pstmt.setString(2,pw);
	pstmt.setString(3,name);
	pstmt.setString(4,email);
	
	pstmt.executeUpdate();
	
	
	} finally {
		if(pstmt != null)try{pstmt.close();}catch(SQLException ex){}
		if(conn != null)try{conn.close();} catch(SQLException ex) {}
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>테이블 삽입</title>
</head>
<body>
MEMBER 테이블에 새로운 레코드를 삽입했습니다.
<% 
response.sendRedirect("./viewMemberList.jsp");
%>
</body>
</html>