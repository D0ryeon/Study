<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="logon.*" %>
<%@ include file="color.jsp" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String name = request.getParameter("name");
	String jumin1 = request.getParameter("jumin1");
	
	LogonDBBean manager = LogonDBBean.getInstance();
	
	String passwd = request.getParameter("passwd");
	
	LogonDataBean member = manager.findPasswd(id,name,jumin1);
	
	if(member !=null){
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<table width="500" border="1" cellspacing="0" cellpadding="5" align="center">
<tr>
	<td colspan="2" height="39" align="center" bgcolor="<%=title_c %>">
	<font size><b> 비밀번호를 찾았습니다. </b></font></td>
</tr>
<tr>
	<td width="200">비밀번호</td>
	<td width="300"><%=member.getPasswd() %></td>
</tr>
</table>
</body>
</html>
<% }else{ %>
<script>
alert("정보가 없습니다.")
history.go(-1)
</script>
<% } %>