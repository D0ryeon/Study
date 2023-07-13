import java.util.Scanner;
 
public class Lotto {
    public static void main(String[] args) {
        /*
         * 로또번호 추출. 중복 없는 난수 6개를 추출한다. 1~45 사이의 난수여야 한다.
         */
        Scanner sc = new Scanner(System.in);
        System.out.print("몇 개의 세트를 추출하시겠습니까?\n>> ");
        int set = sc.nextInt();
        int cnt = 1;
 
        while (set > 0) {
 
            int[] nums = new int[6];
            for (int i = 0; i < nums.length; i++) {
                int temp = (int) (Math.random() * 45) + 1;
                nums[i] = temp;
 
                // 중복제거
                for (int j = 0; j < i; j++) {
                    if (nums[j] == temp) {
                        i--;
                        break;
                    }
                }
 
            } // for end
            System.out.print("\n"+ cnt + "번째 : " );
            for (int i = 0; i < nums.length; i++) {
                System.out.print(nums[i] + " ");
            }
            set--;
            cnt++;
        } // while end
        sc.close();
    }
}

***회원가입+로그인/로그아웃 만들기.........총정리(19일동안) 
 : CRUD
 : 회원정보입력 ==> 회원가입(아이디체크+우편번호검색) : insert
 : 회원정보수정 : update + 회원탈퇴 : delete
 : 로그인 : select + 로그아웃

 ========================================================================
[추가]
1000줄 다 코딩하고 분석까지 다 하면....
 : 우편번호 검색기능 추가. + 다음 우편번호 API 활용
   (수정기능 추가)
   => 1.modifyForm.jsp에 우편번호검색관련 스크립트와 폼을 추가한다.
   => 2.LogonDBBean의 getMember()를 수정
   => 3.LogonDBBean의 updateMember()를 수정
 : 쿠키를 이용해서 ID저장하기(로그인자동)
 : ID찾기/비밀번호 찾기
    select id from MEMBERS where name = ? and jumin1 = ? and jumin2 = ?
    select passwd from MEMBERS where id = ? and name = ? and jumin1 = ?
 
========================================================================
    
 : DBMS : Oracle XE 11g : JDBC Programming(chapter12)
 : 커넥션 풀 : pool.jocl

 : 테이블설계 : members(테이블의 구조를 파악), zipcode
 : 자바빈(DTO : Data transfer Object) : LogonDataBean.java : 데이터 8개를 한 묶음으로 사용
                                         ZipcodeBean.java
 : DAO(Data Access Object) : LogonDBBean.java
     LogonDBBean logon = LogonDBBean.getInstance();
   - LogonDBBean getInstance() : Singleton Pattern
   - private LogonDBBean()
   - private Connection getConnection() : 커넥션풀로부터 커넥션객체 하나를 리턴
   - void insertMember(LogonDataBean m) : 회원가입(inputPro.jsp)
   - int confirmId(String id) : ID중복확인(1-이미사용중, -1:-사용중이아님) : confirmId.jsp
   - int userCheck(String id, String passwd) : 로그온(1-로그온성공, 0-비밀번호틀림, -1-해당아이디 없음) : loginPro.jsp
   - LogonDataBean getMember(String id) : 해당 아이디에 대한 데이터 1줄을 가져오는...(수정폼에 데이터를 보여줄때.) : modifyForm.jsp
   - void updateMember(LogonDataBean m) : 회원정보수정(modifyPro.jsp)
   - int deleteMember(String id, String passwd) : 회원탈퇴(1-삭제성공, -1-비밀번호틀림)
   - Vector<ZipcodeBean> zipcodeRead(String area3) : 우편번호 검색

 : Web 관련 파일.(/webapp/logon/)
   - color.jsp/style.css
   - main.jsp(o) : 회원가입 : inputForm.jsp(o) =====> inputPro.jsp(confirmId.jsp : confirmId()/ zipCheck.jsp : zipCheck()))
                                              : insertMember()
                로그인 : loginForm.jsp(o) =====> loginPro.jsp <===== logout.jsp
		                             : userCheck()
  modify.jsp   회원정보수정 : modifyForm.jsp =====> modifyPro.jsp
 (회원정보변경)              : getMember()         : updateMember()
		    회원탈퇴 : deleteForm.jsp =====> deletePro.jsp
		                                   : deleteMember()

========================================================================

1.table생성
create table members(
id varchar2(12) not null primary key, //아이디
passwd varchar2(12) not null, //비밀번호
name varchar2(10) not null, //이름
jumin1 varchar2(6) not null, //주민번호앞자리
jumin2 varchar2(7) not null, //주민번호뒷자리
email varchar2(30), //이메일
blog varchar2(50), //블로그,홈페이지
reg_date date not null, //가입날짜.
)

2.JavaBean(DTO)생성
[/src/main/java/ez/logon/LogonDataBean.java]
package ez.logon;
import java.sql.Timestamp;

