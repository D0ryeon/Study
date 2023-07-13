<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link href="style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="<%= bodyback_c %>">
<div align="center">
<form action="registerPro.jsp" name="regForm" method="post" onsubmit="return inputCheck()">
	<table border="0" cellpadding="5" cellspacing="0" width="800">
		<colgroup>
			<col width="20%" />
			<col width="*" />
			<col width="30%" />
		</colgroup>
		<tr>
			<td colspan="3"><strong>회원가입</strong></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">아이디</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="id" size="20" /></td>
			<td bgcolor="<%= value_c %>">아이디를 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">비밀번호</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="passwd" size="20" /></td>
			<td bgcolor="<%= value_c %>">비밀번호를 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">비밀번호 확인</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="repasswd" size="20" /></td>
			<td bgcolor="<%= value_c %>">비밀번호를 한번 더 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이름</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="name" size="20" /></td>
			<td bgcolor="<%= value_c %>">이름을 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이메일</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="email" size="30" /></td>
			<td bgcolor="<%= value_c %>">이메일을 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">전화번호</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="tel" size="20" /></td>
			<td bgcolor="<%= value_c %>">전화번호를 입력하세요.</td>
		</tr>
		<tr>
			<td colspan="3" height="50" align="center">
			<input type="submit" value="회원가입" />
			<input type="reset" value="다시작성" /></td>
		</tr>
	</table>
</form>
</div>
</body>
</html>