package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.CommandHandler;
import command.NullHandler;

public class ControllerUsingURI extends HttpServlet{
	// <커맨드,핸들러인스턴스> 매핑 정보 저장
	private Map commandHandlerMap = new java.util.HashMap();
	
	public void init(ServletConfig config)throws ServletException{
		String configFile = config.getInitParameter("configFile2");
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			String configFilePath=config.getServletContext().getRealPath(configFile);
			fis = new FileInputStream(configFilePath);
			prop.load(fis);
		}catch(IOException e) {
			throw new ServletException(e);
		}finally {
			if(fis != null)
				try {
					fis.close();
				}catch(IOException ex) {
				}
		}
		Iterator keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				Class handlerClass = Class.forName(handlerClassName);
				Object handlerInstance = handlerClass.newInstance();
				commandHandlerMap.put(command, handlerInstance);
			}catch(ClassNotFoundException e) {
				throw new ServletException(e);
			}catch(InstantiationException e) {
				throw new ServletException(e);
			}catch(IllegalAccessException e) {
				throw new ServletException(e);
			}
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		process(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		process(request,response);
	}
	
	private void process(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		String command = request.getRequestURI();
		if(command.indexOf(request.getContextPath())==0) {
			command=command.substring(request.getContextPath().length());
		}
		
		CommandHandler handler = (CommandHandler)commandHandlerMap.get(command);
		if(handler == null) {
			handler = new NullHandler();
		}
		String viewPage=null;
		try {
			viewPage = handler.process(request,response);
		}catch(Throwable e) {
			throw new ServletException(e);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}
}