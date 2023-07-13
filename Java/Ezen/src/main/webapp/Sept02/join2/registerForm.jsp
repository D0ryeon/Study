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
	<br><br>
	<table cellpadding="5" cellspacing="0" width="460" id="main">
		<colgroup>
			<col width="50%" />
			<col width="*" />
		
		</colgroup>
		<tr>
			<td colspan="2" class="header" align="center" >
			<a href="http://www.naver.com" class="h_logo">
			<span class="blind" ><img src="./naver.png" width=250></span>
			</a></td>
		</tr>
		<tr><td><br></td></tr>
			<tr><td colspan="2" ><b>아이디</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text" >
			<tr><td><input type="text" name="id" size="20" class="input_text" />
			<span class="idsub">@naver.com</span>
			</td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext">아이디를 입력하세요.</td></tr>
		
	
			<tr><td colspan="2" ><b>비밀번호</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text">
			<tr><td><input type="text" name="passwd" size="20" class="input_text" /></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="3" class="infotext" >비밀번호를 입력하세요.</td></tr>
		
			<tr><td colspan="3" ><b>비밀번호 확인</b></td></tr>
			<tr><td colspan="3" >
			<table class="table_text" >
			<tr><td><input type="text" name="repasswd" size="20" class="input_text" /></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" >비밀번호를 한번 더 입력하세요.</td></tr>
	
			<tr><td colspan="2" ><b>이름</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text">
			<tr><td><input type="text" name="name" size="20" class="input_text" /></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" >이름을 입력하세요.</td></tr>
		
			<tr><td colspan="2" ><b>이메일</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text">
			<tr><td><input type="text" name="email" size="20" class="input_text" /></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" >이메일을 입력하세요.</td></tr>
		
			<tr><td colspan="3" ><b>전화번호</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text" >
			<tr><td><input type="text" name="tel" size="20" class="input_text" /></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" >전화번호를 입력하세요.</td></tr>

		
		<tr>
			<td  height="50" align="center">
			<input type="submit" value="회원가입" class="join" /></td>
			<td  height="50" align="center">
			<input type="reset" value="다시작성" class="reset" /></td>
		</tr>
	</table>
</form>
</div>
</body>
</html>