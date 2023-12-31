***Image Board제작***
 : 톰켓6으로 테스트할것!!!

 : 테이블설계 : THEME_MESSAGE, THEME_CONTENT, ID_SEQUENCES 
             인덱스 THEME_MESSAGE_INDEX

 : ID_SEQUENCES테이블과 관련된 작업을 처리해주는 클래스
   Sequencer 
    - nextId()

 : 자바빈 클래스(THEME_MESSAGE,THEME_CONTENT테이블과 연관된 ^^)
   Theme

 : 예외처리 클래스
   ThemeManagerException 

 : DAO
   ThemeManager
   - insert()
   - update()
   - select()
   - selectList()
   - delete()
   - count()

 : commons-fileupload-1.2.1.jar를 이용한 파일업로드 클래스
   FileUploadRequestWrapper

 : 썸네일 이미지를 만드는 클래스
   ImageUtil

===================================================
 : 웹관련 파일들...
 : 템플릿페이지
   template.jsp : top.jsp, bottom.jsp
   =====================
   ||      top.jsp    ||
   =====================
   ||                 ||
   ||    CONTENTPAGE  ||
   ||                 ||
   =====================
   ||    bottom.jsp   ||
   =====================

 : 에러처리 
   error_view.jsp

 : 리스트
   list.jsp =========> list_view.jsp

 : 글입력
   writeForm.jsp ====> writeForm_view.jsp ====> **write.jsp
                                                경로설정 

 : 글 상세보기
   read.jsp =========> read_view.jsp

 : 글 수정
   updateForm.jsp ====> updateForm_view.jsp ====> **update.jsp
                                                  경로설정

 : 글 삭제
   deleteForm.jsp ====> deleteForm_view.jsp ====> delete.jsp



1.테이블 설계
create table THEME_MESSAGE (
    THEME_MESSAGE_ID number NOT NULL PRIMARY KEY, //글번호
    GROUP_ID number NOT NULL, //메인글과 답변글을 묶어주는 그룹번호
    ORDER_NO numbe r NOT NULL, //글 순서
    LEVELS number NOT NULL, //답변글의 레벨
    PARENT_ID number NOT NULL,//답변글의 상위글의 글번호
    REGISTER date NOT NULL,//글작성 날짜
    NAME VARCHAR2(20) NOT NULL, //글작성자
    EMAIL VARCHAR2(80) NOT NULL, //이메일
    IMAGE VARCHAR2(40) NOT NULL, //업로드이미지 파일 이름
    PASSWORD VARCHAR2(20), //비밀번호
    TITLE VARCHAR2(100) NOT NULL //글제목
);

create index THEME_MESSAGE_INDEX
on THEME_MESSAGE(GROUP_ID DESC, ORDER_NO ASC);

create table THEME_CONTENT (
    THEME_MESSAGE_ID number NOT NULL PRIMARY KEY,
    CONTENT LONG NOT NULL //글내용
);

//시퀀스를 대신해서 사용하는 테이블
create table ID_SEQUENCES (
    TABLE_NAME VARCHAR2(60) NOT NULL PRIMARY KEY, //테이블이름
    MESSAGE_ID INTEGER NOT NULL //증가시키는 ID를 저장하는...
);

2.ID_SEQUENCES테이블과 관련된 작업을 처리해주는 클래스
package madvirus.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sequencer {
    public synchronized static int nextId(Connection conn, String tableName)
    throws SQLException {
        PreparedStatement pstmtSelect = null;
        ResultSet rsSelect = null;
       
        PreparedStatement pstmtUpdate = null;
       
        try {
            pstmtSelect = conn.prepareStatement(
            "select MESSAGE_ID from ID_SEQUENCES where TABLE_NAME = ?");
            pstmtSelect.setString(1, tableName);
           
            rsSelect = pstmtSelect.executeQuery();
           
            if (rsSelect.next()) {
                int id = rsSelect.getInt(1);
                id++;
               
                pstmtUpdate = conn.prepareStatement(
                  "update ID_SEQUENCES set MESSAGE_ID = ? "+
                  "where TABLE_NAME = ?");
                pstmtUpdate.setInt(1, id);
                pstmtUpdate.setString(2, tableName);
                pstmtUpdate.executeUpdate();
               
                return id;
               
            } else {
                pstmtUpdate = conn.prepareStatement(
                "insert into ID_SEQUENCES values (?, ?)");
                pstmtUpdate.setString(1, tableName);
                pstmtUpdate.setInt(2, 1);
                pstmtUpdate.executeUpdate();
               
                return 1;
            }
        } finally {
            if (rsSelect != null)
                try { rsSelect.close(); } catch(SQLException ex) {}
            if (pstmtSelect != null)
                try { pstmtSelect.close(); } catch(SQLException ex) {}
            if (pstmtUpdate != null)
                try { pstmtUpdate.close(); } catch(SQLException ex) {}
        }
    }
}


3.자바빈 클래스(THEME_MESSAGE,THEME_CONTENT테이블과 연관된 ^^)
package madvirus.gallery;

import java.sql.Timestamp;

public class Theme {
    private int id;
    private int groupId;
    private int orderNo;
    private int levels;
    private int parentId;
    private Timestamp register;
    private String name;
    private String email;
    private String image;
    private String password;
    private String title;
    private String content;
   
    public String getEmail() {
        return email;
    }
    public int getGroupId() {
        return groupId;
    }
    public int getId() {
        return id;
    }
    public String getImage() {
        return image;
    }
    public int getLevels() {
        return levels;
    }
    public String getName() {
        return name;
    }
    public int getOrderNo() {
        return orderNo;
    }
    public int getParentId() {
        return parentId;
    }
    public String getPassword() {
        return password;
    }
    public Timestamp getRegister() {
        return register;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
   
    public void setEmail(String value) {
        email = value;
    }
    public void setGroupId(int value) {
        groupId = value;
    }
    public void setId(int value) {
        id = value;
    }
    public void setImage(String value) {
        image = value;
    }
    public void setLevels(int value) {
        levels = value;
    }
    public void setName(String value) {
        name = value;
    }
    public void setOrderNo(int value) {
        orderNo = value;
    }
    public void setParentId(int value) {
        parentId = value;
    }
    public void setPassword(String value) {
        password = value;
    }
    public void setRegister(Timestamp value) {
        register = value;
    }
    public void setTitle(String value) {
        title = value;
    }
    public void setContent(String value) {
        content = value;
    }
}

3.예외처리 클래스
package madvirus.gallery;

public class ThemeManagerException extends Throwable {
    public ThemeManagerException(String msg) {
        super(msg);
    }
    public ThemeManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }
   
}

4.DAO 클래스
package madvirus.gallery;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import madvirus.sequence.Sequencer;

public class ThemeManager {
   
    private static ThemeManager instance = new ThemeManager();
   
    public static ThemeManager getInstance() {
        return instance;
    }
   
