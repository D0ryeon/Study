***Model1 Pattern으로 답변형게시판만들기***
 : CRUD
 : *게시판 리스트(목록) ===> 페이징, 답변, *검색, *댓글갯수
   *게시글 입력(답변)
   *게시글 내용보기(상세보기) ===> 댓글입력, 댓글리스트
   *게시글 수정
   *게시글 삭제

   *게시글 댓글
   *게시글 검색
   로그인 여부에 따른 기능 제한(session.getAttribute("memId")) : writeForm.jsp list.jsp content.jsp
    : 리스트 - 검색, 글쓰기, 로그아웃
    : 상세보기 - 댓글쓰기, 로그인한 이름과 글작성자가 같을 경우에만 수정 삭제 버튼 보이게 하기!!! 
    : 글쓰기 - 로그인한 사용자의 이름 이메일이 폼에 보이게 작업
   *답변이 있는글을 삭제시 '[삭제]' 출력 : BoardDBBean.deleteArticle() : 답변이 있으면 update/답변이 없으면 delete
   회원정보 리스트!!!
    : LogonDBBean - getMemberList selectCount()
    : list.jsp : modifiyForm.jsp+depetePro.jsp 수정

   ===>댓글에 댓글

 : 한줄한줄 코딩 ===> 결과 나오면...
   한줄한줄 주석달아서. 저한테 개인톡 보내주시면...


 : 테이블생성 : board(게시판) 시퀀스생성 : board_num
             [b_comment(댓글)]

 : 자바빈 : BoardDataBean,  [CommentDataBean]
 : DAO : BoardDBBean
   - BoardDBBean getInstance() : Singleton Pattern
   - Connection getConnection() : 커넥션풀로부터 커넥션 하나를 임대

   - void insertArticle(BoardDataBean b) : writePro.jsp : 게시물 입력
   - int getArticleCount() : list.jsp : 입력글의 갯수를 리턴
   *[검색]- int getArticleCount(int n, String searchKeyword) : list.jsp(검색) : 입력글의 갯수를 리턴(검색)
   - List<BoardDataBean> getArticles(int s,int e) : list.jsp : s부터 e까지의 데이터를 가져오는....
   *[검색]- List getArticles(int s,int e,int n, String searchKeyword) : list.jsp(검색) : 검색된 글의 s부터 e까지의 데이터를 가져오는....
   - BoardDataBean getArticle(int num) : content.jsp : 글번의 해당하는 데이터 한줄을 가져오는....
   - BoardDataBean updateGetArticle(int num) : updateForm.jsp : 수정하기 위한 데이터 한줄을 가져오는...
   - int updateArticle(BoardDataBean b) : updatePro.jsp : 수정
   - int deleteArticle(int num, String passwd) : deletePro.jsp : 삭제

   [CommentDBBean]
   - CommentDBBean getInstance() : Singleton Pattern
   - Connection getConnection() : 커넥션풀로부터 커넥션 하나를 임대
   - void insertComment(CommentDataBean c) : contentPro.jsp : 댓글 입력
   - List getComments(String content_num, int s, int e) : content.jsp : 게시글 번호에 해당하는 s부터 e까지의 데이터를 가져오는....
   - getCommentCount() : content.jsp : 댓글의 입력 갯수를 ....
   - deleteComment() : delCommentPro.jsp : 댓글 삭제

   [JdbcUtil]
   - close()

 : 웹관련된 파일(/webapp/board/)
   - color.jsp, style.css, script.js

   - 2.글입력 : writeForm.jsp(o) =============================> writePro.jsp : insertArticle(BoardDataBean b) ===> list.jsp
                               num,ref,re_step,re_level,writer, subject, email, content, passwd
                 
   - 1.리스트 : list.jsp(o)[검색][댓글갯수] ===> writeForm.jsp/content.jsp(num,pageNum)/list.jsp
             : getArticleCount(), getArticles(int s,int e)

   - 3.글상세보기 : list.jsp ===============> content.jsp
                            num pageNum

       content.jsp ===> updateForm.jsp(num pageNum)
       content.jsp ===> deleteForm.jsp(num pageNum)
       content.jsp ===> writeForm.jsp(num ref re_step re_level)	
       content.jsp ===> list.jsp(pageNum)
                                                                                      num, pageNum
   - 댓글입력 : content.jsp(수정)  =================================> *contentPro.jsp =====================> content.jsp
                               commentt, content_num, p_num, comment_num, commenter, passwd
                                                                                             num, pageNum
   - 댓글삭제 : content.jsp(수정)  =====> *delCommentForm.jsp ==========> *delCommentPro.jsp =====================> content.jsp 
                              ctn, cmn, p_num            passwd, content_num, comment_num, p_num

   - 4.글수정 : updateForm.jsp =====> updatePro.jsp =====> list content
   - 5.글삭제 : deleteForm.jsp =====> deletePro.jsp


