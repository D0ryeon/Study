<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>삭제하기</title>
<script type="text/javascript">
function writeSave(){
	if(document.writeform.password.value==""){
		alert("비밀번호를 입력하십시오.");
		document.writeform.password.focus();
		return false;
	}
}
</script>
</head>
<body>
<form action="<c:url value='delete.jsp'/>" method="post" name="writeform" onSubmit="return writeSave()">
<input type="hidden" name="articleId" value="${param.articleId}"/>
글암호 : <input type="password" name="password"/> <br>
<input type="submit" value="삭제" />
</form>
</body>
</html>