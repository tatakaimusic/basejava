<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"
                       pattern="^[A-Za-zА-Яа-яЁё\s]{1,30}" required></dd>
        </dl>
        <h3>Contacts:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Sections:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <jsp:useBean id="type" type="com.urise.webapp.model.SectionType"/>
            <c:if test="<%=type == SectionType.PERSONAL || type == SectionType.OBJECTIVE%>">
                <dl>
                    <dt>${type.title}</dt>
                    <br/>
                    <br/>
                    <dd><input type="text" name="${type.name()}" size="100" value="${resume.getSection(type)}"></dd>
                </dl>
            </c:if>
            <c:if test="<%=type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS%>">
                <dl>
                    <dt>${type.title}</dt>
                    <br/>
                    <br/>
                    <dd>
                        <textarea rows="10" cols="50" name="${type.name()}"><c:if test="<%=resume.getSection(type) != null%>"><c:forEach var="string" items="<%=((ListSection) resume.getSection(type)).getItems()%>"><jsp:useBean id="string" type="java.lang.String"/><%=string.trim() + "\n"%></c:forEach></c:if></textarea>
                    </dd>
                </dl>
            </c:if>
            <c:if test="<%=type == SectionType.EXPERIENCE || type == SectionType.EDUCATION%>">
                <dl>
                    <dt>${type.title}</dt>
                </dl>
                <br/>
                <c:forEach var="organisation" items="<%=((OrganizationSection) resume.getSection(type)).getOrganisations()%>" varStatus="counter">
                    <jsp:useBean id="organisation" type="com.urise.webapp.model.Organisation"/>
                    <dl>
                        <dt>Organisation</dt>
                        <dd><input type="text" name="${type.name()}" size="100" value="<%=organisation.getLink().getName()%>" ></dd>
                    </dl>
                    <dl>
                        <dt>Url</dt>
                        <dd><input type="text"  name="${type.name()}url" size="100" value="<%=organisation.getLink().getUrl()%>"></dd>
                    </dl>
                    <c:forEach var="period" items="<%=organisation.getPeriods()%>">
                        <jsp:useBean id="period" type="com.urise.webapp.model.Organisation.Period"/>
                        <dl>
                            <dt>Start date:</dt>
                            <dd>
                                <input type="text" name="${type.name()}${counter.index}startDate" size="15" value="<%=DateUtil.format(period.getStartDate())%>">
                            </dd>
                        </dl>
                        <dl>
                            <dt>End date:</dt>
                            <dd>
                                <input type="text" name="${type.name()}${counter.index}endDate" size="15" value="<%=DateUtil.format(period.getFinishDate())%>">
                            </dd>
                        </dl>
                        <dl>
                            <dt>Title:</dt>
                            <dd>
                                <input type="text" name="${type.name()}${counter.index}title" size="100" value="<%=period.getTitle()%>">
                            </dd>
                        </dl>
                        <dl>
                            <dt>Description:</dt>
                            <dd>
                                <input type="text" name="${type.name()}${counter.index}description" size="100" value="<%=period.getDescription()%>">
                            </dd>
                        </dl>
                        <br/>
                        <br/>
                    </c:forEach>
                </c:forEach>
            </c:if>
        </c:forEach>
        <hr>
        <button type="submit">Save</button>
        <button type="reset" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
