<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>
<% request.setCharacterEncoding("euc-kr"); %>
<jsp:useBean id="regBean" class="member.RegisterBean"></jsp:useBean>
<jsp:setProperty name="regBean" property="*"></jsp:setProperty>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 확인</title>
<link href="style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="<%= bodyback_c %>">
	<table border="1" cellpadding="5" cellspacing="0" width="800">
		<colgroup>
			<col width="20%" />
			<col width="*" />

		</colgroup>
		<tr>
			<td colspan="3"><strong>회원가입 확인</strong></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">아이디</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="id"></jsp:getProperty></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">비밀번호</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="passwd"></jsp:getProperty></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">비밀번호 확인</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="repasswd"></jsp:getProperty></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이름</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="name"></jsp:getProperty></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이메일</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="email"></jsp:getProperty></td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">전화번호</th>
			<td bgcolor="<%= value_c %>"><jsp:getProperty name="regBean" property="tel"></jsp:getProperty></td>
		</tr>

	</table>
</body>
</html>