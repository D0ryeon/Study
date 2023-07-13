<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page errorPage = "../error/error_view.jsp" %>

<%@ page import = "java.io.File" %>
<%@ page import = "org.apache.commons.fileupload.FileItem" %>

<%@ page import = "madvirus.util.ImageUtil" %>
<%@ page import = "madvirus.fileupload.FileUploadRequestWrapper" %>

<%@ page import = "madvirus.gallery.Theme" %>
<%@ page import = "madvirus.gallery.ThemeManager" %>
<%@ page import = "madvirus.gallery.ThemeManagerException" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    FileUploadRequestWrapper requestWrap = new FileUploadRequestWrapper(
        request, -1, -1,
        "C:\\Java\\ImageBoard\\src\\main\\webapp\\temp");
    HttpServletRequest tempRequest = request;
    request = requestWrap;
%>
<jsp:useBean id="theme" class="madvirus.gallery.Theme">
    <jsp:setProperty name="theme" property="*" />
</jsp:useBean>
<%
   
    ThemeManager manager = ThemeManager.getInstance();
    Theme oldTheme = manager.select(theme.getId());
   
    if (theme.getPassword() == null ||
        oldTheme.getPassword().compareTo(theme.getPassword()) == 0) {
        // ��ȣ�� ���� ��쿡�� �۾� ó��
        FileItem imageFileItem = requestWrap.getFileItem("imageFile");
        String image = "";
        if (imageFileItem.getSize() > 0) {
            int idx = imageFileItem.getName().lastIndexOf("\\");
            if (idx == -1) {
                idx = imageFileItem.getName().lastIndexOf("/");
            }
            image = imageFileItem.getName().substring(idx + 1);
           
            // �̹����� ������ ��ο� ����
            File imageFile = new File(
                "C:\\Java\\ImageBoard\\src\\main\\webapp\\image",
                image);
            // ���� �̸��� �����̸� ó��
            if (imageFile.exists()) {
                for (int i = 0 ; true ; i++) {
                    imageFile = new File(
                        "C:\\Java\\ImageBoard\\src\\main\\webapp\\image",
                        "("+i+")"+image);
                    if (!imageFile.exists()) {
                        image = "("+i+")"+image;
                        break;
                    }
                }
            }
            imageFileItem.write(imageFile);
           
            // ����� �̹��� ����
            File destFile = new File(
                "C:\\Java\\ImageBoard\\src\\main\\webapp\\image",
                image+".small.jpg");
            ImageUtil.resize(imageFile, destFile, 50, ImageUtil.RATIO);
        }
        if (image.equals("")) {
            theme.setImage(oldTheme.getImage());
        } else {
            theme.setImage(image);
        }
        manager.update(theme);
%>
<html>
<head>
<title>����</title>
</head>
<body>

<c:set var="search_cond"
       value='<%= requestWrap.getParameterValues("search_cond") %>' />
<c:set var="pageNo" value='<%= requestWrap.getParameter("page") %>' />
<c:set var="search_key"
       value='<%= requestWrap.getParameter("search_key") %>' />

<form name="move" method="post">
<input type="hidden" name="page" value="${pageNo}">
<c:forEach var="searchCond" items="${search_cond}">
    <c:if test="${searchCond == 'title'}">
    <input type="hidden" name="search_cond" value="title">
    </c:if>
    <c:if test="${searchCond == 'name'}">
    <input type="hidden" name="search_cond" value="name">
    </c:if>
</c:forEach>
<c:if test="${! empty search_key}">
<input type="hidden" name="search_key" value="${search_key}">
</c:if>
</form>

<script language="JavaScript">
alert("�����߽��ϴ�.");
document.move.action = "list.jsp";
document.move.submit();
</script>

</body></html>
<%
    } else {
%>
<script>
alert("��ȣ�� �ٸ��ϴ�.");
history.go(-1);
</script>
<%
    }
%>