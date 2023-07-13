<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문서의 제목</title>
</head>
<body>
<%
	String bookTitle = "문도 JSP";
	String author = "문도";
%>
<b><%= bookTitle %></b>(<%= author %>)입니다.
</body>
</html>