1.테이블 생성
create table board(
num number not null primary key, //글번호
writer varchar2(10) not null, //글작성자
email varchar2(20), //이메일
subject varchar2(50) not null, //글제목
passwd varchar2(12) not null, //비밀번호
reg_date date not null, //글작성일
readcount number default 0, //조회수
ref number not null, // 메인글과 답변글을 묶어준다.
re_step number not null, // 화면출력순서
re_level number not null, // 메인글인지, 답변글인지를 구분해준다. 
content varchar2(4000) not null, //글내용
ip varchar2(20) not null // 글작성자의 IP주소
);

create sequence board_num;

2.JavaBean
:[BoardDataBean.java]
package ez.board;

import java.sql.Timestamp;

public class BoardDataBean{
    private int num;
    private String writer;
    private String subject;
    private String email;
    private String content;
    private String passwd;
    private Timestamp reg_date;
    private int readcount;
    private String ip;
    private int ref;
    private int re_step;
    private int re_level;

    public void setNum(int num){
    	this.num=num;
    }
    public void setWriter (String writer) {
        this.writer = writer;
    }
    public void setSubject (String subject) {
        this.subject = subject;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public void setContent (String content) {
        this.content = content;
    }
    public void setPasswd (String passwd) {
        this.passwd = passwd;
    }
    public void setReg_date (Timestamp reg_date) {
        this.reg_date = reg_date;
    }
    public void setReadcount(int readcount){
    	this.readcount=readcount;
    }
    public void setIp (String ip) {
        this.ip = ip;
    }
    public void setRef (int ref) {
        this.ref = ref;
    }
    public void setRe_level (int re_level) {
        this.re_level=re_level;
    }
    public void setRe_step (int re_step) {
        this.re_step=re_step;
    }
   
    public int getNum(){
    	return num;
    }
    public int getReadcount(){
       return readcount;
    }
    public String getWriter () {
        return writer;
    }
    public String getSubject () {
        return subject;
    }
    public String getEmail () {
        return email;
    }
    public String getContent () {
        return content;
    }
    public String getPasswd () {
        return passwd;
    }
    public Timestamp getReg_date () {
        return reg_date;
    }
    public String getIp () {
        return ip;
    }
    public int getRef () {
        return ref;
    }
    public int getRe_level () {
        return re_level;
    }
    public int getRe_step () {
        return re_step;
    }       
}


:[BoardDBBean.java]
package ez.board;

import java.sql.*;
import java.util.*;

public class BoardDBBean {   
	//Singleton Pattern
    private static BoardDBBean instance = new BoardDBBean();
   
    // BoardDBBean board = BoardDBBean.getInstance();
    public static BoardDBBean getInstance() {
        return instance;
    }
   
    private BoardDBBean() {
    }
   
    private Connection getConnection() throws Exception {
    	String jdbcDriver = "jdbc:apache:commons:dbcp:/pool";         
        return DriverManager.getConnection(jdbcDriver);
    }
    
    //writePro.jsp
    public void insertArticle(BoardDataBean article) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        //답변글인지 일반글인지를 구분해서 입력시켜주는 로직!!!
	int num=article.getNum();
	int ref=article.getRef();
	int re_step=article.getRe_step();
	int re_level=article.getRe_level();
	int number=0;
        String sql="";

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement("select max(num) from board");
            rs = pstmt.executeQuery();

