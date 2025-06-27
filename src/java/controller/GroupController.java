/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Group;
import model.User;
import service.GroupService;

/**
 *
 * @author Do Duan
 */
@WebServlet({"/group", "/group/new", "/group/edit", "/group/save", "/group/change-status"})
public class GroupController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jspList = "/WEB-INF/view/group/list.jsp";
    private final String jspForm = "/WEB-INF/view/group/form.jsp";

    private GroupService groupService;

    @Override
    public void init() {
        groupService = new GroupService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getServletPath();
        if (action.equals("/group/change-status")) {
            changeStatus(req, res);
        } else {
            save(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getServletPath();
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if ((action.equals("/group/new") || action.equals("/group/edit") || action.equals("/group/change-status")) && !isAdmin(currentUser)) {
            forwardToErrorPage(req, res, "Bạn không có quyền truy cập vào chức năng này.");
            return;
        }else if (currentUser == null) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/common/accessDenied.jsp");
            dispatcher.forward(req, res);
            return;
        }
        try {
            switch (action) {
                case "/group/new":
                    showNewForm(req, res);
                    break;
                case "/group/edit":
                    showEditForm(req, res);
                    break;
                case "/group/save":
                    save(req, res);
                    break;
                case "/group":
                    showList(req, res);
                    break;
                default:
                    res.sendRedirect("login");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    private boolean isAdmin(User user) {
        return user != null && "Subject Manager".equals(user.getRole()) || "Admin".equals(user.getRole());
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/team/ErrorPage.jsp");
        dispatcher.forward(req, res);
    }

    private void showList(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;

        // Lấy tham số trang nếu có
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; // Mặc định là trang 1 nếu có lỗi
            }
        }

        int offset = (page - 1) * pageSize;

        // Lấy tham số từ request
        String typeFilter = req.getParameter("typeFilter");
        String searchQuery = req.getParameter("searchQuery");
        String statusFilter = req.getParameter("statusFilter"); // Thêm lọc status

        List<Group> groupList;
        int totalRecords;

        // Lọc theo cả status và type nếu cả hai đều có giá trị
        if (statusFilter != null && !statusFilter.isEmpty() && typeFilter != null && !typeFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            groupList = groupService.filterGroups(typeFilter, statusFilter, offset, pageSize);
            totalRecords = groupService.countGroupsByFilters(typeFilter, statusFilter);
        } // Lọc theo status nếu chỉ có statusFilter
        else if (statusFilter != null && !statusFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            groupList = groupService.getGroupsByStatus(status, offset, pageSize);
            totalRecords = groupService.countGroupsByStatus(status);
        } // Lọc theo tìm kiếm nếu có searchQuery
        else if (searchQuery != null && !searchQuery.isEmpty()) {
            groupList = groupService.searchGroupByName(searchQuery, offset, pageSize);
            totalRecords = groupService.countGroupsByName(searchQuery);
        } // Lọc theo type nếu chỉ có typeFilter
        else if (typeFilter != null && !typeFilter.isEmpty()) {
            groupList = groupService.getGroupsByType(typeFilter, offset, pageSize);
            totalRecords = groupService.countGroupsByType(typeFilter);
        } // Nếu không có điều kiện lọc nào, lấy tất cả cài đặt
        else {
            groupList = groupService.getAllGroups(offset, pageSize);
            totalRecords = groupService.countAll();
        }

        // Tính toán tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Đặt các thuộc tính cho request để chuyển đến JSP
        req.setAttribute("groupList", groupList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("statusFilter", statusFilter); // Giữ lại giá trị statusFilter
        req.setAttribute("typeFilter", typeFilter); // Giữ lại giá trị typeFilter
        req.setAttribute("searchQuery", searchQuery); // Giữ lại giá trị searchQuery

        // Lấy danh sách loại cài đặt
        List<String> types = groupService.getAllTypes();
        req.setAttribute("types", types);
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspList);
        dispatcher.forward(req, res);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Group currentGroup = groupService.getGroupById(id);
        req.setAttribute("group", currentGroup);
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private Group getInputForm(HttpServletRequest req) {
        Group inputGroup = new Group();
        inputGroup.setCode(req.getParameter("code"));
        inputGroup.setName(req.getParameter("name"));
        inputGroup.setType(req.getParameter("type"));

   
        String status = req.getParameter("status");
        inputGroup.setStatus("Active".equals(status) ? 1 : 0);

        inputGroup.setDetail(req.getParameter("detail"));

        return inputGroup;
    }

    private void save(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String idStr = req.getParameter("id");
        Group inputGroup = getInputForm(req);

        if (idStr == null || idStr.isEmpty()) {
            // Thực hiện insert
            if (groupService.isGroupNameExists(inputGroup.getName())) {
                req.setAttribute("errorMessage", "Tên thiết lập đã tồn tại. Vui lòng nhập tên khác.");
                req.setAttribute("group", inputGroup); // Giữ lại thông tin đã nhập
                RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
                dispatcher.forward(req, res);
                return; // Dừng thực hiện thêm
            }
            groupService.addGroup(inputGroup);
        } else {
            // Thực hiện update
            inputGroup.setId(Integer.parseInt(idStr));
            groupService.updateGroup(inputGroup);
        }
        // Lấy các tham số của trang hiện tại
        String page = req.getParameter("page");
        String typeFilter = req.getParameter("typeFilter");
        String searchQuery = req.getParameter("searchQuery");

        // Chuyển hướng lại về trang danh sách với các tham số
        String redirectUrl = req.getContextPath() + "/group?page=" + page;
        if (typeFilter != null && !typeFilter.isEmpty()) {
            redirectUrl += "&typeFilter=" + typeFilter;
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            redirectUrl += "&searchQuery=" + searchQuery;
        }

        res.sendRedirect(redirectUrl);
    }

    private void changeStatus(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        int newStatus = Integer.parseInt(req.getParameter("newStatus"));

        // Cập nhật trạng thái trong service
        groupService.updateGroupStatus(id, newStatus);

        // Lấy tham số hiện tại
        String page = req.getParameter("page");
        String typeFilter = req.getParameter("typeFilter");
        String statusFilter = req.getParameter("statusFilter");
        String searchQuery = req.getParameter("searchQuery");

        // Tạo URL chuyển hướng
        StringBuilder redirectUrl = new StringBuilder(req.getContextPath() + "/group?page=" + page);
        if (typeFilter != null && !typeFilter.isEmpty()) {
            redirectUrl.append("&typeFilter=").append(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            redirectUrl.append("&statusFilter=").append(statusFilter);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            redirectUrl.append("&searchQuery=").append(searchQuery);
        }

        // Chuyển hướng
        res.sendRedirect(redirectUrl.toString());
    }
}
