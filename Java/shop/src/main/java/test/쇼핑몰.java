***MVC 패턴으로 만드는 쇼핑몰***
1. 개발을 위한 설정.
 : /WebContent/META-INF/context.xml
<?xml version="1.0" encoding="UTF-8"?>
<Context> 
     <Resource name="jdbc/OracleDB" 
         auth="Container"
         type="javax.sql.DataSource" 
         username="ezen" 
         password="oracle"
         driverClassName="oracle.jdbc.driver.OracleDriver"
         factory="org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory"
         url="jdbc:oracle:thin:@localhost:1521:XE"
         maxActive="500" 
         maxIdle="100"/>  
 </Context>

  : web.xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app id="WebApp_ID" version="2.4" 
	xmlns="http://java.sun.com/xml/ns/j2ee" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">

	<display-name>ShoppingMall-Example</display-name>

	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>index.htm</welcome-file>
		<welcome-file>index.jsp</welcome-file>
		<welcome-file>default.html</welcome-file>
		<welcome-file>default.htm</welcome-file>
		<welcome-file>default.jsp</welcome-file>
	</welcome-file-list>
	
	<resource-ref>
		<description>Connection</description>
		<res-ref-name>jdbc/OracleDB</res-ref-name>
		<res-type>javax.sql.DataSource</res-type>
		<res-auth>Container</res-auth>
	</resource-ref>
</web-app>

2. 회원 시스템 구현
 : 테이블 생성
CREATE TABLE MEMBER(
	MEMBER_ID VARCHAR2(20),
	MEMBER_PW VARCHAR2(15),
	MEMBER_NAME VARCHAR2(20),
	MEMBER_JUMIN1 INT,
	MEMBER_JUMIN2 INT,
	MEMBER_EMAIL VARCHAR2(25),
	MEMBER_EMAIL_GET VARCHAR2(7),
	MEMBER_MOBILE VARCHAR2(13),
	MEMBER_PHONE VARCHAR2(13),
	MEMBER_ZIPCODE VARCHAR2(13),
	MEMBER_ADDR1 VARCHAR2(70),
	MEMBER_ADDR2 VARCHAR2(70),
	MEMBER_ADMIN INT,
	MEMBER_JOIN_DATE DATE
);

CREATE TABLE ZIPCODE (
  ID INT,
  ZIPCODE VARCHAR2(7),
  SIDO VARCHAR2(20),
  GUGUN VARCHAR2(13),
  DONG VARCHAR2(24),
  RI VARCHAR2(36),
  BUNJI VARCHAR2(17),
  PRIMARY KEY (ID)
);

create sequence zipcode_id_seq;

insert into zipcode
values(zipcode_id_seq.NEXTVAL,'123-456','서울','종로구','관철동','','13-13');

commit;

 : MemberBean
package net.member.db;

import java.sql.*;

public class MemberBean {
	private String MEMBER_ID;
	private String MEMBER_PW;
	private String MEMBER_NAME;
	private int MEMBER_JUMIN1;
	private int MEMBER_JUMIN2;
	private String MEMBER_EMAIL;
	private String MEMBER_EMAIL_GET;
	private String MEMBER_MOBILE;
	private String MEMBER_PHONE;
	private String MEMBER_ZIPCODE;
	private String MEMBER_ADDR1;
	private String MEMBER_ADDR2;
	private int MEMBER_ADMIN;
	private Timestamp MEMBER_JOIN_DATE;
	
	public String getMEMBER_ID() {
		return MEMBER_ID;
	}
	public void setMEMBER_ID(String member_id) {
		MEMBER_ID = member_id;
	}
	public String getMEMBER_PW() {
		return MEMBER_PW;
	}
	public void setMEMBER_PW(String member_pw) {
		MEMBER_PW = member_pw;
	}
	public String getMEMBER_NAME() {
		return MEMBER_NAME;
	}
	public void setMEMBER_NAME(String member_name) {
		MEMBER_NAME = member_name;
	}
	public int getMEMBER_JUMIN1() {
		return MEMBER_JUMIN1;
	}
	public void setMEMBER_JUMIN1(int member_jumin1) {
		MEMBER_JUMIN1 = member_jumin1;
	}
	public int getMEMBER_JUMIN2() {
		return MEMBER_JUMIN2;
	}
	public void setMEMBER_JUMIN2(int member_jumin2) {
		MEMBER_JUMIN2 = member_jumin2;
	}
	public String getMEMBER_EMAIL() {
		return MEMBER_EMAIL;
	}
	public void setMEMBER_EMAIL(String member_email) {
		MEMBER_EMAIL = member_email;
	}
	public String getMEMBER_EMAIL_GET() {
		return MEMBER_EMAIL_GET;
	}
	public void setMEMBER_EMAIL_GET(String member_email_get) {
		MEMBER_EMAIL_GET = member_email_get;
	}
	public String getMEMBER_MOBILE() {
		return MEMBER_MOBILE;
	}
	public void setMEMBER_MOBILE(String member_mobile) {
		MEMBER_MOBILE = member_mobile;
	}
	public String getMEMBER_PHONE() {
		return MEMBER_PHONE;
	}
	public void setMEMBER_PHONE(String member_phone) {
		MEMBER_PHONE = member_phone;
	}
	public String getMEMBER_ZIPCODE() {
		return MEMBER_ZIPCODE;
	}
	public void setMEMBER_ZIPCODE(String member_zipcode) {
		MEMBER_ZIPCODE = member_zipcode;
	}
	public String getMEMBER_ADDR1() {
		return MEMBER_ADDR1;
	}
	public void setMEMBER_ADDR1(String member_addr1) {
		MEMBER_ADDR1 = member_addr1;
	}
	public String getMEMBER_ADDR2() {
		return MEMBER_ADDR2;
	}
	public void setMEMBER_ADDR2(String member_addr2) {
		MEMBER_ADDR2 = member_addr2;
	}
	public int getMEMBER_ADMIN() {
		return MEMBER_ADMIN;
	}
	public void setMEMBER_ADMIN(int member_admin) {
		MEMBER_ADMIN = member_admin;
	}
	public Timestamp getMEMBER_JOIN_DATE() {
		return MEMBER_JOIN_DATE;
	}
	public void setMEMBER_JOIN_DATE(Timestamp member_join_date) {
		MEMBER_JOIN_DATE = member_join_date;
	}	
}

 : MemberDAO
package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	public MemberDAO() {
		try{
			Context initCtx=new InitialContext();
			Context envCtx=(Context)initCtx.lookup("java:comp/env");
			DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			con=ds.getConnection();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public boolean insertMember(MemberBean mb) throws SQLException{
		String sql=null;
		
		try{
			sql="insert into member values "+
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mb.getMEMBER_ID());
			pstmt.setString(2, mb.getMEMBER_PW());
			pstmt.setString(3, mb.getMEMBER_NAME());
			pstmt.setInt(4, mb.getMEMBER_JUMIN1());
			pstmt.setInt(5, mb.getMEMBER_JUMIN2());
			pstmt.setString(6, mb.getMEMBER_EMAIL());
			pstmt.setString(7, mb.getMEMBER_EMAIL_GET());
			pstmt.setString(8, mb.getMEMBER_MOBILE());
			pstmt.setString(9, mb.getMEMBER_PHONE());
			pstmt.setString(10, mb.getMEMBER_ZIPCODE());
			pstmt.setString(11, mb.getMEMBER_ADDR1());
			pstmt.setString(12, mb.getMEMBER_ADDR2());
			pstmt.setInt(13, mb.getMEMBER_ADMIN());
			pstmt.setTimestamp(14, mb.getMEMBER_JOIN_DATE());
			pstmt.executeUpdate();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close();}

		}
		
		return false;
	}
	
	public int userCheck(String id, String pw) throws SQLException{
		String sql=null;
		int x=-1;
		
		try{
			sql="select MEMBER_PW from member where MEMBER_ID=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()){
				String memberpw=rs.getString("MEMBER_PW");
				
				if(memberpw.equals(pw)){
					x=1;
				}else{
					x=0;
				}
			}else{
				x=-1;
			}
			
			return x;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close(); }
			if(rs != null){ rs.close(); }
			
		}
		
		return -1;
	}
	
	public int confirmId(String id) throws SQLException{
		String sql=null;
		int x=-1;
		
		try{
			sql="select MEMBER_ID from member where MEMBER_ID=? ";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				x=1;
			}else{
				x=-1;
			}
			
			return x;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close(); }
			if(rs != null){ rs.close(); }
			
		}
		
		return -1;
	}
	
	public MemberBean getMember(String id) throws SQLException{
		MemberBean member=null;
		String sql=null;
		
		try{
			sql="select * from member where MEMBER_ID=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				member=new MemberBean();
				
				member.setMEMBER_ID(rs.getString("MEMBER_ID"));
				member.setMEMBER_NAME(rs.getString("MEMBER_NAME"));
				member.setMEMBER_JUMIN1(rs.getInt("MEMBER_JUMIN1"));
				member.setMEMBER_JUMIN2(rs.getInt("MEMBER_JUMIN2"));
				member.setMEMBER_EMAIL(rs.getString("MEMBER_EMAIL"));
				member.setMEMBER_EMAIL_GET(
						rs.getString("MEMBER_EMAIL_GET"));
				member.setMEMBER_MOBILE(
						rs.getString("MEMBER_MOBILE"));
				member.setMEMBER_PHONE(
						rs.getString("MEMBER_PHONE"));
				member.setMEMBER_ZIPCODE(
						rs.getString("MEMBER_ZIPCODE"));
				member.setMEMBER_ADDR1(rs.getString("MEMBER_ADDR1"));
				member.setMEMBER_ADDR2(rs.getString("MEMBER_ADDR2"));
				
				return member;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close(); }
			if(rs != null){ rs.close(); }
			
		}
		
		return null;
	}
	
	public void updateMember(MemberBean mb) throws SQLException{
		String sql=null;
		
		try{
			sql="update member set MEMBER_PW=?,MEMBER_NAME=?,"+
			"MEMBER_EMAIL=?,MEMBER_EMAIL_GET=?,MEMBER_MOBILE=?,"+
			"MEMBER_PHONE=?,MEMBER_ZIPCODE=?,MEMBER_ADDR1=?,"+
			"MEMBER_ADDR2=? where MEMBER_ID=?";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mb.getMEMBER_PW());
			pstmt.setString(2, mb.getMEMBER_NAME());
			pstmt.setString(3, mb.getMEMBER_EMAIL());
			pstmt.setString(4, mb.getMEMBER_EMAIL_GET());
			pstmt.setString(5, mb.getMEMBER_MOBILE());
			pstmt.setString(6, mb.getMEMBER_PHONE());
			pstmt.setString(7, mb.getMEMBER_ZIPCODE());
			pstmt.setString(8, mb.getMEMBER_ADDR1());
			pstmt.setString(9, mb.getMEMBER_ADDR2());
			pstmt.setString(10, mb.getMEMBER_ID());			
			pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close();}
			
		}
	}
	
	public int deleteMember(String id, String pw) throws SQLException{
		String sql=null;
		int x=-1;
		
		try{
			sql="select MEMBER_PW from member where MEMBER_ID=?";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				String memberpw=rs.getString("MEMBER_PW");
				if(memberpw.equals(pw)){
					sql="delete from member where MEMBER_ID=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1, id);
					pstmt.executeUpdate();
					x=1;
				}else{
					x=0;
				}
				
				return x;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close(); }
			
		}
		
		return -1;
	}	
	
	public MemberBean findId(String name, String jumin1, String jumin2)
	throws SQLException{
		String sql=null;
		MemberBean member=new MemberBean();
		
		try{
			sql="select MEMBER_ID, MEMBER_PW, MEMBER_JUMIN1,"+
				"MEMBER_JUMIN2 from member where MEMBER_NAME=?";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				String memberjumin1=rs.getString("MEMBER_JUMIN1");
				String memberjumin2=rs.getString("MEMBER_JUMIN2");
				
				if(memberjumin1.equals(jumin1) && 
						memberjumin2.equals(jumin2)){
					member.setMEMBER_ID(
							rs.getString("MEMBER_ID"));
					member.setMEMBER_PW(
							rs.getString("MEMBER_PW"));
					
					return member;
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){ pstmt.close(); }
			if(rs != null){ rs.close(); }
			
		}
		
		return null;
	}
	
	public boolean isAdmin(String id){
		String sql="select MEMBER_ADMIN from MEMBER where MEMBER_ID=?";
		int member_admin=0;
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			rs.next();
			
			member_admin=rs.getInt("MEMBER_ADMIN");
			
			if(member_admin==1){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List searchZipcode(String searchdong){
		String sql="select * from zipcode where dong like ?";
		List zipcodeList=new ArrayList();
		
		try{
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, "%"+searchdong+"%");
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				String sido=rs.getString("sido");
				String gugun=rs.getString("gugun");
				String dong=rs.getString("dong");  
				String ri=rs.getString("ri"); 
				String bunji=rs.getString("bunji");
				if(ri == null) ri="";
				if(bunji == null) bunji="";
				
				String zipcode=rs.getString("zipcode");
				String addr=sido+ " "+gugun+ " "+dong+ " "+ri+ " "+bunji;
				
				zipcodeList.add(zipcode+","+addr);
			}
			
			return zipcodeList;
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
}

: 회원에 관련된 Action 클래스 구현
[Action.java]
package net.member.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.member.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[MemberJoinAction.java]
package net.member.action;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.MemberBean;
import net.member.db.MemberDAO;

public class MemberJoinAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) 
	throws Exception{
		request.setCharacterEncoding("euc-kr");
		
		MemberDAO memberdao=new MemberDAO();
		MemberBean dto=new MemberBean();
		ActionForward forward=null;
		
		dto.setMEMBER_ID(request.getParameter("MEMBER_ID"));
		dto.setMEMBER_PW(request.getParameter("MEMBER_PW"));
		dto.setMEMBER_NAME(request.getParameter("MEMBER_NAME"));
		dto.setMEMBER_JUMIN1(
				Integer.parseInt(request.getParameter("MEMBER_JUMIN1")));
		dto.setMEMBER_JUMIN2(
				Integer.parseInt(request.getParameter("MEMBER_JUMIN2")));
		dto.setMEMBER_EMAIL(request.getParameter("MEMBER_EMAIL1")+"@"+
				request.getParameter("MEMBER_EMAIL2"));
		dto.setMEMBER_EMAIL_GET(request.getParameter("MEMBER_EMAIL_GET"));
		dto.setMEMBER_MOBILE(request.getParameter("MEMBER_MOBILE"));
		dto.setMEMBER_PHONE(request.getParameter("MEMBER_PHONE"));
		dto.setMEMBER_ZIPCODE(request.getParameter("MEMBER_ZIPCODE1")+ " - " +
				request.getParameter("MEMBER_ZIPCODE2"));
		dto.setMEMBER_ADDR1(request.getParameter("MEMBER_ADDR1"));
		dto.setMEMBER_ADDR2(request.getParameter("MEMBER_ADDR2"));
		dto.setMEMBER_ADMIN(0);
		dto.setMEMBER_JOIN_DATE(new Timestamp(System.currentTimeMillis()));
		
		memberdao.insertMember(dto);
		
		response.setContentType("text/html; charset=euc-kr");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('회원가입에 성공하였습니다.');");
		out.println("location.href='./MemberLogin.me';");
		out.println("</script>");			
		out.close();
		
		return forward;
	}
}

[MemberLoginAction.java]
package net.member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberDAO;

public class MemberLoginAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		HttpSession session=request.getSession();
		ActionForward forward=new ActionForward();
		MemberDAO memberdao=new MemberDAO();
		
		String id=request.getParameter("MEMBER_ID");
		String pass=request.getParameter("MEMBER_PW");
		
		int check=memberdao.userCheck(id, pass);
		if(check == 1){
			session.setAttribute("id", id);
			
			if(memberdao.isAdmin(id)){
				forward.setRedirect(true);
				forward.setPath("./GoodsList.ag"); 
				return forward;
			}else{
				forward.setRedirect(true);
				forward.setPath("./GoodsList.go?item=new_item"); 
				return forward;
			}
		}else if(check == 0){
			response.setContentType("text/html; charset=euc-kr");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 일치하지 않습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}else{
			response.setContentType("text/html; charset=euc-kr");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('아이디가 존재하지 않습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		
		return null;
	}
}

[MemberModifyAction_1.java]
package net.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberBean;
import net.member.db.MemberDAO;

public class MemberModifyAction_1 implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		ActionForward forward=new ActionForward();
		HttpSession sesseion=request.getSession(true);
		String id=(String)sesseion.getAttribute("id");
		
		if(id==null){
			forward.setRedirect(true);
			forward.setPath("./MemberLogin.me");
			return forward;
		}
		
		MemberDAO memberdao=new MemberDAO();
		MemberBean dto=memberdao.getMember(id);
		
		request.setAttribute("member", dto);
		
		forward.setRedirect(false);
		forward.setPath("./member/member_info.jsp"); 			
		return forward;
	}
}

[MemberModifyAction_2.java]
package net.member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberBean;
import net.member.db.MemberDAO;

public class MemberModifyAction_2 implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		request.setCharacterEncoding("euc-kr");
		
		ActionForward forward=new ActionForward();
		HttpSession sesseion=request.getSession();
		String id=(String)sesseion.getAttribute("id");
		
		if(id==null){
			forward.setRedirect(true);
			forward.setPath("./MemberLogin.me");
			return forward;
		}
		
		MemberDAO memberdao=new MemberDAO();
		MemberBean dto=new MemberBean();
		
		dto.setMEMBER_ID(id);
		dto.setMEMBER_NAME(request.getParameter("MEMBER_NAME"));
		dto.setMEMBER_PW(request.getParameter("MEMBER_PW"));
		dto.setMEMBER_EMAIL(request.getParameter("MEMBER_EMAIL1")+"@"+
				request.getParameter("MEMBER_EMAIL2"));
		dto.setMEMBER_EMAIL_GET(request.getParameter("MEMBER_EMAIL_GET"));
		dto.setMEMBER_MOBILE(request.getParameter("MEMBER_MOBILE"));
		dto.setMEMBER_PHONE(request.getParameter("MEMBER_PHONE"));
		dto.setMEMBER_ZIPCODE(request.getParameter("MEMBER_ZIPCODE1")+" - "+
				request.getParameter("MEMBER_ZIPCODE2"));
		dto.setMEMBER_ADDR1(request.getParameter("MEMBER_ADDR1"));
		dto.setMEMBER_ADDR2(request.getParameter("MEMBER_ADDR2"));
		
		memberdao.updateMember(dto);
		
		response.setContentType("text/html; charset=euc-kr");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('회원정보 수정에 성공하였습니다.');");
		out.println("</script>");
		
		forward.setRedirect(false);
		forward.setPath("./MemberModifyAction_1.me"); 			
		return forward;
	}
}

[MemberDeleteAction.java]
package net.member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberDAO;

public class MemberDeleteAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		ActionForward forward=new ActionForward();
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		
		if(id==null){
			forward.setRedirect(true);
			forward.setPath("./MemberLogin.me");
			return forward;
		}
		
		MemberDAO memberdao=new MemberDAO();
		String pass=request.getParameter("MEMBER_PW");
		
		try{
			int check=memberdao.deleteMember(id, pass);
			
			if(check == 1){
				session.invalidate();
				
				forward.setRedirect(false);
				forward.setPath("./member/member_out_ok.jsp"); 
				return forward;
			}else{
				response.setContentType("text/html; charset=euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호가 맞지않습니다.');");
				out.println("history.go(-1);");
				out.println("</script>");
				out.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

[MemberFindAction.java]
package net.member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.MemberBean;
import net.member.db.MemberDAO;

public class MemberFindAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		request.setCharacterEncoding("euc-kr");
		
		ActionForward forward=new ActionForward();
		MemberDAO memberdao=new MemberDAO();
		MemberBean member=new MemberBean();
		
		String name=request.getParameter("MEMBER_NAME");
		String jumin1=request.getParameter("MEMBER_JUMIN1");
		String jumin2=request.getParameter("MEMBER_JUMIN2");
		
		member= memberdao.findId(name, jumin1, jumin2);
		
		if(member!=null){
			request.setAttribute("id", member.getMEMBER_ID());
			request.setAttribute("passwd", member.getMEMBER_PW());
			
			forward.setRedirect(false);
			forward.setPath("./member/member_find_ok.jsp"); 
			return forward;
		}else{
			response.setContentType("text/html; charset=euc-kr");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('입력한 정보가 일치하지 않습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		 
		return null;
	}
}

[MemberIDCheckAction.java]
package net.member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.MemberDAO;

public class MemberIDCheckAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		ActionForward forward=new ActionForward();
		
		String id=request.getParameter("MEMBER_ID");
		
		MemberDAO memberdao=new MemberDAO();
		int check=memberdao.confirmId(id);	
		
		request.setAttribute("id", id);
		request.setAttribute("check", check);
		
		forward.setRedirect(false);
		forward.setPath("./member/member_idchk.jsp"); 			
		return forward;
	}
}

