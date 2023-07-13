<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="logon.*"%>

<%
	request.setCharacterEncoding("euc-kr");
%>
<jsp:useBean id="member" class="logon.LogonDataBean">
	<jsp:setProperty name="member" property="*" />	
</jsp:useBean>

<%
	String id = (String) session.getAttribute("memId");
	member.setId(id);
	
	LogonDBBean manager = LogonDBBean.getInstatnce();
	manager.updateMember(member);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR" />
<title>회원정보 수정완료</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="memberWrap">
	<div class="innerDiv">
		<h1>Modify</h1>
		<p class="desc">회원정보가 수정되었습니다.</p>
		<form>
			<div class="btnArea2"><input type="button" value="Go to home" onclick="javascript:window.location='main.jsp'" class="btn" /></div>
		</form>
		<!-- <p>5초후에 메인으로 이동합니다.<meta http-equiv="Refresh" content="5;url=main.jsp"></p> -->
	</div>
</div>
</body>
</html>