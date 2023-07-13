<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정하기</title>
<script type="text/javascript">
function writeSave(){
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
<form action="update.jsp" method="post" name="writeform" onSubmit="return writeSave()">
<input type="hidden" name="articleId" value="${article.id}"/>
<input type="hidden" name="p" value="${param.p}"/>
제목 : <input type="text" name="title" size="20" value="${article.title}"/><br>
글암호 : <input type="password" name="password" /><br>
글내용 : <br>
<textarea name="content" cols="40" rows="5">${article.content }</textarea>
<br>
<input type="submit" value="수정"/>
</form>
</body>
</html>