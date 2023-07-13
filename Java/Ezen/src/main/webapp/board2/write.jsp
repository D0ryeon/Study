<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="service.WriteArticleService"  %>
<%@ page import="model.Article"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id="writingRequest" class="model.WritingRequest" />
<jsp:setProperty name="writingRequest" property="*" />
<% 
Article postedArticle = WriteArticleService.getInstance().write(writingRequest);
request.setAttribute("postedArticle", postedArticle);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
게시글을 등록했습니다.
<br>
<a href="<c:url value='list.jsp'/>">목록보기</a>
<a href="<c:url value='read.jsp?articleId=${postedArticle.id}'/>">게시글읽기</a>
</body>
</html>