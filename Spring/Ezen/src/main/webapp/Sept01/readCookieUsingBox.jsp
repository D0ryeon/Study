<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="util.CookieBox" %>
<%
	CookieBox cookieBox = new CookieBox(request);
%>  
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>쿸끼빢쓰 사용</title>
</head>
<body>
name꾺끼 = <%= cookieBox.getValue("name") %><br>
<% if (cookieBox.exists("id")){ %>
id 쿠키 = <%= cookieBox.getValue("id") %><br>
<%} %>
</body>
</html>