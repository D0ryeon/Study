package ezen.action;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import ezen.board.BoardDBBean;

public class DeleteProAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		
		int num = Integer.parseInt("num");
		String pageNum = request.getParameter("pageNum");
		String passwd = request.getParameter("passwd");
		
		BoardDBBean dbPro = BoardDBBean.getInstance();
		int check = dbPro.deleteArticle(num, passwd);
		
		// 해당 뷰에서 사용할 속성
		request.setAttribute("pageNum", new Integer(pageNum));
		request.setAttribute("check", new Integer(check));
		
		return "/MVC/deletePro.jsp";
	}
	
}