    private ThemeManager() {}
    private Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:/pool");
      }
   
    /**
     * 새로운 글을 삽입한다.
     */
    public void insert(Theme theme) throws Exception {
        Connection conn = null;
        // 새로운 글의 그룹 번호를 구할 때 사용된다.
        Statement stmtGroup = null;
        ResultSet rsGroup = null;
       
        // 특정 글의 답글에 대한 출력 순서를 구할 때 사용된다.
        PreparedStatement pstmtOrder = null;
        ResultSet rsOrder = null;
        PreparedStatement pstmtOrderUpdate = null;
       
        // 글을 삽입할 때 사용된다.
        PreparedStatement pstmtInsertMessage = null;
        PreparedStatement pstmtInsertContent = null;
       
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
           
            if (theme.getParentId() == 0) {
                // 답글이 아닌 경우 그룹번호를 새롭게 구한다.
                stmtGroup = conn.createStatement();
                rsGroup = stmtGroup.executeQuery(
                    "select max(GROUP_ID) from THEME_MESSAGE");
                int maxGroupId = 0;
                if (rsGroup.next()) {
                    maxGroupId = rsGroup.getInt(1);
                }
                maxGroupId++;
               
                theme.setGroupId(maxGroupId);
                theme.setOrderNo(0);
            } else {
                // 특정 글의 답글인 경우,
                // 같은 그룹 번호 내에서의 출력 순서를 구한다. 
                pstmtOrder = conn.prepareStatement(
                "select max(ORDER_NO) from THEME_MESSAGE "+
                "where PARENT_ID = ? or THEME_MESSAGE_ID = ?");
                pstmtOrder.setInt(1, theme.getParentId());
                pstmtOrder.setInt(2, theme.getParentId());
                rsOrder = pstmtOrder.executeQuery();
                int maxOrder = 0;
                if (rsOrder.next()) {
                    maxOrder = rsOrder.getInt(1);
                }
                maxOrder ++;
                theme.setOrderNo(maxOrder);
            }
           
            // 특정 글의 답변 글인 경우 같은 그룹 내에서
            // 순서 번호를 변경한다.
            if (theme.getOrderNo() > 0) {
                pstmtOrderUpdate = conn.prepareStatement(
                "update THEME_MESSAGE set ORDER_NO = ORDER_NO + 1 "+
                "where GROUP_ID = ? and ORDER_NO >= ?");
                pstmtOrderUpdate.setInt(1, theme.getGroupId());
                pstmtOrderUpdate.setInt(2, theme.getOrderNo());
                pstmtOrderUpdate.executeUpdate();
            }
            // 새로운 글의 번호를 구한다.
            theme.setId(Sequencer.nextId(conn, "THEME_MESSAGE"));
            // 글을 삽입한다.
            pstmtInsertMessage = conn.prepareStatement(
            "insert into THEME_MESSAGE values (?,?,?,?,?,?,?,?,?,?,?)");
            pstmtInsertMessage.setInt(1, theme.getId());
            pstmtInsertMessage.setInt(2, theme.getGroupId());
            pstmtInsertMessage.setInt(3, theme.getOrderNo());
            pstmtInsertMessage.setInt(4, theme.getLevels());
            pstmtInsertMessage.setInt(5, theme.getParentId());
            pstmtInsertMessage.setTimestamp(6, theme.getRegister());
            pstmtInsertMessage.setString(7, theme.getName());
            pstmtInsertMessage.setString(8, theme.getEmail());
            pstmtInsertMessage.setString(9, theme.getImage());
            pstmtInsertMessage.setString(10, theme.getPassword());
            pstmtInsertMessage.setString(11, theme.getTitle());
            pstmtInsertMessage.executeUpdate();
           
            pstmtInsertContent = conn.prepareStatement(
            "insert into THEME_CONTENT values (?,?)");
            pstmtInsertContent.setInt(1, theme.getId());
            pstmtInsertContent.setCharacterStream(2,
                new StringReader(theme.getContent()),
                theme.getContent().length());
            pstmtInsertContent.executeUpdate();
           
            conn.commit();
        } catch(SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch(SQLException ex1) {}
           
            throw new Exception("insert", ex);
        } finally {
            if (rsGroup != null)
                try { rsGroup.close(); } catch(SQLException ex) {} 
            if (stmtGroup != null)
                try { stmtGroup.close(); } catch(SQLException ex) {}
            if (rsOrder != null)
                try { rsOrder.close(); } catch(SQLException ex) {} 
            if (pstmtOrder != null)
                try { pstmtOrder.close(); } catch(SQLException ex) {}
            if (pstmtOrderUpdate != null)
                try { pstmtOrderUpdate.close(); } catch(SQLException ex) {}
            if (pstmtInsertMessage!= null)
                try { pstmtInsertMessage.close(); } catch(SQLException ex) {}
            if (pstmtInsertContent != null)
                try { pstmtInsertContent.close(); } catch(SQLException ex) {}
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch(SQLException ex) {}
        }
    }
   
    /**
     * 제목과 내용만 변경한다.
     */
    public void update(Theme theme) throws Exception {
        Connection conn = null;
        PreparedStatement pstmtUpdateMessage = null;
        PreparedStatement pstmtUpdateContent = null;
       
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
           
            pstmtUpdateMessage = conn.prepareStatement(
                "update THEME_MESSAGE set NAME=?,EMAIL=?,IMAGE=?,TITLE=? "+
                "where THEME_MESSAGE_ID=?");
            pstmtUpdateContent = conn.prepareStatement(
                "update THEME_CONTENT set CONTENT=? "+
                "where THEME_MESSAGE_ID=?");
           
            pstmtUpdateMessage.setString(1, theme.getName());
            pstmtUpdateMessage.setString(2, theme.getEmail());
            pstmtUpdateMessage.setString(3, theme.getImage());
            pstmtUpdateMessage.setString(4, theme.getTitle());
            pstmtUpdateMessage.setInt(5, theme.getId());
            pstmtUpdateMessage.executeUpdate();
           
            pstmtUpdateContent.setCharacterStream(1,
                new StringReader(theme.getContent()),
                theme.getContent().length());
            pstmtUpdateContent.setInt(2, theme.getId());
            pstmtUpdateContent.executeUpdate();
           
            conn.commit();
        } catch(SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch(SQLException ex1) {}
           
            throw new Exception("update", ex);
        } finally {
            if (pstmtUpdateMessage != null)
                try { pstmtUpdateMessage.close(); } catch(SQLException ex) {}
            if (pstmtUpdateContent != null)
                try { pstmtUpdateContent.close(); } catch(SQLException ex) {}
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch(SQLException ex) {}
        }
    }
   
    /**
     * 등록된 글의 개수를 구한다.
     */
    public int count(List whereCond, Map valueMap)
    throws Exception {
        if (valueMap == null) valueMap = Collections.EMPTY_MAP;
       
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
       
        try {
            conn = getConnection();
            StringBuffer query = new StringBuffer(200);
            query.append("select count(*) from THEME_MESSAGE ");
            if (whereCond != null && whereCond.size() > 0) {
                query.append("where ");
                for (int i = 0 ; i < whereCond.size() ; i++) {
                    query.append(whereCond.get(i));
                    if (i < whereCond.size() -1 ) {
                        query.append(" or ");
                    }
                }
            }
            pstmt = conn.prepareStatement(query.toString());
           
            Iterator keyIter = valueMap.keySet().iterator();
            while(keyIter.hasNext()) {
                Integer key = (Integer)keyIter.next();
                Object obj = valueMap.get(key);
                if (obj instanceof String) {
                    pstmt.setString(key.intValue(), (String)obj);
                } else if (obj instanceof Integer) {
                    pstmt.setInt(key.intValue(), ((Integer)obj).intValue());
                } else if (obj instanceof Timestamp) {
                    pstmt.setTimestamp(key.intValue(), (Timestamp)obj);
                }
            }
           
            rs = pstmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new Exception("count", ex);
        } finally {
            if (rs != null) try { rs.close(); } catch(SQLException ex) {}
            if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
   
    /**
     * 목록을 읽어온다.
     */
    public List selectList(List whereCond, Map valueMap, int startRow, int endRow) throws Exception {
        if (valueMap == null) valueMap = Collections.EMPTY_MAP;
       
        Connection conn = null;
        PreparedStatement pstmtMessage = null;
        ResultSet rsMessage = null;
       
        try {
            StringBuffer query = new StringBuffer(200);

            query.append("select * from ( ");
            query.append("   select THEME_MESSAGE_ID,GROUP_ID,ORDER_NO,LEVELS,PARENT_ID,REGISTER,NAME,EMAIL,IMAGE,PASSWORD,TITLE,ROWNUM rnum ");
            query.append("   from ( ");
            query.append("       select THEME_MESSAGE_ID,GROUP_ID,ORDER_NO,LEVELS,PARENT_ID,REGISTER,NAME,EMAIL,IMAGE,PASSWORD,TITLE ");
            query.append("       from THEME_MESSAGE ");
            if (whereCond != null && whereCond.size() > 0) {
                query.append("where ");
                for (int i = 0 ; i < whereCond.size() ; i++) {
                    query.append(whereCond.get(i));
                    if (i < whereCond.size() -1 ) {
                        query.append(" or ");
                    }
                }
            }
            query.append("       order by GROUP_ID desc, ORDER_NO asc ");
            query.append("    ) where ROWNUM <= ? ");
            query.append(") where rnum >= ? ");
           
            conn = getConnection();
           
            pstmtMessage = conn.prepareStatement(query.toString());
            Iterator keyIter = valueMap.keySet().iterator();
            while(keyIter.hasNext()) {
                Integer key = (Integer)keyIter.next();
                Object obj = valueMap.get(key);
                if (obj instanceof String) {
                    pstmtMessage.setString(key.intValue(), (String)obj);
                } else if (obj instanceof Integer) {
                    pstmtMessage.setInt(key.intValue(),
                                        ((Integer)obj).intValue());
                } else if (obj instanceof Timestamp) {
                    pstmtMessage.setTimestamp(key.intValue(),
                                             (Timestamp)obj);
                }
            }
           
            pstmtMessage.setInt(valueMap.size()+1, endRow + 1);
            pstmtMessage.setInt(valueMap.size()+2, startRow + 1);
           
            rsMessage = pstmtMessage.executeQuery();
            if (rsMessage.next()) {
                List list = new java.util.ArrayList(endRow-startRow+1);
               
                do {
                    Theme theme = new Theme();
                    theme.setId(rsMessage.getInt("THEME_MESSAGE_ID"));
                    theme.setGroupId(rsMessage.getInt("GROUP_ID"));
                    theme.setOrderNo(rsMessage.getInt("ORDER_NO"));
                    theme.setLevels(rsMessage.getInt("LEVELS"));
                    theme.setParentId(rsMessage.getInt("PARENT_ID"));
                    theme.setRegister(rsMessage.getTimestamp("REGISTER"));
                    theme.setName(rsMessage.getString("NAME"));
                    theme.setEmail(rsMessage.getString("EMAIL"));
                    theme.setImage(rsMessage.getString("IMAGE"));
                    theme.setPassword(rsMessage.getString("PASSWORD"));
                    theme.setTitle(rsMessage.getString("TITLE"));
                    list.add(theme);
                } while(rsMessage.next());
               
                return list;
               
            } else {
                return Collections.EMPTY_LIST;
            }
           
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new Exception("selectList", ex);
        } finally {
            if (rsMessage != null) 
                try { rsMessage.close(); } catch(SQLException ex) {}
            if (pstmtMessage != null)
                try { pstmtMessage.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
   
    /**
     * 지정한 글을 읽어온다.
     */
    public Theme select(int id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmtMessage = null;
        ResultSet rsMessage = null;
        PreparedStatement pstmtContent = null;
        ResultSet rsContent = null;
       
        try {
            Theme theme = null;
           
            conn = getConnection();
            pstmtMessage = conn.prepareStatement(
                "select * from THEME_MESSAGE "+
                "where THEME_MESSAGE_ID = ?");
            pstmtMessage.setInt(1, id);
            rsMessage = pstmtMessage.executeQuery();
            if (rsMessage.next()) {
                theme = new Theme();
                theme.setId(rsMessage.getInt("THEME_MESSAGE_ID"));
                theme.setGroupId(rsMessage.getInt("GROUP_ID"));
                theme.setOrderNo(rsMessage.getInt("ORDER_NO"));
                theme.setLevels(rsMessage.getInt("LEVELS"));
                theme.setParentId(rsMessage.getInt("PARENT_ID"));
                theme.setRegister(rsMessage.getTimestamp("REGISTER"));
                theme.setName(rsMessage.getString("NAME"));
                theme.setEmail(rsMessage.getString("EMAIL"));
                theme.setImage(rsMessage.getString("IMAGE"));
                theme.setPassword(rsMessage.getString("PASSWORD"));
                theme.setTitle(rsMessage.getString("TITLE"));

                pstmtContent = conn.prepareStatement(
                    "select CONTENT from THEME_CONTENT "+
                    "where THEME_MESSAGE_ID = ?");
                pstmtContent.setInt(1, id);
                rsContent = pstmtContent.executeQuery();
                if (rsContent.next()) {
                    Reader reader = null;
                    try {
                        reader = rsContent.getCharacterStream("CONTENT");
                        char[] buff = new char[512];
                        int len = -1;
                        StringBuffer buffer = new StringBuffer(512);
                        while( (len = reader.read(buff)) != -1) {
                            buffer.append(buff, 0, len);
                        }
                        theme.setContent(buffer.toString());
                    } catch(IOException iex) {
                        throw new Exception("select", iex);
                    } finally {
                        if (reader != null)
                            try {
                                reader.close();
                            } catch(IOException iex) {}
                    }          
                } else {
                    return null;
                }
                return theme;
            } else {
                return null;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new Exception("select", ex);
        } finally {
            if (rsMessage != null) 
                try { rsMessage.close(); } catch(SQLException ex) {}
            if (pstmtMessage != null)
                try { pstmtMessage.close(); } catch(SQLException ex) {}
            if (rsContent != null) 
                try { rsContent.close(); } catch(SQLException ex) {}
            if (pstmtContent != null)
                try { pstmtContent.close(); } catch(SQLException ex) {}
            if (conn != null) try { conn.close(); } catch(SQLException ex) {}
        }
    }
   
    public void delete(int id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmtMessage = null;
        PreparedStatement pstmtContent = null;
       
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
           
            pstmtMessage = conn.prepareStatement(
                "delete from THEME_MESSAGE where THEME_MESSAGE_ID = ?");
            pstmtContent = conn.prepareStatement(
                "delete from THEME_CONTENT where THEME_MESSAGE_ID = ?");
           
            pstmtMessage.setInt(1, id);
            pstmtContent.setInt(1, id);
           
            int updatedCount1 = pstmtMessage.executeUpdate();
            int updatedCount2 = pstmtContent.executeUpdate();
           
            if (updatedCount1 + updatedCount2 == 2) {
                conn.commit();
            } else {
                conn.rollback();
                throw new Exception("invalid id:"+id);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch(SQLException ex1) {}
            throw new Exception("delete", ex);
        } finally {
            if (pstmtMessage != null)
                try { pstmtMessage.close(); } catch(SQLException ex) {}
            if (pstmtContent != null)
                try { pstmtContent.close(); } catch(SQLException ex) {}
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch(SQLException ex) {}
        }
    }
}

5.commons-fileupload-1.2.1.jar를 이용한 파일업로드 클래스
package madvirus.fileupload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Iterator;

/**
* FileUpload API를 사용하는 HttpServletRequestWrapper 클래스로서
* HttpServletRequest에 기반한 API를 사용하기 위한 래퍼이다.
*/
public class FileUploadRequestWrapper extends HttpServletRequestWrapper {
   
    private boolean multipart = false;
   
    private HashMap parameterMap;
    private HashMap fileItemMap;
   
    public FileUploadRequestWrapper(HttpServletRequest request)
    throws FileUploadException{
        this(request, -1, -1, null);
    }
   
    public FileUploadRequestWrapper(HttpServletRequest request,
        int threshold, int max, String repositoryPath) throws FileUploadException {
        super(request);
       
        parsing(request, threshold, max, repositoryPath);
    }
    private void parsing(HttpServletRequest request,
        int threshold, int max, String repositoryPath) throws FileUploadException {
       
        if (FileUpload.isMultipartContent(request)) {
            multipart = true;
           
            parameterMap = new java.util.HashMap();
            fileItemMap = new java.util.HashMap();
           
            DiskFileUpload diskFileUpload = new DiskFileUpload();
            if (threshold != -1) {
                diskFileUpload.setSizeThreshold(threshold);
            }
            diskFileUpload.setSizeMax(max);
            if (repositoryPath != null) {
                diskFileUpload.setRepositoryPath(repositoryPath);
            }
           
            java.util.List list = diskFileUpload.parseRequest(request);
            for (int i = 0 ; i « list.size() ; i++) {
                FileItem fileItem = (FileItem) list.get(i);
                String name = fileItem.getFieldName();
               
                if (fileItem.isFormField()) {
                    String value = fileItem.getString();
                    String[] values = (String[]) parameterMap.get(name);
                    if (values == null) {
                        values = new String[] { value };
                    } else {
                        String[] tempValues = new String[values.length + 1];
                        System.arraycopy(values, 0, tempValues, 0, 1);
                        tempValues[tempValues.length - 1] = value;
                        values = tempValues;
                    }
                    parameterMap.put(name, values);
                } else {
                    fileItemMap.put(name, fileItem);
                }
            }
            addTo(); // request 속성으로 설정한다.
        }
    }
   
    public boolean isMultipartContent() {
        return multipart;
    }
   
    public String getParameter(String name) {
        if (multipart) {
            String[] values = (String[])parameterMap.get(name);
            if (values == null) return null;
            return values[0];
        } else
            return super.getParameter(name);
    }
   
    public String[] getParameterValues(String name) {
        if (multipart)
            return (String[])parameterMap.get(name);
        else
            return super.getParameterValues(name);
    }
   
    public Enumeration getParameterNames() {
        if (multipart) {
            return new Enumeration() {
                Iterator iter = parameterMap.keySet().iterator();
               
                public boolean hasMoreElements() {
                    return iter.hasNext();
                }
                public Object nextElement() {
                    return iter.next();
                }
            };
        } else {
            return super.getParameterNames();
        }
    }
   
    public Map getParameterMap() {
        if (multipart)
            return parameterMap;
        else
            return super.getParameterMap();
    }
   
    public FileItem getFileItem(String name) {
        if (multipart)
            return (FileItem) fileItemMap.get(name);
        else
            return null;
    }
   
    /**
     * 관련된 FileItem 들의 delete() 메소드를 호출한다.
     */
    public void delete() {
        if (multipart) {
            Iterator fileItemIter = fileItemMap.values().iterator();
            while( fileItemIter.hasNext()) {
                FileItem fileItem = (FileItem)fileItemIter.next();
                fileItem.delete();
            }
        }
    }
   
    public void addTo() {
        super.setAttribute(FileUploadRequestWrapper.class.getName(), this);
    }
   
    public static FileUploadRequestWrapper
                  getFrom(HttpServletRequest request) {
        return (FileUploadRequestWrapper)
            request.getAttribute(FileUploadRequestWrapper.class.getName());
    }
   
    public static boolean hasWrapper(HttpServletRequest request) {
        if (FileUploadRequestWrapper.getFrom(request) == null) {
            return false;
        } else {
            return true;
        }
    }
}

6.썸네일 이미지를 만드는 클래스
package madvirus.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
   
    public static final int SAME = -1;
    public static final int RATIO = 0;
   
    public static void resize(File src, File dest,
                              int width, int height) throws IOException {
        FileInputStream srcIs = null;
        try {
            srcIs = new FileInputStream(src);
            ImageUtil.resize(srcIs, dest, width, height);
        } finally {
            if (srcIs != null) try { srcIs.close(); } catch(IOException ex) {}
        }
    }

    public static void resize(InputStream src, File dest,
                              int width, int height) throws IOException {
        BufferedImage srcImg = ImageIO.read(src);
        int srcWidth = srcImg.getWidth();
        int srcHeight = srcImg.getHeight();
       
        int destWidth = -1, destHeight = -1;
       
        if (width == SAME) {
            destWidth = srcWidth;
        } else if (width » 0) {
            destWidth = width;
        }
       
        if (height == SAME) {
            destHeight = srcHeight;
        } else if (height » 0) {
            destHeight = height;
        }
       
        if (width == RATIO && height == RATIO) {
            destWidth = srcWidth;
            destHeight = srcHeight;
        } else if (width == RATIO) {
            double ratio = ((double)destHeight) / ((double)srcHeight);
            destWidth = (int)((double)srcWidth * ratio);
        } else if (height == RATIO) {
            double ratio = ((double)destWidth) / ((double)srcWidth);
            destHeight = (int)((double)srcHeight * ratio);
        }
       
        BufferedImage destImg = new BufferedImage(
             destWidth, destHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = destImg.createGraphics();
        g.drawImage(srcImg, 0, 0, destWidth, destHeight, null);
       
        ImageIO.write(destImg, "jpg", dest);
    }
}

7.템플릿페이지
[/WebContent/imageboard/template/template.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page errorPage = "../error/error_view.jsp" %»
«html»
«head»
«title»테마 갤러리«/title»
«style»
A { color: blue; font-weight: bold; text-decoration: none}
A:hover { color: blue; font-weight: bold; text-decoration: underline}
«/style»
«/head»
«body»

«table width="100%" border="1" cellpadding="2" cellspacing="0"»
«tr»
    «td»
        «jsp:include page="../module/top.jsp" flush="false" /»
    «/td»
«/tr»
«tr»
    «td»
        «!-- 내용 부분: 시작 --»
        «jsp:include page="${param.CONTENTPAGE}" flush="false" /»
        «!-- 내용 부분: 끝 --»
    «/td»
«/tr»
«tr»
    «td»
        «jsp:include page="../module/bottom.jsp" flush="false" /»
    «/td»
«/tr»
«/table»
«/body»
«/html»

8.모듈페이지
[/WebContent/imageboard/module/top.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»

«b»테마 겔러리 - 사진을 올려주세요!«/b»

[/WebContent/imageboard/module/bottom.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»

«b»이미지 게시판 작성하기 예제«/b»

[/WebContent/imageboard/error/error_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page import = "javax.servlet.ServletException" %»
«%@ page isErrorPage = "true" %»
«html»
«head»«title»에러 발생«/title»«/head»
«body»

에러가 발생하였습니다!!«br»«br»
에러 메시지: «%= exception.getMessage() %»
«% exception.printStackTrace(); %»
«p»
«%
    Throwable rootCause = null;
    if (exception instanceof ServletException) {
        rootCause = ((ServletException)exception).getRootCause();
    } else {
    rootCause = exception.getCause();
    }
    if (rootCause != null) {
    rootCause.printStackTrace();
        do {
%»
예외 추적: «%= rootCause.getMessage() %»«br»
«%
            rootCause = rootCause.getCause();
        } while(rootCause != null);
    }
%»
«/body»
«/html»

9.목록출력
[/WebContent/imageboard/list.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%
    request.setCharacterEncoding("euc-kr");
%»
«jsp:forward page="../template/template.jsp"»
    «jsp:param name="CONTENTPAGE" value="../list_view.jsp" /»
«/jsp:forward»

[/WebContent/imageboard/list_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page import = "java.util.List" %»
«%@ page import = "java.util.Map" %»
«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»
«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»
«%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %»
«%!
    static int PAGE_SIZE = 5;
%»
«%
    String pageNum = request.getParameter("page");
    if (pageNum == null) pageNum = "1";
    int currentPage = Integer.parseInt(pageNum);
   
    String[] searchCond = request.getParameterValues("search_cond");
    String searchKey = request.getParameter("search_key");
   
    List whereCond = null;
    Map whereValue = null;
   
    boolean searchCondName = false;
    boolean searchCondTitle = false;
   
    if (searchCond != null && searchCond.length » 0 && searchKey != null) {
        whereCond = new java.util.ArrayList();
        whereValue = new java.util.HashMap();
       
        for (int i = 0 ; i « searchCond.length ; i++) {
            if (searchCond[i].equals("name")) {
                whereCond.add("NAME = ?");
                whereValue.put(new Integer(1), searchKey);
                searchCondName = true;
            } else if (searchCond[i].equals("title")) {
                whereCond.add("TITLE LIKE '%"+searchKey+"%'");
                searchCondTitle = true;
            }
        }
    }
   
    ThemeManager manager = ThemeManager.getInstance();
    List list = null;
    int count = manager.count(whereCond, whereValue);
    int totalPageCount = 0; // 전체 페이지 개수를 저장한다.
    int startRow = 0, endRow = 0; // 시작 행과 끝 행의 개수를 구한다.
    if (count » 0) {
        totalPageCount = count / PAGE_SIZE;
        if (count % PAGE_SIZE » 0) totalPageCount++;
       
        startRow = (currentPage - 1) * PAGE_SIZE + 1;
        endRow = currentPage * PAGE_SIZE;
        if (endRow » count) endRow = count;
        list = manager.selectList(whereCond, whereValue,
            startRow-1, endRow-1);
    } else {
        list = java.util.Collections.EMPTY_LIST;
    }
%»
«c:set var="list" value="«%= list %»" /»
«c:if test="«%= searchCondTitle || searchCondName %»"»
검색 조건:  [
    «c:if test="«%= searchCondTitle %»"»제목«/c:if»
    «c:if test="«%= searchCondName %»"»이름«/c:if»
    = ${param.search_key} ]
«/c:if»

«c:if test="«%= count » 0 %»"»
«table width="100%" cellpadding="1" cellspacing="2"»
«tr»
    «td align="right"»
        «b»«%= startRow %»-«%= endRow %» / «%= count %»«b»
    «/td»
«/tr»
«/table»
«/c:if»
«table width="100%" cellpadding="1" cellspacing="2"»
«tr»
    «td bgcolor="#e9e9e9"»«b»이미지«/b»«/td»
    «td bgcolor="#e9e9e9"»«b»제목«/b»«/td»
    «td bgcolor="#e9e9e9"»«b»작성자«/b»«/td»
    «td bgcolor="#e9e9e9"»«b»작성일«/b»«/td»
«/tr»
«c:if test="${empty list}"»
«tr»
    «td bgcolor="#f0f0f0" colspan="4" align="center"»
    등록된 이미지가 없습니다.
    «/td»
«/tr»
«/c:if»
«c:if test="${! empty list}"»
«c:forEach var="theme" items="${list}"»
«tr bgcolor="#f0f0f0"»
    «td»«c:if test="${! empty theme.image}"»
    «%
        Theme theme = (Theme)pageContext.getAttribute("theme");
    %»
    «img src="/EZEN/image/${theme.image}.small.jpg" width="50"»
    «/c:if»«/td»
    «td»«a href="javascript:goView(${theme.id})"»${theme.title}«/a»«/td»
    «td»${theme.name}«/td»
    «td»
    «fmt:formatDate value="${theme.register}" pattern="yyyy-MM-dd" /»
    «/td»
«/tr»

«/c:forEach»
«/c:if»
«tr»
    «td colspan="4" align="right"»«a href="writeForm.jsp"»[이미지등록]«/a»«/td»
«/tr»
«/table»

«script language="JavaScript"»
function goPage(pageNo) {
    document.move.action = "list.jsp";
    document.move.page.value = pageNo;
    document.move.submit();
}
function goView(id) {
    document.move.action = "read.jsp";
    document.move.id.value = id;
    document.move.submit();
}
«/script»

«c:set var="count" value="«%= Integer.toString(count) %»" /»
«c:set var="PAGE_SIZE" value="«%= Integer.toString(PAGE_SIZE) %»" /»
«c:set var="currentPage" value="«%= Integer.toString(currentPage) %»" /»

«c:if test="${count » 0}"»
    «c:set var="pageCount"
        value="${count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1)}" /»
    «c:set var="startPage" value="${currentPage - (currentPage % 10) + 1}" /»
    «c:set var="endPage" value="${startPage + 10}" /»
   
    «c:if test="${endPage » pageCount}"»
        «c:set var="endPage" value="${pageCount}" /»
    «/c:if»
    «c:if test="${startPage » 10}"»
        «a href="javascript:goPage(${startPage - 10})"»[이전]«/a»
    «/c:if»
    «c:forEach var="pageNo" begin="${startPage}" end="${endPage}"»
        «c:if test="${currentPage == pageNo}"»«b»«/c:if»
        «a href="javascript:goPage(${pageNo})"»[${pageNo}]«/a»
        «c:if test="${currentPage == pageNo}"»«/b»«/c:if»
    «/c:forEach»
    «c:if test="${endPage « pageCount}"»
        «a href="javascript:goPage(${startPage + 10})"»[다음]«/a»
    «/c:if»
«/c:if»

«form name="move" method="post"»
    «input type="hidden" name="id" value=""»
    «input type="hidden" name="page" value="${currentPage}"»
    «c:if test="«%= searchCondTitle %»"»
    «input type="hidden" name="search_cond" value="title"»
    «/c:if»
    «c:if test="«%= searchCondName %»"»
    «input type="hidden" name="search_cond" value="name"»
    «/c:if»
    «c:if test="${! empty param.search_key}"»
    «input type="hidden" name="search_key" value="${param.search_key}"»
    «/c:if»
«/form»

«form name="search" action="list.jsp" method="post"»
    «input type="checkbox" name="search_cond" value="title"»제목
    «input type="checkbox" name="search_cond" value="name"»이름
    «input type="text" name="search_key" value="" size="10"»
    «input type="submit" value="검색"»
    «input type="button" value="전체목록"
           onClick="location.href='list.jsp?page=1'"»
«/form»

10.글쓰기
[/WebContent/imageboard/writeForm.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«jsp:forward page="../template/template.jsp"»
    «jsp:param name="CONTENTPAGE" value="../writeForm_view.jsp" /»
«/jsp:forward»

[/WebContent/imageboard/writeForm_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»
«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»
«%
    String parentId = request.getParameter("parentId");
    String title = "";
    Theme theme = null;
    if (parentId != null) {
        ThemeManager manager = ThemeManager.getInstance();
        theme = manager.select(Integer.parseInt(parentId));
        if (theme != null) {
            title = "re:"+theme.getTitle();
        }
    }
%»
«c:set var="theme" value="«%= theme %»" /»
«script language="JavaScript"»
function validate(form) {
    if (form.title.value == "") {
        alert("제목을 입력하세요.");
        return false;
    } else if (form.name.value == "") {
        alert("이름을 입력하세요.");
        return false;
    } else if (form["parentId"] == null && form.imageFile.value == "") {
        alert("이미지 파일을 선택하세요.");
        return false;
    } else if (form.content.value == "") {
        alert("내용을 입력하세요");
        return false;
    }
}
«/script»

«form action="write.jsp" method="post" enctype="multipart/form-data"
      onSubmit="return validate(this)"»
«input type="hidden" name="levels" value="${theme.levels + 1}"»
«c:if test="${! empty param.groupId}"»
«input type="hidden" name="groupId" value="${param.groupId}"»
«/c:if»
«c:if test="${! empty param.parentId}"»
«input type="hidden" name="parentId" value="${param.parentId}"»
«/c:if»
«table width="100%" border="1" cellpadding="1" cellspacing="0"»
«tr»
    «td»제목«/td»
    «td»«input type="text" name="title" size="40" value="«%= title %»"»«/td»
«/tr»
«tr»
    «td»이름«/td»
    «td»«input type="text" name="name" size="10" value=""»«/td»
«/tr»
«tr»
    «td»이메일«/td»
    «td»«input type="text" name="email" size="10" value=""»«/td»
«/tr»
«tr»
    «td»암호«/td»
    «td»«input type="password" name="password" size="10" value=""»«/td»
«/tr»
«tr»
    «td»이미지«/td»
    «td»«input type="file" name="imageFile"»«/td»
«/tr»
«tr»
    «td»내용«/td»
    «td»«textarea name="content" cols="40" rows="8"»«/textarea»«/td»
«/tr»
«tr»
    «td colspan="2"»«input type="submit" value="등록"»«/td»
«/tr»
«/table»

«/form»

[/WebContent/imageboard/write.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page errorPage = "../error/error_view.jsp" %»

«%@ page import = "java.sql.Timestamp" %»
«%@ page import = "java.io.File" %»
«%@ page import = "org.apache.commons.fileupload.FileItem" %»

«%@ page import = "madvirus.util.ImageUtil" %»
«%@ page import = "madvirus.fileupload.FileUploadRequestWrapper" %»

«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»

«%
    FileUploadRequestWrapper requestWrap = new FileUploadRequestWrapper(
        request, -1, -1,
        "C:\\Java2\\App\\EZEN\\WebContent\\temp");
    HttpServletRequest tempRequest = request;
    request = requestWrap;
%»
«jsp:useBean id="theme" class="madvirus.gallery.Theme"»
    «jsp:setProperty name="theme" property="*" /»
«/jsp:useBean»
«%
    FileItem imageFileItem = requestWrap.getFileItem("imageFile");
    String image = "";
    if (imageFileItem.getSize() » 0) {
        image = Long.toString(System.currentTimeMillis());
       
        // 이미지를 지정한 경로에 저장
        File imageFile = new File(
            "C:\\Java2\\App\\EZEN\\WebContent\\image",
            image);
        // 같은 이름의 파일이름 처리
        if (imageFile.exists()) {
            for (int i = 0 ; true ; i++) {
                imageFile = new File(
                    "C:\\Java2\\App\\EZEN\\WebContent\\image",
                    image+"_"+i);
                if (!imageFile.exists()) {
                    image = image + "_" + i;
                    break;
                }
            }
        }
        imageFileItem.write(imageFile);
       
        // 썸네일 이미지 생성
        File destFile = new File(
            "C:\\Java2\\App\\EZEN\\WebContent\\image",
            image+".small.jpg");
        ImageUtil.resize(imageFile, destFile, 50, ImageUtil.RATIO);
    }
   
    theme.setRegister(new Timestamp(System.currentTimeMillis()));
    theme.setImage(image);
   
    ThemeManager manager = ThemeManager.getInstance();
    manager.insert(theme);
%»
«script»
alert("새로운 이미지를 등록했습니다.");
location.href = "list.jsp";
«/script»

11.글읽기
[/WebContent/imageboard/read.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%
    request.setCharacterEncoding("euc-kr");
%»
«jsp:forward page="../template/template.jsp"»
    «jsp:param name="CONTENTPAGE" value="../read_view.jsp" /»
«/jsp:forward»

[/WebContent/imageboard/read_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»
«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»
«%
    String themeId = request.getParameter("id");
   
    ThemeManager manager = ThemeManager.getInstance();
    Theme theme = manager.select(Integer.parseInt(themeId));
%»
«c:set var="theme" value="«%= theme %»" /»
«c:if test="${empty theme}"»
존재하지 않는 테마 이미지입니다.
«/c:if»
«c:if test="${! empty theme}"»
«table width="100%" border="1" cellpadding="1" cellspacing="0"»
«tr»
    «td»제목«/td»
    «td»${theme.title}«/td»
«/tr»
«tr»
    «td»작성자«/td»
    «td»
    ${theme.name}
    «c:if test="${empty theme.email}"»
    «a href="mailto:${theme.email}"»[이메일]«/a»
    «/c:if»
    «/td»
«/tr»
«c:if test="${! empty theme.image}"»
«tr»
    «td colspan="2" align="center"»
        «a href="javascript:viewLarge('/EZEN/image/${theme.image}')"»
        «img src="/EZEN/image/${theme.image}" width="150" border="0"»
        «br»[크게보기]
        «/a»
    «/td»
«/tr»
«/c:if»
«tr»
    «td»내용«/td»
    «td»«pre»${theme.content}«/pre»«/td»
«/tr»
«tr»
    «td colspan="2"»
    «a href="javascript:goReply()"»[답변]«/a»
    «a href="javascript:goModify()"»[수정]«/a»
    «a href="javascript:goDelete()"»[삭제]«/a»
    «a href="javascript:goList()"»[목록]«/a»
    «/td»
«/tr»
«/table»
«/c:if»

«script language="JavaScript"»
function goReply() {
    document.move.action = "writeForm.jsp";
    document.move.submit();
}
function goModify() {
    document.move.action = "updateForm.jsp";
    document.move.submit();
}
function goDelete() {
    document.move.action = "deleteForm.jsp";
    document.move.submit();
}
function goList() {
    document.move.action = "list.jsp";
    document.move.submit();
}
function viewLarge(imgUrl) {

}
«/script»

«form name="move" method="post"»
    «input type="hidden" name="id" value="${theme.id}"»
    «input type="hidden" name="parentId" value="${theme.id}"»
    «input type="hidden" name="groupId" value="${theme.groupId}"»
   
    «input type="hidden" name="page" value="${param.page}"»
    «c:forEach var="searchCond" items="${paramValues.search_cond}"»
        «c:if test="${searchCond == 'title'}"»
        «input type="hidden" name="search_cond" value="title"»
        «/c:if»
        «c:if test="${searchCond == 'name'}"»
        «input type="hidden" name="search_cond" value="name"»
        «/c:if»
    «/c:forEach»
   
    «c:if test="${! empty param.search_key}"»
    «input type="hidden" name="search_key" value="${param.search_key}"»
    «/c:if»
«/form»
**크게보기 스크립트는 직접찾아서 구현해 주세요...


12.글수정
[/WebContent/imageboard/updateForm.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%
    request.setCharacterEncoding("euc-kr");
%»
«jsp:forward page="../template/template.jsp"»
    «jsp:param name="CONTENTPAGE" value="../updateForm_view.jsp" /»
«/jsp:forward»

[updateForm_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»
«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»
«%
    String themeId = request.getParameter("id");
    ThemeManager manager = ThemeManager.getInstance();
    Theme theme = manager.select(Integer.parseInt(themeId));
%»
«c:set var="theme" value="«%= theme %»" /»
«c:if test="${!empty theme}"»
«script language="JavaScript"»
function validate(form) {
    if (form.title.value == "") {
        alert("제목을 입력하세요.");
        return false;
    } else if (form.name.value == "") {
        alert("이름을 입력하세요.");
        return false;
    } else if (form.password.value == "") {
        alert("암호를 입력하세요.");
        return false;
    } else if (form.content.value == "") {
        alert("내용을 입력하세요");
        return false;
    }
}
«/script»

«form action="update.jsp" method="post" enctype="multipart/form-data"
      onSubmit="return validate(this)"»
«input type="hidden" name="id" value="${theme.id}"»
«table width="100%" border="1" cellpadding="1" cellspacing="0"»
«tr»
    «td»제목«/td»
    «td»«input type="text" name="title" size="40" value="${theme.title}"»«/td»
«/tr»
«tr»
    «td»이름«/td»
    «td»«input type="text" name="name" size="10" value="${theme.name}"»«/td»
«/tr»
«tr»
    «td»이메일«/td»
    «td»«input type="text" name="email" size="10" value="${theme.email}"»«/td»
«/tr»
«tr»
    «td»암호«/td»
    «td»«input type="password" name="password" size="10" value=""»«/td»
«/tr»
«tr»
    «td»이미지«/td»
    «td»«input type="file" name="imageFile"»
    «c:if test="${!empty theme.image}"»
    «br»
    «img src="/EZEN/image/${theme.image}" width="150" border="0"»
    «/c:if»
    «/td»
«/tr»
«tr»
    «td»내용«/td»
    «td»
    «textarea name="content" cols="40" rows="8"»${theme.content}«/textarea»
    «/td»
«/tr»
«tr»
    «td colspan="2"»
    «input type="submit" value="수정"»
    «input type="button" value="취소" onClick="javascript:history.go(-1)"»
    «/td»
«/tr»
«/table»

«input type="hidden" name="page" value="${param.page}"»
«c:forEach var="searchCond" items="${paramValues.search_cond}"»
    «c:if test="${searchCond == 'title'}"»
    «input type="hidden" name="search_cond" value="title"»
    «/c:if»
    «c:if test="${searchCond == 'name'}"»
    «input type="hidden" name="search_cond" value="name"»
    «/c:if»
«/c:forEach»

«c:if test="${! empty param.search_key}"»
«input type="hidden" name="search_key" value="${param.search_key}"»
«/c:if»


«/form»
«/c:if»
«c:if test="${empty theme}"»
글이 존재하지 않습니다.
«/c:if»

[/WebContent/imageboard/update.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page errorPage = "../error/error_view.jsp" %»

«%@ page import = "java.io.File" %»
«%@ page import = "org.apache.commons.fileupload.FileItem" %»

«%@ page import = "madvirus.util.ImageUtil" %»
«%@ page import = "madvirus.fileupload.FileUploadRequestWrapper" %»

«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»

«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»

«%
    FileUploadRequestWrapper requestWrap = new FileUploadRequestWrapper(
        request, -1, -1,
        "C:\\Java2\\App\\EZEN\\WebContent\\temp");
    HttpServletRequest tempRequest = request;
    request = requestWrap;
%»
«jsp:useBean id="theme" class="madvirus.gallery.Theme"»
    «jsp:setProperty name="theme" property="*" /»
«/jsp:useBean»
«%
   
    ThemeManager manager = ThemeManager.getInstance();
    Theme oldTheme = manager.select(theme.getId());
   
    if (theme.getPassword() == null ||
        oldTheme.getPassword().compareTo(theme.getPassword()) == 0) {
        // 암호가 같은 경우에만 작업 처리
        FileItem imageFileItem = requestWrap.getFileItem("imageFile");
        String image = "";
        if (imageFileItem.getSize() » 0) {
            int idx = imageFileItem.getName().lastIndexOf("\\");
            if (idx == -1) {
                idx = imageFileItem.getName().lastIndexOf("/");
            }
            image = imageFileItem.getName().substring(idx + 1);
           
            // 이미지를 지정한 경로에 저장
            File imageFile = new File(
                "C:\\Java2\\App\\EZEN\\WebContent\\image",
                image);
            // 같은 이름의 파일이름 처리
            if (imageFile.exists()) {
                for (int i = 0 ; true ; i++) {
                    imageFile = new File(
                        "C:\\Java2\\App\\EZEN\\WebContent\\image",
                        "("+i+")"+image);
                    if (!imageFile.exists()) {
                        image = "("+i+")"+image;
                        break;
                    }
                }
            }
            imageFileItem.write(imageFile);
           
            // 썸네일 이미지 생성
            File destFile = new File(
                "C:\\Java2\\App\\EZEN\\WebContent\\image",
                image+".small.jpg");
            ImageUtil.resize(imageFile, destFile, 50, ImageUtil.RATIO);
        }
        if (image.equals("")) {
            theme.setImage(oldTheme.getImage());
        } else {
            theme.setImage(image);
        }
        manager.update(theme);
%»
«html»«head»«title»수정«/title»«/head»«body»

«c:set var="search_cond"
       value="«%= requestWrap.getParameterValues("search_cond") %»" /»
«c:set var="pageNo" value="«%= requestWrap.getParameter("page") %»" /»
«c:set var="search_key"
       value="«%= requestWrap.getParameter("search_key") %»" /»

«form name="move" method="post"»
«input type="hidden" name="page" value="${pageNo}"»
«c:forEach var="searchCond" items="${search_cond}"»
    «c:if test="${searchCond == 'title'}"»
    «input type="hidden" name="search_cond" value="title"»
    «/c:if»
    «c:if test="${searchCond == 'name'}"»
    «input type="hidden" name="search_cond" value="name"»
    «/c:if»
«/c:forEach»
«c:if test="${! empty search_key}"»
«input type="hidden" name="search_key" value="${search_key}"»
«/c:if»
«/form»

«script language="JavaScript"»
alert("수정했습니다.");
document.move.action = "list.jsp";
document.move.submit();
«/script»

«/body»«/html»
«%
    } else {
%»
«script»
alert("암호가 다릅니다.");
history.go(-1);
«/script»
«%
    }
%»

13.글삭제
[/WebContent/imageboard/deleteForm.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%
    request.setCharacterEncoding("euc-kr");
%»
«jsp:forward page="../template/template.jsp"»
    «jsp:param name="CONTENTPAGE" value="../deleteForm_view.jsp" /»
«/jsp:forward»

[/WebContent/imageboard/deleteForm_view.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»
«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»
«%
    String themeId = request.getParameter("id");
    ThemeManager manager = ThemeManager.getInstance();
    Theme theme = manager.select(Integer.parseInt(themeId));
%»
«c:set var="theme" value="«%= theme %»" /»
«c:if test="${!empty theme}"»
«script language="JavaScript"»
function validate(form) {
    if (form.password.value == "") {
        alert("암호를 입력하세요.");
        return false;
    }
}
«/script»

«form action="delete.jsp" method="post"
      onSubmit="return validate(this)"»
«input type="hidden" name="id" value="${theme.id}"»
«table width="100%" border="1" cellpadding="1" cellspacing="0"»
«tr»
    «td»제목«/td»
    «td»${theme.title}«/td»
«/tr»
«tr»
    «td»작성자«/td»
    «td»${theme.name}«/td»
«/tr»
«tr»
    «td»암호«/td»
    «td»«input type="password" name="password" size="10" value=""»«/td»
«/tr»
«c:if test="${!empty theme.image}"»
«tr»
    «td»이미지«/td»
    «td»
    «img src="/EZEN/image/${theme.image}" width="150" border="0"»
    «/td»
«/tr»
«/c:if»
«tr»
    «td colspan="2"»
    «input type="submit" value="삭제"»
    «input type="button" value="취소" onClick="javascript:history.go(-1)"»
    «/td»
«/tr»
«/table»
«/form»
«/c:if»

«c:if test="${empty theme}"»
글이 존재하지 않습니다.
«/c:if»

[/WebContent/imageboard/delete.jsp]
«%@ page contentType = "text/html; charset=euc-kr" %»
«%@ page errorPage = "../error/error_view.jsp" %»

«%@ page import = "madvirus.gallery.Theme" %»
«%@ page import = "madvirus.gallery.ThemeManager" %»
«%@ page import = "madvirus.gallery.ThemeManagerException" %»

«%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %»

«html»
«head»«title»글삭제«/title»«/head»
«body»
«%
    String themeId = request.getParameter("id");
    ThemeManager manager = ThemeManager.getInstance();
    Theme oldTheme = manager.select(Integer.parseInt(themeId));
%»
«c:set var="oldTheme" value="«%= oldTheme %»" /»
«c:choose»
    «c:when test="${empty oldTheme}"»
    «script»
    alert("글이 존재하지 않습니다.");
    location.href="list.jsp";
    «/script»
    «/c:when»

    «c:when test="${oldTheme.password != param.password}"»
    «script»
    alert("암호가 다릅니다.");
    history.go(-1);
    «/script»
    «/c:when»

    «c:when test="${oldTheme.password == param.password}"»
    «%
        manager.delete(oldTheme.getId());
    %»
    «script»
    alert("글을 삭제하였습니다.");
    location.href="list.jsp";
    «/script»
    «/c:when»
«/c:choose»
«/body»
«/html»