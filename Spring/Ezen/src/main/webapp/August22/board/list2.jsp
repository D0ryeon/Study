<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "ez.board.BoardDBBean" %>
<%@ page import = "ez.board.BoardDataBean" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ include file="color.jsp"%>

<%!
    int pageSize = 10; //한페이지에 보여줄 데이터의 행의 수
    SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<%
    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
        pageNum = "1";
    }

    int currentPage = Integer.parseInt(pageNum);
    //System.out.println(currentPage);
    int startRow = (currentPage * 10) - 9; 
    int endRow = currentPage * pageSize; 
    int count = 0;
    int number=0;

    List<BoardDataBean> articleList = null;
    BoardDBBean dbPro = BoardDBBean.getInstance();
    count = dbPro.getArticleCount(); 
    if (count > 0) {
        articleList = dbPro.getArticles(startRow, endRow);
    }

	number=count-(currentPage-1)*pageSize;
           
%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="<%=bodyback_c%>">
<center><b>글목록(전체 글:<%=count%>)</b>
<table width="700">
<tr>
    <td align="right" bgcolor="<%=value_c%>">
    <a href="writeForm.jsp">글쓰기</a>
    </td>
</table>

<%
    if (count == 0) {
%>
<table width="700" border="1" cellpadding="0" cellspacing="0">
<tr>
    <td align="center">
    게시판에 저장된 글이 없습니다.
    </td>
</table>

<%  } else {    %>
<table border="1" width="700" cellpadding="0" cellspacing="0" align="center">
    <tr height="30" bgcolor="<%=value_c%>">
      <td align="center"  width="50"  >번 호</td>
      <td align="center"  width="250" >제   목</td>
      <td align="center"  width="100" >작성자</td>
      <td align="center"  width="150" >작성일</td>
      <td align="center"  width="50" >조 회</td>
      <td align="center"  width="100" >IP</td>   
    </tr>
<% 
        for (int i = 0 ; i < articleList.size() ; i++) {
          BoardDataBean article = articleList.get(i);
%>
   <tr height="30">
    <td align="center"  width="50" > <%=number--%></td>
    <td  width="250" >
<%
      int wid=0;
      if(article.getRe_level()>0){ //답변글이라면...
        wid=5*(article.getRe_level());
%>
		  <img src="images/level.gif" width="<%=wid%>" height="16">
		  <img src="images/re.gif">
		<%}else{%>
		  <img src="images/level.gif" width="<%=wid%>" height="16">
		<%}%>
          
      <a href="content2.jsp?num=<%=article.getNum()%>&pageNum=<%=currentPage%>">
           <%=article.getSubject()%></a>
          <% if(article.getReadcount()>=20){%>
         <img src="images/hot.gif" border="0"  height="16"><%}%> </td>
    <td align="center"  width="100">
       <a href="mailto:<%=article.getEmail()%>"><%=article.getWriter()%></a></td>
    <td align="center"  width="150"><%= sdf.format(article.getReg_date())%></td>
    <td align="center"  width="50"><%=article.getReadcount()%></td>
    <td align="center" width="100" ><%=article.getIp()%></td>
  </tr>
     <%}%>
</table>
<%}%>
<%--페이징!!! --%>
<%
    if (count > 0) {
    //전체 패이지의 수를 연산
        int pageCount = count / pageSize + ( count % pageSize == 0 ? 0 : 1);

        int startPage = (int)(currentPage/5)*5+1; // 11
		int pageBlock=5;
        int endPage = startPage + pageBlock-1; // 11+5-1 =15
        if (endPage > pageCount) endPage = pageCount;
       
        if (startPage > 5) {    %>
        <a href="list2.jsp?pageNum=<%= startPage - 5 %>">[이전]</a>
<%      }
        for (int i = startPage ; i <= endPage ; i++) {  %>
        <a href="list2.jsp?pageNum=<%= i %>">[<%= i %>]</a>
<%
        }
        if (endPage < pageCount) {  %>
        <a href="list2.jsp?pageNum=<%= startPage + 5 %>">[다음]</a>
<%
        }
    }
%>
</center>
</body>
</html>






