<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>
<% request.setCharacterEncoding("UTF-8"); %>
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
<div align="center">
<body bgcolor="<%= bodyback_c %>">
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
			<table class="table_text_done" >
			<tr><td class="done"><jsp:getProperty name="regBean" property="id"></jsp:getProperty>
			<span class="idsub">@naver.com</span>
			</td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext"> </td></tr>
		
	
			<tr><td colspan="2" ><b>비밀번호</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text_done">
			<tr><td class="done"><jsp:getProperty name="regBean" property="passwd"></jsp:getProperty></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="3" class="infotext" > </td></tr>
		
			<tr><td colspan="3" ><b>비밀번호 확인</b></td></tr>
			<tr><td colspan="3" >
			<table class="table_text_done" >
			<tr><td class="done"><jsp:getProperty name="regBean" property="repasswd"></jsp:getProperty></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" > </td></tr>
	
			<tr><td colspan="2" ><b>이름</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text_done">
			<tr><td class="done"><jsp:getProperty name="regBean" property="name"></jsp:getProperty></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" > </td></tr>
		
			<tr><td colspan="2" ><b>이메일</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text_done">
			<tr><td class="done"><jsp:getProperty name="regBean" property="email"></jsp:getProperty></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" > </td></tr>
		
			<tr><td colspan="3" ><b>전화번호</b></td></tr>
			<tr><td colspan="2" >
			<table class="table_text_done" >
			<tr><td class="done"><jsp:getProperty name="regBean" property="tel"></jsp:getProperty></td></tr>
			</table>
			</td><tr>
			<tr><td colspan="2" class="infotext" > </td></tr>
			<tr><td class="end">끝</td></tr>

		

		
	</table>
	</div>
</body>
</html>