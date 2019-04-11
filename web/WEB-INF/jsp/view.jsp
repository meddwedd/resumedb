<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="me.shamanov.resumedb.model.Establishment.Period" %>
<%@ page import="me.shamanov.resumedb.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%--<%@ page errorPage="error.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <jsp:useBean id="resume" scope="request" type="me.shamanov.resumedb.model.Resume"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="resources/css/view-styles.css"/>
    <title>${resume.fullName}</title>
</head>
<body>
<table id="resume-main-table">
    <caption>coded by mshamanov</caption>
    <thead>
    <tr>
        <th class="main-table-section-name"></th>
    </tr>
    <tr>
        <th class="main-table-item"><span id="resume-name"><c:out value="${resume.fullName}"/></span></th>
    </tr>
    <c:if test="${not empty resume.location}">
        <tr>
            <th class="main-table-item"><span class="item">Место проживания: </span><c:out value="${resume.location}"/></th>
        </tr>
    </c:if>
    <c:if test="${resume.age > 0 and resume.age <= 300}">
        <tr>
            <th class="main-table-item"><span class="item">Возраст: </span><c:out value="${resume.age}"/></th>
        </tr>
    </c:if>
    </thead>
    <tbody>
    <c:if test="${resume.contacts.values().size() > 0}">
        <tr>
            <td class="main-table-section-name">Контакты:</td>
        </tr>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<me.shamanov.resumedb.model.ContactType, java.lang.String>"/>
            <c:set var="contactType" value="${contactEntry.key}"/>
            <c:set var="value" value="${contactEntry.value}"/>
            <c:if test="${not empty value}">
                <tr>
                    <td class="main-table-item"><span
                            class="item">${contactType.title}: </span>${contactType.toLink(value)}
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${resume.sections.values.size ne 0}">
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<me.shamanov.resumedb.model.SectionType, me.shamanov.resumedb.model.Holder>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <c:set var="holder" value="${sectionEntry.value}"/>
            <c:if test="${not empty holder}">
                <tr>
                    <td class="main-table-section-name">${sectionType.title}</td>
                </tr>
                <c:choose>
                    <c:when test="${sectionType ne SectionType.EDUCATION and sectionType ne SectionType.EXPERIENCE}">
                        <tr>
                            <td class="main-table-item">
                                <c:choose>
                                    <c:when test="${sectionType eq SectionType.POSITION}">
                                        <span class="item"><c:out value="${holder.values[0]}"/></span>
                                        <c:if test="${holder.values.size() > 1}">
                                            <ul class="main-list">
                                                <c:forEach var="sectionItem" items="${holder.values}" begin="1">
                                                    <li><c:out value="${sectionItem}"/></li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <ul class="main-list">
                                            <c:forEach var="sectionItem" items="${holder.values}">
                                                <li><c:out value="${sectionItem}"/></li>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="establishment" items="${holder.values}">
                            <jsp:useBean id="establishment" type="me.shamanov.resumedb.model.Establishment"/>
                            <tr>
                                <td class="main-table-item">
                                    <table id="resume-sub-table">
                                        <thead>
                                        <tr>
                                            <th class="sub-table-item">
                                                <span class="item"><c:out value="${establishment.title}"/></span>
                                            </th>
                                            <th class="sub-table-item">
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="period" items="${establishment.periods}">
                                            <jsp:useBean id="period"
                                                         type="me.shamanov.resumedb.model.Establishment.Period"/>
                                            <tr>
                                                <td class="sub-table-item">
                                                    <c:set var="formatter" value="<%=Period.dateTimeFormatter%>"/>
                                                    <c:set var="NOW" value="<%=Period.NOW%>"/>
                                                    <span class="item"><c:out value="${period.start eq NOW ? '' : period.start.format(formatter)}"/> — <c:out value="${period.end eq NOW ? (period.start ne NOW ? 'наст. время' : '') : period.end.format(formatter)}"/></span>
                                                </td>
                                                <td class="sub-table-item">
                                                    <span class="item"><c:out value="${period.position}"/></span>
                                                </td>
                                            </tr>
                                            <c:if test="${not empty period.descriptions}">
                                                <tr>
                                                    <td class="sub-table-item">
                                                        &nbsp;
                                                    </td>
                                                    <td class="sub-table-item">
                                                        <ul class="main-list">
                                                            <c:forEach var="description" items="${period.descriptions}">
                                                                <li><c:out value="${description}"/></li>
                                                            </c:forEach>
                                                        </ul>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
    </c:if>
    <tr>
        <td class="main-table-section-name" style="text-align: center;">
            <form action="resume">
                <input type="hidden" name="id" value="${resume.id}"/>
                <input type="hidden" name="action" value="edit"/>
                <input type="submit" name="submit" value="редактировать"/>
                <input type="button" name="db" value="список" onclick="window.location='/';"/>
                <%--<input type="button" name="back" value="назад" onclick="window.history.back()"/>--%>
            </form>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>
