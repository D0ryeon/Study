<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.CommentDBBean" %>
<%@ page import = "java.sql.Timestamp" %>
<%
	request.setCharacterEncoding("UTF-8");
%>
<jsp:useBean id="cmt" scope="page" class="logon.CommentDataBean">
<jsp:setProperty name="cmt" property="*"/>
</jsp:useBean>
<%
	CommentDBBean comt=CommentDBBean.getInstance();
	cmt.setReg_date(new Timestamp(System.currentTimeMillis()));
	cmt.setIp(request.getRemoteAddr());
	comt.insertCommnet(cmt);
	
	String content_num=request.getParameter("content_num");
	String p_num=request.getParameter("p_num");
	String url="content1.jsp?num="+content_num+"&pageNum="+p_num;
	response.sendRedirect(url);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<body>

</body>
</html>