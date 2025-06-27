/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Assignment;
import model.User;
import service.AssignmentService;

/**
 *
 * @author admin
 */
@WebServlet({"/assignment", "/assignment/add", "/assignment/edit", "/assignment/save", "/assignment/status"})
public class AssignmentController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jsplist = "/WEB-INF/view/assignment/list.jsp";
    private final String jspConfig = "/WEB-INF/view/group/configs.jsp";
    private final String jspdetails = "/WEB-INF/view/assignment/details.jsp";

    private AssignmentService assignmentService;

    public void init() {
        assignmentService = new AssignmentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/assignment":
                showAssignmentsList(request, response);
                break;
            case "/assignment/add":
                showAddAssignments(request, response);
                break;
            case "/assignment/edit":
                showEditAssignments(request, response);
                break;
            case "/assignment/save":
                save(request, response);
                break;
            case "/assignment/status":
                activateAssignments(request, response);
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

    // Check if the user is a teacher (role 4)
    private boolean isSubjectManager(User user) {
        String role = user.getRole();
        return "Subject Manager".equals(role) || "Admin".equals(role);
    }
    // Phương thức để chuyển hướng đến trang lỗi ErrorPage.jsp

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/team/ErropPage.jsp");
        dispatcher.forward(req, res);
    }

//    private void showAssignmentsList(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String codeFilter = request.getParameter("codeFilter");
//        String searchQuery = request.getParameter("searchQuery");
//        String statusFilter = request.getParameter("statusFilter");
//
//        String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "id";
//        String sortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "ASC";
//
//        String subjectIdParam = request.getParameter("subjectId");
//        List<Assignment> assignmentList;
//
//        if (subjectIdParam != null && !subjectIdParam.isEmpty()) {
//            int subjectId = Integer.parseInt(subjectIdParam);
//            // Lấy danh sách assignment theo subjectId
//            assignmentList = assignmentService.getAssignmentsBySubjectId(subjectId);
//        } else if (codeFilter != null && !codeFilter.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
//            boolean status = Boolean.parseBoolean(statusFilter);
//            assignmentList = assignmentService.getAssignmentsByCodeAndStatus(codeFilter, status);
//        } else if (codeFilter != null && !codeFilter.isEmpty()) {
//            assignmentList = assignmentService.getAssignmentsByCode(codeFilter);
//        } else if (statusFilter != null && !statusFilter.isEmpty()) {
//            boolean status = Boolean.parseBoolean(statusFilter);
//            assignmentList = assignmentService.getAssignmentsByStatus(status);
//        } else if (searchQuery != null && !searchQuery.isEmpty()) {
//            assignmentList = assignmentService.searchAssignmentsByName(searchQuery);
//        } else {
//            assignmentList = assignmentService.getAllAssignments(sortBy, sortOrder);
//        }
//
//        request.setAttribute("assignmentList", assignmentList);
//        request.setAttribute("statusFilter", statusFilter);
//        request.setAttribute("codeFilter", codeFilter);
//
//        List<Assignment> codes = assignmentService.getAllCodes();
//        request.setAttribute("codes", codes);
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher(jsplist);
//        dispatcher.forward(request, response);
//    }
    private void showAssignmentsList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String subjectIdStr = request.getParameter("id");
        int subjectId = -1;
        if (subjectIdStr != null && !subjectIdStr.isEmpty()) {
            try {
                subjectId = Integer.parseInt(subjectIdStr);
            } catch (NumberFormatException e) {
                subjectId = -1;
            }
        }

        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList = assignmentService.getAssignmentsBySubjectId(subjectId);
        if (assignmentList.isEmpty()) {
            request.setAttribute("message", "No assignments found for this subject.");
        } else {
            request.setAttribute("assignmentList", assignmentList);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(jsplist);
        dispatcher.forward(request, response);
    }

    private void showAddAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Assignment> codes = assignmentService.getAllCodes();
        request.setAttribute("codes", codes);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
        dispatcher.forward(request, response);
    }

    private void showEditAssignments(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Assignment assignmentByID = assignmentService.getAssignmentsById(id);
        req.setAttribute("assignmentByID", assignmentByID);

        List<Assignment> codes = assignmentService.getAllCodes();
        req.setAttribute("codes", codes);

        RequestDispatcher dispatcher = req.getRequestDispatcher(jspdetails);
        dispatcher.forward(req, res);
    }

    private Assignment getInputForm(HttpServletRequest request) {
        Assignment inputAssignment = new Assignment();
        inputAssignment.setName(request.getParameter("name"));
        inputAssignment.setWeight(Integer.parseInt(request.getParameter("weight")));
        inputAssignment.setDetail(request.getParameter("detail"));

        String finalAssignment = request.getParameter("final_assignment");
        inputAssignment.setFinal_assignment("1".equals(finalAssignment));

        String status = request.getParameter("status");
        inputAssignment.setStatus("1".equals(status));

        String groupIdParam = request.getParameter("group_id");
        if (groupIdParam != null && !groupIdParam.isEmpty()) {
            inputAssignment.setSubject_id(Integer.parseInt(groupIdParam));
        } else {
            inputAssignment.setSubject_id(0);
        }

        return inputAssignment;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        Assignment inputAssignment = getInputForm(request);

        Map<String, String> validationErrors = assignmentService.validateAssignmentInfo(
                inputAssignment.getName(),
                inputAssignment.getWeight(),
                inputAssignment.getDetail(),
                inputAssignment.isFinal_assignment(),
                inputAssignment.isStatus(),
                inputAssignment.getSubject_id()
        );

        if (!validationErrors.isEmpty()) {
            request.setAttribute("validationErrors", validationErrors);
            request.setAttribute("assignmentByID", inputAssignment);
            List<Assignment> codes = assignmentService.getAllCodes();
            request.setAttribute("codes", codes);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
            return;
        }

        try {
            if (id == null || id.isEmpty()) {
                assignmentService.addAssignment(inputAssignment);
                request.getSession().setAttribute("Message", "User has been successfully added!");
            } else {
                inputAssignment.setId(Integer.parseInt(id));
                assignmentService.updateAssignment(inputAssignment);
                request.getSession().setAttribute("Message", "Assignment updated successfully!");
            }
            response.sendRedirect(request.getContextPath() + "/subject?id=" + inputAssignment.getSubject_id());
        } catch (Exception e) {
            request.setAttribute("ErrorMessage", "Error occurred: " + e.getMessage());
            request.setAttribute("assignmentByID", inputAssignment);
            List<Assignment> codes = assignmentService.getAllCodes();
            request.setAttribute("codes", codes);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
        }
    }

    private void activateAssignments(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        boolean status = Boolean.parseBoolean(request.getParameter("status"));
        assignmentService.updateAssignmentsStatus(assignmentId, status);
        request.getSession().setAttribute("Message", "Assgnment has been " + (status == true ? "activated" : "deactivated") + ".");
        request.getRequestDispatcher("/subject?id=" + request.getParameter("subject_id")).forward(request, response);
    }

}
