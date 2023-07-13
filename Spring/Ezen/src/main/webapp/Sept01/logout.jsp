<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "util.CookieBox" %>
<%
	response.addCookie(CookieBox.createCookie("LOGIN", "","/",0));
	response.addCookie(CookieBox.createCookie("ID","","/",0));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그아웃</title>
</head>
<body>
로그아웃하였습니다.
</body>
</html>