public class LogonDataBean{

private String id;
private String passwd;
private String name;
private String jumin1;
private String jumin2;
private String email; 
private String blog;
private Timestamp reg_date;

public void setId (String id){
this.id = id;
}
public void setPasswd (String passwd){
this.passwd = passwd;
}
public void setName (String name){
this.name = name;
}
public void setJumin1 (String jumin1){
this.jumin1 = jumin1;
}
public void setJumin2 (String jumin2){
this.jumin2 = jumin2;
}
public void setEmail (String email){
this.email = email;
}
public void setBlog (String blog){
this.blog = blog;
}
public void setReg_date (Timestamp reg_date){
this.reg_date = reg_date;
}

public String getId(){
return id;
}
public String getPasswd(){
return passwd;
}
public String getName(){
return name;
}
public String getJumin1(){
return jumin1;
}
public String getJumin2(){
return jumin2;
}
public String getEmail(){
return email;
}
public String getBlog(){
return blog;
}

public Timestamp getReg_date(){
return reg_date;
}
}
------------------------------------------------------------------------------
[/src/main/java/ez/logon/LogonDBBean.java]
package ez.logon;

import java.sql.*;

public class LogonDBBean {
	//SingleTon Pattern
	private static LogonDBBean instance = new LogonDBBean();
   
    public static LogonDBBean getInstance() {
        return instance;
    }
   
    private LogonDBBean() {
    }
   
    private Connection getConnection() throws Exception {
    	String jdbcDriver = "jdbc:apache:commons:dbcp:/pool";        
    	return DriverManager.getConnection(jdbcDriver);
    }
    //inputPro.jsp에서 회원가입을 할때 사용.
    public void insertMember(LogonDataBean member) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
            "insert into MEMBERS values (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPasswd());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getJumin1());
            pstmt.setString(5, member.getJumin2());
            pstmt.setString(6, member.getEmail());
            pstmt.setString(7, member.getBlog());
            pstmt.setTimestamp(8, member.getReg_date());

