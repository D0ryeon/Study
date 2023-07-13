<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="logon.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR" />
<title>회원탈퇴</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function begin(){
		document.myform.passwd.focus();
	}
	function checkIt() {
		if(!document.myform.passwd.value){
			alert("비밀번호를 입력하세요!");
			dpcument.myform.passwd.focus();
			return false;
		}	
	}
</script>
</head>
<body onload="begin()">
<div id="loginWrap">
<%
	String id = (String) session.getAttribute("memId");
	LogonDBBean manager = LogonDBBean.getInstatnce();
	LogonDataBean c = manager.getMember(id);
	
	try {
%>
	<h1>Member secession</h1>
	<form name="myform" action="deletePro.jsp" method="post" onsubmit="return checkIt()">
		<fieldset>
			<legend>회원탈퇴</legend>
			<dl>
				<dt>Id</dt>
				<dd><span class="spot"><%= c.getId() %></span></dd>
				<dt>Password</dt>
				<dd><input type="password" name="passwd" maxlength="10" /></dd>
			</dl>
			<div class="btnArea">
				<input type="submit" name="Submit" value="Ok" />
				<input type="button" value="Cancel" onclick="javascript:window.location='main.jsp'" />
			</div>
		</fieldset>
	</form>
</div>
<% } catch(Exception e) {} %>
</body>
</html>