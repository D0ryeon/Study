<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR" />
<title>로그인</title>
<link href="/SSOLDISK/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body onload="focusIt();">
<!-- before login -->
<%
	if(session.getAttribute("memId") == null) {
%>
<script type="text/javascript">
	function focusIt() {
		document.myform.id.focus();
	}
	function checkIt() {
		/* inputForm = eval("document.inform");
		if(!inputForm.id.value){
			alert("아이디를 입력하세요!");
			inputForm.id.focus();
			return false;
		}
		if(!inputForm.passwd.value){
			alert("비밀번호를 입력하세요!");
			inputForm.passwd.focus();
			return false;
		} */
		
		if(!document.myform.id.value){
			alert("아이디를 입력하세요!");
			document.myform.id.focus();
			return false;
		}
		if(!document.myform.passwd.value){
			alert("비밀번호를 입력하세요!");
			document.myform.passwd.focus();
			return false;
		}
	}
</script>

<div id="loginWrap">
	<h1>Member Login</h1>
	<form action="loginPro.jsp" method="post" name="myform" onsubmit="return checkIt();">
		<fieldset>
			<legend>로그인</legend>
			<dl>
				<dt>Id</dt>
				<dd><input type="text" name="id" maxlength="10" /></dd>
				<dt>Password</dt>
				<dd><input type="password" name="passwd" maxlength="10" /></dd>
			</dl>
			<div class="btnArea">
				<input type="submit" name="Submit" value="Ok" />
				<input type="reset" value="Reset" />
				<input type="button" value="Join" onclick="javascript:window.location='inputForm.jsp'" />
			</div>
		</fieldset>
	</form>
</div>

<%
	} else {
%>

<!-- after login -->
<div id="loginWrap">
	<h1>Welcome to my world!</h1>
	<div class="afterLogin">
		<p><strong><%= session.getAttribute("memId") %></strong>님 환영합니다.</p>
		<form action="logut.jsp" method="post">
			<div class="btnArea">
				<input type="submit" name="Submit" value="Logout" />
				<input type="button" value="Modify" onclick="javascript:window.location='modify.jsp'" />
			</div>
		</form>
	</div>
</div>

<% } %>
</body>
</html>