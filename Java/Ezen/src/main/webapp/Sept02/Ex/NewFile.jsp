1./webapp/jB/color.jsp
<%
	String bodyback_c	= "#e0ffff";
	String back_c		= "#8fbc8f";
	String title_c		= "#5f9ea0";
	String value_c		= "#b0e0e6";
	String bar			= "#778899";
%>

2./webapp/jB/style.css
a:link,a:visited,a:active {
	text-decoration: none; 
}

a:hover {
	text-decoration: underline;
	color: red;
}

TABLE,TD,TR,TH {
	font-size: 9pt;
}

p {
	font-size: 9pt;
}

3./webapp/jB/script.js

function inputCheck() {
	var userinput = eval("document.regForm");
	if(userinput.id.value == "") {
		alert("아이디를 입력하세요.");
		userinput.id.focus();
		return false;
	}
	if(userinput.passwd.value == "") {
		alert("비밀번호를 입력하세요.");
		userinput.passwd.focus();
		return false;
	}
	if(userinput.repasswd.value == "") {
		alert("비밀번호를 재입력하세요.");
		userinput.repasswd.focus();
		return false;
	}
	if(userinput.name.value == "") {
		alert("이름을 입력하세요.");
		userinput.name.focus();
		return false;
	}
	if(userinput.email.value == "") {
		alert("이메일을 입력하세요.");
		userinput.email.focus();
		return false;
	}
	if(userinput.tel.value == "") {
		alert("연락처를 입력해 주세요.");
		userinput.tel.focus();
		return false;
	}
	if(userinput.passwd.value != userinput.repasswd.value) {
		alert("비밀번호가 일치하지 않습니다.");
		userinput.repasswd.focus();
		return false;
	}
}  

4.RegisterBean.java
 package ssol.member;

public class RegisterBean {
	private String id;
	private String passwd;
	private String repasswd;
	private String name;
	private String email;
	private String tel;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRepasswd() {
		return repasswd;
	}
	public void setRepasswd(String repasswd) {
		this.repasswd = repasswd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}

 5./WebContent/jB/registerForm.jsp
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ include file="color.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>회원가입</title>
<link href="style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="<%= bodyback_c %>">
<form action="registerPro.jsp" name="regForm" method="post" onsubmit="return inputCheck()">
	<table border="1" cellpadding="5" cellspacing="0" width="800">
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
			<th bgcolor="<%= title_c %>">비밀번호</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="repasswd" size="20" /></td>
			<td bgcolor="<%= value_c %>">비밀번호를 재입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이름</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="name" size="20" /></td>
			<td bgcolor="<%= value_c %>">이름을 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">이메일</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="email" size="30" /></td>
			<td bgcolor="<%= value_c %>">이메일 입력하세요.</td>
		</tr>
		<tr>
			<th bgcolor="<%= title_c %>">전화번호</th>
			<td bgcolor="<%= value_c %>"><input type="text" name="tel" size="20" /></td>
			<td bgcolor="<%= value_c %>">전화번호 입력하세요.</td>
		</tr>
		<tr>
			<td colspan="3" height="50" align="center"><input type="submit" value="회원가입" /> <input type="reset" value="다시작성" ></td>
		</tr>
	</table>
</form>
</body>
</html>
 
6./WebContent/jB/registerPro.jsp
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ include file="color.jsp" %>
<% request.setCharacterEncoding("euc-kr"); %>
<jsp:useBean id="regBean" class="ssol.member.RegisterBean"></jsp:useBean>
<jsp:setProperty name="regBean" property="*"></jsp:setProperty>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
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
		<th bgcolor="<%= title_c %>">비밀번호</th>
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
 
