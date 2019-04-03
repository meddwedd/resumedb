<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="me.shamanov.resumedb.model.SectionType" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="me.shamanov.resumedb.model.Resume" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
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
        <th class="main-table-item"><span id="resume-name">${resume.fullName}</span></th>
    </tr>
    <tr>
        <th class="main-table-item"><span class="item">Место проживания: </span>${resume.location}</th>
    </tr>
    <tr>
        <th class="main-table-item"><span class="item">Домашняя страница: </span>${resume.homepage}</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty resume.contacts.values}">
        <tr>
            <td class="main-table-section-name">Контакты:</td>
        </tr>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<me.shamanov.resumedb.model.ContactType, java.lang.String>"/>
            <c:set var="contactType" value="${contactEntry.key}"/>
            <c:set var="value" value="${contactEntry.value}"/>
            <tr>
                <td class="main-table-item"><span class="item">${contactType.title}: </span>${value}</td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${resume.sections.values.size ne 0}">
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<me.shamanov.resumedb.model.SectionType, me.shamanov.resumedb.model.Section>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <tr>
                <td class="main-table-section-name">${sectionType.title}</td>
            </tr>
            <c:choose>
                <c:when test="${sectionType ne SectionType.EDUCATION and sectionType ne SectionType.EXPERIENCE}">
                    <tr>
                        <td class="main-table-item">
                            <c:choose>
                                <c:when test="${sectionType eq SectionType.POSITION}">
                                    <span class="item">${section.values.get(0)}</span>
                                    <c:if test="${section.values.size() > 1}">
                                        <c:forEach var="sectionItem" items="${section.values}" begin="1">
                                            <div class="list">${sectionItem}</div>
                                        </c:forEach>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="sectionItem" items="${section.values}">
                                        <div class="list">${sectionItem}</div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="establishment" items="${section.values}">
                        <jsp:useBean id="establishment" type="me.shamanov.resumedb.model.Establishment"/>
                        <tr>
                            <td class="main-table-item">
                                <table id="resume-sub-table">
                                    <thead>
                                    <tr>
                                        <th class="sub-table-item">
                                            <span class="item">${establishment.title}</span>
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
                                                <span class="item">${period.start} - ${period.end eq LocalDate.MAX ? 'наст. время' : period.end}</span>
                                            </td>
                                            <td class="sub-table-item">
                                                <span class="item">${period.position}</span>
                                            </td>
                                        </tr>
                                        <c:forEach var="description" items="${period.description}">
                                            <c:if test="${not empty description}">
                                                <tr>
                                                    <td class="sub-table-item">
                                                        &nbsp;
                                                    </td>
                                                    <td class="sub-table-item">
                                                        <div class="list">${description}</div>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                    </tbody>
                                </table>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
    </tbody>
</table>
</body>
</html>
