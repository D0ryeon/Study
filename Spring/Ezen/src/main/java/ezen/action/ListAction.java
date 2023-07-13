package ezen.action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ezen.board.BoardDBBean;

public class ListAction implements CommandAction {// 글목록 처리

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String pageNum = request.getParameter("pageNum"); // 페이지 번호
		
		if(pageNum == null) {
			pageNum = "1";
		}
		int pageSize = 10; // 한 페이지의 글의 개수
		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage-1)
		
		return null;
	}  

	
}
