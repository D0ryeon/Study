<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>main</title>
</head>
<body>
main.jsp에서 생성한 내용.
<jsp:include page="sub.jsp" flush="false" />
include 이후의 내용.
<br><br>
Ex2의 내용<br><br>

main.jsp를 실행한 결과로 생성된 HTML코드<br>

main.jsp에서 생성한 내용.

<p>
sub.jsp에서 생성한 내용.
</p>

include이후의 내용.

</body>
</html>