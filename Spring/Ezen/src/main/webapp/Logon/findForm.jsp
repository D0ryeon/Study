<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 패스워드 찾기</title>
</head>
<body bgcolor="<%=bodyback_c%>">

<table width="500" border="1" cellspacing="0" cellpadding="5" align="center">
	<form method="post" action="findIdPro.jsp">
	<tr>
		<td colspan="2" height="39" align="center" bgcolor="<%=title_c %>">
		<font size="+1"><b>ID/PW 찾기</b></font></td>
	</tr>
	<tr>
		<td colspan="2" width="200" bgcolor="<%=value_c%>">
		<b>주민등록 번호로 ID 찾기</b></td> 
	</tr>
	<tr>
		<td width="200">이름</td>
		<td width="300">
		<input type="text" name="name" size="15" maxlength="10">
		</td>
	</tr>
	<tr>
		<td width="200">주민등록번호</td>
		<td width="300">
		<input type="text" name="jumin1" size="10" maxlength="6">-
		<input type="text" name="jumin2" size="10" maxlength="7"></td>
	</tr>
	<tr>
		<td colspan="2" aling="center" bgcolor="<%=value_c %>">
		<input type="submit" name="confirm" value="찾기">
		<input type="button" value="취소" onclick="javascript:window.location='main.jsp'">
		</td>
	</tr>
	</form>
	<form method="post" action="findPwPro.jsp">
	<tr>
		<td colspan="2" width="200" bgcolor="<%=value_c%>">
		<b>생년월일로 비밀번호 찾기</b></td> 
	</tr>
	<tr>
		<td width="200">ID</td>
		<td width="300">
		<input type="text" name="id" size="10" maxlength="12">
		</td>
	</tr>
	<tr>
		<td width="200">이름</td>
		<td width="300">
		<input type="text" name="name" size="15" maxlength="10">
		</td>
	</tr>
	<tr>
		<td width="200">생년월일</td>
		<td width="300">
		<input type="text" name="jumin1" size="10" maxlength="6">
		</td>
	</tr>
	<tr>
		<td colspan="2" aling="center" bgcolor="<%=value_c %>">
		<input type="submit" name="confirm" value="찾기">
		<input type="button" value="취소" onclick="javascript:window.location='main.jsp'">
		</td>
	</tr>
	</form>
</table>
</form>
</body>
</html>