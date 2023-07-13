<%@page import="javax.swing.ListModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="dao.ArticleDao" %>
<%@page import="model.ArticleListModel" %>
<%@page import="service.ListArticleService"%>
<%@page import="java.util.*" %>
<%
	String pageNumberSting = request.getParameter("p");
	String searchn = request.getParameter("searchn")==null? null : request.getParameter("searchn");
	String search = request.getParameter("search")==null? null: request.getParameter("search");
	
	int pageNumber = 1;
	if(pageNumberSting != null && pageNumberSting.length() > 0){
		pageNumber = Integer.parseInt(pageNumberSting);
	}
	ListArticleService listService = ListArticleService.getInstance();
	ArticleListModel articleListModel;
	
	if(searchn==null||search==null){
		articleListModel = listService.getArticleList(pageNumber);
	}else{
		articleListModel = listService.getArticleList(pageNumber,Integer.parseInt(searchn),search);
		request.setAttribute("searchn", searchn);
		request.setAttribute("search", search);
	}
	
	request.setAttribute("listModel", articleListModel);
	if(articleListModel.getTotalPageCount() > 0){
		int beginPageNumber = (articleListModel.getRequestPage()-1)/10*10+1;
		int endPageNumber = beginPageNumber+9;
		if(endPageNumber>articleListModel.getTotalPageCount()){
			endPageNumber = articleListModel.getTotalPageCount();
		}
		request.setAttribute("beginPage", beginPageNumber);
		request.setAttribute("endPage", endPageNumber);
	}
	System.out.println(searchn);
	System.out.println(search);
	//System.out.println("getTotalPageCount : "+articleListModel.getTotalPageCount()+"*********");
%>
<jsp:forward page="list_view.jsp" />
