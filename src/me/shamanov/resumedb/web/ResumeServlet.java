package me.shamanov.resumedb.web;

import me.shamanov.resumedb.model.Resume;
import me.shamanov.resumedb.storage.Storage;
import me.shamanov.resumedb.storage.XMLFileStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

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
            case "view":
            case "edit":
                resume = storage.load(id);
                logger.info("Resume#id: " + id + " has been loaded!");
                break;
            case "delete":
                storage.delete(id);
                logger.info("Resume#id: " + id + " has been deleted!");
                resp.sendRedirect("/resumedb");
                return;
            default:
                throw new IllegalArgumentException("Action command is invalid!");
        }

        String forwardPage = action.equals("view") ? "WEB-INF/jsp/view.jsp" : "WEB-INF/jsp/edit.jsp";

        req.setAttribute("resume", resume);
        req.getRequestDispatcher(forwardPage).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        super.init();
        storage = new XMLFileStorage("D:\\Java\\projects\\resumedb\\filestorage\\xml");
    }
}
