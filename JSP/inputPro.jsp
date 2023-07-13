<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="logon.LogonDBBean" %>
<%@ page import="java.sql.*" %>

<%
	request.setCharacterEncoding("euc-kr");
%>
<jsp:useBean id="member" class="logon.LogonDataBean">
	<jsp:setProperty name="member" property="*" />
</jsp:useBean>

<%
	member.setReg_date(new Timestamp(System.currentTimeMillis()));
	LogonDBBean manager = LogonDBBean.getInstatnce();
	manager.insertMember(member);
	
	response.sendRedirect("loginForm.jsp");
%>