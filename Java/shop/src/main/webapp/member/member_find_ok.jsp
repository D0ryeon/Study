<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id=(String)request.getAttribute("id");
	String passwd=(String)request.getAttribute("passwd");
%>
<!DOCTYPE html>
<html>
<head>
<script>
function windowclose(){
	self.close();
}
</script>
<meta charset="UTF-8">
<title>아이디/비밀번호찾기</title>
</head>
<body>
<table width="450px" height="35px">
	<tr><td align="left">
	<b>아이디/비밀번호 찾기</b>
	</td></tr>
</table>

<table width="440px">
	<thead>검색된 아이디/비밀번호입니다.<br><br><br></thead>
	<tr><td align="left">아이디 : <%=id%></tr>
	<tr><td align="left">비밀번호 : <%=passwd%></tr>
</table>
<table width="450px">
	<tr>
	 <td align="center">
	 	<hr><br><input type="button" value="닫기" onclick="windowclose()">
	 </td>
	</tr>
</table>

</body>
</html>