<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="util.CookieBox" %>
<%
	response.addCookie(CookieBox.createCookie("name","최범균"));
	response.addCookie(CookieBox.createCookie("id","madvirus","/EZ/Sept01",-1));

	
%>  
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>쿸끼빢쓰</title>
</head>
<body>
CookieBox를 사용하여 쿠키 생ㅅ어
</body>
</html>