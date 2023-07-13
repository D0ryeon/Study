<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.util.Date" %>
<%
	Date now = new Date();
%>
<!DOCTYPE html>
<html>
<head><title>현재 시간</title></head>
<body>
현재 시각:  
<%= now %>
</body>
</html>