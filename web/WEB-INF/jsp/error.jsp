<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="resources/css/styles.css"/>
    <title>${pageContext.servletContext.getInitParameter("title")}</title>
</head>
<body style="align-items: normal;">
<table>
    <caption>coded by mshamanov</caption>
    <thead>
    <tr>
        <th colspan="3" style="background: #eeeeee"><span style="color: red">Что-то пошло не так...</span></th>
    </tr>
    </thead>
    <tbody>
    <c:set var="exception" value="${pageContext.exception}"/>
    <c:forEach begin="0" end="10" varStatus="loop">
        <c:if test="${exception ne null}">
            <tr>
                <td>
                    <c:if test="${!loop.first}">caused by: </c:if>
                        ${exception}
                </td>
            </tr>
            <tr>
                <td>
                    <c:forEach var="trace" items="${exception.stackTrace}">
                        <p><c:out value="${trace}"/></p>
                    </c:forEach>
                </td>
            </tr>
        </c:if>
        <c:if test="${loop.last and exception ne null}">
            <tr>
                <td>and so on...</td>
            </tr>
        </c:if>
        <c:set var="exception" value="${exception.cause}"/>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
