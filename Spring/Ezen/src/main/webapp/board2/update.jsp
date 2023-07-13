<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="service.UpdateArticleService" %>
<%@ page import="model.Article" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="updateRequest" class="model.UpdateRequest"/>
<jsp:setProperty name="updateRequest" property="*" />

<%
	String viewPage = null;

	try{
		Article article = UpdateArticleService.getInstance().update(updateRequest);
		request.setAttribute("updatedArticle", article);
		viewPage = "update_success.jsp";
	}catch(Exception ex){
		request.setAttribute("updateException", ex);
		viewPage = "update_error.jsp";
	}
%>
<jsp:forward page="<%=viewPage%>"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>

</body>
</html>