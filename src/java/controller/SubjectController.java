package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.Assignment;
import model.EvalCriteria;
import model.Group;
import model.SubjectSetting;
import service.AssignmentService;
import service.EvalCriteriaService;
import service.GroupService;
import service.SubjectSettingService;

@WebServlet({"/subject", "/subject/add", "/subject/edit", "/subject/save", "/subject/status"})
public class SubjectController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int RECORDS_PER_PAGE = 5;

    private final String jspList = "/WEB-INF/view/group/configs.jsp";
    private final String jspForm = "/WEB-INF/view/subject/detail.jsp";

    private SubjectSettingService subjectSettingService;
    private GroupService groupService;
    private EvalCriteriaService criteriaService;
    private AssignmentService assignmentService;

    public void init() {
        subjectSettingService = new SubjectSettingService();
        groupService = new GroupService();
        criteriaService = new EvalCriteriaService();
        assignmentService = new AssignmentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/subject":
                showsubjectList(request, response);
                break;
            case "/subject/add":
                showAddSubjectSetting(request, response);
                break;
            case "/subject/edit":
                showEditForm(request, response);
                break;
            case "/subject/save":
                save(request, response);
                break;
            case "/subject/status":
                changeStatus(request, response);
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

    private void showsubjectList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String typeFilter = request.getParameter("typeFilter");
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("statusFilter");
        int groupId = Integer.parseInt(request.getParameter("id"));
        String activeTab = request.getParameter("activeTab");

        if (activeTab == null) {
            activeTab = "subjectsetting"; // Tab mặc định nếu không có tab nào được chọn
        }
        session.setAttribute("activeTab", activeTab);

        session.setAttribute("groupId", groupId);
        int totalRecords = 5;
        int currentPage = 1;
        if (request.getParameter("page") != null) {
            currentPage = Integer.parseInt(request.getParameter("page"));
        }
        int offset = (currentPage - 1) * RECORDS_PER_PAGE;

        List<SubjectSetting> subjectSettingList;

        // In ra các giá trị lọc
        System.out.println("Filtering Parameters:");
        System.out.println("typeFilter: " + typeFilter);
        System.out.println("statusFilter: " + statusFilter);
        System.out.println("searchQuery: " + searchQuery);
        System.out.println("groupId: " + groupId);

        // Filtering and searching
        if (searchQuery != null && !searchQuery.isEmpty()) {
            System.out.println("Executing search by name...");
            subjectSettingList = subjectSettingService.searchSubjectSettingByName(groupId, searchQuery, RECORDS_PER_PAGE, offset);
            totalRecords = subjectSettingService.countSubjectSettingsByName(groupId, searchQuery);
        } else if (typeFilter != null && !typeFilter.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
            boolean status = Boolean.parseBoolean(statusFilter);
            System.out.println("Filtering by type and status...");
            System.out.println("typeFilter: " + typeFilter + ", statusFilter: " + status);
            subjectSettingList = subjectSettingService.getSubjectSettingsFilter(groupId, typeFilter, status, offset, RECORDS_PER_PAGE);
            totalRecords = subjectSettingService.countSubjectSettingsByTypeAndStatus(groupId, typeFilter, status);
        } else if (typeFilter != null && !typeFilter.isEmpty()) {
            System.out.println("Filtering by type only...");
            subjectSettingList = subjectSettingService.getSubjectSettingByType(groupId, typeFilter, RECORDS_PER_PAGE, offset);
            totalRecords = subjectSettingService.countSubjectSettingsByType(groupId, typeFilter);
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            boolean status = Boolean.parseBoolean(statusFilter);
            System.out.println("Filtering by status only...");
            subjectSettingList = subjectSettingService.getSubjectSettingByStatus(groupId, status, RECORDS_PER_PAGE, offset);
            totalRecords = subjectSettingService.countSubjectSettingsByStatus(groupId, status);
        } else {
            System.out.println("No filters applied, retrieving all settings...");
            subjectSettingList = subjectSettingService.getSubjectSettings(groupId, RECORDS_PER_PAGE, offset);
            totalRecords = subjectSettingService.countSubjectSettingsByGroupId(groupId);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);

        // Số bản ghi trên mỗi trang cho criteriaList
        int recordsPerPage = 5;
        int criteriaCurrentPage = 1;
        if (request.getParameter("criteriaPage") != null) {
            criteriaCurrentPage = Integer.parseInt(request.getParameter("criteriaPage"));
        }
        int criteriaOffset = (criteriaCurrentPage - 1) * recordsPerPage;
        // Nhận tham số tìm kiếm và lọc từ request
        String searchName = request.getParameter("searchName");
        String statusFilterCriteria = request.getParameter("statusFilterCriteria");
        String assignmentIdParam = request.getParameter("assignmentFilter");

        List<EvalCriteria> criteriaList;
        int criteriaTotalRecords;

        // Logic xử lý tìm kiếm và lọc
        if (searchName != null && !searchName.trim().isEmpty()) {
            // Nếu có tìm kiếm theo tên
            criteriaList = criteriaService.searchByName(searchName, recordsPerPage, criteriaOffset);
            criteriaTotalRecords = criteriaService.countSearchByName(searchName); // Bạn cần thêm phương thức này để đếm tổng số bản ghi
        } else if (statusFilterCriteria != null) {
            // Nếu chỉ lọc theo trạng thái
            boolean status = Boolean.parseBoolean(statusFilterCriteria);
            criteriaList = criteriaService.filterByStatus(status, recordsPerPage, criteriaOffset);
            criteriaTotalRecords = criteriaService.countByStatus(status); // Bạn cần thêm phương thức này để đếm tổng số bản ghi
        } else {
            // Mặc định: Lấy tất cả dữ liệu với phân trang
            criteriaList = criteriaService.getEvalCriteriaByPage(groupId, recordsPerPage, criteriaOffset);
            criteriaTotalRecords = criteriaService.countEvalCriteriaByGroupId(groupId);
        }
        int criteriaTotalPages = (int) Math.ceil((double) criteriaTotalRecords / RECORDS_PER_PAGE);

        request.setAttribute("criteriaList", criteriaList);
        request.setAttribute("criteriaTotalPages", criteriaTotalPages);
        request.setAttribute("criteriaCurrentPage", criteriaCurrentPage);
        request.setAttribute("searchName", searchName); // Để hiển thị lại giá trị tìm kiếm (nếu có)
        request.setAttribute("statusParam", statusFilterCriteria); // Để hiển thị lại giá trị lọc trạng thái (nếu có)
        request.setAttribute("assignmentIdParam", assignmentIdParam); // Để hiển thị lại giá trị lọc assignmentId (nếu có)

        // Set attributes for the request
        request.setAttribute("subjectSettingList", subjectSettingList);
        request.setAttribute("typeFilter", typeFilter);
        List<String> types = subjectSettingService.getAllTypes();
        request.setAttribute("types", types);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("subjectId", groupId);

        int subjectId = -1;
        String subjectIdStr = request.getParameter("id");
        if (subjectIdStr != null && !subjectIdStr.isEmpty()) {
            try {
                subjectId = Integer.parseInt(subjectIdStr);
            } catch (NumberFormatException e) {
                subjectId = -1;
            }
        }
        List<Assignment> assignmentList = assignmentService.getAssignmentsBySubjectId(subjectId);

        if (assignmentList.isEmpty()) {
            request.setAttribute("message", "No assignments found for this subject.");
        } else {
            request.setAttribute("assignmentList", assignmentList);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/group/configs.jsp");
        dispatcher.forward(request, response);
    }

    private void showAddSubjectSetting(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer groupId = (Integer) session.getAttribute("groupId");
        Map<String, String> messages = subjectSettingService.getMessages();
        req.setAttribute("messages", messages);
        if (groupId != null) {
            Group group = groupService.getGroupById(groupId);

            if (group != null && group.getName() != null) {
                req.setAttribute("groupName", group.getName());
            } else {
                req.setAttribute("errorMessage", "Group Name not found.");
            }
        } else {
            req.setAttribute("errorMessage", "Group ID not found.");
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer groupId = (Integer) session.getAttribute("groupId");
        int id = Integer.parseInt(req.getParameter("id"));
        SubjectSetting subId = subjectSettingService.getSubjectSettingById(id);
        req.setAttribute("subId", subId);

        if (groupId != null) {
            Group group = groupService.getGroupById(groupId);

            if (group != null && group.getName() != null) {
                req.setAttribute("groupName", group.getName());
            } else {
                req.setAttribute("errorMessage", "Group Name not found.");
            }
        } else {
            req.setAttribute("errorMessage", "Group ID not found.");
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private SubjectSetting getInputForm(HttpServletRequest request) {
        SubjectSetting inputSubjectSetting = new SubjectSetting();

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            inputSubjectSetting.setId(Integer.parseInt(idParam));
        }
        inputSubjectSetting.setName(request.getParameter("name"));
        inputSubjectSetting.setType(request.getParameter("type"));

        String valueParam = request.getParameter("value");
        if (valueParam != null && !valueParam.isEmpty()) {
            inputSubjectSetting.setValue(Integer.parseInt(valueParam));
        } else {
            inputSubjectSetting.setValue(0);
        }

        String status = request.getParameter("status");
        inputSubjectSetting.setStatus("1".equals(status));

        String subjectIdParam = request.getParameter("subjectId");
        if (subjectIdParam != null && !subjectIdParam.isEmpty()) {
            inputSubjectSetting.setSubjectId(Integer.parseInt(subjectIdParam));
        } else {
            System.out.println("subjectId is not provided or invalid");
        }

        System.out.println("Adding subject setting: " + inputSubjectSetting);
        return inputSubjectSetting;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Integer groupId = (Integer) session.getAttribute("groupId");
        SubjectSetting subjectSetting = getInputForm(request);
        boolean result;
        if (subjectSetting.getId() == 0) {
            result = subjectSettingService.addSubjectSetting(subjectSetting);
            request.getSession().setAttribute("Message", "Successfully added!");
        } else {
            result = subjectSettingService.editSubjectSetting(subjectSetting);
            request.getSession().setAttribute("Message", "Updated successfully!");
        }
        if (!result) {
            Map<String, String> messages = subjectSettingService.getMessages();
            request.setAttribute("subjectSetting", subjectSetting);
            request.setAttribute("messages", messages);

            response.sendRedirect(request.getContextPath() + "/subject/add");
        } else {
            response.sendRedirect(request.getContextPath() + "/subject?id=" + groupId);
        }
    }

    private void changeStatus(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("idStatus"));
        int newStatus = Integer.parseInt(req.getParameter("newStatus"));
        int groupId = Integer.parseInt(req.getParameter("id"));
        String activeTab = req.getParameter("activeTab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "subjectsetting"; // Tab mặc định
        }
        subjectSettingService.updateSubjectSettingStatus(id, newStatus);
        req.getSession().setAttribute("Message", "Subject has been " + (newStatus == 1 ? "activated" : "deactivated") + ".");
        String redirectUrl = req.getContextPath() + "/subject?id=" + groupId + "&activeTab=" + activeTab;
        res.sendRedirect(redirectUrl);
    }
}
