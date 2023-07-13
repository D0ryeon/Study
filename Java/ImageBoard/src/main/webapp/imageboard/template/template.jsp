<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page errorPage="../error/error_view.jsp" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>테마 갤러리</title>
<style>
A{color: blue; font-weight: bold; text-decoration:none}
A:hover{color:blue; font-weight:bole; text-decoration:underline}
</style>
</head>
<body>
<table width="100%" border="1" cellpadding="2" cellspacing="0">
<tr>
	<td>
		<jsp:include page="../module/top.jsp" flush="false"/>
	</td>
</tr>	
<tr>
	<td>
		<jsp:include page="${param.CONTENTPAGE }" flush="false"/>
	</td>
</tr>
<tr>
	<td>
		<jsp:include page="../module/bottom.jsp" flush="false"/>
	</td>
</tr>
				
</table>

</body>
</html>