<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.LogonDBBean" %>
<% 
request.setCharacterEncoding("UTF-8"); 
	
String id		= request.getParameter("id");
String passwd 	= request.getParameter("passwd");

LogonDBBean manager = LogonDBBean.getInstance();
int check = manager.userCheck(id, passwd);

if(check==1){
session.setAttribute("memId", id);
response.sendRedirect("main.jsp");
}else if(check==0){ 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	alert("비밀번호가 맞지 않습니다.");
	history.go(-1);
</script>
<% }else{ %>
<script>
	alert("아이디가 맞지 않습니다.");
	history.go(-1);
</script>
<% } %>
</head>
<body>

</body>
</html>