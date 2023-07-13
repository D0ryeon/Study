<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	request.setCharacterEncoding("EUC-KR");
%>
<jsp:forward page="./template/template.jsp">
<jsp:param name="CONTENTPAGE" value="../read_view.jsp"/>
</jsp:forward>