		    if (rs.next())
		      number=rs.getInt(1)+1;
		    else
		      number=1;
  
		    if (num!=0) //답변글이라면....
		    { 
		      sql="update board set re_step=re_step+1 where ref= ? and re_step> ?";
		      pstmt = conn.prepareStatement(sql);
		      pstmt.setInt(1, ref);
		      pstmt.setInt(2, re_step);
		      pstmt.executeUpdate();
		      re_step=re_step+1;
		      re_level=re_level+1;
		    }else{
		      ref=number;
		      re_step=0;
		      re_level=0;
		    }
            // 쿼리를 작성
            sql = "insert into board(num,writer,email,subject,passwd,reg_date,";
            sql+="ref,re_step,re_level,content,ip) values(board_num.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, article.getWriter());
            pstmt.setString(2, article.getEmail());
            pstmt.setString(3, article.getSubject());
            pstmt.setString(4, article.getPasswd());
            pstmt.setTimestamp(5, article.getReg_date());
            pstmt.setInt(6, ref);
            pstmt.setInt(7, re_step);
            pstmt.setInt(8, re_level);
            pstmt.setString(9, article.getContent());
            pstmt.setString(10, article.getIp());

            pstmt.executeUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
   
    //list.jsp : 페이징을 위해서 전체 테이블에 입력된 행의수가 필요하다...!!!
    public int getArticleCount() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int x=0;

        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement("select count(*) from board");
            rs = pstmt.executeQuery();

