<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="javax.servlet.ServletException" %>
<%@page isErrorPage="true" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>에러 발생</title>
</head>
<body>

에러가 발생하였습니다.<br/><br/>
에러 메시지:<%=exception.getMessage() %>
<%exception.printStackTrace(); %>
<p>
<%
	Throwable rootCause=null;
	if(exception instanceof ServletException){
		rootCause=((ServletException)exception).getRootCause();
	}else{
		rootCause=exception.getCause();
	}
	if(rootCause!=null){
		rootCause.printStackTrace();
			do{
%>
예외 추적:<%=rootCause.getMessage() %><br>				
<%
			rootCause=rootCause.getCause();
			}while(rootCause!=null);
	}
%>

</body>
</html>