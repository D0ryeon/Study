<%@ page language="java" contentType="text/html; charset=euc-kr"
    pageEncoding="euc-kr" import = "java.net.URLEncoder" %>
<%
	Cookie cookie = new Cookie("name",URLEncoder.encode("최범균","euc-kr"));
	response.addCookie(cookie);
%>
<!DOCTYPE html>
<html>
<head>
<title>쿠키 생성</title>
</head>
<body>
<%= cookie.getName() %> 쿠키의 값 ="<%= cookie.getValue() %>"
</body>
</html>