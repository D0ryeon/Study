<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="service.ReadArticleService" %>
<%@ page import="service.ArticleNotFoundException" %>
<%@ page import="model.Article" %>
<%
	int articleId = Integer.parseInt(request.getParameter("articleId"));
	String viewPage = null;
	try{
		Article article = ReadArticleService.getInstance().readArticle(articleId);
		request.setAttribute("article", article);
		viewPage = "read_view.jsp";
	}catch(ArticleNotFoundException ex){
		viewPage = "article_not_found.jsp";
	}
%>
<jsp:forward page="<%=viewPage%>"/>