            if (rs.next()) {
               x= rs.getInt(1);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return x;
    }
    

	
    //list.jsp ==> Paging!!! DB로부터 여러행을 결과로 받는다.
    public List<BoardDataBean> getArticles(int start, int end) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoardDataBean> articleList=null;
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
            " select num,writer,email,subject,passwd,reg_date,ref,re_step,re_level,content,ip,readcount,r " +
            " from (select num,writer,email,subject,passwd,reg_date,ref,re_step,re_level,content,ip,readcount,rownum r " +
            " from (select num,writer,email,subject,passwd,reg_date,ref,re_step,re_level,content,ip,readcount " +
            " from board order by ref desc, re_step asc) order by ref desc, re_step asc ) where r >= ? and r <= ? ");
            pstmt.setInt(1, start);
            pstmt.setInt(2, end);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                articleList = new ArrayList<BoardDataBean>(end);
                do{
                  BoardDataBean article= new BoardDataBean();
                  article.setNum(rs.getInt("num"));
                  article.setWriter(rs.getString("writer"));
                  article.setEmail(rs.getString("email"));
                  article.setSubject(rs.getString("subject"));
                  article.setPasswd(rs.getString("passwd"));
                  article.setReg_date(rs.getTimestamp("reg_date"));
                  article.setReadcount(rs.getInt("readcount"));
                  article.setRef(rs.getInt("ref"));
                  article.setRe_step(rs.getInt("re_step"));
                  article.setRe_level(rs.getInt("re_level"));
                  article.setContent(rs.getString("content"));
                  article.setIp(rs.getString("ip"));
 
                  articleList.add(article);
                }while(rs.next());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	//JdbcUtil.close(rs);
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return articleList;
    }
    
	

    //content.jsp : DB로부터 한줄의 데이터를 가져온다.
    public BoardDataBean getArticle(int num) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardDataBean article=null;
        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(
            "update board set readcount=readcount+1 where num = ?");
            pstmt.setInt(1, num);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(
            "select * from board where num = ?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                article = new BoardDataBean();
                article.setNum(rs.getInt("num"));
                article.setWriter(rs.getString("writer"));
                article.setEmail(rs.getString("email"));
                article.setSubject(rs.getString("subject"));
                article.setPasswd(rs.getString("passwd"));
                article.setReg_date(rs.getTimestamp("reg_date"));
                article.setReadcount(rs.getInt("readcount"));
                article.setRef(rs.getInt("ref"));
                article.setRe_step(rs.getInt("re_step"));
                article.setRe_level(rs.getInt("re_level"));
                article.setContent(rs.getString("content"));
                article.setIp(rs.getString("ip"));    
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	if(rs != null) try {rs.close();} catch(SQLException ex){}
			if(pstmt != null) try {pstmt.close();} catch(SQLException ex){}
			if(conn != null) try {conn.close();} catch(SQLException ex){}
        }
        return article;
    }

    //updateForm.jsp : 수정폼에 한줄의 데이터를 가져올때.
    public BoardDataBean updateGetArticle(int num) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardDataBean article=null;
        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(
            "select * from board where num = ?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                article = new BoardDataBean();
                article.setNum(rs.getInt("num"));
                article.setWriter(rs.getString("writer"));
                article.setEmail(rs.getString("email"));
                article.setSubject(rs.getString("subject"));
                article.setPasswd(rs.getString("passwd"));
                article.setReg_date(rs.getTimestamp("reg_date"));
                article.setReadcount(rs.getInt("readcount"));
                article.setRef(rs.getInt("ref"));
                article.setRe_step(rs.getInt("re_step"));
                article.setRe_level(rs.getInt("re_level"));
                article.setContent(rs.getString("content"));
                article.setIp(rs.getString("ip"));    
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return article;
    }

    //updatePro.jsp : 실제 데이터를 수정하는 메소드.
    public int updateArticle(BoardDataBean article) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs= null;

        String dbpasswd="";
        String sql="";
        int x=-1;
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
            "select passwd from board where num = ?");
            pstmt.setInt(1, article.getNum());
            rs = pstmt.executeQuery();
           
            if(rs.next()){
            	dbpasswd= rs.getString("passwd");
            	if(dbpasswd.equals(article.getPasswd())){
			        sql="update board set writer=?,email=?,subject=?,passwd=?";
			        sql+=",content=? where num=?";
	                pstmt = conn.prepareStatement(sql);
	
	                pstmt.setString(1, article.getWriter());
	                pstmt.setString(2, article.getEmail());
	                pstmt.setString(3, article.getSubject());
	                pstmt.setString(4, article.getPasswd());
	                pstmt.setString(5, article.getContent());
	                pstmt.setInt(6, article.getNum());
	                pstmt.executeUpdate();
	                x= 1;
				}else{
					x= 0;
				}
          }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return x;
    }
   
    //deletePro.jsp : 실제 데이터를 삭제하는 메소드...
    public int deleteArticle(int num, String passwd) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs= null;
        String dbpasswd="";
        int x=-1;
        try {
        	conn = getConnection();

            pstmt = conn.prepareStatement(
            "select passwd from board where num = ?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
           
            if(rs.next()){
            	dbpasswd= rs.getString("passwd");
            	if(dbpasswd.equals(passwd)){
            		pstmt = conn.prepareStatement(
                  "delete from board where num=?");
                    pstmt.setInt(1, num);
                    pstmt.executeUpdate();
                    x= 1; //글삭제 성공
				}else
				    x= 0; //비밀번호 틀림
				}
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return x;
    }
}

3.Script요소
:[color.jsp]
<%
String bodyback_c="#e0ffff";
String back_c="#8fbc8f";
String title_c="#5f9ea0";
String value_c="#b0e0e6";
String bar="#778899";
%>

:[style.css]
BODY {
FONT-SIZE: 9pt; COLOR: black; LINE-HEIGHT: 160%; FONT-FAMILY: 굴림,verdana,tahoma
}
TD {
FONT-SIZE: 9pt; COLOR: black; LINE-HEIGHT: 160%; FONT-FAMILY: 굴림,verdana,tahoma
}
SELECT {
FONT-SIZE: 9pt; COLOR: black; LINE-HEIGHT: 160%; FONT-FAMILY: 굴림,verdana,tahoma
}
DIV {
FONT-SIZE: 9pt; COLOR: black; LINE-HEIGHT: 160%; FONT-FAMILY: 굴림,verdana,tahoma
}
FORM {
FONT-SIZE: 9pt; COLOR: black; LINE-HEIGHT: 160%; FONT-FAMILY: 굴림,verdana,tahoma
}
TEXTAREA {
BORDER-RIGHT: 1px solid #999999; BORDER-TOP: 1px solid #999999; FONT-SIZE: 9pt;
BORDER-LEFT: 1px solid #999999 ; COLOR: BLACK; BORDER-BOTTOM: 1px solid #999999;
FONT-FAMILY: 굴림,verdana; BACKGROUND-COLOR: white
}
INPUT {
BORDER-RIGHT: 1px solid #999999; BORDER-TOP: 1px solid #999999; FONT-SIZE: 9pt;
BORDER-LEFT: 1px solid #999999; COLOR: BLACK; BORDER-BOTTOM: 1px solid #999999;
FONT-FAMILY: 굴림,verdana; HEIGHT: 19px; BACKGROUND-COLOR: white
}

