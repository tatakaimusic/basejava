package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ResumeServlet extends HttpServlet {
    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.setAttribute("size", storage.size());
            request.getRequestDispatcher("\\WEB-INF\\jsp\\list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "clear":
                storage.clear();
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    OrganizationSection section = (OrganizationSection) r.getSection(type);
                    List<Organisation> organisations = section.getOrganisations();
                    List<Organisation> newOrganisations = new ArrayList<>();
                    for (int i = 0; i < organisations.size(); i++) {
                        if (!organisations.get(i).getLink().getName().trim().equals("")) {
                            newOrganisations.add(organisations.get(i));
                        }
                    }
                    r.setSection(type, new OrganizationSection(newOrganisations));
                }
                break;
            case "save":
                r = Resume.EMPTY;
//                r.setUuid(UUID.randomUUID().toString());
//                r.setFullName("");
//                storage.save(r);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section section = r.getSection(type);
                    switch (type) {
                        case PERSONAL, OBJECTIVE:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT, QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE, EDUCATION:
                            OrganizationSection organisationSection = (OrganizationSection) r.getSection(type);
                            List<Organisation> emptyOrganizations = new ArrayList<>();
                            emptyOrganizations.add(Organisation.EMPTY);
                            if (organisationSection != null) {
                                for (Organisation org : organisationSection.getOrganisations()) {
                                    List<Organisation.Period> emptyPositions = new ArrayList<>();
                                    emptyPositions.add(Organisation.Period.EMPTY);
                                    emptyPositions.addAll(org.getPeriods());
                                    emptyOrganizations.add(new Organisation(org.getLink(), emptyPositions));
                                }
                            }
                            section = new OrganizationSection(emptyOrganizations);
                            break;
                    }
                    r.setSection(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher("view".equals(action) ? "\\WEB-INF\\jsp\\view.jsp" : "\\WEB-INF\\jsp\\edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Boolean exist = true;
        Resume r = new Resume();
        if (uuid == null || uuid.length() == 0) {
            exist = false;
            r.setUuid(UUID.randomUUID().toString());
        } else {
            r = storage.get(uuid);
        }
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            switch (type) {
                case PERSONAL, OBJECTIVE -> {
                    if (value != null && value.trim().length() != 0) {
                        r.setSection(type, new TextSection(value));
                    } else {
                        r.getSections().remove(type);
                    }
                }
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    if (value != null && value.trim().length() != 0) {
                        r.setSection(type, new ListSection(value.lines().toList()));
                    } else {
                        r.getSections().remove(type);
                    }
                }
                case EXPERIENCE, EDUCATION -> {
                    String[] values = request.getParameterValues(type.name());
                    String[] urls = request.getParameterValues(type.name() + "url");
                    List<Organisation> organisations = new ArrayList<>();
                    for (int i = 0; i < values.length; i++) {
                        List<Organisation.Period> periods = new ArrayList<>();
                        String name = values[i];
                        if (name.trim().length() != 0) {
                            String counter = type.name() + i;
                            String[] startDates = request.getParameterValues(counter + "startDate");
                            String[] endDates = request.getParameterValues(counter + "endDate");
                            String[] titles = request.getParameterValues(counter + "title");
                            String[] descriptions = request.getParameterValues(counter + "description");
                            for (int j = 0; j < titles.length; j++) {
                                if (titles[j].trim().length() != 0) {
                                    periods.add(new Organisation.Period(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                }
                            }
                        }
                        organisations.add(new Organisation(new Link(name, urls[i]), periods));
                    }
                    r.setSection(type, new OrganizationSection(organisations));
                }
            }
        }
        if (exist) {
            storage.update(r);
        } else {
            storage.save(r);
        }
        response.sendRedirect("resume");
    }
}
