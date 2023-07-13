<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="service.DeleteArticleService" %>
<jsp:useBean id="deleteRequest" class="model.DeleteRequest" />
<jsp:setProperty name="deleteRequest" property="*" />
<%
	String viewPage = null;
	try {
		DeleteArticleService.getInstance().deleteArticle(deleteRequest);
		viewPage = "delete_success.jsp";
	}catch(Exception ex){
		request.setAttribute("deleteException", ex);
		viewPage = "delete_error.jsp";
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