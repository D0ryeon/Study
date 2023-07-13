<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "JSTL.Member"  %>
<%@ page import = "java.util.HashMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	Member member = new Member();
	HashMap<String, String> pref= new HashMap<String, String>();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL연습</title>

</head>
<body>
<%-- set태그는 변수를 설정할 때 사용한다. 그리고 기본객체의 영역에 속성설정까지 한다.
<c:set var="member" value="<%= member %>" scope="request"/>
<c:set target="${member}" property="name" value="최범균"/>
Member member = new Member();
member.setName("최범균");
pageContext.setAttribute("member",member);

${member.name}
Member member=(Member)pageContext.getAttribute("member");
member.getName(); --%>

<c:set var="member" value="<%= member %>" scope="page"/>
<c:set target="${member}" property="name" value="최범균"/>

<c:set var="pref" value="<%= pref %>" />
<c:set var="favoriateColor" value="#{pref.color}"/>

회원이름 : ${member.name}, 
좋아하는색 : ${favoriateColor}
<br>
<c:set target="${pref}" property="color" value="red"/>

설정 이후 종하하는 색: ${favoriateColor}

</body>
</html>