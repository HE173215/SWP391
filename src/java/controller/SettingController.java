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
import java.util.List;
import model.Setting;
import service.SettingService;

/**
 *
 * @author vqman
 */
@WebServlet({"/setting", "/setting/new", "/setting/edit", "/setting/save", "/setting/change-status"})
public class SettingController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jspList = "/WEB-INF/view/setting/list.jsp";
    private final String jspForm = "/WEB-INF/view/setting/form.jsp";

    private SettingService settingService;

    @Override
    public void init() {
        settingService = new SettingService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getServletPath();
        if (action.equals("/setting/change-status")) {
            changeStatus(req, res);
        } else {
            save(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getServletPath();
        try {
            switch (action) {
                case "/setting/new":
                    showNewForm(req, res);
                    break;
                case "/setting/edit":
                    showEditForm(req, res);
                    break;
                case "/setting/save":
                    save(req, res);
                    break;
                case "/setting":
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

        List<Setting> settingList;
        int totalRecords;

        // Lọc theo cả status và type nếu cả hai đều có giá trị
        if (statusFilter != null && !statusFilter.isEmpty() && typeFilter != null && !typeFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            settingList = settingService.filterSettings(typeFilter, statusFilter, offset, pageSize);
            totalRecords = settingService.countSettingsByFilters(typeFilter, statusFilter);
        } // Lọc theo status nếu chỉ có statusFilter
        else if (statusFilter != null && !statusFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            settingList = settingService.getSettingsByStatus(status, offset, pageSize);
            totalRecords = settingService.countSettingsByStatus(status);
        } // Lọc theo tìm kiếm nếu có searchQuery
        else if (searchQuery != null && !searchQuery.isEmpty()) {
            settingList = settingService.searchSettingByName(searchQuery, offset, pageSize);
            totalRecords = settingService.countSettingsByName(searchQuery);
        } // Lọc theo type nếu chỉ có typeFilter
        else if (typeFilter != null && !typeFilter.isEmpty()) {
            settingList = settingService.getSettingsByType(typeFilter, offset, pageSize);
            totalRecords = settingService.countSettingsByType(typeFilter);
        } // Nếu không có điều kiện lọc nào, lấy tất cả cài đặt
        else {
            settingList = settingService.getAllSettings(offset, pageSize);
            totalRecords = settingService.countAll();
        }

        // Tính toán tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Đặt các thuộc tính cho request để chuyển đến JSP
        req.setAttribute("settingList", settingList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("statusFilter", statusFilter); // Giữ lại giá trị statusFilter
        req.setAttribute("typeFilter", typeFilter); // Giữ lại giá trị typeFilter
        req.setAttribute("searchQuery", searchQuery); // Giữ lại giá trị searchQuery

        // Lấy danh sách loại cài đặt
        List<String> types = settingService.getAllTypes();
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
        Setting currentSetting = settingService.getSettingById(id);
        req.setAttribute("setting", currentSetting);
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
        dispatcher.forward(req, res);
    }

    private Setting getInputForm(HttpServletRequest req) {
        Setting inputSetting = new Setting();
        inputSetting.setSettingName(req.getParameter("name"));
        inputSetting.setType(req.getParameter("type"));

        String priorityStr = req.getParameter("priority");
        if (priorityStr != null && !priorityStr.isEmpty()) {
            inputSetting.setPriority(Integer.parseInt(priorityStr));
        } else {
            inputSetting.setPriority(0); // Giá trị mặc định
        }
        String status = req.getParameter("status");
        inputSetting.setStatus("Active".equals(status) ? 1 : 0);

        inputSetting.setDescription(req.getParameter("description"));

        return inputSetting;
    }

    private void save(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String idStr = req.getParameter("id");
        Setting inputSetting = getInputForm(req);

        // Lấy các tham số của trang hiện tại
        String page = req.getParameter("page");
        String typeFilter = req.getParameter("typeFilter");
        String searchQuery = req.getParameter("searchQuery");

        String errorMessage;

        if (idStr == null || idStr.isEmpty()) {
            // Thực hiện insert
            errorMessage = settingService.addSetting(inputSetting);
        } else {
            // Thực hiện update
            inputSetting.setId(Integer.parseInt(idStr));
            errorMessage = settingService.updateSetting(inputSetting);
        }

        if (errorMessage != null) {
            req.setAttribute("errorMessage", errorMessage);
            req.setAttribute("setting", inputSetting); // Giữ lại thông tin đã nhập
            RequestDispatcher dispatcher = req.getRequestDispatcher(jspForm);
            dispatcher.forward(req, res);
        } else {
            req.getSession().setAttribute("Message", (idStr == null || idStr.isEmpty()) ? "Setting added successfully!" : "Setting updated successfully!");
            // Chuyển hướng lại về trang danh sách với các tham số
            String redirectUrl = req.getContextPath() + "/setting?page=" + page;
            if (typeFilter != null && !typeFilter.isEmpty()) {
                redirectUrl += "&typeFilter=" + typeFilter;
            }
            if (searchQuery != null && !searchQuery.isEmpty()) {
                redirectUrl += "&searchQuery=" + searchQuery;
            }
            res.sendRedirect(redirectUrl);
        }
    }

    private void changeStatus(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        int newStatus = Integer.parseInt(req.getParameter("newStatus"));

        // Cập nhật trạng thái trong service
        settingService.updateSettingStatus(id, newStatus);

        // Lấy tham số hiện tại
        String page = req.getParameter("page");
        String typeFilter = req.getParameter("typeFilter");
        String statusFilter = req.getParameter("statusFilter");
        String searchQuery = req.getParameter("searchQuery");

        // Tạo URL chuyển hướng
        StringBuilder redirectUrl = new StringBuilder(req.getContextPath() + "/setting?page=" + page);
        if (typeFilter != null && !typeFilter.isEmpty()) {
            redirectUrl.append("&typeFilter=").append(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            redirectUrl.append("&statusFilter=").append(statusFilter);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            redirectUrl.append("&searchQuery=").append(searchQuery);
        }
        req.getSession().setAttribute("Message", "User has been " + (newStatus == 1 ? "activated" : "deactivated") + ".");
        // Chuyển hướng
        res.sendRedirect(redirectUrl.toString());
    }
}
