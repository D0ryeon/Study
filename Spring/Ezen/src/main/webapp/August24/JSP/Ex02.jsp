<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date" %>
    <!-- 인코딩에 iso-8859-1을 넣고 영어 외 다른문자를 쓰면 깨짐
 		 이유는 모든 언어를 1byte처리를 하는데 한국어나 그외 다른언어는
 		 2byte 이상의 처리용량이 필요하기 때문   
     -->
<%
	Date now = new Date();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문도 시계</title>
</head>
<body>
현재 시각 : 
<%= now %>
</body>
</html>