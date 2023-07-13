package net.order.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderFrontController extends HttpServlet implements Servlet{
	
	private void execute(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = RequestURI.substring(contextPath.length());
		ActionForward forward = null;
		Action action = null;
		
		if(command.equals("/Orderstart.or")){
			action = new OrderStartAction();
			try {
				forward = action.execute(request, response);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/OrderList.or")) {
			action = new OrderListAction();
			try {
				forward = action.execute(request, response);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/OrderAdd.or")) {
			action = new OrderAddAction();
			try {
				forward = action.execute(request, response);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else if(command.equals("/OrderOK.or")) {
			forward = new ActionForward();
			forward.setRedirect(false);
			forward.setPath("./goods_order/goods_order_ok.jsp");
		}
		
		if(forward.isRedirect()) {
			response.sendRedirect(forward.getPath());
		}else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
			dispatcher.forward(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}
	
	
}
