<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.LogonDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="member" class="logon.LogonDataBean">
	<jsp:setProperty name="member" property="*" />
</jsp:useBean>

<%
	member.setReg_date(new Timestamp(System.currentTimeMillis()));
	LogonDBBean manager = LogonDBBean.getInstance();
	manager.insertMember(member);
	
	response.sendRedirect("loginForm.jsp");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>