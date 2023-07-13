<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.CommentDBBean" %>
<%@ page import = "java.sql.Timestamp" %>
<%
	request.setCharacterEncoding("UTF-8");

	int content_num=Integer.parseInt(request.getParameter("content_num"));
	int comment_num=Integer.parseInt(request.getParameter("comment_num"));

	String pageNum=request.getParameter("p_num");
	String passwd=request.getParameter("passwd");
			
	CommentDBBean cmtPro=CommentDBBean.getInstance();
	int check=cmtPro.deleteComment(content_num, passwd, comment_num);
	
	if(check==1){
%>
	<meta http-equiv=Refresh content="0;url=content1.jsp?num=<%=content_num%>&pageNum=<%=pageNum%>">

<% } else { %>

<%-- 	<script language="javascript">
		alert("비밀번호가 맞지 않습니다.");
		history.go(-1);
	</script>
<%} %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<body>
<%=content_num%>
<%=comment_num%>
<%=pageNum%>
<%=passwd%>
<% } %>
</body>
</html>