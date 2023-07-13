<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "util.CookieBox" %>
<%
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	
	if(id.equals(password)){
		// ID와 암호가 같으면 로그인에 성공한것으로 판단.
		response.addCookie(CookieBox.createCookie("LOGIN","SUCCESS","/",-1));
		response.addCookie(CookieBox.createCookie("ID", id,"/",-1));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 성공</title>
</head>
<body>
로그인에 성공했습니다.
</body>
</html>
<%
  }else{//로그인 실패시
%>
<script>
alert("로그인에 실패하였습니다.")
history.go(-1);
</script>
<% } %>