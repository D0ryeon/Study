***Comment***
1. Table
create table b_comment(
	content_num number not null,
	commenter varchar2(20) not null,
	commentt varchar2(200) not null,
	passwd varchar2(20) not null,
	reg_date date not null,
	ip varchar2(20) not null,
	comment_num number not null 
)
2. JavaBean
[CommentDataBean.java]
package ez.board;

import java.sql.Timestamp;

public class CommentDataBean {
	private int comment_num;
	private int content_num;
	private String commenter;
	private String commentt;
	private String passwd;
	private Timestamp reg_date;
	private String ip;
	
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	private String ip;
	
	public String getCommentt() {
		return commentt;
	}
	public void setCommentt(String commentt) {
		this.commentt = commentt;
	}

	public int getContent_num() {
		return content_num;
	}
	public void setContent_num(int content_num) {
		this.content_num = content_num;
	}
	public String getCommenter() {
		return commenter;
	}
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}

[JdbcUtil.java]
package ez.loader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
			}
		}
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
			}
		}
	}

	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
			}
		}
	}
}


[CommentDBBean.java]
package ez.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import ez.jdbc.JdbcUtil;
import ez.board.CommentDataBean;

public class CommentDBBean {
		
	private static CommentDBBean instance=new CommentDBBean();
	
	public static CommentDBBean getInstance(){
		return instance;
	}
	
	private CommentDBBean(){}
	
	private Connection getConnection() throws Exception{
		String jdbcDriver="jdbc:apache:commons:dbcp:/pool";
		return DriverManager.getConnection(jdbcDriver);
	}
	
