<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="me.shamanov.resumedb.model.ContactType" %>
<%@ page import="me.shamanov.resumedb.model.Establishment.Period" %>
<%@ page import="me.shamanov.resumedb.model.SectionType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%--<%@ page errorPage="error.jsp" %>--%>

<html lang="ru">
<head>
    <jsp:useBean id="resume" scope="request" type="me.shamanov.resumedb.model.Resume"/>
    <link rel="stylesheet" type="text/css" href="resources/css/view-styles.css"/>
    <title>${resume.fullName}</title>
</head>
<body>
<form method="post" action="resume" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${resume.id}">
    <table id="resume-main-table">
        <caption>coded by mshamanov</caption>
        <thead>
        <tr>
            <th class="main-table-section-name"></th>
        </tr>
        <tr>
            <th class="main-table-item"><span class="item">Имя: </span>
                <label>
                    <input type="text" required="required" maxlength="100" name="fullName" value="${resume.fullName}"/>
                </label>
            </th>
        </tr>
        <tr>
            <th class="main-table-item"><span class="item">Место проживания: </span>
                <label>
                    <input type="text" maxlength="100" name="location" value="${resume.location}"/>
                </label>
            </th>
        </tr>
        <tr>
            <th class="main-table-item"><span class="item">Возраст: </span>
                <label>
                    <input type="number" min="0" max="300" name="age" value="${resume.age}"/>
                </label>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="main-table-section-name">Контакты:</td>
        </tr>
        <c:forEach var="contactType" items="${ContactType.values()}">
            <c:set var="contactValue" value="${resume.contacts.get(contactType)}"/>
            <tr>
                <td class="main-table-item">
                        <span
                                class="item">${contactType.title}: </span>
                    <label>
                        <input type="${contactType.inputType.toString().toLowerCase()}" maxlength="100"
                               name="${contactType}" value="${contactValue}"/>
                    </label>
                </td>
            </tr>
        </c:forEach>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<me.shamanov.resumedb.model.SectionType, me.shamanov.resumedb.model.Holder>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <c:set var="holder" value="${sectionEntry.value}"/>
            <tr>
                <td class="main-table-section-name">${sectionType.title}</td>
            </tr>
            <c:choose>
                <c:when test="${sectionType ne SectionType.EDUCATION and sectionType ne SectionType.EXPERIENCE}">
                    <tr>
                        <td class="main-table-item">
                            <c:choose>
                                <c:when test="${sectionType eq SectionType.POSITION}">
                                    <c:set var="positionTitle"
                                           value="${holder.values.size() gt 0 ? holder.values.get(0) : ''}"/>
                                    <label>
                                        <input type="text" size="100" maxlength="200" name="${sectionType}"
                                               value="${positionTitle}"/>
                                    </label>
                                    <div style="padding: 2px">
                                        <label>
<textarea name="${sectionType}" rows="6" cols="80" maxlength="400"><c:forEach
        var="sectionItem" items="${holder.values}" begin="1" varStatus="loop"><c:out
        value="${sectionItem}"/><c:if
        test="${!loop.last}">&#013;&#010;</c:if></c:forEach></textarea>
                                        </label>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <label>
<textarea name="${sectionType}" rows="6" cols="80" maxlength="400"><c:forEach
        var="sectionItem" items="${holder.values}" varStatus="loop"><c:out
        value="${sectionItem}"/><c:if
        test="${!loop.last}">&#013;&#010;</c:if></c:forEach></textarea>
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="establishment" items="${holder.values}" varStatus="сounter">
                        <jsp:useBean id="establishment" type="me.shamanov.resumedb.model.Establishment"/>
                        <tr>
                            <td class="main-table-item">
                                <table id="resume-sub-table">
                                    <thead>
                                    <tr>
                                        <th class="sub-table-item">
                                            <label>
                                                <input type="text" style="background-color: #a5c2ed; font-weight: bold;"
                                                       size="20" maxlength="200" name="${sectionType}"
                                                       value="${establishment.title.replace("\"", "&quot;")}"/>
                                            </label>
                                        </th>
                                        <th class="sub-table-item-edit">
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td colspan="2" class="period-splitter"></td>
                                    </tr>
                                    <c:forEach var="period" items="${establishment.periods}" varStatus="loop">
                                        <jsp:useBean id="period"
                                                     type="me.shamanov.resumedb.model.Establishment.Period"/>
                                        <tr>
                                            <td class="sub-table-item">
                                                <c:set var="formatter" value="<%=Period.dateTimeFormatter%>"/>
                                                <c:set var="NOW" value="<%=Period.NOW%>"/>
                                                <span class="item">Дата начала:</span>
                                                <br>
                                                <input type="text" size="20" maxlength="20" placeholder="MM/YYYY"
                                                       pattern="(0[1-9]|1[0-2])/(1|2){1}[0-9]{3}"
                                                       name="${sectionType}-start-<c:out value="${сounter.index}"/>"
                                                       value="${period.start eq NOW or period.position.equals("") ? "" : period.start.format(formatter)}"/>
                                                <br>
                                                <span class="item">Дата окончания:</span>
                                                <br>
                                                <input type="text" size="20" maxlength="20" placeholder="MM/YYYY"
                                                       pattern="(0[1-9]|1[0-2])/(1|2){1}[0-9]{3}"
                                                       name="${sectionType}-end-<c:out value="${сounter.index}"/>"
                                                       value="${period.end eq NOW or period.position.equals("") ? "" : period.end.format(formatter)}"/>
                                            </td>
                                            <td class="sub-table-item-edit" style="vertical-align:top">
                                                <span class="item">Позиция:</span>
                                                <br>
                                                <label>
                                                    <input type="text" size="20" maxlength="200"
                                                           name="${sectionType}-position-<c:out value="${сounter.index}"/>"
                                                           value="${period.position.replace("\"", "&quot;")}"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="sub-table-item">
                                                &nbsp;
                                            </td>
                                            <td class="sub-table-item-edit" style="vertical-align:top;">
                                                <div style="display: block; position: relative; margin-top: -40px;">
                                                    <label>
<textarea
        name="${sectionType}-description-<c:out value="${сounter.index}"/>"
        rows="6" cols="80" maxlength="400"><c:forEach
        var="description" items="${period.descriptions}"><c:out
        value="${description}"/><c:if
        test="${!loop.last}">&#013;&#010;</c:if></c:forEach></textarea>
                                                    </label>
                                                </div>
                                            </td>
                                        </tr>
                                        <c:if test="${!loop.last}">
                                            <tr>
                                                <td colspan="2" class="period-splitter"></td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    </tbody>
                                </table>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <tr>
            <td class="main-table-section-name" style="text-align: center;">
                <input type="submit" name="ok" value="сохранить"/>
                <input type="button" name="cancel" value="отменить" onclick="window.history.back()"/>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
