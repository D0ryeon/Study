<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "service.IncreaseDownloadCountService" %>
<%@ page import = "java.net.URLEncoder" %>
<%@ page import = "service.PdsItemNotFoundException" %>
<%@ page import = "service.FileDownLoadHelper" %>
<%@ page import = "model.PdsItem" %>
<%@ page import = "service.GetPdsItemService" %>
<%
	int id = Integer.parseInt(request.getParameter("id"));

	try{
		PdsItem item = GetPdsItemService.getInstance().getPdsItem(id);
		
		// 응답 헤더 다운로드로 설정
		response.reset();
		
		String fileName= new String(item.getFileName().getBytes("UTF-8"),"iso-8859-1");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setContentLength((int)item.getFileSize());
		response.setHeader("Pargma", "no-cache;");
		response.setHeader("Expries", "-1;");
		
		FileDownLoadHelper.copy(item.getRealPath(),response.getOutputStream());
		
		response.getOutputStream().close();
		
		IncreaseDownloadCountService.getInstance().increaseCount(id);
	}catch(PdsItemNotFoundException ex){
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}
	out.clear();
	out = pageContext.pushBody();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>