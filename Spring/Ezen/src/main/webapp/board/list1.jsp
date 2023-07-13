<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "logon.BoardDataBean" %>
<%@ page import = "logon.BoardDBBean" %>
<%@ page import = "logon.CommentDataBean" %>
<%@ page import = "logon.CommentDBBean" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.net.URLEncoder" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ include file = "color.jsp" %>

<% 
	int pageSize = 10;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	String pageNum = request.getParameter("pageNum");
	String search = request.getParameter("search");
	
	int searchn = 0;
	
	if(pageNum 	== null){	pageNum="1";	}
	if(search 	== null){	search="";		} else {
		searchn = Integer.parseInt(request.getParameter("serchn"));
	}
	
	int currentPage = Integer.parseInt(pageNum);
	//System.out.println(currentPage);
	int startRow = (currentPage*10)-9;
	int endRow = currentPage*pageSize;
	int count = 0;
	int number = 0;
	
	List articleList  =null;
	BoardDBBean dbPro = BoardDBBean.getInstance();
	
	if(search.equals("") || search == null)
		count = dbPro.getArticleCount();
	else
		count = dbPro.getArticleCount(searchn,search);
	
	CommentDBBean cdb = CommentDBBean.getInstance();
	
	if(count > 0){
		if(search.equals("") || search == null)
			articleList = dbPro.getArticles(startRow, endRow);
		else
			articleList = dbPro.getArticles(startRow, endRow, searchn, search);
	}
	
	number = count - (currentPage - 1) * pageSize;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="<%=bodyback_c%>" align=center>
<center><b>글목록 (전체 글 : <%=count %>)</b>
<table width="700" >
	<tr>
<%
			if(session.getAttribute("memId")!=null){
%>	
		<td align="left" bgcolor="<%=value_c%>">

<%= session.getAttribute("memId") %>님
<input type="button" value="로그아웃" onclick="document.location.href='ez/Logon/logout.jsp'">
</td>
		<td align=right bgcolor="<%=value_c %>">	
			<a href="writeForm.jsp">글쓰기</a>
<% } else { %>
	<td align=right bgcolor="<%=value_c %>">
		<a href="writeForm.jsp">글쓰기</a>
		<a href="../Logon/loginForm.jsp">로그인</a>
<% } %>
		</td>
	</tr>
</table>

<% if(count==0){ %>
<table width="700" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align = "center">게시판에저장된 글이 없습니다.</td>
	</tr>
</table>

<% }else { %>

<table width="700" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align = "center" width="50"	>번 호</td>
		<td align = "center" width="250">제 목</td>
		<td align = "center" width="100">작성자</td>
		<td align = "center" width="150">작성일</td>
		<td align = "center" width="50"	>조 회</td>
		<td align = "center" width="100">IP</td>
	</tr>
<%
		for(int i=0; i<articleList.size(); i++){
			BoardDataBean article=(BoardDataBean)articleList.get(i);
			int com_count=cdb.getCommentCount(article.getNum());
%>
	<tr height="30">
		<td align = "center" width = "50"><%=number--%></td>
		<td align = "left">
<%
			int wid = 0;
			if(article.getRe_level() > 0){
				wid = 5 * (article.getRe_level());	// 답변글 확인
%>

			<img src="images/level.gif" width="<%= wid %>" height="16">
			<img src="images/re.gif" >

<% 			} else {   //답변글아니면 정상적으로 표시 %>								

			<img src="images/level.gif" width="<%= wid %>" height="16">

<%			
			} 
		//리스트 출력
		
		if(com_count > 0){
%>

			<a href="content1.jsp?num=<%=article.getNum()%>&pageNum=<%=currentPage%>">
			<%= article.getSubject() %>[<%=com_count %>]</a>
<%		} else { %>
			<a href="content1.jsp?num=<%=article.getNum()%>&pageNum=<%=currentPage%>">
			<%=article.getWriter()%></a>
<%		
		} 
		if(article.getReadcount() >= 20){ 
%>
		<img scr="images/hot.gif" border="0" height="16">
<%		} %>	
		</td>
		<td align="center"><a href="mailto:<%=article.getEmail()%>">
		<%=article.getWriter()%></a></td>
		<td align="center"><%=sdf.format(article.getReg_date()) %></td>
		<td align="center"><%=article.getReadcount() %></td>
		<td align="center"><%=article.getIp() %></td>
<% 		} %>
</tr>
</table>
<% 		} %>
<p>
<%
	if(count > 0){
		// 전체 페이지 수를 연산
		int pageCount = count / pageSize + (count % pageSize == 0? 0 : 1);
		int startPage = (int)(currentPage/5)*5+1;
		int pageBlock = 5;
		int endPage = startPage + pageBlock-1;
		
		if(endPage>pageCount) endPage = pageCount;
		
		if(startPage > 5){
			if(search.equals("") || search == null){
%>

			<a href = "list.jsp?pageNum=<%=startPage - 5 %>">[이전]</a>
<% 			} else { %>		
			<a href = "list.jsp?pageNum=<%=startPage - 5 %>&search=<%=search%>&searchn=<%=searchn%>">[이전]</a>
<% 			}
		}	
		
		for(int i = startPage; i<=endPage; i++){
			if(search.equals("") || search == null){
%>			
			<a href = "list.jsp?pageNum=<%=i%>">[<%=i %>]</a>
<% 			} else { %>		
			<a href = "list.jsp?pageNum=<%=i%>&search=<%=search%>&searchn=<%=searchn%>">[<%=i %>]</a>
<% 			}
		}	
		
		if(endPage < pageCount ){
			if(search.equals("") || search == null){
%>

			<a href = "list.jsp?pageNum=<%=startPage + 5 %>">[다음]</a>
<% 			} else { %>		
			<a href = "list.jsp?pageNum=<%=startPage + 5 %>&search=<%=search%>&searchn=<%=searchn%>">[다음]</a>
<% 			}
		}	
	}	%>
</p>
<form>
<select name="searchn">
<option value="0">작성자</option>
<option value="1">제목</option>
<option value="2">내용</option>
</select>

<input type="text" name="search" size="15" maxlength="50"/>
<input type="submit" value="검색"/>
 
</form>

</body>
</html>