	public void insertComment(CommentDataBean cdb) throws Exception{
		
		Connection conn=null;
		PreparedStatement pstmt=null;

		try{
			conn=getConnection();
			String sql="insert into b_comment values(?,?,?,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, cdb.getContent_num());
			pstmt.setString(2, cdb.getCommenter());
			pstmt.setString(3, cdb.getCommentt());
			pstmt.setString(4, cdb.getPasswd());
			pstmt.setTimestamp(5, cdb.getReg_date());
			pstmt.setString(6, cdb.getIp());
			pstmt.setInt(7, cdb.getComment_num());
			pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
	}
	
	public ArrayList getComments(int con_num, int start, int end)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ArrayList cm=null;
		try{
			conn=getConnection();
			String sql="select content_num,commenter,commentt,reg_date,ip,comment_num,r "
					+ "from (select content_num,commenter,commentt,reg_date,ip,comment_num, rownum r "
					+ "from (select content_num,commenter,commentt,reg_date,ip,comment_num "
					+ "from b_comment where content_num="+con_num+" order by reg_date desc) order by reg_date desc) where r >= ? and r <= ?";			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				cm=new ArrayList();
				do{
					CommentDataBean cdb=new CommentDataBean();
					cdb.setComment_num(rs.getInt("comment_num"));
					cdb.setContent_num(rs.getInt("content_num"));
					cdb.setCommenter(rs.getString("commenter"));
					cdb.setCommentt(rs.getString("commentt"));
					cdb.setIp(rs.getString("ip"));
					cdb.setReg_date(rs.getTimestamp("reg_date"));
					cm.add(cdb);
				}while(rs.next());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		return cm;
	}
	
	public int getCommentCount(int con_num)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ArrayList cm=null;
		int count=0;
		
		try{
			conn=getConnection();
			String sql="select * from b_comment where content_num="+con_num+" order by reg_date desc";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				cm=new ArrayList();
				do{
					CommentDataBean cdb=new CommentDataBean();
					cdb.setCommenter(rs.getString("commenter"));
					cdb.setCommentt(rs.getString("commentt"));
					cdb.setIp(rs.getString("ip"));
					cdb.setReg_date(rs.getTimestamp("reg_date"));
					cm.add(cdb);
				}while(rs.next());
				count=cm.size();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		return count;
	}
	
	public int deleteComment(int content_num,String passwd,int comment_num)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String dbpasswd="";
		int x=-1;
		
		try{
			conn=getConnection();
			pstmt=conn.prepareStatement("select passwd from b_comment where content_num=? and comment_num=?");
			pstmt.setInt(1, content_num);
			pstmt.setInt(2, comment_num);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				dbpasswd=rs.getString("passwd");
				if(dbpasswd.equals(passwd)){
					pstmt=conn.prepareStatement("delete from b_comment where content_num=? and comment_num=?");
					pstmt.setInt(1, content_num);
					pstmt.setInt(2, comment_num);
					pstmt.executeUpdate();
					x=1;
				}else
					x=0;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		return x;
	}
}

3.JSP
[content1.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="ez.board.BoardDBBean" %>
<%@ page import="ez.board.BoardDataBean" %>
<%@ page import="ez.board.CommentDBBean" %>  
<%@ page import="ez.board.CommentDataBean" %> 
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="color.jsp" %>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script>
function writeSave(){
	if(document.comment.commentt.value==""){
	  alert("작성자를 입력하십시요.");
	  document.comment.commentt.focus();
	  return false;
	}
}
</script>
</head>

<%
	int num=Integer.parseInt(request.getParameter("num"));
	String pageNum=request.getParameter("pageNum");
	int pageSize = 10;
	String cPageNum = request.getParameter("cPageNum");
	if(cPageNum == null)
	{
		cPageNum = "1";
	}
	int cCurrentPage = Integer.parseInt(cPageNum);
	int startRow = (cCurrentPage * 10) -9;
	int endRow = cCurrentPage * pageSize;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	try{
			BoardDBBean dbPro=BoardDBBean.getInstance();
			BoardDataBean article=dbPro.getArticle(num);
			CommentDBBean cdb=CommentDBBean.getInstance();
			ArrayList comments=cdb.getComments(article.getNum(),startRow, endRow);
			int count=cdb.getCommentCount(article.getNum());
			int ref=article.getRef();
			int re_step=article.getRe_step();
			int re_level=article.getRe_level();
%>
			<body bgcolor=<%=bodyback_c %>>
			<center><b>글내용 보기</b><br>
				<table width=500 border=1 cellspacing=0 cellpadding=0 bgcolor=<%=bodyback_c %> align=center>
					<form>
					<tr height=30>
						<td align=center width=125 bgcolor=<%=value_c %>>글번호</td>
						<td align=center width=125><%=article.getNum() %></td>
						<td align=center width=125 bgcolor=<%=value_c %>>조회수</td>
						<td align=center width=125><%=article.getReadcount() %></td>
					</tr>
					<tr height=30>
						<td align=center width=125 bgcolor=<%=value_c %>>작성자</td>
						<td align=center width=125><%=article.getWriter() %></td>
						<td align=center width=125 bgcolor=<%=value_c %>>작성일</td>
						<td align=center width=125><%=sdf.format(article.getReg_date()) %></td>
					</tr>
					<tr height=30>
						<td align=center width=125 bgcolor=<%=value_c %>>글제목</td>
						<td align=center width=375 colspan=3><%=article.getSubject() %></td>
					</tr>
					<tr>
						<td align=center width=125 bgcolor=<%=value_c %>>글내용</td>
						<td align=center width=375 colspan=3><%=article.getContent() %></td>
					</tr>
					<tr height=30>
						<td colspan=4 bgcolor=<%=value_c %> align=right>
							<input type=button value=글수정 onclick="document.location.href='updateForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">&nbsp;&nbsp;&nbsp;&nbsp;
							<input type=button value=글삭제 onclick="document.location.href='deleteForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">&nbsp;&nbsp;&nbsp;&nbsp;
							<input type=button value=답글쓰기 onclick="document.location.href='writeForm.jsp?num=<%=num%>&ref=<%=ref%>&re_step=<%=re_step%>&re_level=<%=re_level%>'">&nbsp;&nbsp;&nbsp;&nbsp;
							<input type=button value=글목록 onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
						</td>
					</tr>
					</form>
					<form method=post action=contentPro.jsp name="comment" onsubmit="return writeSave()">
					<tr bgcolor=<%=value_c %> align=center>
						<td>코멘트 작성</td>
						<td colspan=2>
							<textarea name=commentt rows="6" cols="40"></textarea>
							<input type=hidden name=content_num value=<%=article.getNum() %>>
							<input type=hidden name=p_num value=<%=pageNum%>>
							<input type=hidden name=comment_num value=<%=count+1%>>
						</td>
						<td align=center>
							작성자<br>
							<input type=text name=commenter size=10><br>
							비밀번호<br>
							<input type=password name=passwd size=10><p>
							<input type=submit value=코멘트달기>
						</td>
					</tr>
					</form>
				</table>
		<%if(count>0){ %>
				<p>
				<table width=500 border=0 cellspacing=0 cellpadding=0 bgcolor=<%=bodyback_c %> align=center>
					<tr>
						<td>코멘트 수: <%=comments.size() %>
					</tr>
					<%for(int i=0;i<comments.size();i++){
							CommentDataBean dbc=(CommentDataBean)comments.get(i);
					%>
						<tr>
							<td align=left size=250 bgcolor=<%=value_c %>>
							&nbsp;<b><%=dbc.getCommenter() %>&nbsp;님</b> (<%=sdf.format(dbc.getReg_date())%>)
							</td>
							<td align=right size=250 bgcolor=<%=value_c %>> 접속IP:<%=dbc.getIp() %>
							&nbsp;<a href="delCommentForm.jsp?ctn=<%=dbc.getContent_num()%>&cmn=<%=dbc.getComment_num() %>&p_num=<%=pageNum %>" >[삭제]</a>&nbsp;
							</td>
						</tr>	
						<tr>
							<td colspan=2><%=dbc.getCommentt() %><td>
					<%} %>
						</tr>
						
				</table>
				<table width=500 border=0 cellspacing=0 cellpadding=0 bgcolor=<%=bodyback_c %> align=center>
				<tr>
				<center>
						<%--페이징!!! --%>
<%
    if (count > 0) {
    //전체 패이지의 수를 연산
        int pageCount = count / pageSize + ( count % pageSize == 0 ? 0 : 1);

        int startPage = (int)(cCurrentPage/5)*5+1; // 11
		int pageBlock=5;
        int endPage = startPage + pageBlock-1; // 11+5-1 =15
        if (endPage > pageCount) endPage = pageCount;
       
        if (startPage > 5) {    %>
        <a href="content1.jsp?num=<%=num %>&pageNum=<%=pageNum %>&cPageNum=<%= startPage - 5 %>">[이전]</a>
<%      }
        for (int i = startPage ; i <= endPage ; i++) {  %>
        <a href="content1.jsp?num=<%=num %>&pageNum=<%=pageNum %>&cPageNum=<%= i %>">[<%= i %>]</a>
<%
        }
        if (endPage < pageCount) {  %>
        <a href="content1.jsp?num=<%=num %>&pageNum=<%=pageNum %>&cPageNum=<%= startPage + 5 %>">[다음]</a>
<%
        }
    }
%>
						</center>
						</tr>
				</table>
				<%} %>
			<%
	}catch(Exception e){}
			%>
			</center>
			</body>
</html>

[contentPro.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="ez.board.CommentDBBean" %> 
<%@ page import="java.sql.Timestamp" %> 
<%
	request.setCharacterEncoding("euc-kr");
%>
<jsp:useBean id="cmt" scope="page" class="ssol.board.CommentDataBean">
	<jsp:setProperty name="cmt" property="*"/>
</jsp:useBean>
<%
	CommentDBBean comt=CommentDBBean.getInstance();
	cmt.setReg_date(new Timestamp(System.currentTimeMillis()));
	cmt.setIp(request.getRemoteAddr());	
	comt.insertComment(cmt);
	
	String content_num=request.getParameter("content_num");
	String p_num=request.getParameter("p_num");
	String url="content1.jsp?num="+content_num+"&pageNum="+p_num;
	response.sendRedirect(url);
%>

[delCommentForm.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ include file="color.jsp" %>
<%

	String comment_num=request.getParameter("cmn");
	String content_num=request.getParameter("ctn");
	String p_num=request.getParameter("p_num");
	String url="content1.jsp?num="+content_num+"&pageNum="+p_num;

%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript">     
	<!--     
	function deleteSave(){
		if(document.delForm.passwd.value==''){
			alert("비밀번호를 입력하십시요.");
			document.delForm.passwd.focus();
			return false;
		}
	}   
	// -->     
</script>
</head>
<body bgcolor=<%=bodyback_c %>>
<center>
<form method=post name=delForm action="delCommentPro.jsp" onsubmit="return deleteSave()">
	<table border=1 align=center cellspacing=0 cellpadding=0 width=360>
		<tr height=30>
			<td align=center bgcolor=<%=value_c %>>
				<b>비밀번호를 입력해 주세요</b>
			</td>
		</tr>
		<tr height=30>
			<td align=center>
				비밀번호:<input type="password" name=passwd size=8 maxlength=12>
				<input type=hidden name=content_num value=<%=content_num %>>
				<input type=hidden name=comment_num value=<%=comment_num%>>
				<input type=hidden name=p_num value=<%=p_num%>>
			</td>
		</tr>
		<tr height=30>
			<td align=center bgcolor=<%=value_c %>>
				<input type="submit" value="코멘트 삭제">
				<input type="button" value="취소" onclick="document.location.href='<%=url%>'">
			</td>
		</tr>
	</table>
</form>
</center>
</body>
</html>

[delCommentPro.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="ssol.board.CommentDBBean" %>  
<%@ page import="java.sql.Timestamp" %>
<%
	request.setCharacterEncoding("euc-kr");
%>
<%
	int content_num=Integer.parseInt(request.getParameter("content_num"));
	int comment_num=Integer.parseInt(request.getParameter("comment_num"));
	
	String pageNum=request.getParameter("p_num");
	String passwd=request.getParameter("passwd");
	
	CommentDBBean cmtPro=CommentDBBean.getInstance();
	int check=cmtPro.deleteComment(content_num, passwd, comment_num);
	
	if(check==1){
%>
	<meta http-equiv=Refresh content="0;url=content1.jsp?num=<%=content_num%>&pageNum=<%=pageNum %>">
<%}else{ %>
	<script language="javascript">
	<!--
		alert("비밀번호가 맞지 않습니다.");
		history.go(-1);
	//->
	</script>
<%} %>

[list1.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="ez.board.BoardDBBean" %>
<%@ page import="ez.board.BoardDataBean" %>
<%@ page import="ez.board.CommentDBBean" %> 
<%@ page import="ez.board.CommentDataBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="color.jsp" %>
<%
	int pageSize = 10;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<%
	String pageNum = request.getParameter("pageNum");
	String search = request.getParameter("search");
	
	int searchn = 0;
	
	if(pageNum == null)
	{
		pageNum = "1";
	}
	//pagenum이 parameter값으로 넘어오지 않았다면, 맨 첫번째 페이지로.
	
	
	if(search == null)
	{
		search = "";
	}
	else
	{
		searchn = Integer.parseInt(request.getParameter("searchn"));
	}
	

	
	int currentPage = Integer.parseInt(pageNum);
	//System.out.println(currentPage);
	int startRow = (currentPage * 10) -9;
	int endRow = currentPage * pageSize;
	int count = 0;
	int number = 0;
	
	List articleList = null;
	BoardDBBean dbPro = BoardDBBean.getInstance();
	
	if(search.equals("") || search == null)
		count = dbPro.getArticleCount();
	else
		count = dbPro.getArticleCount(searchn,search); 
	
	CommentDBBean cdb=CommentDBBean.getInstance();
	
	if(count > 0)
	{
		if(search.equals("") || search == null)
			articleList = dbPro.getArticles(startRow, endRow);
		else
			articleList = dbPro.getArticles(startRow, endRow, searchn, search);
	}
	
	number = count-(currentPage - 1) * pageSize;
			// 11 -(2-1) *3 = 8

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css"></link>
</head>
<body bgcolor="<%=bodyback_c %>">
<center>

<b>글목록 (전체 글 : <%=count %>)</b>

<table width="700">
<tr>
<td align="right" bgcolor="<%=value_c %>">

<a href="writeForm.jsp">글쓰기</a>


</td>
</tr>
</table>
<%if(count==0){ %>
<table width="700" border="1" cellpadding="0" cellspacing="0">
<tr>
<td align = "center">
게시판에 저장된 글이 없습니다.
</td>
</tr>
</table>
<%}
else
{%>
<table width="700" border="1" cellpadding="0" cellspacing="0">
<tr height="30" bgcolor="<%=value_c %>">
<td align="center" width="50">
번 호
</td>
<td align="center" width="250">
제 목
</td>
<td align="center" width="100">
작성자
</td>
<td align="center" width="150">
작성일
</td>
<td align="center" width="50">
조회
</td>
<td align="center" width="100">
IP
</td>
</tr>
<%

	for(int i = 0; i < articleList.size() ; i++)
	{
		BoardDataBean article = (BoardDataBean) articleList.get(i);
		int com_count=cdb.getCommentCount(article.getNum());
%>
<tr height="30">
<td align="center" width="50">
<%=number--%>
</td>

<td align="left">
<%		
		
		//
		int wid = 0;
		if(article.getRe_level() >0)
		{
			//답변글이라면.
			 wid = 5 * (article.getRe_level());
%>
<image src="images/level.gif" width="<%=wid %>" height="16">
<image src="images/re.gif">
<%
		}
		else
		{
			//답변글 아니면 정상적으로 표시하고
%>

<image src="images/level.gif" width="<%=wid %>" height="16">
<%
		}
		//리스트를 출력한다.
%>
<% if(com_count > 0){ %>

			<a href="content1.jsp?num=<%=article.getNum() %>&pageNum=<%=currentPage %>">
				<%=article.getSubject() %> [<%=com_count %>]</a>
<%}else{%>
			<a href="content1.jsp?num=<%=article.getNum() %>&pageNum=<%=currentPage %>">
				<%=article.getSubject() %></a>		
<%} %>					

<%if(article.getReadcount() >= 20){ %>
<image src="images/hot.gif" border="0" height="16"><%} %>
</td>
<td align="center"><a href="mailto:<%=article.getEmail() %>"><%=article.getWriter() %></a></td>
<td align="center"><%=sdf.format(article.getReg_date()) %></td>
<td align="center"><%=article.getReadcount() %></td>
<td align="center"><%=article.getIp() %></td>
<%
	}
%>
</table>
<%} %>
<p>
<%
	if(count > 0)
	{
		// 전체 페이지 수를 연산
		int pageCount = count / pageSize + (count % pageSize == 0? 0 : 1);
		
		int startPage = (int)(currentPage/5)*5+1;
		int pageBlock = 5;
		int endPage = startPage + pageBlock-1;
		
		if(endPage > pageCount) endPage = pageCount;
		
		if(startPage > 5)
		{
			//이전 페이지
			if(search.equals("") || search == null)
			{
%>
				<a href="list.jsp?pageNum=<%=startPage - 5 %>">[이전]</a>
<%
			}
			else
			{
%>
				<a href="list.jsp?pageNum=<%=startPage - 5 %>&search=<%=search %>&searchn=<%=searchn %>">[이전]</a>
<%
			}
%>
<%			
		}
		
		for(int i = startPage; i<=endPage; i++)
		{
			//해당페이지로 이동
			if(search.equals("") || search == null)
			{
%>
				<a href="list.jsp?pageNum=<%=i %>">[<%=i %>]</a>
<%
			}
			else
			{
%>
				<a href="list.jsp?pageNum=<%=i %>&search=<%=search %>&searchn=<%=searchn %>">[<%=i %>]</a>
<%
			}
%>
<%			
		}
		
		if(endPage < pageCount)
		{
			//다음페이지
			if(search.equals("") || search == null)
			{
%>
				<a href="list.jsp?pageNum=<%=startPage + 5 %>">[다음]</a>
<%
			}
			else
			{
%>
				<a href="list.jsp?pageNum=<%=startPage + 5 %>&search=<%=search %>&searchn=<%=searchn %>">[다음]</a>
<%
			}
%>
<%	
		}
	}

%>
</p>

<form>
<select name="searchn">
<option value="0">작성자</option>
<option value="1">제목</option>
<option value="2">내용</option>
</select>

<input type="text" name="search" size="15" maxlength="50" /> 
<input type="submit" value="검색" />
</form>

<br></br>
</body>
</html>