[MemberZipcodeAction.java]
package net.member.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.member.db.MemberDAO;

public class MemberZipcodeAction implements Action{
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		request.setCharacterEncoding("euc-kr");
		ActionForward forward=new ActionForward();
		MemberDAO memberdao=new MemberDAO();
		List zipcodeList=new ArrayList();
		
		String searchdong=request.getParameter("dong");
		zipcodeList=memberdao.searchZipcode(searchdong);
		
		request.setAttribute("zipcodelist", zipcodeList);
		forward.setRedirect(false);
		forward.setPath("./member/member_zipcode.jsp"); 
		return forward;
	}
}

[MemberFrontController.java]
package net.member.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberFrontController extends HttpServlet{
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{
		 String RequestURI=request.getRequestURI();
		 String contextPath=request.getContextPath();
		 String command=RequestURI.substring(contextPath.length());
		 ActionForward forward=null;
		 Action action=null;
		 
		 if(command.equals("/MemberLogin.me")){
			 forward=new ActionForward();
			 forward.setRedirect(false);
			 forward.setPath("./member/member_login.jsp");
		 }else if(command.equals("/MemberJoin.me")){
			 forward=new ActionForward();
			 forward.setRedirect(false);
			 forward.setPath("./member/member_join.jsp");
		 }else if(command.equals("/MemberFind.me")){
			 forward=new ActionForward();
			 forward.setRedirect(false);
			 forward.setPath("./member/member_find.jsp");
		 }else if(command.equals("/MemberOut.me")){
			 forward=new ActionForward();
			 forward.setRedirect(false);
			 forward.setPath("./member/member_out.jsp");
		 }else if(command.equals("/Zipcode.me")){
			 forward=new ActionForward();
			 forward.setRedirect(false);
			 forward.setPath("./member/member_zipcode.jsp");
		 }else if(command.equals("/MemberLoginAction.me")){
			 action  = new MemberLoginAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberJoinAction.me")){
			 action  = new MemberJoinAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberModifyAction_1.me")){
			 action  = new MemberModifyAction_1();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberModifyAction_2.me")){
			 action  = new MemberModifyAction_2();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberDeleteAction.me")){
			 action  = new MemberDeleteAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberIDCheckAction.me")){
			 action  = new MemberIDCheckAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberFindAction.me")){
			 action  = new MemberFindAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }else if(command.equals("/MemberZipcodeAction.me")){
			 action  = new MemberZipcodeAction();
			 try {
				 forward=action.execute(request, response);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
		 
		 if(forward.isRedirect()){
			 response.sendRedirect(forward.getPath());
		 }else{
			 RequestDispatcher dispatcher=
				 request.getRequestDispatcher(forward.getPath());
			 dispatcher.forward(request, response);
		 }	   
	}
}

[web.xml]
.....
<servlet>
	<servlet-name>MemberFrontController</servlet-name>
	<servlet-class>net.member.action.MemberFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>MemberFrontController</servlet-name>
	<url-pattern>*.me</url-pattern>
</servlet-mapping>
.....

: View 페이지 작성(/WebContent/member/)
[member_join.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<html>
<head>
<title>쇼핑몰</title>
<script>
function check(){
	var id=joinform.MEMBER_ID.value;
	var password1=joinform.MEMBER_PW.value;
	var password2=joinform.MEMBER_PW2.value;	
	var email1=joinform.MEMBER_EMAIL1.value;
	var email2=joinform.MEMBER_EMAIL2.value;
	var phone=joinform.MEMBER_PHONE.value;
	var addr1=joinform.MEMBER_ADDR1.value;
	var addr2=joinform.MEMBER_ADDR2.value;
	var mobile=joinform.MEMBER_MOBILE.value;
	
	var forms = document.getElementById("joinform");

	if ((forms.MEMBER_NAME.value=="")||(forms.MEMBER_NAME.value.length<=1)){
		alert("이름을 제대로 입력해 주세요.");
		forms.MEMBER_NAME.focus();
         		return false;
	}
	if(id.length == 0){
		alert("아이디를 입력하세요.");
		joinform.MEMBER_ID.focus();
		return false;
	}
	if(password1.length == 0){
		alert("비밀번호를 입력하세요.");
		joinform.MEMBER_PW.focus();
		return false;
	} 
	if(password1 != password2){
		alert("비밀번호가 일치하지 않습니다.");
		joinform.MEMBER_PW.value="";
		joinform.MEMBER_PW2.value="";
		joinform.MEMBER_PW.focus();
		return false;
	}
	if((forms.MEMBER_JUMIN1.value=="")||(forms.MEMBER_JUMIN1.value.length<6)){
		alert("주민등록번호 앞의 6자리를 입력해 주세요.");
      	forms.MEMBER_JUMIN1.focus();
        return false;
 	}
 	if((forms.MEMBER_JUMIN2.value=="")||(forms.MEMBER_JUMIN2.value.length<7)){
		alert("주민등록번호 뒤의 7자리를 입력해 주세요.");
      	forms.MEMBER_JUMIN2.focus();
        return false;
	} 
	if(email1.length == 0 || email2.length ==0){
		alert("이메일을 제대로 입력하세요.");
		joinform.MEMBER_EMAIL1.focus();
		return false;
	}
	if(phone.length == 0){
		alert("집 전화를 입력하세요.");
		joinform.MEMBER_PHONE.focus();
		return false;
	} 
	if((forms.MEMBER_ZIPCODE1.value=="")||(forms.MEMBER_ZIPCODE1.value.length<3)){
		alert("우편번호 앞의 3자리를 입력해 주세요.");
      	forms.MEMBER_ZIPCODE1.focus();
        return false;
 	}
 	if((forms.MEMBER_ZIPCODE2.value=="")||(forms.MEMBER_ZIPCODE2.value.length<3)){
		alert("우편번호 뒤의 3자리 입력해 주세요.");
      	forms.MEMBER_ZIPCODE2.focus();
        return false;
	}  
	if(addr1.length == 0){
		alert("집 주소를 입력하세요.");
		joinform.MEMBER_ADDR1.focus();
		return false;
	} 
	if(addr2.length == 0){
		alert("상세 주소를 입력하세요.");
		joinform.MEMBER_ADDR2.focus();
		return false;
	} 
	if(mobile.length == 0){
		alert("휴대폰 번호를 입력하세요.");
		joinform.MEMBER_MOBILE.focus();
		return false;
	}
	
	return true;
}

function openConfirmId(joinform){			
	var id=joinform.MEMBER_ID.value;
	var url="./MemberIDCheckAction.me?MEMBER_ID="+joinform.MEMBER_ID.value;
	
	if(id.length == 0){
		alert("아이디를 입력하세요.");
		joinform.MEMBER_ID.focus();
		return false;
	}
	open(url, "confirm", "toolbar=no,location=no,status=no,menubar=no,"+
						 "scrollbars=no,resizable=no,width=400,height=200");
}

function openZipcode(joinform){			
	var url="./Zipcode.me"
	open(url, "confirm", "toolbar=no,location=no,"
						+"status=no,menubar=no,"
						+"scrollbars=yes,resizable=no,"
						+"width=410,height=400");
}	

function gNumCheck(){
	if(event.keyCode >=48 && event.keyCode <=57) {
		return true;
	}else{
		event.returnValue=false;	
	}
}		
</script>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0" align="center">
	<tr>
	<td colspan=2>
	<!-- 회원가입 -->
	<form name="joinform" action="./MemberJoinAction.me" method="post" 
		onsubmit="return check()">		
	<p align="center">	
	<table border="1" width="80%" height="80%">
	<tr>
		<td width="17%" bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;이름</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="text" name="MEMBER_NAME" size="20"/>
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;아이디</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="text" name="MEMBER_ID" size="10" maxlength=15/>
			<input type="button" name="confirm_id" value="중복확인" 
				onclick="openConfirmId(this.form)" />
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;비밀번호</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="password" name="MEMBER_PW" size="15"/>
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;비밀번호 확인</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="password" name="MEMBER_PW2" size="15" />
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">&nbsp;</td>
		<td>
		<font size="2">&nbsp;&nbsp;&nbsp;
		(아이디와 비밀번호는 문자와 숫자를 조합하여 2~12자리로 만들어 주세요)</font>
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;주민등록번호</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="text" name="MEMBER_JUMIN1" size="12" 
				onkeypress="gNumCheck()" maxlength="6"/>-	
			<input type="text" name="MEMBER_JUMIN2" size="12" 
				onkeypress="gNumCheck()" maxlength="7"/>
		</td>
	</tr>
	<tr>
		<td bgcolor="#f5f5f5">
			<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;이메일 주소</font>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;
			<input type="text" name="MEMBER_EMAIL1" size="13"/>@
			<input type="text" name="MEMBER_EMAIL2" size="15"/> 
		</td>
		</tr>
		<tr>
		<td bgcolor="#f5f5f5">
		<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;메일 수신 여부</font>
		</td>
		<td>
		&nbsp;&nbsp;&nbsp;
		<input type="radio" name="MEMBER_EMAIL_GET" value="YES" checked/>
		<font size="2">수신</font>
		&nbsp;&nbsp;<input type="radio" name="MEMBER_EMAIL_GET" value="NO"/>
		<font size="2">수신 안함</font>
		</td>
		</tr>
		<tr>
			<td bgcolor="#f5f5f5">
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;집전화</font>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;
				<input type="text" name="MEMBER_PHONE" size="24"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#f5f5f5">
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;우편번호</font>
			</td>
			<td>
			&nbsp;&nbsp;&nbsp;
			<input type="text" name="MEMBER_ZIPCODE1" size="6" 
				onkeypress="gNumCheck()" maxlength="3"/>- 
			<input type="text" name="MEMBER_ZIPCODE2" size="6" 
				onkeypress="gNumCheck()" maxlength="3" />&nbsp;&nbsp;
			<input type="button" name="zipcode" value="우편번호 검색" 
				onclick="openZipcode(this.form)" />
			</td>
		</tr>
		<tr>
			<td bgcolor="#f5f5f5">
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;집주소</font>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;
				<input type="text" name="MEMBER_ADDR1" size="50" />
			</td>
		</tr>
		<tr>
			<td bgcolor="#f5f5f5">
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;상세주소</font>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;
				<input type="text" name="MEMBER_ADDR2" size="50" />
			</td>
		</tr>
		<tr>
			<td bgcolor="#f5f5f5">
				<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;휴대폰</font>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;
				<input type="text" name="MEMBER_MOBILE" size="24" />
				</td>
			</tr>
		</table>
		<table width="80%">
			<tr>
				<td align="center">
					<br/><input type="submit" value="확 인" />
				</td>
			</tr>
		</table>
		</form>
		<!-- 회원가입 -->	
		</td>
	</tr>
</table>
</body>
</html>

[member_login.jsp]
<%@ page contentType="text/html; charset=euc-kr"%>
<html>
<head>
<title>쇼핑몰</title>
<script>
function check(){
	var id=loginform.MEMBER_ID.value;
	var pass=loginform.MEMBER_PW.value;
	
	if(id.length == 0){
		alert("아이디를 입력하세요.");
		loginform.MEMBER_ID.focus();
		return false;
	}
	if(pass.length == 0){
		alert("비밀번호를 입력하세요.");
		loginform.MEMBER_PW.focus();
		return false;
	}
	
	return true;
}
function openConfirmId(loginform){	
	var url="./MemberFind.me";
	open(url, "confirm", "toolbar=no,location=no,status=no,menubar=no,"+
						 "scrollbars=no,resizable=no,width=450px,height=300");
}
</script>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
<tr>
<td colspan=2 align=center>
<table border=0 cellpadding=0 cellspacing=0 width=500>
<!--회원 로그인 -->
<tr>
<form action="./MemberLoginAction.me" method=post name=loginform
	onsubmit="return check()">
<td><br><br>
<table width="400" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="f6f6f6">
		<table width="400" border="0" cellspacing="4" cellpadding="0">
		<tr>
		<td valign="top" bgcolor="#FFFFFF">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td align="center">
		<table cellpadding=0 cellspacing=0 border=0>
			<tr>
			<td width=73>아이디</td>
			<td width=9>:</td>
			<td width=103>
				<input type=text name="MEMBER_ID" size=12 maxlength=20>
			</td>
			<td width=66 rowspan=3><input type="submit" value="로그인">
			</td>
			<td width=26 rowspan=3></td>
			</tr>
			<tr>
			<td height=4 colspan=3></td>
			</tr>
			<tr>
			<td width=73>비밀번호</td>
			<td width=9>:</td>
			<td width=103>
			<input type=password name="MEMBER_PW" size=12 maxlength=12>
			</td>
			</tr>
			<tr>
			<td height=35 colspan=6 align="center">
			<input
				type="button" value="회원가입"
				onclick="javascript:window.location='./MemberJoin.me'">
			<a href="#">
			<input type="button" value="아이디/비밀번호 찾기"
				onclick="openConfirmId(this.form)">
			</a>
			</td>
			</tr>
		</table>
		</td>
		</tr>
		<tr>
			<td style="padding: 15 0 15 70;">
			<table width="400" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td width="8"><img src="#" width="8" height="7">
				</td>
				<td width="392">
				<font size=2 color="565656">
				아이디가 없을 경우 '회원가입'을 클릭하십시오.
				</font>
				</td>
				</tr>
				<tr>
				<td><img src="#" width="8" height="7"></td>
				<td>
				<font size=2 color="565656">
				아이디 또는 비밀번호를 잊어버렸을 경우 '아이디/비밀번호 찾기'를 클릭하십시오.
				</font>
				</td>
				</tr>
			</table>
			</td>
		</tr>
		</table>
		</td>
		</tr>
		</table>
		</td>
	</tr>
</table>
</td>
</form>
</tr>
</table>
<!-- 회원 로그인 -->
</td>
</tr>
</table>
</body>
</html>

[member_idchk.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<%
	String id=(String)request.getAttribute("id");
	int check=((Integer)(request.getAttribute("check"))).intValue();
%>
<html>
<head>
<title>쇼핑몰</title>
<script>
function windowclose(){
	opener.document.joinform.MEMBER_ID.value="<%=id %>";
	self.close();
}
</script>
</head>
<body bgcolor="#f5f5f5">
<% if(check == 1){ %>
<table width="360" border="0" cellspacing="0" cellpadding="5">
	<tr align="center">
	<td height="30">
		<font size="2"><%=id %> 는 이미 사용 중인 아이디입니다.</font>
	</td>
	</tr>
</table>

<form action="./MemberIDCheckAction.me" method="post" name="checkForm" >
<table width="360" border="0" cellspacing="0" cellpadding="5">
	<tr>
	<td align="center">
		<font size="2">다른 아이디를 선택하세요.</font><p>
		<input type="text" size="10" maxlength="12" name="MEMBER_ID"/>
		<input type="submit" value="중복확인" />
	</td>					
	</tr>
</table>
</form>
<% }else{ %>
<table width="360" border="0" cellspacing="0" cellpadding="5">
	<tr>
		<td align="center">
		<font size="2">입력하신 <%=id %> 는 사용할 수 있는 아이디입니다.</font>
		<br/><br/>
		<input type="button" value="닫기" onclick="windowclose()" />
		</td>
	</tr>
</table>
<% } %>
</body>
</html>

[member_find.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<html>
<head>
<script>
function formSubmit(){
	var forms = document.getElementById("findform");
	
	if ((forms.MEMBER_NAME.value=="") ||
		(forms.MEMBER_NAME.value.length<=1)){
		alert("이름을 정확히 입력해 주십시오.");
		forms.MEMBER_NAME.focus();
        return false;
	}else if((forms.MEMBER_JUMIN1.value=="") ||
			(forms.MEMBER_JUMIN1.value.length<6)){
		alert("주민등록번호를 정확히 입력해 주십시오.");
   		forms.MEMBER_JUMIN1.focus();
        return false;
 	}else if((forms.MEMBER_JUMIN2.value=="")||
 			(forms.MEMBER_JUMIN2.value.length<7)){
		alert("주민등록번호를 정확히 입력해 주십시오.");
      	forms.MEMBER_JUMIN2.focus();
        return false;
	}
	
	return true;
}
</script>
</head>
<body>
<table width="450px" height="20px">
	<tr>
		<td align="left">
			<b>아이디/비밀번호 찾기</b>
		</td>
	</tr>
</table>	
<form action="./MemberFindAction.me" method="post" name="findform" 
	onSubmit="return formSubmit();">
<table width="450px" cellspacing="0" cellpadding="0" border="0">
<thead>
	<font size="2">				
	&nbsp;&nbsp;&nbsp;&nbsp;
	이름과 주민등록번호를 정확히 입력해주세요.
	<br/><br/><br/><br/></font>
</thead>
<tr>
	<td height="29" bgcolor="#F3F3F3">
		<font size="2">이름</font>
	</td>
	<td>
		&nbsp;
		<input type="text" name="MEMBER_NAME" maxlength="12" size="14">
	</td>
</tr>
<tr>
	<td colspan="2" height="1"></td>
</tr>
<tr>
	<td height="29" bgcolor="#F3F3F3">
		<font size="2">주민등록번호</font>
	</td>
	<td>
		&nbsp;
		<input type="password" name="MEMBER_JUMIN1" maxlength="6" size="12" > - 
		<input type="password" name="MEMBER_JUMIN2" maxlength="7" size="12">
	</td>
</tr>
<tr>
	<td colspan="2" style="padding:10px 0 20px 0" align="center">
		<input type="submit" value="확인">
	</td>
</tr>				
</table>
</form>	
</body>
</html>

[member_find_ok.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<% 
	String id=(String)request.getAttribute("id"); 
	String passwd=(String)request.getAttribute("passwd"); 
%>

<html>
<head>
<script>
function windowclose(){
	self.close();
}
</script>
</head>
<body>
<table width="450px" height="35px">
	<tr><td align="left">
	<b>아이디/비밀번호 찾기</b>
	</td></tr>
</table>
	
<table width="440px">
	<thead>검색된 아이디/비밀번호입니다.<br/><br/><br/></thead>
	<tr><td align="left">아이디 : <%=id %></td></tr>
	<tr><td align="left">비밀번호 : <%=passwd %></td></tr>
</table>

<table width="450px">
	<tr>
		<td align="center">	
			<hr/><br/><input type="button" value="닫기" onclick="windowclose()"/>
		</td>
	</tr>
</table>
</body>
</html>

[member_out.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>

<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0" align="center">
<tr>
<td colspan=2>
<p align="center">
<form action="./MemberDeleteAction.me" method="post">
<table border="1" width="380" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" colspan="2">
			<font size="4"><b>회원 탈퇴</b></font>
		</td>
	</tr>
	<tr>
		<td align="center" height="35" width="125">
		<font size="2">비밀번호</font></td>
		<td>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="password" name="MEMBER_PW" />
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2" height="35">
			<input type="submit" value="회원 탈퇴" />
			<input type="reset" value="취 소" />
		</td>
	</tr>				
</table>
</form>				
</td>
</tr>
</table>
</body>
</html>

[member_out_ok.jsp]
<%@ page contentType="text/html; charset=euc-kr" %>
<html>
<head><title>쇼핑몰</title></head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0" align="center">
<tr><td colspan=2>
<p align="center">
<table width="380" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" height="50">
			<font size="4"><b>회원정보가 삭제되었습니다.</b></font>
		</td>
	</tr>		
</table>
</td></tr>
</table>
</body>
</html>

[member_zipcode.jsp]
<%@ page contentType="text/html; charset=euc-kr"%>
<%@ page import="java.util.*"%>
<%
	String addr="";
	String zipcode="";
	String zip1="";
	String zip2="";
	
	List zipcodeList=(ArrayList)request.getAttribute("zipcodelist");
%>

<html>
<head>
<title>쇼핑몰</title>
<script>
function setZipcode(zip1,zip2,addr){
	opener.document.forms[0].MEMBER_ZIPCODE1.value=zip1;
	opener.document.forms[0].MEMBER_ZIPCODE2.value=zip2;
	opener.document.forms[0].MEMBER_ADDR1.value=addr;
	self.close();
}
</script>
</head>
<body bgcolor="#f5f5f5">
<center>
<table width="370" border="0" cellspacing="0" cellpadding="5">
	<tr align="center">
		<td align="center">
			<font color="#ff4500">-우편번호 검색-</font><br>
		</td>
	</tr>
</table>
<form action="./MemberZipcodeAction.me" method="post" name="form">
<table width="370" border="0" cellspacing="0" cellpadding="5">
	<tr align="center">
		<td align="center">
			<font size="2">지역명 : </font>
			<input type="text" name="dong"/>
			<input type="submit" value="검색"><br>
			<font size="2">동을 입력하세요.(예:방배, 원천, 2글자 이상입력)</font>
		</td>
	</tr>
</table>
</form>
<br>
<table width="370" border="0" cellspacing="0" cellpadding="5">
<tr height="35">
	<td align="center" colspan="2">-검색결과-</td>
</tr>
<%	
	if(zipcodeList!=null && zipcodeList.size()!=0){	
		for(int i=0;i<zipcodeList.size();i++){
			String data=(String)zipcodeList.get(i);
			
			StringTokenizer st=new StringTokenizer(data,",");
			zipcode=st.nextToken();
			addr=st.nextToken();
			
			zip1=zipcode.split("-")[0];
			zip2=zipcode.split("-")[1];
%>
<tr>
	<td width="20%">
	<a href="#"	onclick="setZipcode(<%=zip1%>,<%=zip2%>,'<%=addr %>')">
		<font size="2"><%=zipcode%></font>
	</a>
	</td>
	<td width="80%"><font size="2"><%=addr %></font></td>
</tr>
<%		}
	}else{ %>
<tr>
	<td colspan="2" align="center">검색 결과가 없습니다.</td>
</tr>
<%	}%>
</table>
</center>
</body>
</html>

3. 상품 관리 시스템 구현
 : 테이블 생성
CREATE TABLE GOODS(
	GOODS_NUM INT,
	GOODS_CATEGORY VARCHAR2(20),
	GOODS_NAME VARCHAR2(50),
	GOODS_CONTENT VARCHAR2(3000),
	GOODS_SIZE VARCHAR2(10),
	GOODS_COLOR VARCHAR2(20),
	GOODS_AMOUNT INT,
	GOODS_PRICE INT,
	GOODS_IMAGE VARCHAR2(50),
	GOODS_BEST INT,
	GOODS_DATE DATE,
	PRIMARY KEY(GOODS_NUM)
);

 : GoodsBean
package net.admin.goods.db;

public class GoodsBean {
	private int GOODS_NUM;
	private String GOODS_CATEGORY;
	private String GOODS_NAME;
	private String GOODS_CONTENT;
	private String GOODS_SIZE;
	private String GOODS_COLOR;
	private String GOODS_IMAGE;
	private int GOODS_AMOUNT;
	private int GOODS_PRICE;
	private int GOODS_BEST;
	private String GOODS_DATE;
	
	public int getGOODS_NUM() {
		return GOODS_NUM;
	}
	public void setGOODS_NUM(int goods_num) {
		GOODS_NUM = goods_num;
	}
	public String getGOODS_CATEGORY() {
		return GOODS_CATEGORY;
	}
	public void setGOODS_CATEGORY(String goods_category) {
		GOODS_CATEGORY = goods_category;
	}
	public String getGOODS_NAME() {
		return GOODS_NAME;
	}
	public void setGOODS_NAME(String goods_name) {
		GOODS_NAME = goods_name;
	}
	public String getGOODS_CONTENT() {
		return GOODS_CONTENT;
	}
	public void setGOODS_CONTENT(String goods_content) {
		GOODS_CONTENT = goods_content;
	}
	public String getGOODS_SIZE() {
		return GOODS_SIZE;
	}
	public void setGOODS_SIZE(String goods_size) {
		GOODS_SIZE = goods_size;
	}
	public String getGOODS_COLOR() {
		return GOODS_COLOR;
	}
	public void setGOODS_COLOR(String goods_color) {
		GOODS_COLOR = goods_color;
	}
	public String getGOODS_IMAGE() {
		return GOODS_IMAGE;
	}
	public void setGOODS_IMAGE(String goods_image) {
		GOODS_IMAGE = goods_image;
	}
	public int getGOODS_AMOUNT() {
		return GOODS_AMOUNT;
	}
	public void setGOODS_AMOUNT(int goods_amount) {
		GOODS_AMOUNT = goods_amount;
	}
	public int getGOODS_PRICE() {
		return GOODS_PRICE;
	}
	public void setGOODS_PRICE(int goods_price) {
		GOODS_PRICE = goods_price;
	}
	public int getGOODS_BEST() {
		return GOODS_BEST;
	}
	public void setGOODS_BEST(int goods_best) {
		GOODS_BEST = goods_best;
	}
	public String getGOODS_DATE() {
		return GOODS_DATE;
	}
	public void setGOODS_DATE(String goods_date) {
		GOODS_DATE = goods_date;
	}
}

 : AdminGoodsDAO
package net.admin.goods.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AdminGoodsDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public AdminGoodsDAO(){
		try{
			Context initCtx=new InitialContext();
			   Context envCtx=(Context)initCtx.lookup("java:comp/env");
			   DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			   con=ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}
	
	public List getGoodsList() {
		List goodslist = new ArrayList();
		try {
			String sql="select * from goods order by goods_num";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GoodsBean agb = new GoodsBean();
				agb.setGOODS_NUM(rs.getInt("goods_num"));
				agb.setGOODS_CATEGORY(rs.getString("goods_category"));
				agb.setGOODS_NAME(rs.getString("goods_name"));
				agb.setGOODS_CONTENT(rs.getString("goods_content"));
				agb.setGOODS_SIZE(rs.getString("goods_size"));
				agb.setGOODS_COLOR(rs.getString("goods_color"));
				agb.setGOODS_AMOUNT(rs.getInt("goods_amount"));
				agb.setGOODS_PRICE(rs.getInt("goods_price"));
				agb.setGOODS_IMAGE(rs.getString("goods_image"));
				agb.setGOODS_BEST(rs.getInt("goods_best"));
				agb.setGOODS_DATE(rs.getString("goods_date"));
				
				goodslist.add(agb);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return goodslist;
	}
	
	public GoodsBean getGoods(int num) {
		try {
			String sql="select * from goods where goods_num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			rs=pstmt.executeQuery();
			rs.next();
			
			GoodsBean agb=new GoodsBean();
			agb.setGOODS_NUM(rs.getInt("goods_num"));
			agb.setGOODS_CATEGORY(rs.getString("goods_category"));
			agb.setGOODS_NAME(rs.getString("goods_name"));
			agb.setGOODS_CONTENT(rs.getString("goods_content"));
			agb.setGOODS_SIZE(rs.getString("goods_size"));
			agb.setGOODS_COLOR(rs.getString("goods_color"));
			agb.setGOODS_AMOUNT(rs.getInt("goods_amount"));
			agb.setGOODS_PRICE(rs.getInt("goods_price"));
			agb.setGOODS_IMAGE(rs.getString("goods_image"));
			agb.setGOODS_BEST(rs.getInt("goods_best"));
			
			return agb;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public int insertGoods(GoodsBean agb) {
		int result = 0;
		int num=0;
		
		String sql="select max(goods_num) from goods";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			num=rs.getInt(1)+1;
			
			sql="insert into goods values "+
			"(?,?,?,?,?,?,?,?,?,?,sysdate)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, agb.getGOODS_CATEGORY());
			pstmt.setString(3, agb.getGOODS_NAME());
			pstmt.setString(4, agb.getGOODS_CONTENT());
			pstmt.setString(5, agb.getGOODS_SIZE());
			pstmt.setString(6, agb.getGOODS_COLOR());
			pstmt.setInt(7, agb.getGOODS_AMOUNT());
			pstmt.setInt(8, agb.getGOODS_PRICE());
			pstmt.setString(9, agb.getGOODS_IMAGE());
			pstmt.setInt(10, agb.getGOODS_BEST());
			result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public int deleteGoods(GoodsBean agb){
		int result = 0;
		
		try {
			String sql="delete from goods where goods_num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, agb.getGOODS_NUM());
			
			result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return 0;
	}
	
	public int modifyGoods(GoodsBean agb) {
		int result = 0;
		
		try {
			String sql="update goods set "+
			"goods_category=?, goods_name=?, goods_price=? ,"+
			"goods_color=? ,goods_amount=? ,goods_size=? ,"+
			"goods_content=?,goods_best=? where goods_num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, agb.getGOODS_CATEGORY());
			pstmt.setString(2, agb.getGOODS_NAME());
			pstmt.setInt(3, agb.getGOODS_PRICE());
			pstmt.setString(4, agb.getGOODS_COLOR());
			pstmt.setInt(5, agb.getGOODS_AMOUNT());
			pstmt.setString(6, agb.getGOODS_SIZE());
			pstmt.setString(7, agb.getGOODS_CONTENT());
			pstmt.setInt(8, agb.getGOODS_BEST());
			pstmt.setInt(9, agb.getGOODS_NUM());
			result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}

 : 상품 관리에 대한 Action 클래스
[Action.java]
package net.admin.goods.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.admin.goods.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[AdminGoodsListAction.java]
package net.admin.goods.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.goods.db.*;

public class AdminGoodsListAction implements Action {
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) {
		AdminGoodsDAO agoodsdao=new AdminGoodsDAO();
		GoodsBean agb=new GoodsBean();
		
		ActionForward forward=new ActionForward();
		
		Collection list=(Collection)agoodsdao.getGoodsList();
		
		request.setAttribute("list",list);
		
		forward.setRedirect(false);
		forward.setPath("./admingoods/admin_goods_list.jsp");
		return forward;
	}
}

[AdminGoodsAddAction.java]
package net.admin.goods.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.admin.goods.db.*;

public class AdminGoodsAddAction implements Action {
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		ActionForward forward = new ActionForward();
		AdminGoodsDAO agoodsdao= new AdminGoodsDAO();
		GoodsBean agb = new GoodsBean();
		
		String realPath = "";
		String savePath = "upload";
		int maxSize = 5 * 1024 * 1024;
		
		realPath = request.getRealPath(savePath);
		
		List savefiles=new ArrayList();
		
		try {

			MultipartRequest multi = null;
			multi = new MultipartRequest(request, realPath, maxSize, "euc-kr",
					new DefaultFileRenamePolicy());
			
			Enumeration files=multi.getFileNames();
			while(files.hasMoreElements()){
				String name=(String)files.nextElement();
				if(files.hasMoreElements()){
					savefiles.add(multi.getFilesystemName(name)+",");
				}else{
					savefiles.add(multi.getFilesystemName(name));
				}
			}
			
			StringBuffer fl=new StringBuffer();
			for(int i=0;i<savefiles.size();i++){
				fl.append(savefiles.get(i));	
			}
			
			agb.setGOODS_CATEGORY(multi.getParameter("goods_category"));
			agb.setGOODS_NAME(multi.getParameter("goods_name"));
			agb.setGOODS_CONTENT(multi.getParameter("goods_content"));
			agb.setGOODS_SIZE(multi.getParameter("goods_size"));
			agb.setGOODS_COLOR(multi.getParameter("goods_color"));
			agb.setGOODS_AMOUNT(
					Integer.parseInt(multi.getParameter("goods_amount")));
			agb.setGOODS_PRICE(
					Integer.parseInt(multi.getParameter("goods_price")));
			agb.setGOODS_IMAGE(fl.toString());
			agb.setGOODS_BEST(
					Integer.parseInt(multi.getParameter("goods_best")));
			
			int result = agoodsdao.insertGoods(agb);
			if (result <= 0){
				return null;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forward.setRedirect(true);
		forward.setPath("GoodsList.ag");
		return forward;
	}
}

[AdminGoodsDeleteAction.java]
package net.admin.goods.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.goods.db.*;

public class AdminGoodsDeleteAction implements Action {
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) {	
		ActionForward forward=new ActionForward();
		AdminGoodsDAO agoodsdao=new AdminGoodsDAO();
		GoodsBean agb= new GoodsBean();
		
		agb.setGOODS_NUM(
				Integer.parseInt(request.getParameter("goods_num")));
		
		int check=agoodsdao.deleteGoods(agb);
		if(check>0){
			forward.setRedirect(true);
			forward.setPath("GoodsList.ag");
		}
		
		return forward;
	}
}

[AdminGoodsModifyAction.java]
package net.admin.goods.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.goods.db.*;

public class AdminGoodsModifyAction implements Action {

	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("euc-kr");
		ActionForward forward=new ActionForward();
		AdminGoodsDAO agoodsdao= new AdminGoodsDAO();
		GoodsBean agb=new GoodsBean();
		
		agb.setGOODS_NUM(Integer.parseInt(request.getParameter("goods_num")));
		agb.setGOODS_CATEGORY(request.getParameter("goods_category"));
		agb.setGOODS_NAME(request.getParameter("goods_name"));
		agb.setGOODS_CONTENT(request.getParameter("goods_content"));
		agb.setGOODS_SIZE(request.getParameter("goods_size"));
		agb.setGOODS_COLOR(request.getParameter("goods_color"));
		agb.setGOODS_AMOUNT(Integer.parseInt(request.getParameter("goods_amount")));
		agb.setGOODS_PRICE(Integer.parseInt(request.getParameter("goods_price")));
		agb.setGOODS_BEST(Integer.parseInt(request.getParameter("goods_best")));
		
		int result=agoodsdao.modifyGoods(agb);
		if(result<=0){
			System.out.println("상품 수정 실패");
			return null;
		}
		
		forward.setPath("./GoodsList.ag");
		forward.setRedirect(true);
		return forward;
	}
}

[AdminGoodsModifyForm.java]
package net.admin.goods.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.goods.db.*;

public class AdminGoodsModifyForm implements Action {
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) {
		ActionForward forward=new ActionForward();
		
		AdminGoodsDAO agoodsdao=new AdminGoodsDAO();
		GoodsBean agb=new GoodsBean();
		
		String num=request.getParameter("goods_num");
		
		agb=agoodsdao.getGoods(Integer.parseInt(num));
		
		request.setAttribute("agb", agb);
		
		forward.setPath("./admingoods/admin_goods_modify.jsp");
		forward.setRedirect(false);
		return forward;
	}
}

[AdminGoodsFrontController.java]
package net.admin.goods.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminGoodsFrontController extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.processRequest(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		ActionForward forward=null;
		Action action=null;
		
		if(command.equals("/GoodsAddAction.ag")){
			action= new AdminGoodsAddAction();
			try {
				forward=action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}else if(command.equals("/GoodsList.ag")){
			action=new AdminGoodsListAction();
			try {
				forward=action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/GoodsAdd.ag")){
			forward=new ActionForward();
			forward.setRedirect(false);
			forward.setPath("./admingoods/admin_goods_write.jsp");
		}else if(command.equals("/GoodsDelete.ag")){
			action=new AdminGoodsDeleteAction();
			try {
				forward=action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/GoodsModify.ag")){
			action=new AdminGoodsModifyForm();
			try {
				forward=action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/GoodsModifyAction.ag")){
			action=new AdminGoodsModifyAction();
			try {
				forward=action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(forward.isRedirect()){			
			response.sendRedirect(forward.getPath());			
		}else{			
			RequestDispatcher dispatcher=
				request.getRequestDispatcher(forward.getPath());
			dispatcher.forward(request, response);			
		}
	}
}

 : web.xml
...
<servlet>
	<description></description>
	<display-name>AdminGoodsFrontController</display-name>
	<servlet-name>AdminGoodsFrontController</servlet-name>
	<servlet-class>net.admin.goods.action.AdminGoodsFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>AdminGoodsFrontController</servlet-name>
	<url-pattern>*.ag</url-pattern>
</servlet-mapping>
<welcome-file-list>
	<welcome-file>index.html</welcome-file>
	<welcome-file>index.htm</welcome-file>
	<welcome-file>index.jsp</welcome-file>
	<welcome-file>default.html</welcome-file>
	<welcome-file>default.htm</welcome-file>
	<welcome-file>default.jsp</welcome-file>
</welcome-file-list>
	
<resource-ref>
	<description>Connection</description>
	<res-ref-name>jdbc/OracleDB</res-ref-name>
	<res-type>javax.sql.DataSource</res-type>
	<res-auth>Container</res-auth>
</resource-ref>
...

 : View 페이지 작성(/WenContent/admingoods/)
[admin_goods_list.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.util.*"%>
<%@ page import="net.admin.goods.db.*"%>
<%
	Collection list = (Collection) request.getAttribute("list");
	Object obj[] = list.toArray();
	GoodsBean agb = null;
%>

<html>
<head>
<title>쇼핑몰</title>
<script type="text/javascript">
function goodsdelete(goods_num){
	document.goodsform.action="./GoodsDelete.ag?goods_num="+goods_num;
	document.goodsform.submit();	
}
function goodsmodify(goods_num){
	document.goodsform.action="./GoodsModify.ag?goods_num="+goods_num;
	document.goodsform.submit();	
}
</script>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	color="gray" align="center">
	<tr>
		<td colspan=2>
		<!-- 상품 목록 -->
		<table border="0" width="80%">
			<tr>
				<td height="22" bgcolor="#6699FF">
					<p align="center">
						<font size=2>상품목록</font>
					</p>
				</td>
			</tr>
			<tr>
				<td>
				<p align="right">
					<font size=2>
						<a href="./GoodsAdd.ag">상품등록</a>
					</font>
				</p>
				</td>
			</tr>
			<tr>
			<td>
			<form name=goodsform method="post">
			<table border="1">
			<tr>
				<td width="50">
				<p align="center"><font size=2>번호</font></p>
				</td>
				<td width="141">
				<p align="center"><font size=2>카테고리</font></p>
				</td>
				<td width="100">
				<p align="center"><font size=2>사진</font></p>
				</td>
				<td width="141">
				<p align="center"><font size=2>상품명</font></p>
				</td>
				<td width="141">
				<p align="center"><font size=2>단가</font></p>
				</td>
				<td width="80">
				<p align="center"><font size=2>수량</font></p>
				</td>
				<td width="141">
				<p align="center"><font size=2>등록일자</font></p>
				</td>
				<td width="100">
				<p align="center"><font size=2>&nbsp;</font></p>
				</td>
			</tr>
			<%
					for (int i = 0; i < list.size(); i++) {
					agb = (GoodsBean) obj[i];
			%>
			<tr>
			<td>
			<p align="center">
				<font size=2><%=agb.getGOODS_NUM()%></font>
			</p>
			</td>
			<td>
			<p align="center">
				<font size=2>
					<%=agb.getGOODS_CATEGORY()%>
				</font>
			</p>
			</td>
			<td>
			<p align="center"><img
			src="./upload/<%=agb.getGOODS_IMAGE().split(",")[0] %>"
			width="50" height="50" border="0"></p>
			</td>
			<td>
			<p align="center">
				<font size=2><%=agb.getGOODS_NAME()%></font>
			</p>
			</td>
			<td>
			<p align="center">
				<font size=2><%=agb.getGOODS_PRICE()%></font>
			</p>
			</td>
			<td>
			<p align="center">
				<font size=2><%=agb.getGOODS_AMOUNT()%></font>
			</p>
			</td>
			<td>
			<p align="center">
				<font size=2>
					<%=agb.getGOODS_DATE().substring(0,10) %>
				</font>
			</p>
			</td>
			<td>
			<p align="center">
			<a href="javascript:goodsmodify(<%=agb.getGOODS_NUM()%>);">
				<font size=2>수정</font>
			</a>/
			<a href="javascript:goodsdelete(<%=agb.getGOODS_NUM()%>);">
				<font size=2>삭제</font>
			</a>
			</p>
			</td>
			</tr>
			<%
				}
			%>
			</table>
			</td>
			</tr>
			</form>
		</table>
		<!-- 상품 목록 -->
		</td>
	</tr>
</table>
</body>
</html>

[admin_goods_write.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	color="gray" align="center">
<tr>
<td colspan=2>
<!-- 상품 등록 -->
<table border="0" width="80%">
<form name="goodsform" action="./GoodsAddAction.ag" method="post"
	enctype="multipart/form-data">
	<input type="hidden" name="goods_best" value="0">
	<tr> 
		<td>
			<p align="center">
				<span style="font-size: 26pt;">상 품 등 록</span>
			</p>
		</td>
	</tr>
	<tr>
		<td height="331">
		<table border="1" align="center" width="558">
			<tr>
			<td width="196" height="30">
				<p align="center">
					<font size=2>카테고리</font>
				</p>
			</td>
			<td width="346" height="30">
				<select name="goods_category" size="1">
					<option value="outwear" selected>아웃웨어</option>
					<option value="fulldress">정장/신사복</option>
					<option value="Tshirts">티셔츠</option>
					<option value="shirts">와이셔츠</option>
					<option value="pants">팬츠</option>
					<option value="shoes">슈즈</option>
				</select>
			</td>
			</tr>
			<tr>
			<td>
				<p align="center"><font size=2>상품이름</font></p>
			</td>
			<td><input type="text" name="goods_name"></td>
		</tr>
		<tr>
			<td>
				<p align="center"><font size=2>판매가</font></p>
			</td>
			<td><input type="text" name="goods_price"></td>
		</tr>
		<tr>
			<td>
				<p align="center"><font size=2>색깔</font></p>
			</td>
			<td><input type="text" name="goods_color"></td>
		</tr>
		<tr>
			<td>
				<p align="center"><font size=2>수량</font></p>
			</td>
			<td><input type="text" name="goods_amount"></td>
		</tr>
		<tr>
			<td>
				<p align="center"><font size=2>사이즈</font></p>
			</td>
			<td><input type="text" name="goods_size"></td>
		</tr>
		<tr>
			<td width="196">
				<p align="center"><font size=2>제품정보</font></p>
			</td>
			<td width="346">
				<textarea 
				name="goods_content" cols=50 rows=15></textarea>
			</td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>메인 제품이미지(gif)</font></p>
			</td>
			<td><input type="file" name="file4"></td></tr>
			<tr>
			<td>
			<p align="center"><font size=2>제품이미지1(gif)</font></p>
			</td>
			<td><input type="file" name="file3"></td></tr>
			<tr>
			<td>
			<p align="center"><font size=2>제품이미지2(gif)</font></p>
			</td>
			<td><input type="file" name="file2"></td></tr>
			<tr>
			<td>
			<p align="center"><font size=2>제품이미지3(gif)</font></p>
			</td>
			<td><input type="file" name="file1"></td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td height="75">
	<p align="center"><input type="submit" value="등록">&nbsp;
	<input type="reset" value="다시쓰기"></p>
	</td>
</tr>
</table>
</form>		
<!-- 상품 등록 -->
</td>
</tr>
</table>
</body>
</html>

[admin_goods_modify.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="net.admin.goods.db.*"%>
<%  
	GoodsBean agb=(GoodsBean)request.getAttribute("agb");
%>

<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	color="gray" align="center">
<tr>
	<td colspan=2>
	<!-- 상품 수정 -->
	<table border="0" width="80%">
	<form name="goodsform" action="./GoodsModifyAction.ag" method="post">
	<input type="hidden" name="goods_num" value=<%=agb.getGOODS_NUM() %>>
	<tr>
		<td>
		<p align="center"><span style="font-size: 26pt;">상 품 수 정</span></p>
		</td>
	</tr>
		
	<tr>
		<td height="331">
		<table border="1" align="center" width="558">
		<tr>
			<td width="196" height="30">
			<p align="center"><font size=2>카테고리</font></p>
			</td>
			
			<td width="346" height="30">
			<select name="goods_category" size="1" 
					value=<%=agb.getGOODS_CATEGORY() %>>
				<option value="outwear">아웃웨어</option>
				<option value="fulldress">정장/신사복</option>
				<option value="tshirts">티셔츠</option>
				<option value="shirts">와이셔츠</option>
				<option value="pants">팬츠</option>
				<option value="shoes">슈즈</option>
			</select>
			</td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>상품이름</font></p>
			</td>
			<td><input type="text" name="goods_name" 
					value=<%=agb.getGOODS_NAME() %>></td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>판매가</font></p>
			</td>
			<td><input type="text" name="goods_price" 
					value=<%=agb.getGOODS_PRICE() %>></td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>색깔</font></p>
			</td>
			<td><input type="text" name="goods_color" 
					value=<%=agb.getGOODS_COLOR() %>></td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>수량</font></p>
			</td>
			<td><input type="text" name="goods_amount" 
					value=<%=agb.getGOODS_AMOUNT() %>></td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>사이즈</font></p>
			</td>
			<td><input type="text" name="goods_size" 
					value=<%=agb.getGOODS_SIZE() %>></td>
		</tr>
		<tr>
			<td>
			<p align="center"><font size=2>인기상품</font></p>
			</td>
			<td><input type="radio" name="goods_best" value=1
				<%if(agb.getGOODS_BEST()==1){%>CHECKED<%}%>>
				<font size=2>예</font>
				<input type="radio" name="goods_best" value=0 
				<%if(agb.getGOODS_BEST()==0){%>CHECKED<%}%>>
				<font size=2>아니오</font></td>
		</tr>
		<tr>
			<td width="196">
			<p align="center"><font size=2>제품정보</font></p>
			</td>
			<td width="346">
				<textarea name="goods_content" cols=50 rows=15>
					<%=agb.getGOODS_CONTENT() %>
				</textarea>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="75">
		<p align="center"><input type="submit" value="수정">&nbsp;
		<input type="reset" value="다시쓰기"></p>
		</td>
	</tr>
	</form>
	</table>
	<!-- 상품 수정 -->
	</td>
</tr>
</table>
</body>
</html>

4. 상품 관련 시스템 구현
 : GoodsBean
package net.goods.db;

import java.sql.Timestamp;

public class GoodsBean {
	private int GOODS_NUM;
	private String GOODS_CATEGORY;
	private String GOODS_NAME;
	private String GOODS_CONTENT;
	private String GOODS_SIZE;
	private String GOODS_COLOR;
	private int GOODS_AMOUNT;
	private int GOODS_PRICE;
	private String GOODS_IMAGE;
	private int GOODS_BEST;
	private Timestamp GOODS_DATE;
	
	public int getGOODS_NUM() {
		return GOODS_NUM;
	}
	public void setGOODS_NUM(int goods_num) {
		GOODS_NUM = goods_num;
	}
	public String getGOODS_CATEGORY() {
		return GOODS_CATEGORY;
	}
	public void setGOODS_CATEGORY(String goods_category) {
		GOODS_CATEGORY = goods_category;
	}
	public String getGOODS_NAME() {
		return GOODS_NAME;
	}
	public void setGOODS_NAME(String goods_name) {
		GOODS_NAME = goods_name;
	}
	public String getGOODS_CONTENT() {
		return GOODS_CONTENT;
	}
	public void setGOODS_CONTENT(String goods_content) {
		GOODS_CONTENT = goods_content;
	}
	public String getGOODS_SIZE() {
		return GOODS_SIZE;
	}
	public void setGOODS_SIZE(String goods_size) {
		GOODS_SIZE = goods_size;
	}
	public String getGOODS_COLOR() {
		return GOODS_COLOR;
	}
	public void setGOODS_COLOR(String goods_color) {
		GOODS_COLOR = goods_color;
	}
	public int getGOODS_AMOUNT() {
		return GOODS_AMOUNT;
	}
	public void setGOODS_AMOUNT(int goods_amount) {
		GOODS_AMOUNT = goods_amount;
	}
	public int getGOODS_PRICE() {
		return GOODS_PRICE;
	}
	public void setGOODS_PRICE(int goods_price) {
		GOODS_PRICE = goods_price;
	}
	public String getGOODS_IMAGE() {
		return GOODS_IMAGE;
	}
	public void setGOODS_IMAGE(String goods_image) {
		GOODS_IMAGE = goods_image;
	}
	public int getGOODS_BEST() {
		return GOODS_BEST;
	}
	public void setGOODS_BEST(int goods_best) {
		GOODS_BEST = goods_best;
	}
	public Timestamp getGOODS_DATE() {
		return GOODS_DATE;
	}
	public void setGOODS_DATE(Timestamp goods_date) {
		GOODS_DATE = goods_date;
	}
}

 : GoodsDAO
package net.goods.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class GoodsDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public GoodsDAO(){
		try{
			Context initCtx=new InitialContext();
			Context envCtx=(Context)initCtx.lookup("java:comp/env");
			DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			con=ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}
	
	public List item_List(String item, int page) {
		List itemList = new ArrayList();
		
		int startnum=page*12-11;
		int endnum=page*12;
		
		try {
			StringBuffer findQuery = new StringBuffer();
			
			findQuery.append("SELECT * FROM (SELECT GOODS_NUM,");
			findQuery.append("GOODS_CATEGORY, GOODS_NAME, ");
			findQuery.append("GOODS_CONTENT,GOODS_PRICE,GOODS_IMAGE,");
			findQuery.append("GOODS_BEST,GOODS_DATE, rownum r FROM ");
			findQuery.append("GOODS WHERE ");
			
			if (item.equals("new_item")) {
				findQuery.append("GOODS_DATE>=GOODS_DATE-7");
			}else if (item.equals("hit_item")) { 
				findQuery.append("GOODS_BEST=1 ");
			}else{
				findQuery.append("GOODS_CATEGORY=? ");
			}
			findQuery.append("ORDER BY GOODS_NUM DESC) ");
			findQuery.append("WHERE r>=? AND r<=? ");		
			
			pstmt = con.prepareStatement(findQuery.toString(), 
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY );
			
			if (item.equals("new_item") || item.equals("hit_item")){
				pstmt.setInt(1, startnum);
				pstmt.setInt(2, endnum);
			}else {
				pstmt.setString(1, item);
				pstmt.setInt(2, startnum);
				pstmt.setInt(3, endnum);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				GoodsBean goodsbean = new GoodsBean();
				goodsbean.setGOODS_NUM(rs.getInt("GOODS_NUM"));
				goodsbean.setGOODS_CATEGORY(
						rs.getString("GOODS_CATEGORY"));
				goodsbean.setGOODS_NAME(rs.getString("GOODS_NAME"));
				goodsbean.setGOODS_PRICE(rs.getInt("GOODS_PRICE"));
				
				StringTokenizer st=new StringTokenizer(
						rs.getString("GOODS_IMAGE"),",");
				String firstImg=st.nextToken();					 					
				goodsbean.setGOODS_IMAGE(firstImg);				
				goodsbean.setGOODS_BEST(rs.getInt("GOODS_BEST"));
								
				itemList.add(goodsbean);
			} 		
				
			rs.close();			
			pstmt.close();
			
			return itemList;
		} catch(SQLException e) {
			e.printStackTrace(); 
		}

		return itemList;
	}
	
	public List item_List(String item, int page, String searchprice) {
		List itemList=new ArrayList();
		int startnum=page*12-11;
		int endnum=page*12;
		
		int firstprice=0;
		int secondprice=0;
		
		if(searchprice.equals("1~3")){
			firstprice=1;
			secondprice=29999;
		} else if (searchprice.equals("3~5")) {
			firstprice=30000;
			secondprice=49999;
		} else if (searchprice.equals("5~7")) {
			firstprice=50000;
			secondprice=69999;
		} else if (searchprice.equals("7~10")) {
			firstprice=70000;
			secondprice=99999;
		} else {
			firstprice=100000;
			secondprice=999999;
		}
		
		try {
			StringBuffer findQuery = new StringBuffer();
			
			findQuery.append("SELECT * FROM (SELECT GOODS_NUM, ");
			findQuery.append("GOODS_CATEGORY, GOODS_NAME, ");
			findQuery.append("GOODS_CONTENT,GOODS_PRICE,GOODS_IMAGE,");
			findQuery.append("GOODS_BEST, rownum r FROM GOODS WHERE ");
			if (item.equals("new_item")){
				findQuery.append("GOODS_DATE>=GOODS_DATE-7");
			}else if (item.equals("hit_item")) { 
				findQuery.append("GOODS_BEST=1 ");
			}else {
				findQuery.append("GOODS_CATEGORY=? ");
			}
			findQuery.append(" AND (GOODS_PRICE BETWEEN ? AND ? ) ");
			findQuery.append("ORDER BY GOODS_NUM DESC) ");
			findQuery.append("WHERE r>=? AND r<=? ");		
			
			pstmt = con.prepareStatement(findQuery.toString(), 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY );
			
			if (item.equals("new_item") || item.equals("hit_item")) {
				pstmt.setInt(1, firstprice);
				pstmt.setInt(2, secondprice);
				pstmt.setInt(3, startnum);
				pstmt.setInt(4, endnum);
			} else {
				pstmt.setString(1, item);
				pstmt.setInt(2, firstprice);
				pstmt.setInt(3, secondprice);
				pstmt.setInt(4, startnum);
				pstmt.setInt(5, endnum);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				GoodsBean goodsbean = new GoodsBean();
				goodsbean.setGOODS_NUM(rs.getInt("GOODS_NUM"));
				goodsbean.setGOODS_CATEGORY(
						rs.getString("GOODS_CATEGORY"));
				goodsbean.setGOODS_NAME(rs.getString("GOODS_NAME"));
				goodsbean.setGOODS_PRICE(rs.getInt("GOODS_PRICE"));
				
				StringTokenizer st=new StringTokenizer(
						rs.getString("GOODS_IMAGE"),",");
				String firstimg=st.nextToken();
				
				goodsbean.setGOODS_IMAGE(firstimg);
				goodsbean.setGOODS_BEST(rs.getInt("GOODS_BEST"));
				
				itemList.add(goodsbean);
			} 		
			
			rs.close();			
			pstmt.close();
				
			return itemList;
		} catch(SQLException e){
			e.printStackTrace();
		}
			
		return itemList;
	}
	
	public GoodsBean findDetail(int goods_num, String item, 
			String price,String direction) {
		GoodsBean goods=new GoodsBean();
		
		int firstprice=0;
		int secondprice=0;
		
		if(price.equals("1~3")) {
			firstprice=1;
			secondprice=29999;
		} else if (price.equals("3~5")) {
			firstprice=30000;
			secondprice=49999;
			
		} else if (price.equals("5~7")) {
			firstprice=50000;
			secondprice=69999;
		} else if (price.equals("7~10")) {
			firstprice=70000;
			secondprice=99999;
		} else if (price.equals("10")){
			firstprice=100000;
			secondprice=999999;
		}
		
		StringBuffer dQuery = new StringBuffer();
		if(direction.equals("next")){
			dQuery.append("SELECT GOODS_NUM,GOODS_CATEGORY,");
			dQuery.append("GOODS_IMAGE,GOODS_NAME FROM GOODS ");
			dQuery.append("WHERE GOODS_NUM>? AND ");
			if(item.equals("new_item")) {
				dQuery.append("GOODS_DATE>=GOODS_DATE-7");
			} else if (item.equals("hit_item")) {
				dQuery.append(" GOODS_BEST=1 ");
			} else {
				dQuery.append(" GOODS_CATEGORY=? ");			
			}
			if (!price.equals("no")) {
				dQuery.append(" AND (GOODS_PRICE BETWEEN ? AND ? ) ");
			}
		}else if(direction.equals("prev")){
			dQuery.append(
			"SELECT GOODS_NUM,GOODS_CATEGORY,GOODS_IMAGE,");
			dQuery.append(
			"GOODS_NAME FROM GOODS WHERE GOODS_NUM<? AND ");
			if(item.equals("new_item")) {
				dQuery.append("GOODS_DATE>=GOODS_DATE-7");
			} else if (item.equals("hit_item")) {
				dQuery.append(" GOODS_BEST=1 ");
			} else {
				dQuery.append(" GOODS_CATEGORY=? ");			
			}
			if (!price.equals("no")) {
				dQuery.append(" AND (GOODS_PRICE BETWEEN ? AND ? ) ");
			}
			dQuery.append("ORDER BY GOODS_NUM DESC ");
		}
		
		try {
			pstmt = con.prepareStatement(dQuery.toString(), 
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY );
			
			if (item.equals("new_item") || item.equals("hit_item")){
				if (price.equals("no")) {
					pstmt.setInt(1, goods_num);
				} else {
					pstmt.setInt(1, goods_num);
					pstmt.setInt(2, firstprice);
					pstmt.setInt(3, secondprice);
				}
			} else {
				if (price.equals("no")) {
					pstmt.setInt(1, goods_num);
					pstmt.setString(2, item);
				} else{
					pstmt.setInt(1, goods_num);
					pstmt.setString(2, item);
					pstmt.setInt(3, firstprice);
					pstmt.setInt(4, secondprice);
				}
			}
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				goods.setGOODS_NUM(rs.getInt("GOODS_NUM"));
				goods.setGOODS_CATEGORY(
						rs.getString("GOODS_CATEGORY"));
				goods.setGOODS_NAME(rs.getString("GOODS_NAME"));
				StringTokenizer st=new StringTokenizer(
						rs.getString("GOODS_IMAGE"),",");
				goods.setGOODS_IMAGE(st.nextToken());
			}
			
			rs.close();			
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return goods;	
	}
	
	public GoodsBean findDetailList(int goods_num, String item){
		GoodsBean goods=new GoodsBean();
		
		try {
			StringBuffer dQuery = new StringBuffer();
		
			dQuery.append("SELECT *");		
			dQuery.append(" FROM GOODS WHERE GOODS_NUM=?  AND ");
			
			if(item.equals("new_item")) {
				dQuery.append("GOODS_DATE>=GOODS_DATE-7");
			} else if (item.equals("hit_item")) {
				dQuery.append("GOODS_BEST=1 ");
			} else {
				dQuery.append("GOODS_CATEGORY=? ");	
			}
			
			pstmt = con.prepareStatement(dQuery.toString(), 
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY );
			if (item.equals("new_item") || item.equals("hit_item")){
				pstmt.setInt(1, goods_num);
			} else {
				pstmt.setInt(1, goods_num);
				pstmt.setString(2, item);
			}
			
			rs = pstmt.executeQuery();			
			
			if (rs.next()) {	
				goods.setGOODS_NUM(rs.getInt("GOODS_NUM"));
				goods.setGOODS_CATEGORY(
						rs.getString("GOODS_CATEGORY"));
				goods.setGOODS_NAME(rs.getString("GOODS_NAME"));
				goods.setGOODS_CONTENT(
						rs.getString("GOODS_CONTENT"));
				goods.setGOODS_SIZE(rs.getString("GOODS_SIZE"));
				goods.setGOODS_COLOR(rs.getString("GOODS_COLOR"));
				goods.setGOODS_AMOUNT(rs.getInt("GOODS_AMOUNT"));
				goods.setGOODS_PRICE(rs.getInt("GOODS_PRICE"));
				goods.setGOODS_IMAGE(rs.getString("GOODS_IMAGE"));
				goods.setGOODS_BEST(rs.getInt("GOODS_BEST"));					
			}
			
			rs.close();			
			pstmt.close();

			return goods;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getCount(String item) {
		int count=0;
		
		StringBuffer findQuery = new StringBuffer();
		
		findQuery.append("SELECT COUNT(GOODS_NUM) FROM GOODS WHERE ");
		
		if (item.equals("new_item")) {
			findQuery.append("GOODS_DATE>=GOODS_DATE-7");
		} else if (item.equals("hit_item")) { 
			findQuery.append("GOODS_BEST=? ");
		}else {
			findQuery.append("GOODS_CATEGORY=?");
		}
		
		try{
			pstmt=con.prepareStatement(findQuery.toString());
			if (item.equals("new_item")){
			}else if (item.equals("hit_item")) {
				pstmt.setInt(1,1);
			}else{
				pstmt.setString(1, item);
			}
			
			rs=pstmt.executeQuery();
			rs.next();
			
			count=rs.getInt(1);
			
			rs.close();
			pstmt.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
	
	public int getCount(String item, String searchprice) {
		int count=0;
		int firstprice=0;
		int secondprice=0;
		
		if(searchprice.equals("1~3")) {
			firstprice=1;
			secondprice=29999;
		} else if (searchprice.equals("3~5")) {
			firstprice=30000;
			secondprice=49999;
		} else if (searchprice.equals("5~7")) {
			firstprice=50000;
			secondprice=69999;
		} else if (searchprice.equals("7~10")) {
			firstprice=70000;
			secondprice=99999;
		} else {
			firstprice=100000;
			secondprice=999999;
		}
		
		StringBuffer findQuery = new StringBuffer();
		
		findQuery.append("SELECT COUNT(GOODS_NUM) FROM GOODS WHERE ");
		if (item.equals("new_item")) {
			findQuery.append("GOODS_DATE>=GOODS_DATE-7");
		} else if (item.equals("hit_item")) { 
			findQuery.append("GOODS_BEST=? ");
		}else {
			findQuery.append("GOODS_CATEGORY=?");
		}
		findQuery.append(" and (goods_price between ? and ?)");
		
		try
		{
			pstmt=con.prepareStatement(findQuery.toString());
			
			if(item.equals("new_item")){
				pstmt.setInt(1, firstprice);
				pstmt.setInt(2, secondprice);
			}else if (item.equals("hit_item")) {
				pstmt.setInt(1, 1);
				pstmt.setInt(2, firstprice);
				pstmt.setInt(3, secondprice);
			}else{
				pstmt.setString(1, item);
				pstmt.setInt(2, firstprice);
				pstmt.setInt(3, secondprice);
			}
			
			rs=pstmt.executeQuery();
			rs.next();
			count=rs.getInt(1);
			
			rs.close();
			pstmt.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
}

 : 상품 정보에 대한 Action 클래스 구현
[Action.java]
package net.goods.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.goods.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[GoodsListAction.java]
package net.goods.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.goods.db.*;

public class GoodsListAction implements Action{
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response){
		ActionForward forward=new ActionForward();
		GoodsDAO goodsdao=new GoodsDAO();
		
		List itemList=null;
		String item=null;
		String price="";
		int count=1;
		int page=1;
		if(request.getParameter("page")!=null){
			page=Integer.parseInt(request.getParameter("page"));
		}
		
		item=request.getParameter("item");
		
		if (request.getParameter("searchprice")==null || 
				request.getParameter("searchprice").equals("")) {
			itemList= goodsdao.item_List(item,page);
			count=goodsdao.getCount(item);
		} else {
			price=request.getParameter("searchprice");
			itemList= goodsdao.item_List(item,page,price);
			count=goodsdao.getCount(item, price);
		}
		
		int pageSize=12;
		int pageCount=count/pageSize+(count % pageSize==0?0:1);
		int startPage=(int)((page-1)/10)*10+1;
		int endPage=startPage+10-1;
		if (endPage>pageCount) endPage=pageCount;
		
		request.setAttribute("itemList", itemList);
		request.setAttribute("category", item);
		request.setAttribute("count", count);
		request.setAttribute("price", price);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		
		forward.setRedirect(false);
		forward.setPath("./goods/goods_list.jsp");
		return forward;
	}
}

[GoodsDetailAction.java]
package net.goods.action;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.goods.db.*;

public class GoodsDetailAction implements Action {
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) {
		ActionForward forward = new ActionForward();
		GoodsDAO goodsdao = new GoodsDAO();
		
		List imgList = new ArrayList();
		int gr_goods_num = 0;
		GoodsBean isnextpage = null;
		GoodsBean isprevpage = null;
		GoodsBean itemArray = null;
		GoodsBean next_Bean = null;
		GoodsBean prev_Bean = null;
		String item = null;
		String price = "no";

		gr_goods_num = Integer.parseInt(
				request.getParameter("gr_goods_num"));
		item = request.getParameter("item");
		
		int nextpage = 0;
		int prevpage = 0;
		
		if (request.getParameter("price") != null &&
			!request.getParameter("price").equals("")){
			price = request.getParameter("price");
		}
		if (request.getParameter("search")!= null){
			if (request.getParameter("search").equals("next")) {
				next_Bean = goodsdao.findDetail(
						gr_goods_num, item, price,"next");
				nextpage = next_Bean.getGOODS_NUM();
				itemArray = goodsdao.findDetailList(nextpage, item);
				isnextpage = goodsdao.findDetail(
						nextpage, item, price,"next");
				isprevpage = goodsdao.findDetail(
						nextpage, item, price,"prev");
			}else if (request.getParameter("search").equals("prev")){
				prev_Bean = goodsdao.findDetail(
						gr_goods_num, item,price,"prev");
				prevpage = prev_Bean.getGOODS_NUM();
				itemArray = goodsdao.findDetailList(prevpage, item);
				isnextpage = goodsdao.findDetail(
						prevpage, item, price,"next");
				isprevpage = goodsdao.findDetail(
						prevpage, item, price,"prev");
			}
		}else {
			itemArray = goodsdao.findDetailList(gr_goods_num, item);
			
			if (request.getParameter("isitem").equals("new")) {
				item = "new_item";
			}else if (request.getParameter("isitem").equals("hit")) {
				item = "hit_item";
			}
			
			isnextpage = goodsdao.findDetail(
					gr_goods_num, item, price,"next");
			isprevpage = goodsdao.findDetail(
					gr_goods_num, item, price,"prev");
		}
		
		String images = itemArray.getGOODS_IMAGE();
		StringTokenizer st = new StringTokenizer(images, ",");
		while (st.hasMoreTokens()) {
			imgList.add(st.nextToken());
		}
		
		String mainImage = (String) imgList.get(1);
		List contentImage = new ArrayList();
		for (int i = 2; i < imgList.size(); i++) {
			contentImage.add(imgList.get(i));
		}
		
		request.setAttribute("itemArray", itemArray);
		request.setAttribute("item", item);
		request.setAttribute("mainImage", mainImage);
		request.setAttribute("contentImage", contentImage);
		request.setAttribute("prevpage", isprevpage);
		request.setAttribute("nextpage", isnextpage);
		request.setAttribute("price", price);
		
		forward.setRedirect(false);
		forward.setPath("./goods/goods_detail.jsp");
		return forward;
	}
}

[GoodsFrontController.java]
package net.goods.action;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.goods.action.GoodsDetailAction;
import net.goods.action.GoodsListAction;

public class GoodsFrontController extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.processRequest(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String requestURI = request.getRequestURI();
		String contextpath = request.getContextPath();
		String command = requestURI.substring(contextpath.length());
		
		ActionForward forward = null;
		Action action = null;
		
		if (command.equals("/Goods_Detail.go")) {
			action = new GoodsDetailAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (command.equals("/GoodsList.go")) {
			action = new GoodsListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			} else {
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	}
}

 : web.xml
...
<servlet>
	<description></description>
	<display-name>GoodsFrontController</display-name>
	<servlet-name>GoodsFrontController</servlet-name>
	<servlet-class>net.goods.action.GoodsFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>GoodsFrontController</servlet-name>
	<url-pattern>*.go</url-pattern>
</servlet-mapping>
...

 : View 페이지 생성(/WenContent/goods/)
[goods_list.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String category=(String) request.getAttribute("category");
	String price=(String) request.getAttribute("price");
	int nowpage=1;
	if(request.getParameter("page")!=null){
		nowpage=Integer.parseInt(request.getParameter("page"));
	}
	int count=((Integer)(request.getAttribute("count"))).intValue();
	int pageCount=((Integer)(
			request.getAttribute("pageCount"))).intValue();
	int startPage=((Integer)(
			request.getAttribute("startPage"))).intValue();
	int endPage=((Integer)(
			request.getAttribute("endPage"))).intValue();
%>

<html>
<head>
<title>쇼핑몰</title>
<script>
function searchPrice(item,searchprice) {
	if (searchprice!="all") {
		window.location.href=
		"GoodsList.go?item="+item+"&page=1&searchprice="+searchprice;
	} else {
		window.location.href="GoodsList.go?item="+item+"&page=1";
	}
}
</script>
</head>
<body>		
	<table width="960" cellspacing="0" cellpadding="0" border="0" 
		align="center">
	<tr>
	<td colspan=2>
	<table width=700 height="460" border="0" align="center">
		<tr valign="middle">
			<td height="50" width="700" align="right" valign="middle" 
				colspan="4">
			-상품 목록-&nbsp;
			<a href="javascript:searchPrice('<%=category%>','1~3')">
			[3만원 미만]
			</a>
			<a href="javascript:searchPrice('<%=category%>','3~5')">
			[3~5만원]
			</a>
			<a href="javascript:searchPrice('<%=category%>','5~7')">
			[5~7만원]
			</a>
			<a href="javascript:searchPrice('<%=category%>','7~10')">
			[7~10만원]
			</a>
			<a href="javascript:searchPrice('<%=category%>','10')">
			[10만원 이상]
			</a>
			<a href="javascript:searchPrice('<%=category%>','all')">
			[전체 보기]
			</a>
			</td>
		</tr>
		
		<tr>
		<td valign="top">
		<!-- 상품 리스트 -->
		<table border="0">
		<tr>	   
		   <c:choose> 
				<c:when test=
					"${requestScope.itemList[0].GOODS_NUM==null}">
					<tr>
					<td width="700" height="300" align="center">
					등록된 글이 없습니다.
					</td>
					</tr>
				</c:when>
				
				<c:otherwise> 
				<c:forEach var="item" items="${requestScope.itemList}">
				<td width="180" valign="top" >
				<br>
				<div align="center">
				<%if(category.equals("new_item")) { 
			      	if(price.equals("no")) {
				%>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}&isitem=new">
				<%	}else{ %>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}
						&isitem=new&price=${price}">
				<% 	}
			      }else if (category.equals("hit_item")) { 
			      	if(price.equals("no")) {
			 	%>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}&isitem=hit">
				<% 	}else{ %>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}
						&isitem=hit&price=${price}">
				<%	}
			      } else { 
					if(price.equals("no")) {
				%>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}&isitem=other">
				<% 	}else{ %>
				<a href="Goods_Detail.go?item=${item.GOODS_CATEGORY}
						&gr_goods_num=${item.GOODS_NUM}
						&isitem=other&price=${price}">
				<%	}
				 }
				%>
			 	
			 	<img src="./upload/${fn:trim(item.GOODS_IMAGE)}" 
			 		width="130" height="130" border="0"/>
			 	<br/>${item.GOODS_NAME}<br/>
				</a>
				<br/><b>${item.GOODS_PRICE}원</b>
				</div>
				<br>
				</td>
				</c:forEach>
				</c:otherwise>
			</c:choose>
		</tr>
		</table>
		<!-- 상품 리스트 -->
		</td>
		</tr>
		<tr>
		<td height="20" colspan="4" align="center">		
		<!-- 페이징 -->
		<%
		if (count>0) {
			if (startPage>10) { %>
			<a href="GoodsList.go?item=<%=category%>&
					page=<%=startPage-1%>">[이전]</a>
			<% }
			for (int i=startPage;i<=endPage ; i++) { 
				if (i==nowpage) {   %>
				<font color=red>[<%=i%>]</font>
				<% } else { 
					if (price.equals("no")) {
				%>
					<a href="GoodsList.go?item=<%=category%>
						&page=<%=i%>">[<%=i%>]</a>
					<% } else { %>  
					<a href="GoodsList.go?item=<%=category%>
						&page=<%=i%>&searchprice=<%=price%>">[<%=i%>]</a>
					<%
			 		 }
				}
			}
			if (endPage<pageCount) { %>
			<a href="GoodsList.go?item=<%=category%>
					&page=<%=endPage+1%>">[다음]</a>
			<%
			}
		}
		%>			
		<!-- 페이징 -->
		</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
</body>
</html>

[goods_detail.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@page import="net.goods.db.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	GoodsBean itemArray = (GoodsBean) request.getAttribute("itemArray");
	String category = (String) request.getAttribute("item");
	String price = (String) request.getAttribute("price");

	GoodsBean prevpage = (GoodsBean) request.getAttribute("prevpage");
	GoodsBean nextpage = (GoodsBean) request.getAttribute("nextpage");
%>

<html>
<head>
<title>쇼핑몰</title>
<script>
function isBuy(goodsform) {
	if (document.goodsform.size.value=="") {
		alert("사이즈를 선택해주세요.")
		return;
	} else if (document.goodsform.color.value=="") {
		alert("색상을 선택해주세요.")
		return;
	}

	var amount=document.goodsform.amount.value;
	var size=document.goodsform.size.value;;
	var type=document.goodsform.color.value;;
	
	var isbuy=confirm("구매하시겠습니까?");
	if(isbuy==true) {
		goodsform.action="OrderStart.or";
		goodsform.submit();
	} else {
		return;
	}
}

function isBasket(basketform) {
	if (document.goodsform.size.value=="") {
		alert("사이즈를 선택해주세요.")
		return;
	} else if (document.goodsform.color.value=="") {
		alert("색상을 선택해주세요.")
		return;
	}
	
	var amount=document.goodsform.amount.value;
	var size=document.goodsform.size.value;;
	var type=document.goodsform.color.value;;
	
	var isbuy=confirm("장바구니에 저장하시겠습니까?");
	
	if(isbuy==true) {
		basketform.action="BasketAdd.ba";
		basketform.submit();
	} else {
		return;
	}
}

function count_change(temp){
	var test=document.goodsform.amount.value;
	if(temp==0){
		test++;
	}else if(temp==1){
		if(test> 1) test--;
	}
	
	document.goodsform.amount.value=test;
}
</script>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
	<tr>
		<td colspan=2 align=center><!-- 상품 내용 -->
		<form name="goodsform" action="#" method="post">
		<input type="hidden" name="goodsnum" 
			value="${itemArray.GOODS_NUM }">
		<input type="hidden" name="item"
			value="<%=request.getParameter("item") %>"> 
		<input type="hidden" name="gr_goods_num"
			value="<%=request.getParameter("gr_goods_num") %>">
		<input type="hidden" name="isitem"
			value="<%=request.getParameter("isitem") %>">
		<input type="hidden" name="order" value="goods">
		<input type="hidden" name="price" 
			value="<%=itemArray.getGOODS_PRICE() %>">
		<input type="hidden" name="goodsname" 
			value="<%=itemArray.getGOODS_NAME() %>"> 
		<input type="hidden" name="goodsimage"
			value="${fn:trim(mainImage)}">
		
		<table width="600" border="0" align="center">
		<tr>
			<td height="60" colspan="2">상 세 보 기</td>
		</tr>
		<tr>
			<td width="303" height="223" align="center" valign="middle">
			<img src="./upload/${fn:trim(mainImage)}" 
				width="300" height="300"/>
			</td>
			<td width="381" align="center" valign="middle">
			<table width="300" height="200" border="0">
				<tr>
					<td colspan="4" align="center" height="50">
						<b>${itemArray.GOODS_NAME}</b>
					</td>
				</tr>
				<tr>
					<td>판매가격 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</td>
					<td colspan="3">${itemArray.GOODS_PRICE} 원</td>
				</tr>
				<tr>
					<td rowspan="2">수량
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
					</td>
					<td width="58" rowspan="2">
						<input name="amount" type="text" 
							style="text-align: right"
							value="1"	size="4" />
					</td>
					<td valign="bottom">
						<div align="center">
							<a href="JavaScript:count_change(0)">▲</a>
						</div>
					</td>
					<td width="69" rowspan="2" align="left">개</td>
				</tr>
				<tr>
					<td valign="top">
						<div align="center">
							<a href="JavaScript:count_change(1)">▼</a>
						</div>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="4" height="30" 
						valign="middle">
						남은수량(${itemArray.GOODS_AMOUNT })개
					</td>
				</tr>
				<tr>
					<td colspan="4">크기&nbsp;&nbsp;&nbsp;&nbsp; 
						<select name="size" size="1">
							<option value="">크기를 선택하세요</option>
							<option value="">-----------------</option>

							<c:forTokens var="size" 
							items="${itemArray.GOODS_SIZE}"	delims=",">
								<option value=${fn:trim(size)}>
									${fn:trim(size)}
								</option>
							</c:forTokens>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="4">색깔&nbsp;&nbsp;&nbsp;&nbsp;
						<select name="color" size="1">
							<option value="">색깔을 선택하세요</option>
							<option value="">-----------------</option>
							<c:forTokens var="color" 
								items="${itemArray.GOODS_COLOR}"
								delims=",">
								<option value="${fn:trim(color)}">
									${fn:trim(color)}
								</option>
							</c:forTokens>
						</select>
					</td>
				</tr>
				<tr>
					<td height="50" colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4">
						<a href="javascript:isBuy(goodsform);">
						[구매하기]
						</a> 
						<a href="javascript:isBasket(goodsform);">
						[장바구니 담기]
						</a>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td align="center"></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">
			<table align="center">
				<tr>
					<td>
					<%
					if (prevpage.getGOODS_NUM() != 0) {
						if (price.equals("no")) {
					%>
						<a href="Goods_Detail.go?search=prev
						&gr_goods_num=<%=itemArray.getGOODS_NUM()%>
						&item=<%=category%>">
					<%
						} else {
					%> 
						<a href="Goods_Detail.go?search=prev
						&gr_goods_num=<%=itemArray.getGOODS_NUM()%>
						&item=<%=category%>&price=<%=price%>">
					<%
						}
					%>
						[이전상품] 
						</a>
					</td>
					<td width="100" align="left">
					<div align="center">
						<img 
						src="./upload/<%=prevpage.getGOODS_IMAGE()%>"
						width="70" height="50" border="0">
						<br><%=prevpage.getGOODS_NAME()%>
					</div>
					</td>
					<%
					}
					%>
					<td width="100" align="right">
					<%
					if (nextpage.getGOODS_NUM() != 0) {
					%>
						<div align="center">
						<img
						src="./upload/<%=nextpage.getGOODS_IMAGE()%>"
						width="70" height="50" border="0">
						<br><%=nextpage.getGOODS_NAME()%>
					</div>
					</td>
					<td>
					<%
						if (price.equals("no")) {
					%>
						<a href="Goods_Detail.go?search=next
						&gr_goods_num=<%=itemArray.getGOODS_NUM()%>
						&item=<%=category%>">
					<%
						} else {
					%>
						<a href="Goods_Detail.go?search=next
						&gr_goods_num=<%=itemArray.getGOODS_NUM()%>
						&item=<%=category%>&price=<%=price%>">
					<%
						}
					%> [다음상품] 
						</a>
					<%
 					}
 					%>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		</table>
		</form><!-- 상품 구매 메뉴 끝 -->
		<!-- 상품 내용 -->
		<table width="700" height="300" align="enter">
			<tr>
				<td align="center">
				<div>${itemArray.GOODS_CONTENT }</div>
				</td>
			</tr>
		</table>
		<br><br>
		<table width="200" border="0" align="center">
			<tr align="left">
				<td colspan="3"><c:forEach var="itemimg"
					items="${requestScope.contentImage}">
					<img src="./upload/${fn:trim(itemimg)}" />
				</c:forEach></td>
			</tr>
		</table>
		<!-- 상품 내용 끝 --></td>
	</tr>
</table>
</body>
</html>

5. 장바구니 시스템 구현
 : 테이블 생성
CREATE TABLE BASKET(
	BASKET_NUM INT,
	BASKET_MEMBER_ID VARCHAR2(20),
	BASKET_GOODS_NUM INT,
	BASKET_GOODS_AMOUNT INT,
	BASKET_GOODS_SIZE VARCHAR2(20),
	BASKET_GOODS_COLOR VARCHAR2(20),
	BASKET_DATE DATE,
	PRIMARY KEY(BASKET_NUM)
);

 : BasketBean
package net.basket.db;

import java.util.Date;

public class BasketBean {
	private int BASKET_NUM;
	private String BASKET_MEMBER_ID;
	private int BASKET_GOODS_NUM;
	private int BASKET_GOODS_AMOUNT;
	private String BASKET_GOODS_SIZE;
	private String BASKET_GOODS_COLOR;
	private Date BASKET_DATE;
	
	public int getBASKET_NUM() {
		return BASKET_NUM;
	}
	public void setBASKET_NUM(int basket_num) {
		BASKET_NUM = basket_num;
	}
	public String getBASKET_MEMBER_ID() {
		return BASKET_MEMBER_ID;
	}
	public void setBASKET_MEMBER_ID(String basket_member_id) {
		BASKET_MEMBER_ID = basket_member_id;
	}
	public int getBASKET_GOODS_NUM() {
		return BASKET_GOODS_NUM;
	}
	public void setBASKET_GOODS_NUM(int basket_goods_num) {
		BASKET_GOODS_NUM = basket_goods_num;
	}
	public int getBASKET_GOODS_AMOUNT() {
		return BASKET_GOODS_AMOUNT;
	}
	public void setBASKET_GOODS_AMOUNT(int basket_goods_amount) {
		BASKET_GOODS_AMOUNT = basket_goods_amount;
	}
	public String getBASKET_GOODS_SIZE() {
		return BASKET_GOODS_SIZE;
	}
	public void setBASKET_GOODS_SIZE(String basket_goods_size) {
		BASKET_GOODS_SIZE = basket_goods_size;
	}
	public String getBASKET_GOODS_COLOR() {
		return BASKET_GOODS_COLOR;
	}
	public void setBASKET_GOODS_COLOR(String basket_goods_color) {
		BASKET_GOODS_COLOR = basket_goods_color;
	}
	public Date getBASKET_DATE() {
		return BASKET_DATE;
	}
	public void setBASKET_DATE(Date basket_date) {
		BASKET_DATE = basket_date;
	}
}

 : BasketDAO
package net.basket.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.goods.db.GoodsBean;

public class BasketDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs, rs1;
	
	public BasketDAO(){
		try{
			Context initCtx=new InitialContext();
			Context envCtx=(Context)initCtx.lookup("java:comp/env");
			DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			conn=ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}
	
	public Vector getBasketList(String id) {
		Vector vector=new Vector();
		List basketlist = new ArrayList();
		List goodslist = new ArrayList();
		
		String sql = "select * from basket where BASKET_MEMBER_ID=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BasketBean dto = new BasketBean();
				GoodsBean goods = new GoodsBean();
				
				dto.setBASKET_NUM(rs.getInt("BASKET_NUM"));
				dto.setBASKET_MEMBER_ID(
						rs.getString("BASKET_MEMBER_ID"));
				dto.setBASKET_GOODS_NUM(
						rs.getInt("BASKET_GOODS_NUM"));
				dto.setBASKET_GOODS_AMOUNT(
						rs.getInt("BASKET_GOODS_AMOUNT"));
				dto.setBASKET_GOODS_SIZE(
						rs.getString("BASKET_GOODS_SIZE"));
				dto.setBASKET_GOODS_COLOR(
						rs.getString("BASKET_GOODS_COLOR"));
				dto.setBASKET_DATE(
						rs.getDate("BASKET_DATE"));
				
				sql = "select * from goods where goods_num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getBASKET_GOODS_NUM());
				rs1 = pstmt.executeQuery();
				
				if(rs1.next()){
					goods.setGOODS_NAME(
							rs1.getString("goods_name"));
					goods.setGOODS_PRICE(
							rs1.getInt("goods_price"));
					goods.setGOODS_IMAGE(
							rs1.getString("GOODS_IMAGE"));
				}else{
					return null;
				}
				
				basketlist.add(dto);
				goodslist.add(goods);
			}
			
			vector.add(basketlist);
			vector.add(goodslist);
			
			return vector;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void basketAdd(String id,int goodsnum,
			int amount,String size,String color){
		String sql="select max(basket_num) from basket";
		int num=0;
		
		try{
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			num=rs.getInt(1)+1;
			
			sql="insert into basket values "+
				"(?,?,?,?,?,?,sysdate)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, id);
			pstmt.setInt(3, goodsnum);
			pstmt.setInt(4, amount);
			pstmt.setString(5,size);
			pstmt.setString(6, color);
			
			pstmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean basketRemove(int num) {
		String sql = "delete from BASKET where BASKET_NUM=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			if(pstmt.executeUpdate()!=0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}

 : 장바구니에 대한 Action 클래스 구현
[Action.java]
package net.basket.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.basket.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[BasketListAction.java]
package net.basket.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.basket.action.Action;
import net.basket.action.ActionForward;
import net.basket.db.BasketDAO;

public class BasketListAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		BasketDAO basketdao=new BasketDAO();
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		
		if(id==null){
			PrintWriter out=response.getWriter();
			out.println("<script>");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		
		Vector vector = basketdao.getBasketList(id);
		List basketlist=(ArrayList)vector.get(0);
		List goodslist=(ArrayList)vector.get(1);
		
		request.setAttribute("basketlist", basketlist);
		request.setAttribute("goodslist", goodslist);
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(false);
		forward.setPath("./goods_order/goods_basket.jsp");
		
		return forward;
	}
}

[BasketAddAction.java]
package net.basket.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.basket.db.BasketDAO;

public class BasketAddAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		BasketDAO basketdao=new BasketDAO();
		HttpSession session = request.getSession();
		String id=(String)session.getAttribute("id");
		
		if(id==null){
			PrintWriter out=response.getWriter();
			out.println("<script>");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		
		int num=Integer.parseInt(request.getParameter("goodsnum"));
		int amount=Integer.parseInt(request.getParameter("amount"));
		String size=request.getParameter("size");
		String color=request.getParameter("color");
		
		String item=request.getParameter("item");
		String gr_goods_num=request.getParameter("gr_goods_num");
		String isitem=request.getParameter("isitem");
		
		basketdao.basketAdd(id,num,amount,size,color);
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(true);
		forward.setPath("./BasketList.ba?item="+item+
				"&gr_goods_num="+gr_goods_num+"&isitem="+isitem);
		return forward;
	}
}

[BasketDeleteAction.java]
package net.basket.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.basket.db.BasketDAO;

public class BasketDeleteAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		BasketDAO basketdao=new BasketDAO();
		HttpSession session = request.getSession();
		String id=(String)session.getAttribute("id");
		
		if(id==null){ //로그인되지 않았을 경우.
			PrintWriter out=response.getWriter();
			out.println("<script>");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		String num=request.getParameter("num");
		if(num==null){
			return null;
		}
		
		basketdao.basketRemove(Integer.parseInt(num));
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(true);
		forward.setPath("./BasketList.ba");
		return forward;
	}
}

[BasketFrontController.java]
package net.basket.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 public class BasketFrontController extends javax.servlet.http.HttpServlet 
 		implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   protected void doProcess(HttpServletRequest request, HttpServletResponse response) 
   throws ServletException, IOException {
	   String RequestURI=request.getRequestURI();
	   String contextPath=request.getContextPath();
	   String command=RequestURI.substring(contextPath.length());
	   ActionForward forward=null;
	   Action action=null;
	   
	   if(command.equals("/BasketList.ba")){
		   action  = new BasketListAction();
		   try {
			   forward=action.execute(request, response );
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	   }else if(command.equals("/BasketAdd.ba")){
		   action  = new BasketAddAction();
		   try {
			   forward=action.execute(request, response );
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	   }else if(command.equals("/BasketDelete.ba")){
		   action=new BasketDeleteAction();
		   try {
			   forward=action.execute(request, response );
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	   }
	   
	   if(forward.isRedirect()){
		   response.sendRedirect(forward.getPath());
	   }else{
		   RequestDispatcher dispatcher=
			   request.getRequestDispatcher(forward.getPath());
		   dispatcher.forward(request, response);
	   }
   }
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
   throws ServletException, IOException {
		doProcess(request,response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		doProcess(request,response);
	}   	  	    
}

 : web.xml
...
<servlet>
	<description></description>
	<display-name>BasketFrontController</display-name>
	<servlet-name>BasketFrontController</servlet-name>
	<servlet-class>net.basket.action.BasketFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>BasketFrontController</servlet-name>
	<url-pattern>*.ba</url-pattern>
</servlet-mapping>
...

 : View 페이지 작성(/WebContent/goods_order/)
[goods_basket.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.util.*"%>
<%@ page import="net.basket.db.*"%>
<%@ page import="net.goods.db.*" %>

<%
	List basketList = (ArrayList) request.getAttribute("basketlist");
	List goodsList = (ArrayList) request.getAttribute("goodslist");
	
	String item = request.getParameter("item");
	String gr_goods_num = request.getParameter("gr_goods_num");
	String isitem = request.getParameter("isitem");
%>

<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
	<tr>
		<td colspan=2><!-- 장바구니 -->
		<p align="center">
		<form action="./OrderStart.or" name="basketform" method="post">
		<input type="hidden" name="order" value="basket">
		<table width="80%">
			<tr align=center>
				<td><b>장 바 구 니</b></td>
			</tr>
		</table>
		<table width="80%" cellpadding="0" cellspacing="0">
			<tr height=26 bgcolor="94d7e7">
				<td height="3" colspan="7" align=right></td>
			</tr>
			<tr bgcolor="#f0f8ff" align="center">
				<td width="5%"><font size="2">번호</font></td>
				<td width="5%"><font size="2">사진</font></td>
				<td width="25%"><font size="2">제품명</font></td>
				<td width="8%"><font size="2">수량</font></td>
				<td width="8%"><font size="2">가격</font></td>
				<td width="8%"><font size="2">취소</font></td>
			</tr>
			<%
			if (basketList != null && basketList.size() != 0) {
				for (int i = 0; i < basketList.size(); i++) {
					BasketBean dto = (BasketBean) basketList.get(i);
					GoodsBean goods=(GoodsBean) goodsList.get(i);
			%>
			<tr align="center">
			<td><font size="2"><%=dto.getBASKET_NUM()%></font></td>
			<td><font size="2"><img 
				src="./upload/<%=goods.getGOODS_IMAGE().split(",")[0] %>" 
				width=50 height=50></font></td>
			<td><font size="2"><%=goods.getGOODS_NAME()%></font></td>
			<td><font size="2">
				<%=dto.getBASKET_GOODS_AMOUNT()%>
			</font></td>
			<td><font size="2"><%=goods.getGOODS_PRICE()%></font></td>
			<td><font size="2">
			<a href="BasketDelete.ba?num=<%=dto.getBASKET_NUM()%>"
				onclick="return confirm('취소하시겠습니까?')">취소</a>
			</font></td>
			</tr>
			<%
				}
			}else{
			%>
			<tr>
			<td colspan="7" align="center">
				<font size="2">장바구니에 담긴 상품이 없습니다.</font>
			</td>
			</tr>
			<%
			}
			%>
		</table>
		
		<table width="80%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="2" bgcolor="94d7e7"></td>
			</tr>
		</table>
		<br>
		
		<table width="80%" cellpadding="0" cellspacing="0">
			<tr>
			<td align="center">
			<%
			if (basketList != null && basketList.size() != 0) {
			%>
			<a href="javascript:basketform.submit()">
			[구매하기]
			</a>
			<%}else{%>
			<a href="#" onclick="javascript:alert('주문할 상품이 없습니다.')">
			[구매하기]
			</a>
			<%}
			if (item == null) {%>
			<a href="./GoodsList.go?item=new_item">
			[계속 쇼핑하기]</a>
			<%}else{%>
			<a href="./Goods_Detail.go?item=<%=item %>
			&gr_goods_num=<%=gr_goods_num %>
			&isitem=<%=isitem %>">
			[계속 쇼핑하기]</a>
			<%}%>
			</td>
			</tr>
		</table>
		</form>
		<!-- 장바구니 -->
		</p>
		</td>
	</tr>
</table>
</body>
</html>

6. 주문 처리 시스템 구현
 : 테이블 생성
CREATE TABLE GOODS_ORDER(
	ORDER_NUM INT,
	ORDER_TRADE_NUM VARCHAR2(50),
	ORDER_TRANS_NUM VARCHAR2(50),
	ORDER_GOODS_NUM	INT,
	ORDER_GOODS_NAME VARCHAR2(50),
	ORDER_GOODS_AMOUNT INT,
	ORDER_GOODS_SIZE VARCHAR2(20),
	ORDER_GOODS_COLOR VARCHAR2(20),
	ORDER_MEMBER_ID VARCHAR2(20),
	ORDER_RECEIVE_NAME VARCHAR2(20),
	ORDER_RECEIVE_ADDR1 VARCHAR2(70),
	ORDER_RECEIVE_ADDR2 VARCHAR2(70),
	ORDER_RECEIVE_PHONE VARCHAR2(13),
	ORDER_RECEIVE_MOBILE VARCHAR2(13),
	ORDER_MEMO VARCHAR2(3000),
	ORDER_SUM_MONEY INT,
	ORDER_TRADE_TYPE VARCHAR2(20),
	ORDER_TRADE_DATE DATE,
	ORDER_TRADE_PAYER VARCHAR2(20),
	ORDER_DATE DATE,
	ORDER_STATUS INT,
	PRIMARY KEY(ORDER_NUM)
);

 : OrderBean
package net.order.db;

import java.sql.Date;

public class OrderBean {
	private int ORDER_NUM;
	private String ORDER_TRADE_NUM;
	private String ORDER_TRANS_NUM;
	private int ORDER_GOODS_NUM;
	private String ORDER_GOODS_NAME;
	private int ORDER_GOODS_AMOUNT;
	private String ORDER_GOODS_SIZE;
	private String ORDER_GOODS_COLOR;
	private String ORDER_MEMBER_ID;
	private String ORDER_RECEIVE_NAME;
	private String ORDER_RECEIVE_ADDR1;
	private String ORDER_RECEIVE_ADDR2;
	private String ORDER_RECEIVE_PHONE;
	private String ORDER_RECEIVE_MOBILE;
	private String ORDER_MEMO;
	private int ORDER_SUM_MONEY;
	private String ORDER_TRADE_TYPE;
	private Date ORDER_TRADE_DATE;
	private String ORDER_TRADE_PAYER;
	private Date ORDER_DATE;
	private int ORDER_STATUS;
	
	public String getORDER_GOODS_NAME() {
		return ORDER_GOODS_NAME;
	}
	public void setORDER_GOODS_NAME(String order_goods_name) {
		ORDER_GOODS_NAME = order_goods_name;
	}
	public String getORDER_GOODS_SIZE() {
		return ORDER_GOODS_SIZE;
	}
	public void setORDER_GOODS_SIZE(String order_goods_size) {
		ORDER_GOODS_SIZE = order_goods_size;
	}
	public String getORDER_GOODS_COLOR() {
		return ORDER_GOODS_COLOR;
	}
	public void setORDER_GOODS_COLOR(String order_goods_color) {
		ORDER_GOODS_COLOR = order_goods_color;
	}
	public String getORDER_TRANS_NUM() {
		return ORDER_TRANS_NUM;
	}
	public void setORDER_TRANS_NUM(String order_trans_num) {
		ORDER_TRANS_NUM = order_trans_num;
	}
	public int getORDER_NUM() {
		return ORDER_NUM;
	}
	public void setORDER_NUM(int order_num) {
		ORDER_NUM = order_num;
	}
	public String getORDER_TRADE_NUM() {
		return ORDER_TRADE_NUM;
	}
	public void setORDER_TRADE_NUM(String order_trade_num) {
		ORDER_TRADE_NUM = order_trade_num;
	}
	public int getORDER_GOODS_NUM() {
		return ORDER_GOODS_NUM;
	}
	public void setORDER_GOODS_NUM(int order_goods_num) {
		ORDER_GOODS_NUM = order_goods_num;
	}
	public int getORDER_GOODS_AMOUNT() {
		return ORDER_GOODS_AMOUNT;
	}
	public void setORDER_GOODS_AMOUNT(int order_goods_amount) {
		ORDER_GOODS_AMOUNT = order_goods_amount;
	}
	public String getORDER_MEMBER_ID() {
		return ORDER_MEMBER_ID;
	}
	public void setORDER_MEMBER_ID(String order_member_id) {
		ORDER_MEMBER_ID = order_member_id;
	}
	public String getORDER_RECEIVE_NAME() {
		return ORDER_RECEIVE_NAME;
	}
	public void setORDER_RECEIVE_NAME(String order_receive_name) {
		ORDER_RECEIVE_NAME = order_receive_name;
	}
	public String getORDER_RECEIVE_ADDR1() {
		return ORDER_RECEIVE_ADDR1;
	}
	public void setORDER_RECEIVE_ADDR1(String order_receive_addr1) {
		ORDER_RECEIVE_ADDR1 = order_receive_addr1;
	}
	public String getORDER_RECEIVE_ADDR2() {
		return ORDER_RECEIVE_ADDR2;
	}
	public void setORDER_RECEIVE_ADDR2(String order_receive_addr2) {
		ORDER_RECEIVE_ADDR2 = order_receive_addr2;
	}
	public String getORDER_RECEIVE_PHONE() {
		return ORDER_RECEIVE_PHONE;
	}
	public void setORDER_RECEIVE_PHONE(String order_receive_phone) {
		ORDER_RECEIVE_PHONE = order_receive_phone;
	}
	public String getORDER_RECEIVE_MOBILE() {
		return ORDER_RECEIVE_MOBILE;
	}
	public void setORDER_RECEIVE_MOBILE(String order_receive_mobile) {
		ORDER_RECEIVE_MOBILE = order_receive_mobile;
	}
	public String getORDER_MEMO() {
		return ORDER_MEMO;
	}
	public void setORDER_MEMO(String order_memo) {
		ORDER_MEMO = order_memo;
	}
	public int getORDER_SUM_MONEY() {
		return ORDER_SUM_MONEY;
	}
	public void setORDER_SUM_MONEY(int order_sum_money) {
		ORDER_SUM_MONEY = order_sum_money;
	}
	public String getORDER_TRADE_TYPE() {
		return ORDER_TRADE_TYPE;
	}
	public void setORDER_TRADE_TYPE(String order_trade_type) {
		ORDER_TRADE_TYPE = order_trade_type;
	}
	public Date getORDER_TRADE_DATE() {
		return ORDER_TRADE_DATE;
	}
	public void setORDER_TRADE_DATE(Date order_trade_date) {
		ORDER_TRADE_DATE = order_trade_date;
	}
	public String getORDER_TRADE_PAYER() {
		return ORDER_TRADE_PAYER;
	}
	public void setORDER_TRADE_PAYER(String order_trade_payer) {
		ORDER_TRADE_PAYER = order_trade_payer;
	}
	public Date getORDER_DATE() {
		return ORDER_DATE;
	}
	public void setORDER_DATE(Date order_date) {
		ORDER_DATE = order_date;
	}
	public int getORDER_STATUS() {
		return ORDER_STATUS;
	}
	public void setORDER_STATUS(int order_status) {
		ORDER_STATUS = order_status;
	}
}

 : OrderDAO
package net.order.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.basket.db.BasketBean;
import net.goods.db.GoodsBean;

public class OrderDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs, rs1;
	
	public OrderDAO(){
		try{
			Context initCtx=new InitialContext();
			Context envCtx=(Context)initCtx.lookup("java:comp/env");
			DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			conn=ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}
	
	public int getOrderCount(String id) throws SQLException {
		String sql="select count(*) from goods_order where ORDER_MEMBER_ID=?";
		
		try{
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			rs.next();			
			return rs.getInt(1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	
	public int getOrderSumMoney(String id) throws SQLException{
		String sql="select sum(ORDER_SUM_MONEY) from goods_order "+
				   "where ORDER_MEMBER_ID=?";
		
		try{
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			rs.next();
			
			return rs.getInt(1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return 0;
	}
	
	public List getOrderList(int page,int limit,String id) throws SQLException {
		String sql="select * from (select rownum rnum,ORDER_GOODS_NUM,"+
				"ORDER_GOODS_NAME,ORDER_GOODS_AMOUNT,ORDER_GOODS_SIZE,"+
				"ORDER_GOODS_COLOR,ORDER_SUM_MONEY,ORDER_DATE,"+
				"ORDER_STATUS from (select * from GOODS_ORDER " +
				"where ORDER_MEMBER_ID=? order by ORDER_DATE desc)) "+
				"where rnum>=? and rnum<=?";
		List goods_order_list=new ArrayList();
		
		int startrow=(page-1)*10+1;
		int endrow=startrow+limit-1;
		
		try{
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setInt(2,startrow);
			pstmt.setInt(3,endrow);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				OrderBean order=new OrderBean();
				order.setORDER_GOODS_NUM(
						rs.getInt("ORDER_GOODS_NUM"));
				order.setORDER_GOODS_NAME(
						rs.getString("ORDER_GOODS_NAME"));
				order.setORDER_GOODS_AMOUNT(
						rs.getInt("ORDER_GOODS_AMOUNT"));
				order.setORDER_GOODS_SIZE(
						rs.getString("ORDER_GOODS_SIZE"));
				order.setORDER_GOODS_COLOR(
						rs.getString("ORDER_GOODS_COLOR"));
				order.setORDER_SUM_MONEY(
						rs.getInt("ORDER_SUM_MONEY"));
				order.setORDER_STATUS(rs.getInt("ORDER_STATUS"));
				order.setORDER_DATE(rs.getDate("ORDER_DATE"));
				
				goods_order_list.add(order);
			}
			
			return goods_order_list;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public int addOrder(OrderBean order, Vector goodsvector){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		
		int ordernum=0;
		String sql="select max(ORDER_NUM) from goods_order";
		try {
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			ordernum=rs.getInt(1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		List basketlist=(ArrayList)goodsvector.get(0);
		List goodslist=(ArrayList)goodsvector.get(1);
		
		for (int i = 0; i < basketlist.size(); i++) {
			BasketBean basket=(BasketBean)basketlist.get(i);
			GoodsBean goods=(GoodsBean)goodslist.get(i);
			
			try {
				++ordernum;
				
				sql = "insert into goods_order values(?,?,?,?,?,"+
					"?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,0)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ordernum);
				pstmt.setString(2,sdf.format(
						cal.getTime()).toString()+ "-" + ordernum);
				pstmt.setString(3, " ");
				pstmt.setInt(4, goods.getGOODS_NUM());
				pstmt.setString(5, goods.getGOODS_NAME());
				pstmt.setInt(6, basket.getBASKET_GOODS_AMOUNT());
				pstmt.setString(7, basket.getBASKET_GOODS_SIZE());
				pstmt.setString(8, basket.getBASKET_GOODS_COLOR());
				pstmt.setString(9, order.getORDER_MEMBER_ID());
				pstmt.setString(10, order.getORDER_RECEIVE_NAME());
				pstmt.setString(11, order.getORDER_RECEIVE_ADDR1());
				pstmt.setString(12, order.getORDER_RECEIVE_ADDR2());
				pstmt.setString(13, order.getORDER_RECEIVE_PHONE());
				pstmt.setString(14, order.getORDER_RECEIVE_MOBILE());
				pstmt.setString(15, order.getORDER_MEMO());
				pstmt.setInt(16, (goods.getGOODS_PRICE()*basket.getBASKET_GOODS_AMOUNT()));
				pstmt.setString(17, order.getORDER_TRADE_TYPE());
				pstmt.setString(18, order.getORDER_TRADE_PAYER());
      			
      			pstmt.executeUpdate();
      		} catch (SQLException e) {
      			e.printStackTrace();
      		}
      	}
		
		return ordernum;
	}
}

 : 주문 처리에 대한 Action 클래스 구현
[Action.java]
package net.order.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.order.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[OrderListAction.java]
package net.order.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.order.db.OrderDAO;

public class OrderListAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response){
		ActionForward forward=new ActionForward();
		
		HttpSession session = request.getSession();
		String id =(String)session.getAttribute("id");
		
		if(id==null){			
			forward.setRedirect(true);
			forward.setPath("./MemberLogin.me");
			return forward;
		}
		
		int page=1;
		int limit=10;
		OrderDAO orderdao=new OrderDAO();
		List goods_order_list=new ArrayList();
		
		if(request.getParameter("page")!=null){
			page=Integer.parseInt(request.getParameter("page"));
		}
		
		try{
			int ordercount=orderdao.getOrderCount(id);
			int totalmoney=orderdao.getOrderSumMoney(id);
			goods_order_list = orderdao.getOrderList(page,limit,id);
			
			int maxpage=(int)((double)ordercount/limit+0.95);
			int startpage = (((int) ((double)page / 10 + 0.9)) - 1) * 10 + 1;
			int endpage = maxpage;
			if (endpage>startpage+10-1) endpage=startpage+10-1;
			
			request.setAttribute("page", page);
			request.setAttribute("maxpage", maxpage);
			request.setAttribute("startpage", startpage);
			request.setAttribute("endpage", endpage);
			request.setAttribute("ordercount", ordercount);
			request.setAttribute("totalmoney", totalmoney);
			request.setAttribute("goods_order_list", goods_order_list);
			
			forward.setRedirect(false);
			forward.setPath("./goods_order/goods_my_order.jsp");
			return forward;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	 } 
}

[OrderStartAction.java]
package net.order.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.basket.db.BasketDAO;
import net.member.db.MemberDAO;
import net.member.db.MemberBean;

public class OrderStartAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		ActionForward forward=new ActionForward();
		
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		if(id==null){
			forward.setRedirect(true);
			forward.setPath("./MemberLogin.me");
			return forward;
		}
		
		request.setCharacterEncoding("euc-kr");
		
		List orderinfo=new ArrayList();
		String order=request.getParameter("order");
		
		if(order.equals("goods")){
			orderinfo.add(Integer.parseInt(request.getParameter("goodsnum")));
			orderinfo.add(request.getParameter("goodsname"));
			orderinfo.add(Integer.parseInt(request.getParameter("amount")));
			orderinfo.add(request.getParameter("size"));
			orderinfo.add(request.getParameter("color"));
			orderinfo.add(Integer.parseInt(request.getParameter("price")));
			orderinfo.add(request.getParameter("goodsimage"));
			
			request.setAttribute("ordertype", "goods");
			request.setAttribute("orderinfo", orderinfo);
		}else{
			BasketDAO basketdao=new BasketDAO();
			Vector vector=basketdao.getBasketList(id);
			List basketlist=(ArrayList)vector.get(0);
			List goodslist=(ArrayList)vector.get(1);
			
			request.setAttribute("ordertype", "basket");
			request.setAttribute("basketlist", basketlist);
			request.setAttribute("goodslist", goodslist);
		}
		
		MemberDAO memberdao=new MemberDAO();
		MemberBean member=(MemberBean)memberdao.getMember(id);
		request.setAttribute("member", member);
		
		forward.setRedirect(false);
		forward.setPath("./goods_order/goods_buy.jsp");
		return forward;
	}
}

[OrderAddAction.java]
package net.order.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.basket.db.BasketBean;
import net.basket.db.BasketDAO;

import net.goods.db.GoodsBean;

import net.order.db.OrderBean;
import net.order.db.OrderDAO;

public class OrderAddAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		request.setCharacterEncoding("euc-kr");
		
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		
		if(id==null){
			PrintWriter out=response.getWriter();
			out.println("<script>");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		
		Vector goodsvector=new Vector();
		
		OrderDAO orderdao=new OrderDAO();
		OrderBean order=new OrderBean();
		
		List basketlist=new ArrayList();
		List goodslist=new ArrayList();
		GoodsBean goods=new GoodsBean();
		BasketDAO basketdao=new BasketDAO();
		BasketBean basket=new BasketBean();
		
		order.setORDER_MEMBER_ID(request.getParameter("memberid"));
		order.setORDER_RECEIVE_NAME(
				request.getParameter("ORDER_RECEIVE_NAME"));
		order.setORDER_RECEIVE_PHONE(
				request.getParameter("ORDER_RECEIVE_PHONE"));
		order.setORDER_RECEIVE_MOBILE(
				request.getParameter("ORDER_RECEIVE_MOBILE"));
		order.setORDER_RECEIVE_ADDR1(
				request.getParameter("ORDER_RECEIVE_ZIPCODE")+
				" "+request.getParameter("ORDER_RECEIVE_ADDR1"));
		order.setORDER_RECEIVE_ADDR2(
				request.getParameter("ORDER_RECEIVE_ADDR2"));
		order.setORDER_MEMO(request.getParameter("ORDER_MEMO"));
		order.setORDER_TRADE_TYPE("온라인입금");
		order.setORDER_TRADE_PAYER(request.getParameter("ORDER_TRADE_PAYER"));
		
		String ordertype=request.getParameter("ordertype");
		if(ordertype.equals("goods")){
			basket.setBASKET_GOODS_NUM(	Integer.parseInt(
						request.getParameter("goodsnum")));
			basket.setBASKET_GOODS_AMOUNT(Integer.parseInt(
						request.getParameter("goodsamount")));
			basket.setBASKET_GOODS_SIZE(
						request.getParameter("goodssize"));
			basket.setBASKET_GOODS_COLOR(
						request.getParameter("goodscolor"));
			
			goods.setGOODS_NAME(request.getParameter("goodsname"));
			goods.setGOODS_PRICE(Integer.parseInt(
						request.getParameter("goodsprice")));
			
			basketlist.add(basket);
			goodslist.add(goods);
			goodsvector.add(basketlist);
			goodsvector.add(goodslist);
		}else{
			goodsvector=basketdao.getBasketList(id);
		}
		
		orderdao.addOrder(order, goodsvector);
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(true);
		forward.setPath("./OrderOk.or");
		return forward;
	}
}

[OrderFrontController.java]
package net.order.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderFrontController extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {
	private void execute(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = RequestURI.substring(contextPath.length());
		ActionForward forward = null;
		Action action = null;
		
		if (command.equals("/OrderStart.or")) {
			action  = new OrderStartAction();
			try {
				forward=action.execute(request, response );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (command.equals("/OrderList.or")) {
			action  = new OrderListAction();
			try {
				forward=action.execute(request, response );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (command.equals("/OrderAdd.or")) {
			action  = new OrderAddAction();
			try {
				forward=action.execute(request, response );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (command.equals("/OrderOk.or")){
			forward=new ActionForward();
			forward.setRedirect(false);
			forward.setPath("./goods_order/goods_order_ok.jsp");
		}

		if (forward.isRedirect()) {
			response.sendRedirect(forward.getPath());
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(forward
					.getPath());
			dispatcher.forward(request, response);
		}
	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}
}

 : web.xml
...
<servlet>
	<description></description>
	<display-name>OrderFrontController</display-name>
	<servlet-name>OrderFrontController</servlet-name>
	<servlet-class>net.order.action.OrderFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>OrderFrontController</servlet-name>
	<url-pattern>*.or</url-pattern>
</servlet-mapping>
...

 : View 페이지 작성(/WebContent/goods_order/)
[goods_buy.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.util.*"%>
<%@ page import="net.member.db.MemberBean"%>
<%@ page import="net.basket.db.BasketBean"%>
<%@ page import="net.goods.db.GoodsBean" %>
<%
	MemberBean member = (MemberBean) request.getAttribute("member");
	String ordertype = (String) request.getAttribute("ordertype");
	List orderinfo = null;
	List basketlist = null;
	List goodslist=null;
	
	if (ordertype.equals("goods")) {
		orderinfo = (ArrayList) request.getAttribute("orderinfo");
	} else {
		basketlist = (ArrayList) request.getAttribute("basketlist");
		goodslist = (ArrayList) request.getAttribute("goodslist");
	}
%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
<tr>
	<td colspan=2 align=right>
	<!-- 주문 페이지 -->
	<form action="./OrderAdd.or" method="post" name="orderform">
	<input type="hidden" name="ordertype" value="<%=ordertype%>"> 
	<%if (ordertype.equals("goods")) {%>
	<input type="hidden" name="goodsnum" value="<%=orderinfo.get(0)%>">
	<input type="hidden" name="goodsname" value="<%=orderinfo.get(1)%>">
	<input type="hidden" name="goodsamount" value="<%=orderinfo.get(2)%>">
	<input type="hidden" name="goodscolor" value="<%=orderinfo.get(3)%>">
	<input type="hidden" name="goodssize" value="<%=orderinfo.get(4)%>">
	<input type="hidden" name="goodsprice" value="<%=orderinfo.get(5)%>">
	<%}%>
	<input type="hidden" name="memberid" value="<%=member.getMEMBER_ID() %>">
	
	<!-- 주문 상세내역 -->
	<table border=0 cellspacing=1 cellpadding=0 width=80%>
		<tr>
			<p align=left><b><font size=2>주문 상세내역</font></b></p>
		</tr>
		<tr align=center height=20>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">사진</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">상품명</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">수량</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">색깔</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">사이즈</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">가격</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<%
			if (ordertype.equals("goods")) {
		%>
		<tr align=center height=20>
		<td style="font-family: Tahoma; font-size: 7pt;"><img
			src="./upload/<%=orderinfo.get(6) %>" width=50 height=50></td>
		<td style="font-family: Tahoma; font-size: 8pt;"><%=orderinfo.get(1)%></td>
		<td style="font-family: Tahoma; font-size: 8pt;"><%=orderinfo.get(2)%></td>
		<td style="font-family: Tahoma; font-size: 8pt;"><%=orderinfo.get(3)%></td>
		<td style="font-family: Tahoma; font-size: 8pt;"><%=orderinfo.get(4)%></td>
		<td style="font-family: Tahoma; font-size: 8pt;"><%=orderinfo.get(5)%></td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<%
			} else {
				for (int i = 0; i < basketlist.size(); i++) {
					BasketBean basket = (BasketBean) basketlist.get(i);
					GoodsBean goods = (GoodsBean) goodslist.get(i);
		%>
		<tr align=center height=20>
		<td style="font-family: Tahoma; font-size: 7pt;">
			<img src="./upload/<%=goods.getGOODS_IMAGE().split(",")[0] %>" 
				width=50 height=50>
		</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=goods.getGOODS_NAME()%>
		</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=basket.getBASKET_GOODS_AMOUNT()%>
		</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=basket.getBASKET_GOODS_COLOR()%>
		</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=basket.getBASKET_GOODS_SIZE()%>
		</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=goods.getGOODS_PRICE()%>
		</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<%
				}
			}
		%>
	</table>

	<table width=80% border=0 cellpadding="0" cellspacing="1">
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td><b><font size=2>주문자 정보</font></b></td>
		</tr>
		<tr>
			<td style="font-family: Tahoma; font-size: 8pt;" width=80 height=24
				bgcolor="f7f7f7">이름</td>
			<td width=320 height=24>
				<font size=2><%=member.getMEMBER_NAME()%></font>
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr>
			<td style="font-family: Tahoma; font-size: 8pt;" height=24
				bgcolor="f7f7f7">휴대폰</td>
			<td width=320 height=24>
				<font size=2><%=member.getMEMBER_MOBILE()%></font>
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr>
			<td style="font-family: Tahoma; font-size: 8pt;" height=24
				bgcolor="f7f7f7">이메일 주소</td>
			<td width=320 height=24>
				<font size=2><%=member.getMEMBER_EMAIL()%></font>
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
	</table>

	<table width=80% border=0 cellpadding="0" cellspacing="1">
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td><b><font size=2>배송지 정보</font></b></td>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">받는사람</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_NAME" size=12
				value="<%=member.getMEMBER_NAME() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=23>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">집 전화</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_PHONE" size=15
				value="<%=member.getMEMBER_PHONE() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">휴대폰</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_MOBILE" size=15
				value="<%=member.getMEMBER_MOBILE() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">배송지 우편번호</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_ZIPCODE" size=7
				value="<%=member.getMEMBER_ZIPCODE() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">배송지 주소</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_ADDR1" size=50
				value="<%=member.getMEMBER_ADDR1() %>"></td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">배송지 나머지 주소</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
				<input type="text" name="ORDER_RECEIVE_ADDR2" size=50
				value="<%=member.getMEMBER_ADDR2() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr height=20>
			<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
				bgcolor="f7f7f7">기타 요구사항</td>
			<td style="font-family: Tahoma; font-size: 8pt;">
			<textarea name="ORDER_MEMO" cols=60 rows=12></textarea>
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
	</table>

	<table width=80% border=0 cellpadding="0" cellspacing="1">
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td height=10>
			<td>
		</tr>
		<tr>
			<td><b><font size=2>결제 정보</font></b></td>
		</tr>
		<tr>
			<td style="font-family: Tahoma; font-size: 8pt;" width=200 height=24
				bgcolor="f7f7f7">입금자명(온라인입금일 경우) :</td>
			<td width=320 height=24>
			<input type="text" name="ORDER_TRADE_PAYER"
				size=20 value="<%=member.getMEMBER_NAME() %>">
			</td>
		</tr>
		<tr>
			<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
		</tr>
		<tr>
			<td align=center style="background-color: #F0F0F0; height: 1px;"
				colspan=6>
				<input type=submit value="주문">&nbsp;
				<input type=button value="취소">
			</td>
		</tr>
	</table>
	</form>
	<!-- 주문 페이지 -->
	</td>
</tr>
</table>
</body>
</html>

[goods_order_ok.jsp]
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
<tr>
<td colspan=2 align=center>
	<b><font size=5>주문이 완료되었습니다.</font></b>
	<table width=50% border=0 cellpadding="0" cellspacing="1">
	<tr><td height=10><td></tr>
	<tr><td height=10><td></tr>
	<tr>
		<td><b><font size=2>배송 정보</font></b></td>
	</tr>
	<tr align=center>
		<td style="font-family: Tahoma; font-size: 8pt;" width=80 height=24
			bgcolor="f7f7f7">입금 은행</td>
		<td width=250 height=24><font size=2>국민은행</font></td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr align=center>
		<td style="font-family: Tahoma; font-size: 8pt;" width=80 height=24
			bgcolor="f7f7f7">예금주</td>
		<td width=250 height=24><font size=2>홍길동</font></td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr align=center>
		<td style="font-family: Tahoma; font-size: 8pt;" width=80 height=24
			bgcolor="f7f7f7">입금 계좌</td>
		<td width=250 height=24><font size=2>601xxx-xx-xxxxxxx</font></td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
</table>
</td>
</tr>
</table>
</body>
</html>

[goods_my_order.jsp]
<%@ page contentType="text/html;charset=euc-kr"%>
<%@ page import="java.util.*"%>
<%@ page import="net.order.db.*"%>
<%
	String id=(String)session.getAttribute("id");
	List goods_order_list=(List)request.getAttribute("goods_order_list");
	int ordercount=((Integer)request.getAttribute("ordercount")).intValue();
	int nowpage=((Integer)request.getAttribute("page")).intValue();
	int maxpage=((Integer)request.getAttribute("maxpage")).intValue();
	int startpage=((Integer)request.getAttribute("startpage")).intValue();
	int endpage=((Integer)request.getAttribute("endpage")).intValue();
	int totalmoney=((Integer)request.getAttribute("totalmoney")).intValue();
%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
<tr>
<td colspan=2>
<!-- 회원의 주문내역 보기 -->
<table width="700" border="0" align="center">
<tr>
	<td>현재 (<%=id%>)고객님께서 주문하신 내역이 총 (<%=ordercount%>)개 있습니다.</td>
</tr>
<tr>
	<td height="62" align="center" valign="middle">
	<table width="700" border="1" cellspacing="0" cellpadding="0"
		bordercolor="#CCCCCC">
		<tr>
			<td height="20"><div align="center">상품명</div></td>
			<td><div align="center">색상/사이즈</div>	</td>
			<td><div align="center">수량</div></td>
			<td><div align="center">총 금액</div></td>
			<td><div align="center">주문 상태</div></td>
			<td><div align="center">주문 날짜</div></td>
		</tr>
		<%
		if (goods_order_list.size() == 0) {
		%>
		<td align=center colspan=6>주문 내역이 없습니다.</td>
		<%
		}
		
		for (int i = 0; i < goods_order_list.size(); i++) {
			OrderBean order = new OrderBean();
			order = (OrderBean) goods_order_list.get(i);
		%>
		<tr align=center>
			<td height="20"><%=order.getORDER_GOODS_NAME()%></td>
			<td>
				<%=order.getORDER_GOODS_COLOR()%>/
				<%=order.getORDER_GOODS_SIZE()%>
			</td>
			<td><%=order.getORDER_GOODS_AMOUNT()%></td>
			<td><%=order.getORDER_SUM_MONEY()%></td>
			<td>
			<%if (order.getORDER_STATUS() == 0) {%>대기중
			<%}else if (order.getORDER_STATUS() == 1){%>발송준비
			<%}else if (order.getORDER_STATUS() == 2){%>발송완료
			<%}else if (order.getORDER_STATUS() == 3){%>배송중
			<%}else if (order.getORDER_STATUS() == 4){%>배송완료
			<%}else if (order.getORDER_STATUS() == 5){%>주문취소
			<%}%>
			</td>
			<td><%=order.getORDER_DATE()%></td>
		</tr>
		<%
			}
		%>
		<tr align=center height=20>
			<td colspan=7 style="font-family: Tahoma; font-size: 10pt;">
			<%if (nowpage <= 1) {%>
			[이전]&nbsp;
			<%}else{%>
			<a href="./OrderList.or?page=<%=nowpage-1 %>">[이전]</a>&nbsp;
			<%}%>
			<%
			for (int a = startpage; a <= endpage; a++) {
				if (a == nowpage) {
			%>
			[<%=a%>]
			<%
				}else{
			%>
			<a href="./OrderList.or?page=<%=a %>">[<%=a%>]</a>&nbsp;
			<%	}
			}
			%>
			<%if (nowpage >= maxpage) {%>
			[다음]
			<%}else{%>
			<a href="./OrderList.or?page=<%=nowpage+1 %>">[다음]</a>
			<%}%>
			</td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td height="28">
	<div align="right">총 주문금액 : <%=totalmoney%>원</div>
	</td>
</tr>
</table>
<!-- 회원의 주문내역 보기 -->
</td>
</tr>
</table>
</body>
</html>

7. 주문 관리 시스템 구현
 : AdminOrderDAO
package net.admin.order.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.order.db.OrderBean;

public class AdminOrderDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public AdminOrderDAO(){
		try{
			Context initCtx=new InitialContext();
			   Context envCtx=(Context)initCtx.lookup("java:comp/env");
			   DataSource ds=(DataSource)envCtx.lookup("jdbc/OracleDB");
			   conn=ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}
	
	public int getOrderCount(){
		String order_count_sql="select count(*) from GOODS_ORDER";		
		try{
			pstmt=conn.prepareStatement(order_count_sql);
			rs=pstmt.executeQuery();
			rs.next();
			
			return rs.getInt(1);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		return 0;
	}
	
	public List getOrderList(int page,int limit){
		String order_list_sql=
			"select * from (select rownum rnum,ORDER_NUM,"+
			"ORDER_TRADE_NUM,ORDER_TRANS_NUM,ORDER_GOODS_NUM,"+
			"ORDER_GOODS_AMOUNT,ORDER_MEMBER_ID,"+
			"ORDER_RECEIVE_NAME,ORDER_RECEIVE_ADDR1,"+
			"ORDER_RECEIVE_ADDR2,ORDER_RECEIVE_PHONE,"+
			"ORDER_RECEIVE_MOBILE,ORDER_MEMO,"+
			"ORDER_SUM_MONEY,ORDER_TRADE_TYPE,"+
			"ORDER_TRADE_DATE,ORDER_TRADE_PAYER,"+
			"ORDER_DATE,ORDER_STATUS from "+
			"(select * from GOODS_ORDER order by "+
			"ORDER_DATE desc)) where rnum>=? and rnum<=?";
		List orderlist=new ArrayList();
		
		int startrow=(page-1)*10+1;
		int endrow=startrow+limit-1;
		try {
			pstmt=conn.prepareStatement(order_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs=pstmt.executeQuery();
			while(rs.next()){
				OrderBean order=new OrderBean();
				order.setORDER_NUM(rs.getInt("ORDER_NUM"));
				order.setORDER_TRADE_NUM(
						rs.getString("ORDER_TRADE_NUM"));
				order.setORDER_TRANS_NUM(
						rs.getString("ORDER_TRANS_NUM"));
				order.setORDER_GOODS_NUM(
						rs.getInt("ORDER_GOODS_NUM"));
				order.setORDER_GOODS_AMOUNT(
						rs.getInt("ORDER_GOODS_AMOUNT"));
				order.setORDER_MEMBER_ID(
						rs.getString("ORDER_MEMBER_ID"));
				order.setORDER_RECEIVE_NAME(
						rs.getString("ORDER_RECEIVE_NAME"));
				order.setORDER_RECEIVE_ADDR1(
						rs.getString("ORDER_RECEIVE_ADDR1"));
				order.setORDER_RECEIVE_ADDR2(
						rs.getString("ORDER_RECEIVE_ADDR2"));
				order.setORDER_RECEIVE_PHONE(
						rs.getString("ORDER_RECEIVE_PHONE"));
				order.setORDER_RECEIVE_MOBILE(
						rs.getString("ORDER_RECEIVE_MOBILE"));
				order.setORDER_MEMO(rs.getString("ORDER_MEMO"));
				order.setORDER_SUM_MONEY(
						rs.getInt("ORDER_SUM_MONEY"));
				order.setORDER_TRADE_TYPE(
						rs.getString("ORDER_TRADE_TYPE"));
				order.setORDER_TRADE_DATE(
						rs.getDate("ORDER_TRADE_DATE"));
				order.setORDER_TRADE_PAYER(
						rs.getString("ORDER_TRADE_PAYER"));
				order.setORDER_DATE(rs.getDate("ORDER_DATE"));
				order.setORDER_STATUS(rs.getInt("ORDER_STATUS"));
				orderlist.add(order);
			}
			
			return orderlist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderBean getOrderDetail(int ordernum){
		String order_detail_sql="select * from GOODS_ORDER where ORDER_NUM=?";
		
		try {
			pstmt=conn.prepareStatement(order_detail_sql);
			pstmt.setInt(1, ordernum);
			rs=pstmt.executeQuery();
			rs.next();
			
			OrderBean order=new OrderBean();
			order.setORDER_NUM(rs.getInt("ORDER_NUM"));
			order.setORDER_TRADE_NUM(rs.getString("ORDER_TRADE_NUM"));
			order.setORDER_TRANS_NUM(rs.getString("ORDER_TRANS_NUM"));
			order.setORDER_GOODS_NUM(rs.getInt("ORDER_GOODS_NUM"));
			order.setORDER_GOODS_NAME(rs.getString("ORDER_GOODS_NAME"));
			order.setORDER_GOODS_AMOUNT(
					rs.getInt("ORDER_GOODS_AMOUNT"));
			order.setORDER_GOODS_SIZE(rs.getString("ORDER_GOODS_SIZE"));
			order.setORDER_GOODS_COLOR(
					rs.getString("ORDER_GOODS_COLOR"));
			order.setORDER_MEMBER_ID(rs.getString("ORDER_MEMBER_ID"));
			order.setORDER_RECEIVE_NAME(
					rs.getString("ORDER_RECEIVE_NAME"));
			order.setORDER_RECEIVE_ADDR1(
					rs.getString("ORDER_RECEIVE_ADDR1"));
			order.setORDER_RECEIVE_ADDR2(
					rs.getString("ORDER_RECEIVE_ADDR2"));
			order.setORDER_RECEIVE_PHONE(
					rs.getString("ORDER_RECEIVE_PHONE"));
			order.setORDER_RECEIVE_MOBILE(
					rs.getString("ORDER_RECEIVE_MOBILE"));
			order.setORDER_MEMO(rs.getString("ORDER_MEMO"));
			order.setORDER_SUM_MONEY(rs.getInt("ORDER_SUM_MONEY"));
			order.setORDER_TRADE_TYPE(rs.getString("ORDER_TRADE_TYPE"));
			order.setORDER_TRADE_DATE(rs.getDate("ORDER_TRADE_DATE"));
			order.setORDER_TRADE_PAYER(
					rs.getString("ORDER_TRADE_PAYER"));
			order.setORDER_DATE(rs.getDate("ORDER_DATE"));
			order.setORDER_STATUS(rs.getInt("ORDER_STATUS"));
			
			return order;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean modifyOrder(OrderBean order){
		String order_modify_sql=
			"update GOODS_ORDER set ORDER_TRANS_NUM=?,"+
			"ORDER_MEMO=?,ORDER_STATUS=? where ORDER_NUM=?";
		int result=0;
		
		try{
			pstmt=conn.prepareStatement(order_modify_sql);
			pstmt.setString(1, order.getORDER_TRANS_NUM());
			pstmt.setString(2, order.getORDER_MEMO());
			pstmt.setInt(3, order.getORDER_STATUS());
			pstmt.setInt(4, order.getORDER_NUM());
			result=pstmt.executeUpdate();
			
			if(result==1){
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean deleteOrder(int ordernum){
		String order_delete_sql="delete from GOODS_ORDER where ORDER_NUM=?";
		int result=0;
		
		try{
			pstmt=conn.prepareStatement(order_delete_sql);
			pstmt.setInt(1, ordernum);
			result=pstmt.executeUpdate();
			
			if(result==1){
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
}

 : 주문 관리에 대한 Action 클래스 구현
[Action.java]
package net.admin.order.action;

import javax.servlet.http.*;

public interface Action {
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception;
}

[ActionForward.java]
package net.admin.order.action;

public class ActionForward {
	private boolean isRedirect=false;
	private String path=null;
	
	public boolean isRedirect(){
		return isRedirect;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRedirect(boolean b){
		isRedirect=b;
	}
	
	public void setPath(String string){
		path=string;
	}
}

[AdminOrderListAction.java]
package net.admin.order.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.order.db.AdminOrderDAO;

public class AdminOrderListAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{		
		AdminOrderDAO orderdao=new AdminOrderDAO();
		List orderlist = new ArrayList();
		int page=1;
		int limit=10;
		
		if(request.getParameter("page")!=null){
			page=Integer.parseInt(request.getParameter("page"));
		}
		int ordercount=orderdao.getOrderCount();
		orderlist = orderdao.getOrderList(page,limit);
		
	   	int maxpage=(int)((double)ordercount/limit+0.95);
	   	int startpage = (((int) ((double)page / 10 + 0.9)) - 1) * 10 + 1;
	   	int endpage = maxpage;
	   	if (endpage>startpage+10-1) endpage=startpage+10-1;
	   	
	   	request.setAttribute("page", page);
	   	request.setAttribute("maxpage", maxpage);
	   	request.setAttribute("startpage", startpage);
	   	request.setAttribute("endpage", endpage);
		request.setAttribute("ordercount", ordercount);
		request.setAttribute("orderlist", orderlist);
		
		ActionForward forward=new ActionForward();
		forward.setPath("./adminorder/admin_order_list.jsp");
		return forward;
	 } 
}

[AdminOrderDetailAction.java]
package net.admin.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.order.db.AdminOrderDAO;
import net.member.db.MemberBean;
import net.member.db.MemberDAO;
import net.order.db.OrderBean;

public class AdminOrderDetailAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		MemberDAO memberdao=new MemberDAO();
		MemberBean member=new MemberBean();
		AdminOrderDAO orderdao=new AdminOrderDAO();
		OrderBean order=new OrderBean();
		
		String num=request.getParameter("num");
		order = orderdao.getOrderDetail(Integer.parseInt(num));
		member=memberdao.getMember(order.getORDER_MEMBER_ID());
		request.setAttribute("order", order);
		request.setAttribute("ordermember", member);
		
		ActionForward forward=new ActionForward();
		forward.setPath("./adminorder/admin_order_modify.jsp");
		return forward;
	 } 
}

[AdminOrderModifyAction.java]
package net.admin.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.order.db.AdminOrderDAO;
import net.order.db.OrderBean;

public class AdminOrderModifyAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		AdminOrderDAO orderdao=new AdminOrderDAO();
		OrderBean order=new OrderBean();
		
		boolean result=false;
		request.setCharacterEncoding("euc-kr");
		order.setORDER_NUM(Integer.parseInt(request.getParameter("num")));
		order.setORDER_TRANS_NUM(request.getParameter("transportnum"));
		order.setORDER_MEMO(request.getParameter("memo"));
		order.setORDER_STATUS(Integer.parseInt(request.getParameter("status")));
		
		result=orderdao.modifyOrder(order);
		if(result==false){
			System.out.println("상품 수정 실패");
			return null;
		}
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(true);
		forward.setPath("./AdminOrderList.ao");
		return forward;
	 } 
}

[AdminOrderDeleteAction.java]
package net.admin.order.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.admin.order.db.AdminOrderDAO;

public class AdminOrderDeleteAction implements Action{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	throws Exception{
		AdminOrderDAO orderdao=new AdminOrderDAO();
		
		boolean result=false;
		String num=request.getParameter("num");
		result=orderdao.deleteOrder(Integer.parseInt(num));
		
		if(result==false){
			System.out.println("상품 삭제 실패");
			return null;
		}
		
		ActionForward forward=new ActionForward();
		forward.setRedirect(true);
		forward.setPath("./AdminOrderList.ao");
		return forward;
	 } 
}

[AdminOrderFrontController.java]
package net.admin.order.action;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminOrderFrontController extends javax.servlet.http.HttpServlet 
 	implements javax.servlet.Servlet {
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		   String RequestURI=request.getRequestURI();
		   String contextPath=request.getContextPath();
		   String command=RequestURI.substring(contextPath.length());
		   ActionForward forward=null;
		   Action action=null;
		   
		   if(command.equals("/AdminOrderList.ao")){
			   action  = new AdminOrderListAction();
			   try {
				   forward=action.execute(request, response );
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		   }else if(command.equals("/AdminOrderDetail.ao")){
			   action = new AdminOrderDetailAction();
			   try{
				   forward=action.execute(request, response);
			   }catch(Exception e){
				   e.printStackTrace();
			   }
		   }else if(command.equals("/AdminOrderModify.ao")){
			   action = new AdminOrderModifyAction();
			   try{
				   forward=action.execute(request, response);
			   }catch(Exception e){
				   e.printStackTrace();
			   }
		   }else if(command.equals("/AdminOrderDelete.ao")){
			   action = new AdminOrderDeleteAction();
			   try{
				   forward=action.execute(request, response);
			   }catch(Exception e){
				   e.printStackTrace();
			   }
		   }
		   
		   if(forward.isRedirect()){
			   response.sendRedirect(forward.getPath());
		   }else{
			   RequestDispatcher dispatcher=
				   request.getRequestDispatcher(forward.getPath());
			   dispatcher.forward(request, response);
		   }
	 }
	 
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	 throws ServletException, IOException {
		 doProcess(request,response);
	 }  	
	
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	 throws ServletException, IOException {
		 doProcess(request,response);
	 }   	  	    
}

 : web.xml
.....
<servlet>
	<description></description>
	<display-name>AdminOrderFrontController</display-name>
	<servlet-name>AdminOrderFrontController</servlet-name>
	<servlet-class>net.admin.order.action.AdminOrderFrontController</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>AdminOrderFrontController</servlet-name>
	<url-pattern>*.ao</url-pattern>
</servlet-mapping>
.....

 : View 페이지 작성(/WebContent/adminorder/)
[admin_order_list.jsp]
<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page import="net.order.db.*" %>
<%@ page import="java.util.*" %>
<%
	List orderlist=(List)request.getAttribute("orderlist");
	int ordercount=((Integer)request.getAttribute("ordercount")).intValue();
	int nowpage=((Integer)request.getAttribute("page")).intValue();
	int maxpage=((Integer)request.getAttribute("maxpage")).intValue();
	int startpage=((Integer)request.getAttribute("startpage")).intValue();
	int endpage=((Integer)request.getAttribute("endpage")).intValue();
%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0" align="center">
<tr>
<td colspan=2 align=center>
<table border=0 cellspacing=1 cellpadding=0 width=80%>
	<tr>
	<td align=right colspan=10 height=25 colspan=2 style=font-family:Tahoma;font-size:8pt;>
	전체 주문 수 : <b><%=ordercount %></b> 개&nbsp;&nbsp;&nbsp;
	</td>
	</tr>
	<tr align=center height=20>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>주문번호</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>주문자</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>결제방법</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>주문금액</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>주문상태</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>주문일시</td>
	  <td style=font-family:Tahoma;font-size:8pt;font-weight:bold;>수정/삭제</td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0; height:1px;" colspan=6>
	</tr>
	<%for(int i=0;i<orderlist.size();i++){ 
		OrderBean order=new OrderBean();
		order=(OrderBean)orderlist.get(i); %>
	<tr align=center height=20>
	<td style=font-family:Tahoma;font-size:7pt;><%=order.getORDER_TRADE_NUM()%></td>
	<td style=font-family:Tahoma;font-size:8pt;><%=order.getORDER_MEMBER_ID()%></td>
	<td style=font-family:Tahoma;font-size:8pt;><%=order.getORDER_TRADE_TYPE()%></td>
	<td style=font-family:Tahoma;font-size:8pt;><%=order.getORDER_SUM_MONEY()%></td>
	<td style=font-family:Tahoma;font-size:8pt;>
   		<%if(order.getORDER_STATUS()==0){ %>
   			대기중
   		<%}else if(order.getORDER_STATUS()==1){ %>
   			발송준비
   		<%}else if(order.getORDER_STATUS()==2){ %>
   			발송완료
   		<%}else if(order.getORDER_STATUS()==3){ %>
   			배송중
   		<%}else if(order.getORDER_STATUS()==4){ %>
   			배송완료
   		<%}else if(order.getORDER_STATUS()==5){ %>
   			주문취소
   		<%} %>
   	</td>
   	<td style=font-family:Tahoma;font-size:8pt;><%=order.getORDER_DATE()%></td>
   	<td style=font-family:Tahoma;font-size:8pt;>
   	<a href="./AdminOrderDetail.ao?num=<%=order.getORDER_NUM() %>">
   	Modify</a>/
   	<a href="./AdminOrderDelete.ao?num=<%=order.getORDER_NUM() %>" 
   		onclick="return confirm('삭제하시겠습니까?')">Delete</a>
   	</td>
	</tr>
	<tr>
		<td style="background-color:#F0F0F0; height:1px;" colspan=6>
	</tr>
	<%} %>
	<tr align=center height=20>
		<td colspan=7 style=font-family:Tahoma;font-size:10pt;>
			<%if(nowpage<=1){ %>[이전]&nbsp;
			<%}else{ %>
			<a href="./AdminOrderList.ao?page=<%=nowpage-1 %>">
			[이전]</a>&nbsp;
			<%}%>
			<%for(int a=startpage;a<=endpage;a++){
				if(a==nowpage){%>[<%=a%>]
				<%}else{ %>
					<a href="./AdminOrderList.ao?page=<%=a %>">
					[<%=a %>]
					</a>&nbsp;
				<%} %>
			<%} %>
			<%if(nowpage>=maxpage){ %>[다음]
			<%}else{ %>
			<a href="./AdminOrderList.ao?page=<%=nowpage+1 %>">[다음]</a>
			<%} %>
		</td>
	</tr>
</table>
</td>
</tr>	
</table>
</body>
</html>

[admin_order_modify.jsp]
<%@ page contentType="text/html;charset=euc-kr"%>
<%@ page import="net.order.db.*"%>
<%@ page import="net.member.db.*"%>
<%
	OrderBean order=(OrderBean)request.getAttribute("order");
	MemberBean ordermember=(MemberBean)request.getAttribute("ordermember");
%>
<html>
<head>
<title>쇼핑몰</title>
</head>
<body>
<table width="960" cellspacing="0" cellpadding="0" border="0"
	align="center">
<tr>
<td colspan=2 align=center>
<!-- 주문 정보 수정(관리자) -->
<form action="./AdminOrderModify.ao" name="orderform" method="post">
<input type="hidden" name="num" value="<%=order.getORDER_NUM() %>">
<table width=80% border=0 cellpadding="0" cellspacing="1">
	<tr><td height=10></td></tr>
	<tr><td height=10></td></tr>
	<tr>
	<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;"
		width=130 height=24 bgcolor="f7f7f7">운송장(등기)번호</td>
	<td width=320 height=24><input type="text" name="transportnum"
		size=15 maxlength=20
		value=<%if(order.getORDER_TRANS_NUM()!=null){%>
		<%=order.getORDER_TRANS_NUM()%> <%} %>></td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;"
			width=130 height=24 bgcolor="f7f7f7">주문번호</td>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;">
		<%=order.getORDER_TRADE_NUM() %></td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;"
			width=130 height=24 bgcolor="f7f7f7" colspan=2>상품 정보</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">상품이름</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_GOODS_NAME() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=23>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">수량</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_GOODS_AMOUNT() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">사이즈</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_GOODS_SIZE()%>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">색깔</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_GOODS_COLOR() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;"
			width=130 height=24 bgcolor="f7f7f7" colspan=2>배송지 정보</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">받는사람</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_RECEIVE_NAME() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=23>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">집전화</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_RECEIVE_PHONE() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">휴대폰</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_RECEIVE_MOBILE()%>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">배송지 주소</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_RECEIVE_ADDR1() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">배송지 나머지 주소</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=order.getORDER_RECEIVE_ADDR2() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt; font-weight: bold;"
			width=130 height=24 bgcolor="f7f7f7" colspan=2>주문자 정보</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">이메일주소</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=ordermember.getMEMBER_EMAIL() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=23>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">집전화</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=ordermember.getMEMBER_PHONE() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">휴대폰</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
			<%=ordermember.getMEMBER_MOBILE() %>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr height=20>
		<td style="font-family: Tahoma; font-size: 8pt;" width=130 height=24
			bgcolor="f7f7f7">기타 요구사항</td>
		<td style="font-family: Tahoma; font-size: 8pt;">
		<textarea name="memo" cols=60 rows=12><%=order.getORDER_MEMO() %>
		</textarea>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
</table>

<table width=80% border=0 cellpadding="0" cellspacing="1">
	<tr><td height=10></td></tr>
	<tr><td height=10></td></tr>
	<tr>
		<td><b><font size=2>결제 정보</font></b></td>
	</tr>
	<tr>
		<td style="font-family: Tahoma; font-size: 8pt;" width=200 height=24
			bgcolor="f7f7f7">주문 합계금액 :</td>
		<td width=320 height=24>
			<font size=2><%=order.getORDER_SUM_MONEY() %>원</font>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr>
		<td style="font-family: Tahoma; font-size: 8pt;" width=200 height=24
			bgcolor="f7f7f7">결제방법 :</td>
		<td width=320 height=24>
			<font size=2><%=order.getORDER_TRADE_TYPE() %></font>
		</td>
	</tr>
	<tr>
		<td style="background-color: #F0F0F0; height: 1px;" colspan=6>
	</tr>
	<tr>
		<td style="font-family: Tahoma; font-size: 8pt;" width=200 height=24
			bgcolor="f7f7f7">입금자명 :</td>
		<td width=320 height=24>
			<font size=2><%=order.getORDER_TRADE_PAYER() %></font>
		</td>
	</tr>
	<tr>
		<td style="font-family: Tahoma; font-size: 8pt;" width=200 height=24
			bgcolor="f7f7f7">주문 상태 :</td>
		<td width=320 height=24>
		<select name="status">
			<option value="0" <%if(order.getORDER_STATUS()==0){%> selected
				<%}%>>대기중</option>
			<option value="1" <%if(order.getORDER_STATUS()==1){%> selected
				<%}%>>발송 준비</option>
			<option value="2" <%if(order.getORDER_STATUS()==2){%> selected
				<%}%>>발송 완료</option>
			<option value="3" <%if(order.getORDER_STATUS()==3){%> selected
				<%}%>>배송중</option>
			<option value="4" <%if(order.getORDER_STATUS()==4){%> selected
				<%}%>>배송 완료</option>
			<option value="5" <%if(order.getORDER_STATUS()==5){%> selected
				<%}%>>주문 취소</option>
		</select>
		</td>
	</tr>
	<tr><td style="background-color: #F0F0F0; height: 1px;" colspan=6></tr>
	<tr><td style="background-color: #F0F0F0; height: 1px;" colspan=6></tr>
	<tr>
		<td align=center style="background-color: #F0F0F0; height: 1px;"
			colspan=6>
			<input type=submit value="수정">&nbsp; 
			<input type=button
			onclick="javascript:location.href('./AdminOrderList.ao')"
			value="취소">
		</td>
	</tr>
</table>
</form>
<!-- 주문 정보 수정 -->
</td>
</tr>
</table>
</body>
</html>