            pstmt.executeUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
    //loginPro.jsp에서 로그온을 시도할때 호출.
    public int userCheck(String id, String passwd) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;    
        ResultSet rs= null;
        String dbpasswd="";
        int x=-1;
        
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
            "select passwd from MEMBERS where id = ?");
            pstmt.setString(1, id);
            rs= pstmt.executeQuery();

            if(rs.next()){
				dbpasswd= rs.getString("passwd");
				if(dbpasswd.equals(passwd))
					x= 1; //인증 성공
				else
					x= 0; //비밀번호 틀림
			}else
					x= -1;//해당 아이디 없음

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return x;
	}
    //confirmId.jsp에서 ID을 체크할때 사용.
	public int confirmId(String id) throws Exception {
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs= null;
	    String dbpasswd="";
	    int x=-1;
	       
	    try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement("select id from MEMBERS where id = ?");
            pstmt.setString(1, id);
            rs= pstmt.executeQuery();

			if(rs.next())
				x= 1; //해당 아이디 있음
			else
				x= -1;//해당 아이디 없음
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
        	if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
	return x;
	}
	
	//updateMember.jsp에서 수정폼에 가입된 회원의 정보를 보여줄때 사용.
    public LogonDataBean getMember(String id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LogonDataBean member=null;
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement("select * from MEMBERS where id = ?");
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new LogonDataBean();
                member.setId(rs.getString("id"));
                member.setPasswd(rs.getString("passwd"));
                member.setName(rs.getString("name"));
                member.setJumin1(rs.getString("jumin1"));
                member.setJumin2(rs.getString("jumin2"));
                member.setEmail(rs.getString("email"));
                member.setBlog(rs.getString("blog"));
                member.setReg_date(rs.getTimestamp("reg_date"));    
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
        return member;
    }
   
    public void updateMember(LogonDataBean member) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
              "update MEMBERS set passwd=?,name=?,email=?,blog=? "+
              "where id=?");
            pstmt.setString(1, member.getPasswd());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getBlog());
            pstmt.setString(5, member.getId());
           
            pstmt.executeUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
   
    public int deleteMember(String id, String passwd) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs= null;
        String dbpasswd="";
        int x=-1;
        
        try {
        	conn = getConnection();

            pstmt = conn.prepareStatement(
            "select passwd from MEMBERS where id = ?");
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
           
			if(rs.next()){
				dbpasswd= rs.getString("passwd");
				if(dbpasswd.equals(passwd)){
					pstmt = conn.prepareStatement("delete from MEMBERS where id=?");
                    pstmt.setString(1, id);
                    pstmt.executeUpdate();
                    x= 1; //회원탈퇴 성공
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

----------------------------------------------------------------------------
[color.jsp]
<%
String bodyback_c="#e0ffff";
String back_c="#8fbc8f";
String title_c="#5f9ea0";
String value_c="#b0e0e6";
String bar="#778899";
%>
-----------------------------------------------------------------------------
[style.css]
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
BORDER-RIGHT: 1px solid #999999; BORDER-TOP: 1px solid #999999; FONT-SIZE: 9pt; BORDER-LEFT: 1px solid #999999 ; COLOR: BLACK; BORDER-BOTTOM: 1px solid #999999; FONT-FAMILY: 굴림,verdana; BACKGROUND-COLOR: white
}
INPUT {
BORDER-RIGHT: 1px solid #999999; BORDER-TOP: 1px solid #999999; FONT-SIZE: 9pt; BORDER-LEFT: 1px solid #999999; COLOR: BLACK; BORDER-BOTTOM: 1px solid #999999; FONT-FAMILY: 굴림,verdana; HEIGHT: 19px; BACKGROUND-COLOR: white
}

A:link {text-decoration:none;color:#696969}
A:hover{text-decoration:yes;color:#66CCFF}
A:visited {text-decoration:none;color:#330066}
----------------------------------------------------------------------
[main.jsp]
<%@ page  contentType="text/html; charset=euc-kr"%>
<%@ include file="color.jsp"%>
<html>
<head><title>메인입니다..</title>
<link href="style.css" rel="stylesheet" type="text/css">

<%
try{
   if(session.getAttribute("memId")==null){%>
<script language="javascript">

function focusIt()
{     
   document.inform.id.focus();
}

function checkIt()
{
   inputForm=eval("document.inform");
   if(!inputForm.id.value){
     alert("아이디를 입력하세요..");
inputForm.id.focus();
return false;
   }
   if(!inputForm.passwd.value){
     alert("아이디를 입력하세요..");
inputForm.passwd.focus();
return false;
   }
}

</script>
</head>

<body onLoad="focusIt();" bgcolor="<%=bodyback_c%>">
  <table width=500 cellpadding="0" cellspacing="0"  align="center" border="1" >
      <tr>
       <td width="300" bgcolor="<%=bodyback_c%>" height="20">
       &nbsp;
       </td>
  
       <form name="inform" method="post" action="loginPro.jsp"  onSubmit="return checkIt();">

        <td bgcolor="<%=title_c%>"  width="100" align="right">아이디</td>
        <td width="100" bgcolor="<%=value_c%>">
            <input type="text" name="id" size="15" maxlength="10"></td>
      </tr>
      <tr >
         <td rowspan="2" bgcolor="<%=bodyback_c%>" width="300" >메인입니다.</td>
         <td bgcolor="<%=title_c%>"  width="100" align="right">패스워드</td>
         <td width="100" bgcolor="<%=value_c%>">
            <input type="password" name="passwd" size="15" maxlength="10"></td>
       </tr>
       <tr>
          <td colspan="3" bgcolor="<%=title_c%>"   align="center">
            <input type="submit" name="Submit" value="로그인">
            <input type="button"  value="회원가입" onclick="javascript:window.location='inputForm.jsp'">
          </td></form></tr></table>
     <%}else{%>
       <table width=500 cellpadding="0" cellspacing="0"  align="center" border="1" >
         <tr>
           <td width="300" bgcolor="<%=bodyback_c%>" height="20">하하하</td>

           <td rowspan="3" bgcolor="<%=value_c%>" align="center">
             <%=session.getAttribute("memId")%>님이 <br>
             방문하셨습니다
             <form  method="post" action="logout.jsp"> 
             <input type="submit"  value="로그아웃">
             <input type="button" value="회원정보변경" onclick="javascript:window.location='modify.jsp'">
             </form>
         </td>
        </tr>
       <tr >
         <td rowspan="2" bgcolor="<%=bodyback_c%>" width="300" >메인입니다.</td>
      </tr>
     </table>
     <br>
<%}
}catch(NullPointerException e){}
%>
</body>
</html>
==========================================================
[inputForm.jsp]
<%@ page contentType="text/html; charset=euc-kr"%>
<%@ include file="color.jsp"%>
<html>
<head>
<title>회원가입</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript">
   
    function checkIt() {
        var userinput = eval("document.userinput");
        if(!userinput.id.value) {
            alert("ID를 입력하세요");
            return false;
        }
       
        if(!userinput.passwd.value ) {
            alert("비밀번호를 입력하세요");
            return false;
        }
        if(userinput.passwd.value != userinput.passwd2.value)
        {
            alert("비밀번호를 동일하게 입력하세요");
            return false;
        }
      
        if(!userinput.name.value) {
            alert("사용자 이름을 입력하세요");
            return false;
        }
        if(!userinput.jumin1.value  || !userinput.jumin2.value )
        {
            alert("주민등록번호를 입력하세요");
            return false;
        }
    }

    // 아이디 중복 여부를 판단
    function openConfirmid(userinput) {
        // 아이디를 입력했는지 검사
        if (userinput.id.value == "") {
            alert("아이디를 입력하세요");
            return;
        }
        // url과 사용자 입력 id를 조합합니다.
        url = "confirmId.jsp?id=" + userinput.id.value ;
       
        // 새로운 윈도우를 엽니다.
        open(url, "confirm",
        "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300, height=200");
    }
</script>
</head>

<body bgcolor="<%=bodyback_c%>">

<form method="post" action="inputPro.jsp" name="userinput" onSubmit="return checkIt()">
  <table width="600" border="1" cellspacing="0" cellpadding="3" align="center" >
    <tr>
    <td colspan="2" height="39" align="center" bgcolor="<%=value_c%>" >
       <font size="+1" ><b>회원가입</b></font></td>
    </tr>
    <tr>
      <td width="200" bgcolor="<%=value_c%>"><b>아이디 입력</b></td>
      <td width="400" bgcolor="<%=value_c%>"> </td>
    </tr> 

    <tr>
      <td width="200"> 사용자 ID</td>
      <td width="400">
        <input type="text" name="id" size="10" maxlength="12">
        <input type="button" name="confirm_id" value="ID중복확인" OnClick="openConfirmid(this.form)">
      </td>
    </tr>
    <tr>
      <td width="200"> 비밀번호</td>
      <td width="400" >
        <input type="password" name="passwd" size="15" maxlength="12">
      </td>
    <tr> 
      <td width="200">비밀번호 확인</td>
      <td width="400">
        <input type="password" name="passwd2" size="15" maxlength="12">
      </td>
    </tr>
   
    <tr>
      <td width="200" bgcolor="<%=value_c%>"><b>개인정보 입력</b></td>
      <td width="400" bgcolor="<%=value_c%>"> </td>
    <tr> 
    <tr>
      <td width="200">사용자 이름</td>
      <td width="400">
        <input type="text" name="name" size="15" maxlength="10">
      </td>
    </tr>
    <tr>
      <td width="200">주민등록번호</td>
      <td width="400">
        <input type="text" name="jumin1" size="7" maxlength="6">
        -<input type="text" name="jumin2" size="7" maxlength="7">
      </td>
    </tr>
    <tr>
      <td width="200">E-Mail</td>
      <td width="400">
        <input type="text" name="email" size="40" maxlength="30">
      </td>
    </tr>
    <tr>
      <td width="200"> Blog</td>
      <td width="400">
        <input type="text" name="blog" size="60" maxlength="50">
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center" bgcolor="<%=value_c%>">
          <input type="submit" name="confirm" value="등   록" >
          <input type="reset" name="reset" value="다시입력">
          <input type="button" value="가입안함" onclick="javascript:window.location='main.jsp'">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
-----------------------------------------------------------------------------
[confirmId.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.LogonDBBean" %>
<%@ include file="color.jsp"%>
<html>
<head><title>ID 중복확인</title>
<link href="style.css" rel="stylesheet" type="text/css">

<% request.setCharacterEncoding("euc-kr");%>

<%
    String id = request.getParameter("id");
LogonDBBean manager = LogonDBBean.getInstance();
    int check= manager.confirmId(id);
 
%>



<body bgcolor="<%=bodyback_c%>">
<%
    if(check == 1) {
%>
<table width="270" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="<%=title_c%>">
    <td height="39" ><%=id%>이미 사용중인 아이디입니다.</td>
  </tr>
</table>
<form name="checkForm" method="post" action="confirmId.jsp">
<table width="270" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td bgcolor="<%=value_c%>" align="center">
       다른 아이디를 선택하세요.<p>
       <input type="text" size="10" maxlength="12" name="id">
       <input type="submit" value="ID중복확인">
    </td>
  </tr>
</table>
</form>
<%
    } else {
%>
<table width="270" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="<%=title_c%>">
    <td align="center">
      <p>입력하신 <%=id%> 는 사용하실 수 있는 ID입니다. </p>
      <input type="button" value="닫기" onclick="setid()">
    </td>
  </tr>
</table>
<%
    }
%>
</body>
</html>
<script language="javascript">

  function setid()
    {
    opener.document.userinput.id.value="<%=id%>";
self.close();
}

</script>
-----------------------------------------------------------------------------
[inputPro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.LogonDBBean" %>
<%@ page import = "java.sql.Timestamp" %>

<% request.setCharacterEncoding("euc-kr");%>

<jsp:useBean id="member" class="ez.logon.LogonDataBean">
    <jsp:setProperty name="member" property="*" />
</jsp:useBean>

<%
    member.setReg_date(new Timestamp(System.currentTimeMillis()) );
    LogonDBBean manager = LogonDBBean.getInstance();
    manager.insertMember(member);

    response.sendRedirect("loginForm.jsp");
%>
----------------------------------------------------------------------------
[loginForm.jsp]
<%@ page language="java" contentType="text/html; charset=euc-kr"%>
<%@ include file="color.jsp"%>
<html>
<head><title>로그인</title>
<link href="style.css" rel="stylesheet" type="text/css">

   <script language="javascript">
     
       function begin(){
         document.myform.id.focus();
       }
       function checkIt(){
         if(!document.myform.id.value){
           alert("이름을 입력하지 않으셨습니다.");
           document.myform.id.focus();
           return false;
         }
         if(!document.myform.passwd.value){
           alert("비밀번호를 입력하지 않으셨습니다.");
           document.myform.passwd.focus();
           return false;
         }
         
       }
     
   </script>
</head>
<BODY onload="begin()" bgcolor="<%=bodyback_c%>">
<form name="myform" action="loginPro.jsp" method="post" onSubmit="return checkIt()">
<TABLE cellSpacing=1 cellPadding=1 width="260" border=1 align="center" >
 
  <TR height="30">
    <TD colspan="2" align="middle" bgcolor="<%=title_c%>"><STRONG>회원로그인</STRONG></TD></TR>
 
  <TR height="30">
    <TD width="110" bgcolor="<%=title_c%>" align=center>아이디</TD>
    <TD width="150" bgcolor="<%=value_c%>" align=center>
       <INPUT type="text" name="id" size="15" maxlength="12"></TD></TR>
  <TR height="30">
    <TD width="110" bgcolor="<%=title_c%>" align=center>비밀번호</TD>
    <TD width="150" bgcolor="<%=value_c%>" align=center>
      <INPUT type=password name="passwd"  size="15" maxlength="12"></TD></TR>
  <TR height="30">
    <TD colspan="2" align="middle" bgcolor="<%=title_c%>" >
      <INPUT type=submit value="로그인">
      <INPUT type=reset value="다시입력">
      <input type="button" value="회원가입" onclick="javascript:window.location='inputForm.jsp'"></TD></TR>
</TABLE>
</form>

</BODY>
</HTML>
-----------------------------------------------------------------------------
[loginPro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.LogonDBBean" %>

<% request.setCharacterEncoding("euc-kr");%>

<%
    String id = request.getParameter("id");
String passwd  = request.getParameter("passwd");

LogonDBBean manager = LogonDBBean.getInstance();
    int check= manager.userCheck(id,passwd);

if(check==1){
session.setAttribute("memId",id);
response.sendRedirect("main.jsp");
}else if(check==0){%>
<script>
  alert("비밀번호가 맞지 않습니다.");
      history.go(-1);
</script>
<% }else{ %>
<script>
  alert("아이디가 맞지 않습니다..");
  history.go(-1);
</script>
<%} %>
-------------------------------------------------------------------------------
[logout.jsp]
<%
session.invalidate();
response.sendRedirect("main.jsp");
%>
--------------------------------------------------------------------------------
[modify.jsp]
<%@ page contentType="text/html; charset=euc-kr"%>

  <body>
    <p>
    <a href="modifyForm.jsp">정보수정</a>
    <a href="deleteForm.jsp">탈퇴</a>
    </p>
  </body>
</html>
-------------------------------------------------------------------------------
[modifyForm.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.*" %>
<%@ include file="color.jsp"%>

<html>
<head>
<title>회원정보수정</title>
<link href="style.css" rel="stylesheet" type="text/css">


<script language="JavaScript">
   
    function checkIt() {
        var userinput = eval("document.userinput");
              
        if(!userinput.passwd.value ) {
            alert("비밀번호를 입력하세요");
            return false;
        }
        if(userinput.passwd.value != userinput.passwd2.value)
        {
            alert("비밀번호를 동일하게 입력하세요");
            return false;
        }
      
        if(!userinput.username.value) {
            alert("사용자 이름을 입력하세요");
            return false;
        }
        if(!userinput.jumin1.value  || !userinput.jumin2.value )
        {
            alert("주민등록번호를 입력하세요");
            return false;
        }
    }

</script>

<%
    String id = (String)session.getAttribute("memId");
  
    LogonDBBean manager = LogonDBBean.getInstance();
    LogonDataBean c = manager.getMember(id);

try{
%>

<body bgcolor="<%=bodyback_c%>">
<form method="post" action="modifyPro.jsp" name="userinput" onsubmit="return checkIt()">

  <table width="600" border="1" cellspacing="0" cellpadding="3"  align="center">
    <tr >
      <td  colspan="2" height="39" bgcolor="<%=title_c%>" align="center">
     <font size="+1" ><b>회원 정보수정</b></font></td>
    </tr>
    <tr>
      <td colspan="2" class="normal" align="center">회원의 정보를 수정합니다.</td>
    </tr>
     <tr>
      <td width="200" bgcolor="<%=value_c%>"><b>아이디 입력</b></td>
      <td width="400" bgcolor="<%=value_c%>"> </td>
    <tr> 

    <tr>
      <td  width="200"> 사용자 ID</td>
      <td  width="400"><%=c.getId()%></td>
    </tr>
   
     <tr>
      <td width="200"> 비밀번호</td>
      <td width="400">
        <input type="password" name="passwd" size="10" maxlength="10" value="<%=c.getPasswd()%>">
      </td>
    <tr> 
    <tr>
      <td  width="200" bgcolor="<%=value_c%>"><b>개인정보 입력</b></td>
      <td width="400" bgcolor="<%=value_c%>"> </td>
    <tr> 
    <tr>
      <td   width="200">사용자 이름</td>
      <td  width="400">
        <input type="text" name="name" size="15" maxlength="20" value="<%=c.getName()%>">
      </td>
    </tr>
    <tr>
      <td width="200">주민등록번호</td>
      <td width="400">
        <%=c.getJumin1()%>-<%=c.getJumin2()%>
      </td>
    </tr>
   <tr>
      <td width="200">E-Mail</td>
      <td width="400">
    <%if(c.getEmail()==null){%>
  <input type="text" name="email" size="40" maxlength="30" >
<%}else{%>
          <input type="text" name="email" size="40" maxlength="30" value="<%=c.getEmail()%>">
<%}%>
      </td>
    </tr>
    <tr>
      <td width="200">Blog</td>
      <td width="400">
    <%if(c.getBlog()==null){%>
  <input type="text" name="blog" size="60" maxlength="50" >
<%}else{%>
          <input type="text" name="blog" size="60" maxlength="50" value="<%=c.getBlog()%>">
<%}%>
      </td>
    </tr>     
    <tr>
      <td colspan="2" align="center" bgcolor="<%=value_c%>">
       <input type="submit" name="modify" value="수   정" >
       <input type="button" value="취  소" onclick="javascript:window.location='main.jsp'">     
      </td>
    </tr>
  </table>
</form>
</body>
<%}catch(Exception e){}%>
</html>
--------------------------------------------------------------------------------
[modifyPro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.LogonDBBean" %>
<%@ include file="color.jsp"%>

<% request.setCharacterEncoding("euc-kr");%>

<jsp:useBean id="member" class="soldesk.logon.LogonDataBean">
    <jsp:setProperty name="member" property="*" />
</jsp:useBean>

<%
    String id = (String)session.getAttribute("memId");
member.setId(id);

LogonDBBean manager = LogonDBBean.getInstance();
    manager.updateMember(member);
%>
<link href="style.css" rel="stylesheet" type="text/css">

<table width="270" border="0" cellspacing="0" cellpadding="5" align="center">
  <tr bgcolor="<%=title_c%>">
    <td height="39"  align="center">
  <font size="+1" ><b>회원정보가 수정되었습니다.</b></font></td>
  </tr>
  <tr>
    <td bgcolor="<%=value_c%>" align="center">
      <p>입력하신 내용대로 수정이 완료되었습니다.</p>
    </td>
  </tr>
  <tr>
    <td bgcolor="<%=value_c%>" align="center">
      <form>
    <input type="button" value="메인으로" onclick="window.location='main.jsp'">
      </form>
      5초후에 메인으로 이동합니다.<meta http-equiv="Refresh" content="5;url=main.jsp" >
    </td>
  </tr>
</table>
</body>
</html>
--------------------------------------------------------------------------------
[deleteForm.jsp]
<%@ page language="java" contentType="text/html; charset=euc-kr"%>
<%@ include file="color.jsp"%>
<html>
<head><title>회원탈퇴</title>
<link href="style.css" rel="stylesheet" type="text/css">

   <script language="javascript">
     
       function begin(){
         document.myform.passwd.focus();
       }

       function checkIt(){
  if(!document.myform.passwd.value){
           alert("비밀번호를 입력하지 않으셨습니다.");
           document.myform.passwd.focus();
           return false;
         }
   }  
     
   </script>
</head>
<BODY onload="begin()" bgcolor="<%=bodyback_c%>">
<form name="myform" action="deletePro.jsp" method="post" onSubmit="return checkIt()">
<TABLE cellSpacing=1 cellPadding=1 width="260" border=1 align="center" >
 
  <TR height="30">
    <TD colspan="2" align="middle" bgcolor="<%=title_c%>">
  <font size="+1" ><b>회원 탈퇴</b></font></TD></TR>
 
  <TR height="30">
    <TD width="110" bgcolor="<%=value_c%>" align=center>비밀번호</TD>
    <TD width="150" align=center>
      <INPUT type=password name="passwd"  size="15" maxlength="12"></TD></TR>
  <TR height="30">
    <TD colspan="2" align="middle" bgcolor="<%=value_c%>" >
      <INPUT type=submit value="회원탈퇴">
      <input type="button" value="취  소" onclick="javascript:window.location='main.jsp'"></TD></TR>
</TABLE>
</form>
</BODY>
</HTML>
--------------------------------------------------------------------------------
[deletePro.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import = "ez.logon.LogonDBBean" %>
<%@ include file="color.jsp"%>
<html>
<head>
<title>회원탈퇴</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>

<%
    String id = (String)session.getAttribute("memId");
String passwd  = request.getParameter("passwd");

LogonDBBean manager = LogonDBBean.getInstance();
    int check = manager.deleteMember(id,passwd);

if(check==1){
session.invalidate();
%>
<body bgcolor="<%=bodyback_c%>">
<form method="post" action="main.jsp" name="userinput" >
<table width="270" border="0" cellspacing="0" cellpadding="5" align="center">
  <tr bgcolor="<%=title_c%>">
    <td height="39" align="center">
  <font size="+1" ><b>회원정보가 삭제되었습니다.</b></font></td>
  </tr>
  <tr bgcolor="<%=value_c%>">
    <td align="center">
      <p>흑흑.... 서운합니다. 안녕히 가세요.</p>
      <meta http-equiv="Refresh" content="5;url=main.jsp" >
    </td>
  </tr>
  <tr bgcolor="<%=value_c%>">
    <td align="center">
      <input type="submit" value="확인">
    </td>
  </tr>
</table>
</form>
<%}else {%>
<script>
  alert("비밀번호가 맞지 않습니다.");
      history.go(-1);
</script>
<%}%>

</body>
</html>



******우편번호 체크로직.....
1.테이블을 만든다
   -테이블명 : zipcode
   -컬럼명 : zipcode, area1,area2,area3,area4
    create table zipcode(
	zipcode varchar2(7),
	area1 varchar2(20),
	area2 varchar2(40),
	area3 varchar2(40),
	area4 varchar2(120)
    )
   - zipcode.txt파일의 데이터를 입력....반드시 commit; 

2.저장빈클래스 작성
[ZipcodeBean.java]
package ez.logon;
public class ZipcodeBean {
private String zipcode;
private String area1;
private String area2;
private String area3;
private String area4;

public void setZipcode(String zipcode){
this.zipcode=zipcode;
}

public void setArea1(String area1){
this.area1=area1;
}

public void setArea2(String area2){
this.area2=area2;
}

public void setArea3(String area3){
this.area3=area3;
}

public void setArea4(String area4){
this.area4=area4;
}


    public String getZipcode(){
return zipcode;
}

    public String getArea1(){
return area1;
}
public String getArea2(){
return area2;
}
public String getArea3(){
return area3;
}
public String getArea4(){
return area4;
}
}

3.members테이블에 컬럼 추가
  alter table members
  add (zipcode varchar2(7),address varchar2(200));
  --zipcode varchar2(7) null
  --address varchar2(30) null

4.LogonDataBean에 프로퍼티 추가....
[LogonDataBean에.java]
private String zipcode;
private String address;

public String getAddress() {
return address;
}
public void setAddress(String address) {
this.address = address;
}
public String getZipcode() {
return zipcode;
}
public void setZipcode(String zipcode) {
this.zipcode = zipcode;
}

5.inputForm.jsp 수정
«%@ page contentType="text/html; charset=euc-kr"%»
«%@ include file="../view/color.jsp"%»
«html»
«head»
«title»회원가입«/title»
«link href="style.css" rel="stylesheet" type="text/css"»
«script language="JavaScript"»
   
    function checkIt() {
        var userinput = eval("document.userinput");
        if(!userinput.id.value) {
            alert("ID를 입력하세요");
            return false;
        }
       
        if(!userinput.passwd.value ) {
            alert("비밀번호를 입력하세요");
            return false;
        }
        if(userinput.passwd.value != userinput.passwd2.value)
        {
            alert("비밀번호를 동일하게 입력하세요");
            return false;
        }
      
        if(!userinput.username.value) {
            alert("사용자 이름을 입력하세요");
            return false;
        }
        if(!userinput.jumin1.value  || !userinput.jumin2.value )
        {
            alert("주민등록번호를 입력하세요");
            return false;
        }
    }

    // 아이디 중복 여부를 판단
    function openConfirmid(userinput) {
        // 아이디를 입력했는지 검사
        if (userinput.id.value == "") {
            alert("아이디를 입력하세요");
            return;
        }
        // url과 사용자 입력 id를 조합합니다.
        url = "confirmId.jsp?id=" + userinput.id.value ;
       
        // 새로운 윈도우를 엽니다.
        open(url, "confirm",
        "toolbar=no, location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300, height=200");
    }
=================================================   추가
    function zipCheck(){
url="ZipCheck.jsp?check=y";
window.open(url,"post","toolbar=no ,width=500 ,height=300 ,directories=no,status=yes,scrollbars=yes,menubar=no");
    }
=================================================
«/script»


«body bgcolor="«%=bodyback_c%»"»

«form method="post" action="inputPro.jsp" name="userinput" onSubmit="return checkIt()"»
  «table width="600" border="1" cellspacing="0" cellpadding="3" align="center" »
    «tr»
    «td colspan="2" height="39" align="center" bgcolor="«%=value_c%»" »
       «font size="+1" »«b»회원가입«/b»«/font»«/td»
    «/tr»
    «tr»
      «td width="200" bgcolor="«%=value_c%»"»«b»아이디 입력«/b»«/td»
      «td width="400" bgcolor="«%=value_c%»"»&nbsp;«/td»
    «/tr» 

    «tr»
      «td width="200"» 사용자 ID«/td»
      «td width="400"»
        «input type="text" name="id" size="10" maxlength="12"»
        «input type="button" name="confirm_id" value="ID중복확인" OnClick="openConfirmid(this.form)"»
      «/td»
    «/tr»
    «tr»
      «td width="200"» 비밀번호«/td»
      «td width="400" »
        «input type="password" name="passwd" size="15" maxlength="12"»
      «/td»
    «tr» 
      «td width="200"»비밀번호 확인«/td»
      «td width="400"»
        «input type="password" name="passwd2" size="15" maxlength="12"»
      «/td»
    «/tr»
   
    «tr»
      «td width="200" bgcolor="«%=value_c%»"»«b»개인정보 입력«/b»«/td»
      «td width="400" bgcolor="«%=value_c%»"»&nbsp;«/td»
    «tr» 
    «tr»
      «td width="200"»사용자 이름«/td»
      «td width="400"»
        «input type="text" name="name" size="15" maxlength="10"»
      «/td»
    «/tr»
    «tr»
      «td width="200"»주민등록번호«/td»
      «td width="400"»
        «input type="text" name="jumin1" size="7" maxlength="6"»
        -«input type="text" name="jumin2" size="7" maxlength="7"»
      «/td»
    «/tr»
    «tr»
      «td width="200"»E-Mail«/td»
      «td width="400"»
        «input type="text" name="email" size="40" maxlength="30"»
      «/td»
    «/tr»
    «tr»
      «td width="200"» Blog«/td»
      «td width="400"»
        «input type="text" name="blog" size="60" maxlength="50"»
      «/td»
    «/tr»
================================================= 추가
    <tr> 
       <td width="200">우편번호</td>
       <td> <input type="text" name="zipcode" size="7">
               <input type="button" value="우편번호찾기" onClick="zipCheck()">
               우편번호를 검색 하세요.</td>
          </tr>
    <tr>
    <tr> 
       <td>주소</td>
       <td><input type="text" name="address" size="70">
       주소를 적어 주세요.</td>
    </tr>
=================================================
    «tr»
      «td colspan="2" align="center" bgcolor="«%=value_c%»"»
          «input type="submit" name="confirm" value="등   록" »
          «input type="reset" name="reset" value="다시입력"»
          «input type="button" value="가입안함" onclick="javascript:window.location='main.jsp'"»
      «/td»
    «/tr»
  «/table»
«/form»
«/body»
«/html»

6.LogonDBBean에 메소드 추가/수정
public void insertMember(LogonDataBean member) //수정
    throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
       
        try {
            conn = getConnection();
           
            pstmt = conn.prepareStatement(
            "insert into MEMBERS(id,passwd,name,jumin1,jumin2,email" +
            ",blog,reg_date,zipcode,address) values (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPasswd());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getJumin1());
            pstmt.setString(5, member.getJumin2());
            pstmt.setString(6, member.getEmail());
            pstmt.setString(7, member.getBlog());
	    pstmt.setTimestamp(8, member.getReg_date());
		pstmt.setString(9, member.getZipcode());
		pstmt.setString(10, member.getAddress());
            pstmt.executeUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }

public Vector zipcodeRead(String area3)  { //추가
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector vecList = new Vector();
        try {
            con = getConnection();
            String strQuery = "select * from zipcode where area3 like '"+area3+"%'";
            pstmt = con.prepareStatement(strQuery);
            rs = pstmt.executeQuery();
            while(rs.next()){
                ZipcodeBean tempZipcode = new ZipcodeBean();
                tempZipcode.setZipcode(rs.getString("zipcode"));
                tempZipcode.setArea1(rs.getString("area1"));
                tempZipcode.setArea2(rs.getString("area2"));
                tempZipcode.setArea3(rs.getString("area3"));
                tempZipcode.setArea4(rs.getString("area4"));
                vecList.addElement(tempZipcode);
            }

        }catch(Exception ex) {
            System.out.println("Exception" + ex);
        }finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (con != null) try { con.close(); } catch(SQLException ex) {}
        }
        return vecList;
    }

7.ZipCheck.jsp
«%@ page contentType="text/html;charset=EUC-KR" %»
«%@ page import="java.util.*,ez.logon.*" %»

«%
   request.setCharacterEncoding("euc-kr");

   String check = request.getParameter("check");
   String area3 = request.getParameter("area3");
   LogonDBBean manager = LogonDBBean.getInstance();  
   Vector zipcodeList = manager.zipcodeRead(area3);
   int totalList = zipcodeList.size();
%»
«html»
«head»
«title»우편번호검색«/title»
«link href="style.css" rel="stylesheet" type="text/css"»

«script»
function dongCheck(){
if (document.zipForm.area3.value == ""){
alert("동이름을 입력하세요");
document.zipForm.area3.focus();
return;
}
document.zipForm.submit();
}

function sendAddress(zipcode,area1,area2,area3,area4){
var address =area1+ " "+area2+ " " +area3+ " " +area4;
opener.document.userinput.zipcode.value=zipcode;
opener.document.userinput.address.value=address;
self.close();
}
«/script»
«/head»
«body bgcolor="#FFFFCC"»
«center»
«b»우편번호 찾기«/b»
«table»
«form name="zipForm" method="post" action="ZipCheck.jsp"»
      «tr»
        «td»«br»
          동이름 입력 : «input name="area3" type="text"»
          «input type="button" value="검색" onclick= "dongCheck();"»
        «/td»
      «/tr»
     «input type="hidden" name="check" value="n"»
    «/form»
«%
if(check.equals("n")){
%»
«%
   if (zipcodeList.isEmpty()) {
%»
   «tr»«td align="center"»«br»검색된 결과가 없습니다.«/td»«/tr»
«% }
else {
%»
«tr»«td align="center"»«br»
    ※검색 후, 아래 우편번호를 클릭하면 자동으로 입력됩니다.«/td»«/tr»
«%
for (int i=0;i«totalList;i++) {
ZipcodeBean zipBean = (ZipcodeBean)zipcodeList.elementAt(i);
String tempZipcode =zipBean.getZipcode();
String temptArea1 =zipBean.getArea1();
String temptArea2 =zipBean.getArea2();
String temptArea3 =zipBean.getArea3();
String temptArea4 =zipBean.getArea4();
%»
«tr»«td»
«a href="javascript:sendAddress('«%=tempZipcode%»','«%=temptArea1%»','«%=temptArea2%»','«%=temptArea3%»','«%=temptArea4%»')"»
         «%=tempZipcode%»&nbsp;«%=temptArea1%»&nbsp;«%=temptArea2%»&nbsp;
«%=temptArea3%»&nbsp;«%=temptArea4%»«/a»«br»
«%
}//for
}
%»
«%}%»
«/td»«/tr»
«tr»«td align="center"»«br»«a href="javascript:this.close();"»닫기«/a»«tr»«/td»
«/table»
«/center»
«/body»
«/html»


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
//-->     
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