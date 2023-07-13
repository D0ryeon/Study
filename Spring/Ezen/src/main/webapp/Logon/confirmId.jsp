<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="logon.LogonDBBean" %>
<%@ include file = "color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ID 중복확인</title>
<link href="style.css" rel="stylesheet" type="text/css">
<% 
request.setCharacterEncoding("UTF-8");

String id = request.getParameter("id");
LogonDBBean manager = LogonDBBean.getInstance();
	int check = manager.confirmId(id);
%>
</head>
<body bgcolor= "<%= bodyback_c %>">
<% if(check == 1 ){ %>
<table width="270" border="0" cellspacting="0" cellpadding="5">
	<tr bgcolor="<%= title_c %>">
		<td height="39"> <%= id %>이미 사용중인 아이디입니다.</td>
	</tr>
</table>
<form name="checkForm" method="post" action="confirmId.jsp">
<table width="270" border="0" cellspacting="0" cellpadding="5">
	<tr>
		<td bgcolor="<%= value_c %>" align="center">
			<p>다른 아이디를 선택하세요.</p>
			<input type="text" size="10" maxlength="12" name="id">
			<input type="submit" value="ID중복확인"></td>
	</tr>
</table>
</form>
<% } else { %>
<table width="270" border="0" cellspacting="0" cellpadding="5">
	<tr bgcolor="<%= title_c %>">
		<td align="center">
		<p>입력하신 <%= id %>는 사용가능한 아이디입니다.</p>
		<input type="button" value="닫기" onclick="setid()"></td>
	</tr>
</table>
<% } %>
</body>
</html>

<script language="javascript">
function setid(){
	opener.document.userinput.id.value="<%= id %>";
	self.close();
}
</script>
