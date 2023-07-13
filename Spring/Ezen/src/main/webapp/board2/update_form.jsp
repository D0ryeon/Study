<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="service.ReadArticleService"%>
<%@ page import ="model.Article" %>
<%@ page import ="service.ArticleNotFoundException" %>
<%
	String viewPage = null;
	try{
		int articleId = Integer.parseInt(request.getParameter("articleId"));
		Article article = ReadArticleService.getInstance().getArticle(articleId);
		viewPage = "update_form_view.jsp";
		request.setAttribute("article", article);
	} catch(ArticleNotFoundException ex){
		viewPage = "article_not_found.jsp";
	}
%>    
<jsp:forward page="<%=viewPage%>"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>