A:link {text-decoration:none;color:#696969}
A:hover{text-decoration:yes;color:#66CCFF}
A:visited {text-decoration:none;color:#330066}

:[script.js]
function writeSave(){
if(document.writeform.writer.value==""){
  alert("작성자를 입력하십시요.");
  document.writeform.writer.focus();
  return false;
}
if(document.writeform.subject.value==""){
  alert("제목을 입력하십시요.");
  document.writeform.subject.focus();
  return false;
}

if(document.writeform.content.value==""){
  alert("내용을 입력하십시요.");
  document.writeform.content.focus();
  return false;
}
       
if(document.writeform.passwd.value==""){
  alert(" 비밀번호를 입력하십시요.");
  document.writeform.passwd.focus();
  return false;
}
}   

4.글입력
:[writeForm.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ include file="./color.jsp"%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="script.js"></script>
</head>

<%
  int num=0,ref=1,re_step=0,re_level=0;
  try{ 
    if(request.getParameter("num")!=null){
num=Integer.parseInt(request.getParameter("num"));
ref=Integer.parseInt(request.getParameter("ref"));
re_step=Integer.parseInt(request.getParameter("re_step"));
re_level=Integer.parseInt(request.getParameter("re_level"));
}
%>
  
<body bgcolor="<%=bodyback_c%>"> 
<center><b>글쓰기</b>
<br>
<form method="post" name="writeform" action="writePro.jsp" onsubmit="return writeSave()">
<input type="hidden" name="num" value="<%=num%>">
<input type="hidden" name="ref" value="<%=ref%>">
<input type="hidden" name="re_step" value="<%=re_step%>">
<input type="hidden" name="re_level" value="<%=re_level%>">

<table width="400" border="1" cellspacing="0" cellpadding="0"  bgcolor="<%=bodyback_c%>"
   align="center">
   <tr>
    <td align="right" colspan="2" bgcolor="<%=value_c%>">
    <a href="list.jsp"> 글목록</a>
   </td>
   </tr>
   <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center">이 름</td>
    <td  width="330">
       <input type="text" size="10" maxlength="10" name="writer"></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >제 목</td>
    <td  width="330">
    <%if(request.getParameter("num")==null){%>
       <input type="text" size="40" maxlength="50" name="subject"></td>
<%}else{%>
   <input type="text" size="40" maxlength="50" name="subject" value="[답변]"></td>
<%}%>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center">Email</td>
    <td  width="330">
       <input type="text" size="40" maxlength="30" name="email" ></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >내 용</td>
    <td  width="330" >
     <textarea name="content" rows="13" cols="40"></textarea> </td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >비밀번호</td>
    <td  width="330" >
     <input type="password" size="8" maxlength="12" name="passwd">
</td>
  </tr>
<tr>     
<td colspan=2 bgcolor="<%=value_c%>" align="center">
  <input type="submit" value="글쓰기" > 
  <input type="reset" value="다시작성">
  <input type="button" value="목록보기" OnClick="window.location='list.jsp'">
</td></tr></table>   
<%
  }catch(Exception e){}
%>    
</form>     
</body>
</html>     

:[writePro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("euc-kr");%>

<jsp:useBean id="article" scope="page" class="soldesk.board.BoardDataBean">
   <jsp:setProperty name="article" property="*"/>
</jsp:useBean>

<%
    article.setReg_date(new Timestamp(System.currentTimeMillis()) );
article.setIp(request.getRemoteAddr());

    BoardDBBean dbPro = BoardDBBean.getInstance();
    dbPro.insertArticle(article);

    response.sendRedirect("list.jsp");
%>

5.글목록
:[list.jsp]
<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "soldesk.board.BoardDataBean" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ include file="./color.jsp"%>

<%!
    int pageSize = 3;
    SimpleDateFormat sdf =
        new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<%
    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
        pageNum = "1";
    }

    int currentPage = Integer.parseInt(pageNum);
    System.out.println(currentPage);
    int startRow = (currentPage * 3) - 2;
    int endRow = currentPage * pageSize;
    int count = 0;
    int number=0;

    List articleList = null;
    BoardDBBean dbPro = BoardDBBean.getInstance();
    count = dbPro.getArticleCount();
    if (count > 0) {
        articleList = dbPro.getArticles(startRow, endRow);
    }

number=count-(currentPage-1)*pageSize;
           // 11 -(2-1)*3 =8
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
          BoardDataBean article = (BoardDataBean)articleList.get(i);
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
          
      <a href="content.jsp?num=<%=article.getNum()%>&pageNum=<%=currentPage%>">
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

<%
    if (count > 0) {
    //전체 패이지의 수를 연산
        int pageCount = count / pageSize + ( count % pageSize == 0 ? 0 : 1);

        int startPage = (int)(currentPage/5)*5+1;
int pageBlock=5;
        int endPage = startPage + pageBlock-1;
        if (endPage > pageCount) endPage = pageCount;
       
        if (startPage > 5) {    %>
        <a href="list.jsp?pageNum=<%= startPage - 5 %>">[이전]</a>
<%      }
        for (int i = startPage ; i <= endPage ; i++) {  %>
        <a href="list.jsp?pageNum=<%= i %>">[<%= i %>]</a>
<%
        }
        if (endPage < pageCount) {  %>
        <a href="list.jsp?pageNum=<%= startPage + 5 %>">[다음]</a>
<%
        }
    }
%>
</center>
</body>
</html>

6.글상세보기
:[content.jsp]
<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "soldesk.board.BoardDataBean" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ include file="../view/color.jsp"%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>

<%
   int num = Integer.parseInt(request.getParameter("num"));
   String pageNum = request.getParameter("pageNum");

   SimpleDateFormat sdf =
        new SimpleDateFormat("yyyy-MM-dd HH:mm");

   try{
      BoardDBBean dbPro = BoardDBBean.getInstance();
      BoardDataBean article =  dbPro.getArticle(num);
 
  int ref=article.getRef();
  int re_step=article.getRe_step();
  int re_level=article.getRe_level();
%>
<body bgcolor="<%=bodyback_c%>"> 
<center><b>글내용 보기</b>
<br>
<form>
<table width="500" border="1" cellspacing="0" cellpadding="0" 
  bgcolor="<%=bodyback_c%>" align="center"> 
  <tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">글번호</td>
    <td align="center" width="125" align="center">
     <%=article.getNum()%></td>
    <td align="center" width="125" bgcolor="<%=value_c%>">조회수</td>
    <td align="center" width="125" align="center">
     <%=article.getReadcount()%></td>
  </tr>
  <tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">작성자</td>
    <td align="center" width="125" align="center">
     <%=article.getWriter()%></td>
    <td align="center" width="125" bgcolor="<%=value_c%>" >작성일</td>
    <td align="center" width="125" align="center">
     <%= sdf.format(article.getReg_date())%></td>
  </tr>
  <tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">글제목</td>
    <td align="center" width="375" align="center" colspan="3">
     <%=article.getSubject()%></td>
  </tr>
  <tr>
    <td align="center" width="125" bgcolor="<%=value_c%>">글내용</td>
    <td align="left" width="375" colspan="3"><pre><%=article.getContent()%></pre></td>
  </tr>
  <tr height="30">     
    <td colspan="4" bgcolor="<%=value_c%>" align="right" >
  <input type="button" value="글수정"
       onclick="document.location.href='updateForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
   &nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="글삭제"
       onclick="document.location.href='deleteForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
   &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" value="답글쓰기"
       onclick="document.location.href='writeForm.jsp?num=<%=num%>&ref=<%=ref%>&re_step=<%=re_step%>&re_level=<%=re_level%>'">
   &nbsp;&nbsp;&nbsp;&nbsp;
       <input type="button" value="글목록"
       onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
    </td>
  </tr>
</table>   
<%
}catch(Exception e){}
%>
</form>     
</body>
</html>     

7.글 수정하기
:[updateForm.jsp]
<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "soldesk.board.BoardDataBean" %>
<%@ include file="../view/color.jsp"%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="script.js"></script>
</head>

<%
  int num = Integer.parseInt(request.getParameter("num"));
  String pageNum = request.getParameter("pageNum");
  try{
      BoardDBBean dbPro = BoardDBBean.getInstance();
      BoardDataBean article =  dbPro.updateGetArticle(num);

%>

<body bgcolor="<%=bodyback_c%>"> 
<center><b>글수정</b>
<br>
<form method="post" name="writeform" action="updatePro.jsp?pageNum=<%=pageNum%>"
  onsubmit="return writeSave()">
<table width="400" border="1" cellspacing="0" cellpadding="0"  bgcolor="<%=bodyback_c%>"
  align="center">
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center">이 름</td>
    <td align="left" width="330">
       <input type="text" size="10" maxlength="10" name="writer" value="<%=article.getWriter()%>">
   <input type="hidden" name="num" value="<%=article.getNum()%>"></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >제 목</td>
    <td align="left" width="330">
       <input type="text" size="40" maxlength="50" name="subject" value="<%=article.getSubject()%>"></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center">Email</td>
    <td align="left" width="330">
       <input type="text" size="40" maxlength="30" name="email" value="<%=article.getEmail()%>"></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >내 용</td>
    <td align="left" width="330">
     <textarea name="content" rows="13" cols="40"><%=article.getContent()%></textarea></td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >비밀번호</td>
    <td align="left" width="330" >
     <input type="password" size="8" maxlength="12" name="passwd">
    
</td>
  </tr>
  <tr>     
   <td colspan=2 bgcolor="<%=value_c%>" align="center">
     <input type="submit" value="글수정" > 
     <input type="reset" value="다시작성">
     <input type="button" value="목록보기"
       onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
   </td>
</tr>
</table>
</form>
<%
}catch(Exception e){}%>     
     
</body>
</html>     

:[updatePro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("euc-kr");%>

<jsp:useBean id="article" scope="page" class="soldesk.board.BoardDataBean">
   <jsp:setProperty name="article" property="*"/>
</jsp:useBean>
<%

    String pageNum = request.getParameter("pageNum");

BoardDBBean dbPro = BoardDBBean.getInstance();
    int check = dbPro.updateArticle(article);

    if(check==1){
%>00
  <meta http-equiv="Refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>" >
<% }else{%>
      <script language="JavaScript">     
      <!--     
        alert("비밀번호가 맞지 않습니다");
        history.go(-1);
      -->
     </script>
<%
    }
%> 

8.글 삭제하기
:[deleteForm.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ include file="../view/color.jsp"%>

<%
  int num = Integer.parseInt(request.getParameter("num"));
  String pageNum = request.getParameter("pageNum");

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

<body bgcolor="<%=bodyback_c%>">
<center><b>글삭제</b>
<br>
<form method="POST" name="delForm"  action="deletePro.jsp?pageNum=<%=pageNum%>"
   onsubmit="return deleteSave()">
<table border="1" align="center" cellspacing="0" cellpadding="0" width="360">
  <tr height="30">
     <td align=center  bgcolor="<%=value_c%>">
       <b>비밀번호를 입력해 주세요.</b></td>
  </tr>
  <tr height="30">
     <td align=center >비밀번호 :  
       <input type="password" name="passwd" size="8" maxlength="12">
   <input type="hidden" name="num" value="<%=num%>"></td>
</tr>
<tr height="30">
    <td align=center bgcolor="<%=value_c%>">
      <input type="submit" value="글삭제" >
      <input type="button" value="글목록"
       onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">    
   </td>
</tr> 
</table>
</form>
</body>
</html>

:[deletePro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "soldesk.board.BoardDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("euc-kr");%>

<%
  int num = Integer.parseInt(request.getParameter("num"));
  String pageNum = request.getParameter("pageNum");
  String passwd = request.getParameter("passwd");

  BoardDBBean dbPro = BoardDBBean.getInstance();
  int check = dbPro.deleteArticle(num, passwd);

  if(check==1){
%>
  <meta http-equiv="Refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>" >
<% }else{%>
       <script language="JavaScript">     
       <!--     
         alert("비밀번호가 맞지 않습니다");
         history.go(-1);
       -->
      </script>
<%
    }
%>

 : 한줄한줄 코딩 ===> 결과 나오면...
   한줄한줄 주석달아서. 저한테 개인톡 보내주시면...
 : **[추가기능] 답변이 있는글을 삭제시 '삭제된 글입니다.' 출력
 : **검색기능
 : **댓글달기
 : 로그인 여부에 따른 기능 제한
 : 회원정보 리스트!!!
 : 댓글에 댓글
 
 
 
 public int deleteArticle(int num, String passwd) throws Exception {
     Connection conn = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;

     String dbpasswd = "";
     int x = -1;
     try {
        conn = getConnection();
        pstmt = conn.prepareStatement("select passwd from board where num = ?");
        pstmt.setInt(1, num);
        rs = pstmt.executeQuery();

        if (rs.next()) {
           dbpasswd = rs.getString("passwd");
           if (dbpasswd.equals(passwd)) {               
              
//              pstmt = conn.prepareStatement("select passwd from board where num = ?");
//              pstmt.setInt(1, num);
//              if()
              //노드이면 그냥 삭제 하고 만약 아래에 임시삭제가 있다면 노드판단하고 다시삭제
              //노드가 아니면 삭제보류
              //노드인지 판단하려면? 
              
              pstmt = conn.prepareStatement("select * from board where ref = (select ref from board where num = " +num+ ") " + 
                    "and re_step = (select re_step from board where num = " +num+ ")+1 " + 
                    "and re_level = (select re_level from board where num = " +num+ ")+1");
              
              rs = pstmt.executeQuery();
              
              if(rs.next()) //업데이트
              {
                 //1번게시물이라면?
                 //삭제한다 
                 //앞에넘버 확인해서 깨진거면 삭제한다 재귀함수?
                 
                 pstmt = conn.prepareStatement("update board set email = '[삭제됨]' ,writer = '[삭제]',subject = '[삭제됨]',content = '[삭제됨]'  where num = ?");
                 pstmt.setInt(1, num);
                 pstmt.executeUpdate();
                 
                 x = 1;
                 
              }
              else //삭제
              {
                 pstmt = conn.prepareStatement("delete from board where num=?");
                 pstmt.setInt(1, num);
                 pstmt.executeUpdate();
                 
                 x = 1;// 글삭제 성공
              }
           } else
              x = 0;// 비밀번호 틀림
        }
     } catch (Exception ex) {
        ex.printStackTrace();
     } finally {
        if (rs != null)
           try {
              rs.close();
           } catch (SQLException ex) {
           }
        if (pstmt != null)
           try {
              pstmt.close();
           } catch (SQLException ex) {
           }
        if (conn != null)
           try {
              conn.close();
           } catch (SQLException ex) {
           }
     }
     return x;

  }
 
 
 
 pstmt=conn.prepareStatement("select * from board where ref = (select ref from board where num = " +num+ ") " + 
         "and re_step = (select re_step from board where num = " +num+ ")+1 " + 
         "and re_level = (select re_level from board where num = " +num+ ")+1");
	rs = pstmt.executeQuery();
	
	if(rs.next()) {
		pstmt = conn.prepareStatement("update board set email = '[삭제됨]',writer = '[삭제]',subject = '[삭제됨]',content = [삭제됨] where num = ?");
		pstmt.setInt(1, num);
		pstmt.executeUpdate();
		
		x=1;
	}else {
	
	pstmt = conn.prepareStatement("delete from board where num=?");
	pstmt.setInt(1, num);
	pstmt.executeUpdate();
	x=1; //글삭제 성공
	}
}else
	x=0; //비밀번호 틀림
}