package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Article;
import jdbc.JdbcUtil;

public class ArticleDao {

		private static ArticleDao instance = new ArticleDao();
		public static ArticleDao getInstance() {
			return instance;
		}
		
		private ArticleDao() {}
		
		public int selectCount(Connection conn)throws SQLException{
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select count(*) from article");
				rs.next();
				return rs.getInt(1);
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(stmt);
			}
		}
		
		public List<Article> select(Connection conn,int firstRow, int endRow)throws SQLException{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement("select article_id, group_id, sequence_no, posting_date, read_count, writer_name, password, title from ( select rownum rnum, article_id, group_id, sequence_no, posting_date, read_count, writer_name, password, title from ( select * from article m order by m.sequence_no desc ) where rownum <= ? ) where rnum >= ?");
				pstmt.setInt(1, endRow);
				pstmt.setInt(2, firstRow);
				rs = pstmt.executeQuery();
				if(!rs.next()) {
					return Collections.emptyList();
				}
				List<Article> articleList = new ArrayList<Article>();
				do {
					Article article = makeArticleFromResultSet(rs, false);
					articleList.add(article);
				}while(rs.next());
				return articleList;
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
		}
		
		private Article makeArticleFromResultSet(ResultSet rs, boolean readContent)throws SQLException{
			Article article = new Article();
			article.setId(rs.getInt("article_id"));
			article.setGroupId(rs.getInt("group_id"));
			article.setSequenceNumber(rs.getString("sequence_no"));
			article.setPostingDate(rs.getTimestamp("posting_date"));
			article.setReadCount(rs.getInt("read_count"));
			article.setWriterName(rs.getString("writer_name"));
			article.setPassword(rs.getString("password"));
			article.setTitle(rs.getString("title"));
			if(readContent) {
				article.setContent(rs.getString("content"));
			}
			return article;
		}
		
		public int insert(Connection conn, Article article)throws SQLException{
			PreparedStatement pstmt = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement("insert into article (article_id, group_id, sequence_no, posting_date, read_count, writer_name, password, title, content) values (article_id_seq.NEXTVAL, ?, ?, ?, 0, ?, ?, ?, ?)");
				pstmt.setInt(1, article.getGroupId());
				pstmt.setString(2, article.getSequenceNumber());
				pstmt.setTimestamp(3, new Timestamp(article.getPostingDate().getTime()));
				pstmt.setString(4, article.getWriterName());
				pstmt.setString(5, article.getPassword());
				pstmt.setString(6, article.getTitle());
				pstmt.setString(7, article.getContent());
				int insertedCount = pstmt.executeUpdate();
				
				if(insertedCount > 0) {
					stmt = conn.createStatement();
					rs = stmt.executeQuery("select article_id_seq.CURRVAL from dual");
					if(rs.next()) {
						return rs.getInt(1);
					}
				}
				return -1;
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(stmt);
				JdbcUtil.close(pstmt);
			}
		}
		
		public Article selectById(Connection conn,int articleId)throws SQLException {
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement("select * from article where article_id = ?");
				pstmt.setInt(1, articleId);
				rs = pstmt.executeQuery();
				if(!rs.next()) {
					return null;
				}
				Article article = makeArticleFromResultSet(rs, true);
				return article;
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
				
			}
		}
		
		public void increaseReadCount(Connection conn, int articleId)throws SQLException {
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement("update article set read_count = read_count + 1 where article_id = ?");
				pstmt.setInt(1, articleId);
				pstmt.executeQuery();
			}finally {
				JdbcUtil.close(pstmt);
			}
		}
		
		public String selectLastSequenceNumber(Connection conn, String searchMaxSeqNum, String searchMinSeqNum)throws SQLException{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement("select min(sequence_no) from article where sequence_no < ? and sequence_no >= ?");
				pstmt.setString(1, searchMaxSeqNum);
				pstmt.setString(2, searchMinSeqNum);
				rs=pstmt.executeQuery();
				if(!rs.next()){
					return null;
				}
				return rs.getString(1);
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
		}
		
		public int update(Connection conn, Article article)throws SQLException{
			PreparedStatement pstmt = null;
			try {
				pstmt=conn.prepareStatement("update article set title = ?,content = ? where article_id = ?");
				pstmt.setString(1, article.getTitle());
				pstmt.setString(2, article.getContent());
				pstmt.setInt(3, article.getId());
				return pstmt.executeUpdate();
			}finally {
				JdbcUtil.close(pstmt);
			}
		}
		
		public void delete(Connection conn, int articleId)throws SQLException{
			PreparedStatement pstmt = null;
			try {
				pstmt=conn.prepareStatement("delete from article where article_id = ?");
				pstmt.setInt(1, articleId);
				pstmt.executeUpdate();
			}finally {
				JdbcUtil.close(pstmt);
			}
		}
		
		public int selectCount(Connection conn, int requestPageNumber, int n, String searchKeyword)throws Exception{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			int x = 0;
			String[] column_name= {"writer_name", "title","content"};
			
			try {
				pstmt = conn.prepareStatement("select count (*) from article where " + column_name[n] + " like '%'||?||'%'");
				pstmt.setString(1, searchKeyword);
				rs = pstmt.executeQuery();
				if(rs.next())
					x=rs.getInt(1);
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
				
			}
			return x;
		}
		
		public List<Article> select(Connection conn, int firstRow, int endRow, int n, String searchKeyword)throws Exception{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String[] column_name = {"writer_name", "title","content"};
		
			try {
				pstmt = conn.prepareStatement("select article_id, group_id, sequence_no, posting_date, read_count, writer_name, password, title from ( select rownum rnum, article_id, group_id, sequence_no, posting_date, read_count, writer_name, password, title from ( select * from article m  where " + column_name[n] + " like '%'||?||'%' order by m.sequence_no desc ) where rownum <= ? ) where rnum >= ?"); 
				pstmt.setString(1, searchKeyword);
				pstmt.setInt(2, endRow);
				pstmt.setInt(3, firstRow);
				rs = pstmt.executeQuery();
				if(!rs.next()) {
					return Collections.emptyList();
				}
				List<Article> articleList = new ArrayList<Article>();
				do {
					Article article = makeArticleFromResultSet(rs, false);
					articleList.add(article);
				} while (rs.next());
				return articleList;
			}finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
			}
		}
		
}
