<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link href="style.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function begin(){
			document.myform.id.focus();
		}
		function checkIt(){
		if(!document.myform.id.value){
			alert("아이디를 입력하세요.");
			return false;
		}
		if(!document.myform.passwd.value){
			alert("비밀번호를 입력하세요.");
			document.myform.passwd.focus();
			return false;
		}
		}
	</script>
</head>
<body onload="begin()" bgcolor="<%= bodyback_c %>">
<form name="myform" action="loginPro.jsp" method="post" onSubmit="return checkIt()">
<table cellSpacing="1" cellPadding="1" width="260" border="1" align="center" >
	<tr height="30">
		<td colspan="2" align="center" bgcolor="<%= title_c %>">
		<strong>회원로그인</strong></td>
	</tr>
	
	<tr height="30">
		<td width="110" bgcolor="<%= title_c %>" align="center">아이디</td>
		<td width="150" bgcolor="<%= value_c %>" align="center">
		<input type="text" name="id" size="15" maxlength="12"></td>
	</tr>
	<tr height="30">
		<td width="110" bgcolor="<%= title_c %>" align="center">비밀번호</td>
		<td width="150" bgcolor="<%= value_c %>" align="center">
		<input type="text" name="passwd" size="15" maxlength="12"></td>
	</tr>
	<tr height="30">
		<td colspan="2" bgcolor="<%= title_c %>" align="center">
		<input type="submit" value="로그인" >
		<input type="reset" value="다시입력" >
		<input type="button" value="회원가입" onclick="javascript:window.location='inputForm.jsp'" >
		</td>
	</tr>
		
</table>
</form>
</body>
</html>