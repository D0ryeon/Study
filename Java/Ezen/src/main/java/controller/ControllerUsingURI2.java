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

import ezen.action.CommandAction;
import ezen.action.NullAction;

public class ControllerUsingURI2 extends HttpServlet{
	// <커맨드,핸들러인스턴스> 매핑 정보 저장
	private Map commandMap = new java.util.HashMap();  // 명령어와 명령어 처리 클래스를 쌍으로 저장
	
	// 명령어와 처리 클래스가 매핑 되어 있는 properties 파일을 읽어서 Map객체인 commandMap에 저장
	// 명령어와 처리 클래스가 매핑 되어 있는 properties 파일은 Command.properties 파일
	public void init(ServletConfig config)throws ServletException{
		String props = config.getInitParameter("configFile2");  // web.xml에서 propertyConfig에 해당하는 init-param 의 값을 읽어옴
		Properties prop = new Properties(); // 명령어와 처리클래스의 매핑정보를 저장할 Properties 객체 생성
		FileInputStream fis = null;
		try {
			String configFilePath=config.getServletContext().getRealPath(props);
			
			fis = new FileInputStream(configFilePath); // Command.properties 파일의 내용을 읽어옴
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
				commandMap.put(command, handlerInstance);
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
		requestPro(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		requestPro(request,response);
	}
	
	private void requestPro(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		String view = null;
		CommandAction com = null;
		try {
			String command = request.getRequestURI();
			if(command.indexOf(request.getContextPath())==0) {
				command = command.substring(request.getContextPath().length());
			}
			com = (CommandAction)commandMap.get(command);
				if(com==null) {
					comm = new NullAction();
				}
			view = com.requestPro(request,response);
		}catch(Throwable e) {
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
		
}