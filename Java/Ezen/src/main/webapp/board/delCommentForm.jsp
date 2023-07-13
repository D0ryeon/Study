<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>
<%
	String comment_num=request.getParameter("cmn");
	String content_num=request.getParameter("ctn");
	String p_num=request.getParameter("p_num");
	String url="content1.jsp?num="+content_num+"&pageNum="+p_num;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="javascript">
	function deleteSave(){
		if(document.delForm.passwd.value==''){
			alert("비밀번호를 입력하십시오.");
			document.delForm.passwd.focus();
			return false;
		}
	}
</script>
</head>
<body bgcolor=<%=bodyback_c %>>
<center></center>
<form method=post name=delForm action="delCommentPro.jsp" onsubmit="return deleteSave()">
	<table border=1 align=center cellspacing=0 cellpadding=0 width=360>
		<tr height="30">
			<td align=center bgcolor=<%=value_c%>>
				<b>비밀번호를 입력해주세요.</b>
			</td>
		</tr>
		<tr height="30">
			<td align=center> 비밀번호 : 
			<input type="password" name=passwd size=8 maxlength=12>
			<input type="hidden" name=content_num value=<%=content_num %>>
			<input type="hidden" name=comment_num value=<%=comment_num %>>
			<input type="hidden" name=p_num value=<%=p_num %>>
			</td>
		</tr>
		<tr height="30">
			<td align=center bgcolor=<%=value_c %>>
				<input type="submit" value="코멘트 삭제">
				<input type="button" value="취소" onclick="document.location.href='<%=url %>'">
			</td>
		</tr>
	</table> 
</form>
</body>
</html>