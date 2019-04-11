<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%--<%@ page errorPage="error.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>${pageContext.servletContext.getInitParameter("title")}</title>
</head>
<body>
<table>
    <caption>coded by mshamanov</caption>
    <thead>
    <tr>
        <th colspan="2"><span>Список резюме</span></th>
        <th><a href="resume?action=create"><span>добавить</span></a></th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${fn:length(resumes) eq 0}">
            <tr>
                <td colspan="3">Список пуст</td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="resume" items="${resumes}">
                <jsp:useBean id="resume" type="me.shamanov.resumedb.model.Resume"/>
                <tr>
                    <td><a id="resume-title" href="resume?id=${resume.id}&action=view">${resume.fullName}</a></td>
                    <td><a href="resume?id=${resume.id}&action=edit">редактировать</a></td>
                    <td><a href="resume?id=${resume.id}&action=delete">удалить</a></td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
</body>
</html>
