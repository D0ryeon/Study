<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.board.BoardDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("euc-kr");%>

<jsp:useBean id="article" scope="page" class="ez.board.BoardDataBean">
   <jsp:setProperty name="article" property="*"/>
</jsp:useBean>

<%
    article.setReg_date(new Timestamp(System.currentTimeMillis()) );
	article.setIp(request.getRemoteAddr());

    BoardDBBean dbPro = BoardDBBean.getInstance();
    dbPro.insertArticle(article);

    response.sendRedirect("list2.jsp");
%>










