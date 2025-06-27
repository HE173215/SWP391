/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import model.ReviewCouncil;
import model.User;
import service.ReviewCouncilService;

/**
 *
 * @author admin
 */
@WebServlet({"/council", "/council/add", "/council/edit", "/council/save", "/council/status", "/council/details"})
public class ReviewCouncilController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jsplist = "/WEB-INF/view/councils/list.jsp";
    private final String jspdetails = "/WEB-INF/view/councils/details.jsp";
    private final String jspview = "/WEB-INF/view/councils/view.jsp";

    private ReviewCouncilService councilService;

    public void init() {
        councilService = new ReviewCouncilService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
//        if ((action.equals("/council/add") || action.equals("/council/edit") || action.equals("/council/status")) && !isSubjectManager(currentUser)) {
//            forwardToErrorPage(request, response, "Bạn không có quyền truy cập vào chức năng này.");
//            return;
//        }
        if (currentUser == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/common/accessDenied.jsp");
            dispatcher.forward(request, response);
            return;
        }
        switch (action) {
            case "/council":
                showCouncilsList(request, response);
                break;
            case "/council/add":
                showAddCouncils(request, response);
                break;
            case "/council/edit":
                showEditCouncils(request, response);
                break;
            case "/council/save":
                save(request, response);
                break;
            case "/council/status":
                activateCouncils(request, response);
                break;
            case "/council/details":
                showCouncilDetails(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        doGet(request, response);
    }

    private boolean isSubjectManager(User user) {
        return user != null && "Subject Manager".equals(user.getRole()) || "Admin".equals(user.getRole());
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/team/ErrorPage.jsp");
        dispatcher.forward(req, res);
    }

    private void showCouncilsList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;

        // Lấy tham số trang nếu có
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        String classesFilter = request.getParameter("classesFilter");
        String subjectFilter = request.getParameter("subjectFilter");
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("statusFilter");

        String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "id";
        String sortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "ASC";

        List<ReviewCouncil> councilsList;
        int totalRecords;

        if (statusFilter != null && !statusFilter.isEmpty()) {
            boolean status = Boolean.parseBoolean(statusFilter);
            councilsList = councilService.getCouncilsByStatus(status, offset, pageSize);
            totalRecords = councilService.countCouncilsByStatus(status);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            councilsList = councilService.searchCouncilsByName(searchQuery, offset, pageSize);
            totalRecords = councilService.countCouncilsByName(searchQuery);
        } else if (classesFilter != null && !classesFilter.isEmpty()) {
            councilsList = councilService.getCouncilsByClass(classesFilter, offset, pageSize);
            totalRecords = councilService.countCouncilsByClass(classesFilter);
        } else if (subjectFilter != null && !subjectFilter.isEmpty()) {
            councilsList = councilService.getCouncilsBySubject(subjectFilter, offset, pageSize);
            totalRecords = councilService.countCouncilsBySubject(subjectFilter);
        } else {
            councilsList = councilService.getAllCouncils(offset, pageSize, sortBy, sortOrder);
            totalRecords = councilService.countAllCouncils();
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("councilsList", councilsList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", statusFilter);

        List<ReviewCouncil> classes = councilService.getAllClasses();
        request.setAttribute("classes", classes);
        List<ReviewCouncil> subjects = councilService.getAllSubjects();
        request.setAttribute("subjects", subjects);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/councils/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showAddCouncils(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ReviewCouncil> classes = councilService.getAllClasses();
        request.setAttribute("classes", classes);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
        dispatcher.forward(request, response);
    }

    private void showEditCouncils(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ReviewCouncil councilByID = councilService.getCouncilsById(id);
        request.setAttribute("council", councilByID);

        List<ReviewCouncil> classes = councilService.getAllClasses();
        request.setAttribute("classes", classes);

        RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
        dispatcher.forward(request, response);
    }

    private ReviewCouncil getInputForm(HttpServletRequest request) {
        ReviewCouncil inputCouncil = new ReviewCouncil();
        inputCouncil.setName(request.getParameter("name"));
        inputCouncil.setDescription(request.getParameter("description"));
        String status = request.getParameter("status");
        inputCouncil.setStatus("1".equals(status));

        String createDateString = request.getParameter("createDate");
        if (createDateString != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date createDate = dateFormat.parse(createDateString);
                inputCouncil.setCreatedDate(createDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String classIdParam = request.getParameter("class_id");
        if (classIdParam != null && !classIdParam.isEmpty()) {
            inputCouncil.setClassId(Integer.parseInt(classIdParam));
        } else {
            inputCouncil.setClassId(0);
        }

        return inputCouncil;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        ReviewCouncil inputCouncil = getInputForm(request);

        Map<String, String> validationErrors = councilService.validateCouncilsInfo(
                inputCouncil.getName(),
                inputCouncil.getDescription(),
                inputCouncil.isStatus(),
                inputCouncil.getCreatedDate(),
                inputCouncil.getClassId()
        );

        if (!validationErrors.isEmpty()) {
            request.setAttribute("validationErrors", validationErrors);
            request.setAttribute("council", inputCouncil);
            List<ReviewCouncil> classes = councilService.getAllClasses();
            request.setAttribute("classes", classes);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
            return;
        }

        try {
            if (id == null || id.isEmpty()) {
                councilService.addCouncils(inputCouncil);
                request.getSession().setAttribute("Message", "Councils added successfully!");
            } else {
                inputCouncil.setId(Integer.parseInt(id));
                councilService.updateCouncils(inputCouncil);
                request.getSession().setAttribute("Message", "Councils updated successfully!");
            }
            response.sendRedirect(request.getContextPath() + "/council");
        } catch (Exception e) {
            request.setAttribute("ErrorMessage", "Error occurred: " + e.getMessage());
            request.setAttribute("council", inputCouncil);
            List<ReviewCouncil> classes = councilService.getAllClasses();
            request.setAttribute("classes", classes);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
        }

    }

    private void activateCouncils(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int councilId = Integer.parseInt(request.getParameter("id"));
        boolean status = Boolean.parseBoolean(request.getParameter("status"));
        councilService.updateCouncilStatus(councilId, status);
        request.getSession().setAttribute("Message", "Assgnment has been " + (status == true ? "activated" : "deactivated") + ".");
        response.sendRedirect(request.getContextPath() + "/council");
    }

    private void showCouncilDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ReviewCouncil councilByID = councilService.getCouncilsById(id);
        request.setAttribute("council", councilByID);
        List<ReviewCouncil> classes = councilService.getAllClasses();
        request.setAttribute("classes", classes);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspview);
        dispatcher.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
