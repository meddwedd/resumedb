package me.shamanov.resumedb.web;

import me.shamanov.resumedb.model.*;
import me.shamanov.resumedb.storage.Storage;
import me.shamanov.resumedb.web.config.ResumeServletConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Mike
 * Date: 02.04.2019
 */

@WebServlet(urlPatterns = "/resume")
public class ResumeServlet extends HttpServlet {
    private Storage storage;
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String action = req.getParameter("action");

        if (action == null) {
            req.setAttribute("resumes", storage.getSortedResumeList());
            req.getRequestDispatcher("WEB-INF/jsp/index.jsp").forward(req, resp);
            return;
        }

        Resume resume;

        switch (action) {
            case "create":
                //assign EMPTY instance of Resume class so that it would contain all fields empty when being edited.
                resume = Resume.EMPTY;
                logger.info("Creating new resume.");
                break;
            case "view":
                //just loads resume from the storage and send the result of the request to view.jsp page.
                resume = storage.load(id);
                logger.info("Resume#id: " + id + " has been loaded!");
                break;
            case "edit":
                //loads the resume from the storage and adds empty representatives of missed sections so that a user could add the info he didn't the first time when it was created.
                resume = storage.load(id);
                logger.info("Resume#id: " + id + " has been loaded!");
                for (ContactType contactType : ContactType.values()) {
                    resume.getContacts().putIfAbsent(contactType, "");
                }
                //for those sections that are totally empty it would add one empty Holder representative and fill with empty values, otherwise it would add those for already loaded information.
                for (SectionType sectionType : SectionType.values()) {
                    resume.getSections().compute(sectionType, (k, v) -> {
                        if (v == null) {
                            if (k == SectionType.EDUCATION || k == SectionType.EXPERIENCE) {
                                return EstablishmentHolder.from(Establishment.of("", Establishment.Period.of(LocalDate.now(), LocalDate.now(), "")));
                            } else {
                                return MultipleTextHolder.from();
                            }
                        } else if (k == SectionType.EDUCATION || k == SectionType.EXPERIENCE) {
                            ((EstablishmentHolder) v).getValues().forEach(establishment -> establishment.getPeriods().add(Establishment.Period.of(LocalDate.now(), LocalDate.now(), "")));
                            ((EstablishmentHolder) v).getValues().add(Establishment.of("", Establishment.Period.of(LocalDate.now(), LocalDate.now(), "")));
                            return v;
                        } else {
                            return v;
                        }
                    });
                }
                break;
            case "delete":
                storage.delete(id);
                logger.info("Resume#id: " + id + " has been deleted!");
            default:
                resp.sendRedirect("/");
                return;
        }

        String forwardPage = action.equals("view") ? "WEB-INF/jsp/view.jsp" : "WEB-INF/jsp/edit.jsp";

        req.setAttribute("resume", resume);
        req.getRequestDispatcher(forwardPage).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String fullName = req.getParameter("fullName");
        String location = req.getParameter("location");
        int age = 0;

        try {
            age = Integer.parseInt(req.getParameter("age"));
        } catch (Exception e) {
            //do nothing
        }

        boolean isNewResume = id == null || id.isEmpty();

        Resume resume;
        if (isNewResume) {
            resume = Resume.of(fullName, location, age);
        } else {
            resume = storage.load(id);
            if (!fullName.isEmpty()) {
                resume.setFullName(fullName);
            }
            resume.setLocation(location);
            resume.setAge(age);
        }

        //processing all input fields and textareas from edit.jsp
        for (ContactType contactType : ContactType.values()) {
            String reqContact = req.getParameter(contactType.name());
            resume.getContacts().compute(contactType, (k, v) -> !reqContact.isEmpty() ? reqContact : null);
        }

        for (SectionType sectionType : SectionType.values()) {
            String[] reqSection = req.getParameterValues(sectionType.name());
            switch (sectionType) {
                case POSITION:
                case ACCOMPLISHMENT:
                case QUALITY:
                    MultipleTextHolder textHolder = (MultipleTextHolder) resume.getSections().get(sectionType);
                    List<String> collect = Stream.of(reqSection).flatMap(line -> Stream.of(line.split("(\r\n|\n)+"))).filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        if (textHolder != null && textHolder.getClass() == MultipleTextHolder.class) {
                            textHolder.setValues(collect);
                        } else {
                            resume.addSection(sectionType, MultipleTextHolder.from(collect));
                        }
                    } else {
                        resume.getSections().remove(sectionType);
                    }
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    List<Establishment> establishments = new LinkedList<>();
                    for (int i = 0; i < reqSection.length; i++) {
                        String establishmentTitle = reqSection[i];
                        if (!establishmentTitle.isEmpty()) {
                            String[] positions = req.getParameterValues(sectionType + "-position-" + i);
                            String[] descriptions = req.getParameterValues(sectionType + "-description-" + i);
                            String[] startDates = req.getParameterValues(sectionType + "-start-" + i);
                            String[] endDates = req.getParameterValues(sectionType + "-end-" + i);
                            List<Establishment.Period> periods = new LinkedList<>();
                            for (int j = 0; j < positions.length; j++) {
                                if (!positions[j].isEmpty()) {
                                    String[] positionDescription = descriptions[j].split("(\r\n|\n)+");
                                    if (positionDescription.length > 0 && positionDescription[0].equals(""))
                                        positionDescription = null;
                                    periods.add(Establishment.Period.of(startDates[j], endDates[j], positions[j], positionDescription));
                                }
                            }
                            if (periods.size() > 0) {
                                establishments.add(Establishment.of(establishmentTitle, periods.toArray(new Establishment.Period[0])));
                            }
                        }
                    }
                    if (establishments.size() > 0) {
                        resume.addSection(sectionType, EstablishmentHolder.from(establishments));
                    } else {
                        resume.getSections().remove(sectionType);
                    }
                    break;
            }
        }

        if (isNewResume) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        resp.sendRedirect("resume?id=" + resume.getId() + "&action=view");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        storage = ResumeServletConfig.getStorage();
    }
}
