<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="logon.*" %>
<%@ include file="color.jsp" %>
<%
	request.setCharacterEncoding("UTF-8");
	String name = request.getParameter("name");
	String jumin1 = request.getParameter("jumin1");
	String jumin2 = request.getParameter("jumin2");
	System.out.println(name);
	System.out.println(jumin1);
	System.out.println(jumin2);
	
	LogonDBBean manager = LogonDBBean.getInstance();
	
	String id = request.getParameter("id");
	
	LogonDataBean member = manager.findId(name,jumin1,jumin2);
	
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
	<font size><b> 아이디를 찾았습니다. </b></font></td>
</tr>
<tr>
	<td width="200">아이디</td>
	<td width="300"><%=member.getId() %></td>
</tr>
</table>
</body>
</html>
<% }else{ %>
<script>
alert("아이디가 없습니다.")
history.go(-1)
</script>
<% } %>