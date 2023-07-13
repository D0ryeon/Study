<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="logon.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR" />
<title>회원정보 수정</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function checkIt() {
		var userinput = eval("document.userinput");
		if(!userinput.passwd.value){
			alert("비밀번호를 입력하세요!");
			return false;
		}
		if(userinput.passwd.value != userinput.passwd2.value){
			alert("비밀번호를 동일하게 입력하세요!");
			return false;
		}
		if(!userinput.name.value){
			alert("이름을 입력하세요!");
			return false;
		}
		if(!userinput.jumin1.value || !userinput.jumin2.value){
			alert("주민등록번호를 입력하세요!");
			return false;
		}	
	}
	// 우편번호 체크
	function zipCheck(){
		url = "ZipCheck.jsp?check=y";
		open(url,"post","toolbar=no ,width=500 ,height=300 ,directories=no,status=yes,scrollbars=yes,menubar=no");
	}
</script>
</head>
<body>
<%
	String id = (String) session.getAttribute("memId");
	LogonDBBean manager = LogonDBBean.getInstatnce();
	LogonDataBean c = manager.getMember(id);
	try {
%>
<div id="joinWrap">
	<h1>Modify</h1>
	<form action="modifyPro.jsp" name="userinput" method="post" onsubmit="return checkIt()">
		<table>
			<colgroup>
				<col width="25%" />
				<col width="*" />
			</colgroup>
			<tr>
				<th><span class="star">*</span>아이디</th>
				<td><%= c.getId() %></td>
			</tr>
			<tr>
				<th><span class="star">*</span>비밀번호</th>
				<td><input type="password" name="passwd" maxlength="10" value="<%= c.getPasswd() %>" /></td>
			</tr>
			<tr>
				<th><span class="star">*</span>비밀번호 확인</th>
				<td><input type="password" name="passwd2" maxlength="10" value="<%= c.getPasswd() %>" /></td>
			</tr>
			<tr>
				<th><span class="star">*</span>이름</th>
				<td><input type="text" name="name" maxlength="10" value="<%= c.getName() %>" /></td>
			</tr>
			<tr>
				<th><span class="star">*</span>주민등록번호</th>
				<td><%= c.getJumin1() %> - <%= c.getJumin2() %></td>
			</tr>
			<tr>
				<th>주소</th>
				<td>
					<input type="text" name="zipcode" maxlength="7" value="<%= c.getZipcode() %>" class="w50" />
					<span class="zipCheck"><input type="button" onclick="zipCheck()" /></span>
					<span class="addr"><input type="text" name="address" maxlength="70" value="<%= c.getAddress() %>" class="w300" /></span>
				</td>
			</tr>
			<tr>
				<th>E-Mail</th>
				<td>
					<% if(c.getEmail() == null){%>
						<input type="text" name="email" maxlength="50" class="w300" />
					<%		
						} else {
					 %>
					 	<input type="text" name="email" maxlength="50" value="<%= c.getEmail() %>" class="w300" />
					<% } %>
				</td>
			</tr>
			<tr>
				<th>Blog</th>
				<td>
					<% if(c.getBlog() == null){%>
						<input type="text" name="blog" maxlength="50" class="w300" />
					<%		
						} else {
					 %>
					 	<input type="text" name="blog" maxlength="50" value="<%= c.getBlog() %>" class="w300" />
					<% } %>
				</td>
			</tr>
		</table>
		<div class="btnArea">
			<input type="submit" value="Ok" />
			<input type="button" value="Cancel" onclick="javascript:window.location='main.jsp'" />
			<input type="button" value="Member secession" onclick="javascript:window.location='deleteForm.jsp'" />
		</div>
	</form>
	<% } catch(Exception e) {} %>
</div>
</body>
</html>