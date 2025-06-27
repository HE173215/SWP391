/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Milestone;
import model.Requirement;
import model.SubjectSetting;
import model.User;
import service.MilestoneService;
import service.RequirementService;
import service.SubjectSettingService;
import service.TeamService;

/**
 *
 * @author Do Duan
 */
@WebServlet(name = "RequirementController", urlPatterns = {"/requirement", "/requirement/changeStatus", "/requirement/save"})
public class RequirementController extends HttpServlet {

    private static final String jspForm = "/WEB-INF/view/requirement/form.jsp";

    private RequirementService requirementService;
    private TeamService teamService;
    private SubjectSettingService subjectSettingService;
    private MilestoneService milestoneService;

    public void init() {
        requirementService = new RequirementService();
        teamService = new TeamService();
        subjectSettingService = new SubjectSettingService();
        milestoneService = new MilestoneService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/requirement":
                showRequirementDetail(request, response);
                break;

            case "/requirement/changeStatus":
                changeStatus(request, response);
                break;

            case "/requirement/save":
                save(request, response);
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

    // /requirement
    private void showRequirementDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Lấy giá trị của tab từ URL, mặc định là "personal" nếu không có tab nào được chọn
        String selectedTab = request.getParameter("tab");
        if (selectedTab == null || selectedTab.isEmpty()) {
            selectedTab = "team"; // Tab mặc định là "personal"
        }
        request.setAttribute("selectedTab", selectedTab);

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

//         Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();
        int teamID = 0;
        if (request.getParameter("teamID") != null) {
            teamID = Integer.parseInt(request.getParameter("teamID"));
            request.setAttribute("teamID", teamID);
        }
        int classID = (int) session.getAttribute("classID");

        // Team members
        List<User> teamMember = teamService.getTeamMembers(teamID);
        request.setAttribute("teamMember", teamMember);

        // Complexity
        int subjectID = 2; // SWP
        List<SubjectSetting> complexityList = subjectSettingService.getSubjectSettingByType(subjectID, "Complexity", 10, 0);
        request.setAttribute("complexityList", complexityList);

        // Milestone
        List<Milestone> milestoneList = milestoneService.getMilestoneListByClassID(classID + "");
        request.setAttribute("milestoneList", milestoneList);

        if (action.equals("Add")) {
            request.setAttribute("pageTitle", "Add New Requirement");
        } else if (action.equals("Edit")) {
            request.setAttribute("pageTitle", "Update Requirement");
            String id = request.getParameter("id");
            Requirement requirement = requirementService.getRequirementByID(id);
            request.setAttribute("requirement", requirement);
//            User member = requirementService.getMemberByRequirementID(id);
//            request.setAttribute("member", member);
            request.setAttribute("userID", userID);
        } else if (action.equals("View")) {
            request.setAttribute("pageTitle", "Requirement Detail");
        }

        request.setAttribute("action", action);
        request.getRequestDispatcher(jspForm).forward(request, response);
    }

    // /requirement/save
    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();

        // Lấy giá trị của tab từ URL, mặc định là "personal" nếu không có tab nào được chọn
        String selectedTab = request.getParameter("tab");
        if (selectedTab == null || selectedTab.isEmpty()) {
            selectedTab = "team"; // Tab mặc định là "personal"
        }
        request.setAttribute("selectedTab", selectedTab);

        String id = request.getParameter("id");
        String tittle = request.getParameter("tittle");
        String complexityID = request.getParameter("complexityF");
        String teamID = request.getParameter("teamID");
        String ownerID = request.getParameter("ownerF");
        String status = "0";

        if (request.getParameter("statusF") != null) {
            status = request.getParameter("statusF");
        }
        String description = request.getParameter("description");

        String milestone = request.getParameter("milestoneF");

        if (!requirementService.validateInput(tittle.trim())) {
            request.setAttribute("tittleInputed", tittle);
            request.setAttribute("action", action);
            request.getRequestDispatcher("/requirement").forward(request, response);
        }
        String currentO = request.getParameter("currentOwner");
        if (ownerID == null) {
            ownerID = currentO;
        }
        if (action.equals("Add")) {
            int result = requirementService.addNewRequirement(tittle, complexityID, teamID, ownerID, status, description, userID + "", milestone);
            if (result > 0) {
                request.getSession().setAttribute("reqMess", "Requirement added successfully!");
            }
        } else {

            int result = requirementService.updateRequirement(id, tittle, complexityID, teamID, ownerID, status, description, milestone);
            if (result > 0) {
                request.getSession().setAttribute("reqMess", "Requirement updated successfully!");
            }
        }
        int classID = (int) session.getAttribute("classID");

        request.getRequestDispatcher(
                "/dashboard/detail?id=" + classID).forward(request, response);
    }

    // requirement/changeStatus
    private void changeStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqID = request.getParameter("reqID");
        String newStatus = request.getParameter("newStatus");
        requirementService.updateStatus(reqID, newStatus);

        request.getRequestDispatcher(request.getContextPath() + "/class/detail");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
