<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="service.ReplyArticleService" %>
<%@ page import="model.Article" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="replyingRequest" class="model.ReplyingRequest"/>
<jsp:setProperty name="replyingRequest" property="*" />
<%
	String viewPage=null;
	try{
		Article postedArticle = ReplyArticleService.getInstance().reply(replyingRequest);
		request.setAttribute("postedArticle", postedArticle);
		viewPage = "reply_success.jsp";
	}catch(Exception ex){
		viewPage = "reply_error.jsp";
		request.setAttribute("replyException", ex);
	}
%>
<jsp:forward page="<%=viewPage%>"/>