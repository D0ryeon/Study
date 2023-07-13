<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="util.CookieBox" %>
<%
Cookie[] cookies = request.getCookies();

String lastId ="";
if(cookies == null) {
	for (Cookie cookie : cookies) {
		if(cookie.getName().equals("idCheck")) {
			lastId= cookie.getValue();
		}
	}
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 환영</title>
</head>
<body>
<form action="./sessionLogin.jsp" method="post">
아이디 <input type="text" name="id" size="10" value=<%= lastId %>>
아이디 저장<input class="form-check-input" type="checkbox" name="idCheck">
<br>
암호 <input type="password" name="password" size="10">
<input type="submit" value="로그인">

</form>
</body>
</html>