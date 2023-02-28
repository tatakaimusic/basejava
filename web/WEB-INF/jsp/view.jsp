<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
</section>
<section>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
    <h4><%=sectionEntry.getKey().getTitle()%>
    </h4>
    <c:if test="<%=sectionEntry.getKey() == SectionType.PERSONAL || sectionEntry.getKey() == SectionType.OBJECTIVE%>">
        <%=sectionEntry.getKey().toHtml(sectionEntry.getValue())%><br/>
        <br/>
    </c:if>
    <c:if test="<%=sectionEntry.getKey() == SectionType.ACHIEVEMENT || sectionEntry.getKey() == SectionType.QUALIFICATIONS%>">
        <ul>
            <c:forEach var="string" items="<%=((ListSection)sectionEntry.getValue()).getItems()%>">
                <jsp:useBean id="string" type="java.lang.String"/>
                <li><%=string%>
                </li>
            </c:forEach>
        </ul>
        <br/>
    </c:if>
    <c:if test="<%=sectionEntry.getKey() == SectionType.EXPERIENCE || sectionEntry.getKey() == SectionType.EDUCATION%>">
        <ul>
            <c:forEach var="organisation"
                       items="<%=((OrganizationSection)sectionEntry.getValue()).getOrganisations()%>">
                <jsp:useBean id="organisation" type="com.urise.webapp.model.Organisation"/>
                <li><a href="<%=organisation.getLink().getUrl()%>"><%=organisation.getLink().getName()%>
                </a></li>
                <ol>
                    <c:forEach var="period" items="<%=organisation.getPeriods()%>">
                        <jsp:useBean id="period" type="com.urise.webapp.model.Organisation.Period"/>
                        <li><%=period.getStartDate() + " - " + period.getFinishDate() + " " + period.getTitle() + "\n" + period.getDescription()%>
                        </li>
                    </c:forEach>
                </ol>
            </c:forEach>
        </ul>
        <br/>
    </c:if>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
