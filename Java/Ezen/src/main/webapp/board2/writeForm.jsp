<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri= "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<script type="text/javascript">
function writeSave(){
	if(document.writeform.writerName.value==""){
		alert("작성자를 입력하십시오.");
		document.writeform.writerName.focus();
		return false;
	}
	if(document.writeform.title.value==""){
		alert("제목을 입력하십시오.");
		document.writeform.title.focus();
		return false;
	}
	if(document.writeform.content.value==""){
		alert("내용을 입력하십시오.");
		document.writeform.content.focus();
		return false;
	}
	if(document.writeform.password.value==""){
		alert("비밀번호를 입력하십시오.");
		document.writeform.password.focus();
		return false;
	}
}
</script>
</head>
<body>
<form action="<c:url value='write.jsp'/>" method="post" name="writeform" onSubmit="return writeSave()">
제목 : <input type="text" name="title" size="20"/> <br>
작성자 : <input type="text" name="writerName"/> <br>
글암호 : <input type="text" name="password" /> <br>
글내용 : <br>
<textarea name="content" cols="40" rows="5"></textarea>
<br>
<input type="submit" value="전송"/>
</form>
</body>
</html>