<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>  
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@page import="madvirus.gallery.Theme" %>
<%@page import="madvirus.gallery.ThemeManager" %>
<%@page import="madvirus.gallery.ThemeManagerException" %>
<%
	String themeId= request.getParameter("id");
	ThemeManager manager=ThemeManager.getInstance();
	Theme theme=manager.select(Integer.parseInt(themeId));
%> 
<c:set var="theme" value="<%=theme %>"/>
<c:if test="${!empty theme }">
<script language="JavaScript">
function validate(form){
	if(form.password.value==""){
		alert("��ȣ�� �Է��ϼ���,");
		return false;
	}
}
</script>

<form action="delete.jsp" method="post" onsubmit="return validate(this)">
<input type="hidden" name="id" value="${theme.id }">
<table width="100%" border="1" cellpadding="1" cellspacing="0">
<tr>
	<td>����</td>
	<td>${theme.title }</td>
</tr>
<tr>
	<td>�ۼ���</td>
	<td>${theme.name }</td>
</tr>
<tr>
	<td>��й�ȣ</td>
	<td><input type="password" name="password" size="10" value=""></td>
</tr>
<c:if test="${!empty theme.image }">
<tr>
	<td>�̹���</td>
	<td>
	<img src="/imageboard/image/${theme.image }" width="150" border="0">
	</td>
</tr>
</c:if>
<tr>
	<td colspan="2">
	<input type="submit" value="����">
	<input type="button" value="���" onclick="javascript:history.go(-1)">
	</td>
</tr>					
</table>
</form>
</c:if>

<c:if test="${empty theme }">
���� �������� �ʽ��ϴ�.
</c:if>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

</body>
</html>