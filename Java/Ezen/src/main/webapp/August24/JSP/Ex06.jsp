<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
	public int add(int a, int b){
		int c = a+b;
		return c;
}
	public int subtract(int a, int b){
		int c = a-b;
		return c;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1~10까지의 합</title>
</head>
<body>
<%
	int sum = 0;
	for(int i=1;i <= 10;i++){
		sum += i;
	}
%>
1부터 10까지의 합은 <%= sum %>이다.
<br>
<%
	sum = 0 ;
	for(int i=11;i <= 20;i++){
	sum += i;
}
%>
11부터 20까지의 합은 <%= sum %>이다.
<br>
1부터 10 까지의 합은 
<%= 1+2+3+4+5+6+7+8+9+10 %>
<br>
스크립트릿에서 선언부 사용하기
<br><%
	int value1 = 3, value2 = 9;
	
	int addResult = add(value1,value2);
	int subtractResult = subtract(value1, value2);
%>
<%= value1 %> + <%= value2 %> = <%= addResult %>
<br>
<%= value1 %> - <%= value2 %> = <%= subtractResult %>



</body>
</html>