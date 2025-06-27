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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import service.ClassService;
import model.Class;
import model.Group;
import model.Milestone;
import model.Requirement;
import model.Setting;
import model.SubjectSetting;
import model.Team;
import service.CommonService;
import service.GroupService;
import service.MilestoneService;
import service.RequirementService;
import service.SettingService;
import service.SubjectSettingService;
import service.TeamService;
import service.UserService;

/**
 *
 * @author Do Duan
 */
@WebServlet(urlPatterns = {"/dashboard", "/class", "/class/detail", "/class/new", "/class/configs", "/class/changeStatus", "/dashboard/detail"})
public class ClassController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CommonService commonService;
    private ClassService classService;
    private SettingService settingService;
    private GroupService groupService;
    private MilestoneService milestoneService;
    private UserService userService;
    private TeamService teamService;
    private RequirementService requirementService;
    private SubjectSettingService subjectSettingService;
    private static final String jspForm = "/WEB-INF/view/class/form.jsp";
    private static final String jspList = "/WEB-INF/view/class/list.jsp";

    public void init() {
        commonService = new CommonService();
        classService = new ClassService();
        settingService = new SettingService();
        groupService = new GroupService();
        milestoneService = new MilestoneService();
        userService = new UserService();
        teamService = new TeamService();
        requirementService = new RequirementService();
        subjectSettingService = new SubjectSettingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/dashboard":
                showDashboard(request, response);
                break;

            case "/class":
                showClassList(request, response);
                break;

            case "/class/detail":
                showClassDetail(request, response);
                break;

            case "/class/configs": {
                try {
                    showClassConfigs(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(ClassController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;

            case "/class/changeStatus":
                changeStatus(request, response);
                break;

            case "/dashboard/detail":
                showDashboardDetail(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        String action = request.getServletPath();
        switch (action) {
//            case "/class/changeStatus":
//                changeStatus(request, response);
//                break;
        }
        doGet(request, response);
    }

    // dashboard
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

//         Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();

        List<Setting> semesterList = settingService.getSettingsByType("Semester", 1);
        List<Group> subjectList = groupService.getSubjectList();

        request.setAttribute("semesterList", semesterList);
        request.setAttribute("subjectList", subjectList);

        try {
            int selectedSemesterID = Integer.parseInt(request.getParameter("semesterID")); // Nhận semesterID từ JSP
            request.setAttribute("selectedSemesterID", selectedSemesterID); // Lưu trữ giá trị selectedSemesterId trong request

            int selectedSubjectID = Integer.parseInt(request.getParameter("subjectID")); // Nhận semesterID từ JSP
            request.setAttribute("selectedSubjectID", selectedSubjectID); // Lưu trữ giá trị selectedSemesterId trong request

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
//            // Phân trang
//            int page = 1;
            int pageSize = 6; // Số lượng bản ghi mỗi trang

            String page_raw = request.getParameter("page");
            int page = commonService.getPageNumber(page_raw);
            int offset = commonService.getOffset(page, pageSize);

            String semesterID = null;
            String subjectID = null;
            String search = null;
            try {
                semesterID = request.getParameter("semesterID");
                subjectID = request.getParameter("subjectID");
                search = request.getParameter("searchString");

                // Lấy tổng số bản ghi (tổng số lớp học)
                int totalRecords = classService.getTotalRecords(userID, semesterID, subjectID, search);

                // Tính tổng số trang
                int totalPages = commonService.getTotalPage(totalRecords, pageSize);

                // Gửi totalPages sang JSP
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("currentPage", page);

                if (currentUser.getRoleID() == 1) {
                    List<Class> classList = classService.getClassList(userID, semesterID, subjectID, search, pageSize, offset);
                    request.setAttribute("classList", classList);
                } else if (currentUser.getRoleID() == 2) {
                    List<Class> classList = classService.getClassTeacher(userID, semesterID, subjectID, search, pageSize, offset);
                    request.setAttribute("classList", classList);
                } else {
                    List<Class> classList = classService.getAllClass(semesterID, subjectID, "1", search, pageSize, offset, "name", "ASC");
                    request.setAttribute("classList", classList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

// Lấy danh sách lớp học từ service với offset và pageSize
// Gửi classList và các thông tin liên quan đến trang tới JSP
        // Chuyển tiếp đến trang dashboard
//        request.setAttribute("user", currentUser); // Gửi thông tin người dùng đến JSP
        request.getRequestDispatcher("/WEB-INF/view/class/dashboard.jsp").forward(request, response);
    }

    // class
    private void showClassList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

//         Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();

        List<Setting> semesterList = settingService.getSettingsByType("Semester");
        List<Group> subjectList = groupService.getSubjectListByUserID(userID);

        request.setAttribute("semesterList", semesterList);
        request.setAttribute("subjectList", subjectList);

        try {
            int selectedSemesterID = Integer.parseInt(request.getParameter("semesterID")); // Nhận semesterID từ JSP
            request.setAttribute("selectedSemesterID", selectedSemesterID); // Lưu trữ giá trị selectedSemesterId trong request

            int selectedSubjectID = Integer.parseInt(request.getParameter("subjectID")); // Nhận semesterID từ JSP
            request.setAttribute("selectedSubjectID", selectedSubjectID); // Lưu trữ giá trị selectedSemesterId trong request

            int selectedStatus = Integer.parseInt(request.getParameter("status"));
            request.setAttribute("selectedStatus", selectedStatus);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        try {
            // Phân trang
            int page = 1;
            int pageSize = 6; // Số lượng bản ghi mỗi trang

            // Lấy tham số trang nếu có từ request
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException e) {
                    page = 1; // Nếu không hợp lệ, mặc định là trang 1
                }
            }

            // Tính offset (điểm bắt đầu lấy dữ liệu)
            int offset = (page - 1) * pageSize;

            String semesterID = null;
            String subjectID = null;
            String status = null;
            String search = null;
            try {
                semesterID = request.getParameter("semesterID");
                subjectID = request.getParameter("subjectID");
                status = request.getParameter("status");
                search = request.getParameter("searchString");

                if (currentUser.getRoleID() == 5) {
                    // Lấy tổng số bản ghi (tổng số lớp học)
                    int totalRecords = classService.getTotalClassByDept(userID, semesterID, subjectID, status, search);

                    // Tính tổng số trang
                    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                    // Gửi totalPages sang JSP
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);

                    List<Class> classList = classService.getClassListByDeptUserID(userID, semesterID, subjectID,
                            status, search, 6, offset, sortBy, sortOrder);
                    request.setAttribute("classList", classList);
                } else if (currentUser.getRoleID() == 2) {
                    // class teacher

                    // Lấy tổng số bản ghi (tổng số lớp học)
                    int totalRecords = classService.getTotalClassByTeacher(userID, semesterID, subjectID, status, search);

                    // Tính tổng số trang
                    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                    // Gửi totalPages sang JSP
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);

                    List<Class> classList = classService.getClassListByTeacherID(userID, semesterID, subjectID,
                            status, search, 6, offset, sortBy, sortOrder);
                    request.setAttribute("classList", classList);
                } else {
                    // Lấy tổng số bản ghi (tổng số lớp học)
                    int totalRecords = classService.getTotalClass();
                    // Tính tổng số trang
                    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                    // Gửi totalPages sang JSP
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);

                    List<Class> classList = classService.getAllClass(semesterID, subjectID, status, search,
                            6, offset, sortBy, sortOrder);

                    request.setAttribute("classList", classList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher(jspList).forward(request, response);
    }

    // class/configs
    private void showClassConfigs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        int userID = currentUser.getId();

        // Lấy classID từ request nếu có
        String classID = request.getParameter("classID");

        int subjectID = 0;
        if (classID != null && !classID.equalsIgnoreCase("")) {
            subjectID = groupService.getSubjectByClassID(classID).getId();

        }
        String subjectID_raw = request.getParameter("subjectID");

        if (subjectID_raw != null) {
            subjectID = Integer.parseInt(subjectID_raw);
        }

        // Lấy danh sách subject theo userID
        List<Group> subjectList = groupService.getSubjectListByUserID(userID);
        request.setAttribute("subjectList", subjectList);

        // Lấy danh sách class theo subjectID và userID
        List<Class> classList = classService.getClassListByUserID(userID, subjectID);

        // Chuyển các giá trị đã chọn sang JSP
        request.setAttribute("selectedSubjectID", subjectID);
        if (classID == null && !classList.isEmpty()) {
            classID = classList.get(0).getId() + "";
            request.setAttribute("selectedClassID", classList.get(0).getId());
        } else if (classList.isEmpty()) {
            request.setAttribute("emptyClassListMess", "Class list is empty");
        }
        session.setAttribute("selectedClassID", classID);
        request.setAttribute("classList", classList);

        // Milestone Content Start
        List<Milestone> milestoneList = milestoneService.getMilestoneListByClassID(classID);
        request.setAttribute("milestoneList", milestoneList);
        // Milestone Content End

        // Team Content Start
        List<Team> teams;
        String activeTab = request.getParameter("activeTab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "milestones"; // Default to the milestones tab if no tab is specified
        }

        // Get parameters from the request
        String searchQuery = request.getParameter("search");
        teams = teamService.getTeamsByClassId(classID, searchQuery);
        // Set attributes for the view
        request.setAttribute("teams", teams);
        request.setAttribute("classID", classID);
        request.setAttribute("activeTab", activeTab);
        // Team Content End

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("/WEB-INF/view/class/configs.jsp").forward(request, response);
    }

    // /class/changeStatus
    private void changeStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int classID = Integer.parseInt(request.getParameter("id"));
        int newStatus = Integer.parseInt(request.getParameter("newStatus"));

        classService.updateStatusClass(classID, newStatus);
        request.setAttribute("Mess", "Class has been " + (newStatus == 1 ? "activated" : "deactivated") + ".");

        showClassList(request, response);

//        request.getRequestDispatcher("/class").forward(request, response);
    }

    //    /class/detail
    private void showClassDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

//         Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();

        List<Group> subjectList = groupService.getSubjectListByUserID(userID);
        List<Setting> semeterList = settingService.getSettingsByType("Semester", 1);

        request.setAttribute("subjectList", subjectList);
        request.setAttribute("semeterList", semeterList);

        switch (action) {
            case "New":
                addNewClass(request, response);
                break;
            case "Edit":
                editClass(request, response);
                break;
            case "Save":
                saveEdit(request, response);
                break;
            case "SaveNew":
                saveNewClass(request, response);
                break;
        }
    }

    private void addNewClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "Add new");

        request.getRequestDispatcher(jspForm).forward(request, response);
    }

    private void saveNewClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String className = request.getParameter("name");
            String subjectId = request.getParameter("subject");
            String semeterID = request.getParameter("semester");
            String status = request.getParameter("status");
            String detail = request.getParameter("detail");

//            Validate
            if (classService.checkClassIsExist(className, subjectId, semeterID)) {
                request.setAttribute("errMess", "The class already exists this semester. Please rename the class or choose another subject or semester.");
                request.setAttribute("className", className);
                request.getRequestDispatcher("/class/detail?action=New").forward(request, response);
                return;
            } else {
//                request.setAttribute("addSuccessMess", this);
                boolean isSuccess = classService.addClass(className, detail, status, semeterID, subjectId);
                int classID = classService.getClassID(className, subjectId, semeterID);
                milestoneService.cloneMilestoneToClass(classID + "");
                if (isSuccess) {
                    request.getSession().setAttribute("messSucces", "Add successfully");
                } else {
                    request.getSession().setAttribute("messFail", "Add fail");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/class").forward(request, response);

    }

    private void editClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "Edit");

        int classID = Integer.parseInt(request.getParameter("id"));
        Class currentClass = classService.searchClassByID(classID);
        request.setAttribute("currentClass", currentClass);

        request.getRequestDispatcher(jspForm).forward(request, response);
    }

    private void saveEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int classID = Integer.parseInt(request.getParameter("id"));
        try {
            String className = request.getParameter("name");
            int subjectId = Integer.parseInt(request.getParameter("subject"));
            int semeterID = Integer.parseInt(request.getParameter("semester"));
            int status = Integer.parseInt(request.getParameter("status"));
            String detail = request.getParameter("detail");

            request.setAttribute("selectedSubjectID", subjectId);
//            Validate
            if (classService.checkClassIsExist(className, subjectId + "", semeterID + "")) {
                request.setAttribute("errMess", "The class already exists this semester. Please rename the class or choose another subject or semester.");
                request.setAttribute("className", className);
            } else {
                Class updatedClass = new Class();
                updatedClass.setId(classID);
                updatedClass.setName(className);
                updatedClass.setDetail(detail);
                updatedClass.setSemesterID(semeterID);
                updatedClass.setGroupID(subjectId);
                updatedClass.setStatus(status);

                if (classService.updateClass(updatedClass)) {
                    request.getSession().setAttribute("messSucces", "Update successfully");
                } else {
                    request.getSession().setAttribute("messFail", "Update fail");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/class");
//        request.getRequestDispatcher("/class").forward(request, response);
    }

    // /dashboard/detail
    private void showDashboardDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy giá trị của tab từ URL, mặc định là "personal" nếu không có tab nào được chọn
        String selectedTab = request.getParameter("tab");
        if (selectedTab == null || selectedTab.isEmpty()) {
            selectedTab = "team"; // Tab mặc định là "personal"
        }
        request.setAttribute("selectedTab", selectedTab);

        // Message
        String mess = (String) request.getAttribute("Message");
        request.setAttribute("Message", mess);

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

//         Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            return;
        }
        int userID = currentUser.getId();
        session.removeAttribute("classID");
        int classID = Integer.parseInt(request.getParameter("id"));
        session.setAttribute("classID", classID);
        request.setAttribute("classID", classID);

        // Filter
        int subjectID = 2; // SWP
        List<SubjectSetting> complexities = subjectSettingService.getSubjectSettingByType(subjectID, "Complexity", 10, 0);
        request.setAttribute("complexities", complexities);

        // Team members
        Team t = teamService.getTeam(classID, userID);

        int teamID = 0;
        if (t != null) {
            teamID = t.getId();
            request.setAttribute("teamID", teamID);
        }
        // Team Allocation
        int teamAllocation = teamService.getTeamAllocation(userID, teamID);
        request.setAttribute("teamRole", teamAllocation);

        List<User> teamMember = teamService.getTeamMembers(teamID);
        request.setAttribute("teamMember", teamMember);

        Class c = classService.searchClassByID(classID);
        request.setAttribute("className", c.getName());
        request.setAttribute("subject", c.getSubjectCode());

        int pageSize = 8; // Số lượng bản ghi mỗi trang

        // Class req
        if (currentUser.getRoleID() != 1) {
            request.setAttribute("class", selectedTab);
            // Filter
            List<Team> teams = teamService.getTeamsByClassId(classID + "", "");
            request.setAttribute("teams", teams);

            List<Milestone> milestones = milestoneService.getMilestoneListByClassID(classID + "");
            request.setAttribute("milestones", milestones);

            String teamIdfilter = request.getParameter("teamID");
            request.setAttribute("teamIdFilter", teamIdfilter);

            String milestone = request.getParameter("milestone");
            request.setAttribute("milestone", milestone);

            String status = request.getParameter("status");
            request.setAttribute("status_s", status);

            String searchStr = request.getParameter("searchString");
            request.setAttribute("searchStr_s", searchStr);

            // Paging
            String page_raw = request.getParameter("page");
            int page = commonService.getPageNumber(page_raw);
            int offset = commonService.getOffset(page, pageSize);

            // Lấy tổng số bản ghi 
            int totalRecords = requirementService.getTotalRecordClassRequirement(classID, teamIdfilter, milestone, status, searchStr);
            // Tính tổng số trang
            int totalPages = commonService.getTotalPage(totalRecords, pageSize);

            // Gửi totalPages sang JSP
            request.setAttribute("totalPagesPerson", totalPages);
            request.setAttribute("currentPagePerson", page);

            requirementService.getClassRequirement(classID, teamIdfilter, milestone, status, searchStr, pageSize, offset);

        } // Personal Requirement
        else if (selectedTab.equals("personal")) {
            String complexityID = request.getParameter("complexity");
            request.setAttribute("complexityID_s", complexityID);

            String status = request.getParameter("status");
            request.setAttribute("status_s", status);

            String searchStr = request.getParameter("searchString");
            request.setAttribute("searchStr_s", searchStr);

            // Paging
            String page_raw = request.getParameter("page");
            int page = commonService.getPageNumber(page_raw);
            int offset = commonService.getOffset(page, pageSize);

            // Lấy tổng số bản ghi 
            int totalRecords = requirementService.getTotalRecordPersonal(classID, userID, teamID, complexityID, status, searchStr);

            // Tính tổng số trang
            int totalPages = commonService.getTotalPage(totalRecords, pageSize);

            // Gửi totalPages sang JSP
            request.setAttribute("totalPagesPerson", totalPages);
            request.setAttribute("currentPagePerson", page);

            List<Requirement> requirementList = requirementService.getRequirement(classID, userID, teamID, complexityID, status, searchStr, pageSize, offset);
            request.setAttribute("requirementList", requirementList);

            List<Requirement> teamRequirements = requirementService.getTeamRequirements(teamID, "0", userID + "", "-1", "", pageSize, 0);
            request.setAttribute("teamRequirements", teamRequirements);

        } else {  // Team's Requirement
            String complexityID = request.getParameter("complexity");
            request.setAttribute("complexityID_s", complexityID);

            String ownerID = request.getParameter("owner");
            request.setAttribute("ownerID_s", ownerID);

            String status = request.getParameter("status");
            request.setAttribute("status_s", status);

            String searchStr = request.getParameter("searchString");
            request.setAttribute("searchStr_s", searchStr);

            // Paging
            // Số lượng bản ghi mỗi trang
            String page_raw = request.getParameter("page");
            int page = commonService.getPageNumber(page_raw);
            int offset = commonService.getOffset(page, pageSize);

            // Lấy tổng số bản ghi 
            int totalRecordsTeam = requirementService.getTotalRecordTeam(teamID, complexityID, ownerID, status, searchStr);

            // Tính tổng số trang
            int totalPagesTeam = commonService.getTotalPage(totalRecordsTeam, pageSize);

            // Gửi totalPages sang JSP
            request.setAttribute("totalPages", totalPagesTeam);
            request.setAttribute("currentPage", page);

            List<Requirement> teamRequirements = requirementService.getTeamRequirements(teamID, complexityID, ownerID, status, searchStr, pageSize, offset);
            request.setAttribute("teamRequirements", teamRequirements);

            List<Requirement> requirementList = requirementService.getRequirement(classID, userID, teamID, "0", "-1", "", pageSize, 0);
            request.setAttribute("requirementList", requirementList);

        }
        request.getRequestDispatcher("/WEB-INF/view/class/detail.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
