<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.BoardDataBean" %>
<%@ page import = "logon.BoardDBBean" %>
<%@ include file="color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 수정</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="script.js"></script>
</head>
<%
	int num = Integer.parseInt(request.getParameter("num"));
	String pageNum = request.getParameter("pageNum");
	try{
		BoardDBBean dbPro = BoardDBBean.getInstance();
		BoardDataBean article = dbPro.updateGetArticle(num);
%>
<body bgcolor="<%=bodyback_c%>">
<center><b>글수정</b></center>
<br>
<form method="post" name="writeform" action="updatePro.jsp?pageNum=<%=pageNum %>" onsubmit="return witeSave()">
<table width="400" border="1" cellspacing="0" cellpadding="0" bgcolor="<%=bodyback_c %>" align="center">
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">이 름</td>
	<td align="left" width="330">
	<input type="text" size="10" maxlength="10" name="writer" value="<%= article.getWriter() %>">
	<input type="hidden" name="num" value="<%=article.getNum() %>">
	</td>
</tr>
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">제 목</td>
	<td align="left" width="330">
	<input type="text" size="40" maxlength="50" name="subject" value="<%= article.getSubject() %>">
	</td>
</tr>
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">email</td>
	<td align="left" width="330">
	<input type="text" size="40" maxlength="30" name="email" value="<%= article.getEmail() %>">
	</td>
</tr>
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">내 용</td>
	<td align="left" width="330">
	<textarea name="content" rows="13" cols="40" ><%= article.getContent() %></textarea>
	</td>
</tr>
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">비밀번호</td>
	<td align="left" width="330">
	<input type="password" size="8" maxlength="12" name="passwd">
	</td>
</tr>
<tr>
	<td colspan="2" align="center" bgcolor="<%=value_c%>">
	<input type="submit" value="글수정">
	<input type="reset" value="다시작성">
	<input type="button" value="목록보기" onclick="document.location.href='list.jsp?pageNum=<%=pageNum %>'">
	</td>
</tr>
</table>
</form>
<%
	}catch(Exception e){}
%>
</body>
</html>