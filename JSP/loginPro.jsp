<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="logon.LogonDBBean" %>

<%
	request.setCharacterEncoding("euc-kr");
%>

<%
	String id = request.getParameter("id");
	String passwd = request.getParameter("passwd");
	
	LogonDBBean manager = LogonDBBean.getInstatnce();
	int check = manager.userCheck(id, passwd);
	
	if(check == 1){
		session.setAttribute("memId", id);
		response.sendRedirect("main.jsp");
	}else if(check == 0){
%>
<script type="text/javascript">
	alert("비밀번호가 맞지 않습니다.");
	history.go(-1);
</script>
<%
	}else {
%>			
<script type="text/javascript">
	alert("아이디가 맞지 않습니다.");
	history.go(-1);
</script>
<% } %>
