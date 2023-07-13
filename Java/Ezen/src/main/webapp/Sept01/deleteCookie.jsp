<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.URLEncoder" %>
<%
	Cookie[] cookies = request.getCookies();
	if(cookies != null && cookies.length > 0){
	for(int i=0;i<cookies.length;i++){
		if(cookies[i].getName().equals("path3")){
			Cookie cookie = new Cookie("path3","");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}
	}
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>쿸키 삭제</title>
</head>
<body>
name 쿠키를 삭제한다.
</body>
</html>