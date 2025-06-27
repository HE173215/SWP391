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
import java.util.Map;
import model.Assignment;
import model.EvalCriteria;
import service.EvalCriteriaService;
import service.GroupService;

/**
 *
 * @author vqman
 */
@WebServlet({"/evalcriteria", "/evalcriteria/add", "/evalcriteria/edit", "/evalcriteria/save", "/evalcriteria/status"})
public class EvalCriteriaController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final String jspList = "/WEB-INF/view/group/configs.jsp";
    private final String jspForm = "/WEB-INF/view/evalcriteria/form.jsp";

    private EvalCriteriaService criteriaService;
    private GroupService groupService;

    public void init() {
        criteriaService = new EvalCriteriaService();
        groupService = new GroupService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/evalcriteria/add":
                showFormAdd(request, response);
                break;
            case "/evalcriteria/edit":
                showFormEdit(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/evalcriteria/save":
                save(request, response);
                break;
            case "/evalcriteria/status":
                changestatus(request, response);
                break;
        }
    }

    private void showFormAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer subjectId = (Integer) session.getAttribute("groupId");
        List<Assignment> assignments = criteriaService.getAssignmentBySubject(subjectId);
        // Đặt danh sách assignment vào request attribute để chuyển đến JSP
        request.setAttribute("assignments", assignments);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspForm);
        dispatcher.forward(request, response);
    }

    private void showFormEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer subjectId = (Integer) session.getAttribute("groupId");
        int id = Integer.parseInt(request.getParameter("id"));
        EvalCriteria criId = criteriaService.getCriteriaById(id);
        request.setAttribute("criId", criId);
        List<Assignment> assignments = criteriaService.getAssignmentBySubject(subjectId);
        // Đặt danh sách assignment vào request attribute để chuyển đến JSP
        request.setAttribute("assignments", assignments);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspForm);
        dispatcher.forward(request, response);
    }

    private EvalCriteria getInputForm(HttpServletRequest request) {
        EvalCriteria inputCriteria = new EvalCriteria();

        // Lấy ID nếu có
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            inputCriteria.setId(Integer.parseInt(idParam));
        }

        // Lấy tên (Name)
        inputCriteria.setName(request.getParameter("name"));

        // Lấy chi tiết (Detail)
        inputCriteria.setDetail(request.getParameter("detail"));

        // Lấy trọng số (Weight)
        String weightParam = request.getParameter("weight");
        if (weightParam != null && !weightParam.isEmpty()) {
            inputCriteria.setWeight(Integer.parseInt(weightParam));
        } else {
            inputCriteria.setWeight(0); // Đặt giá trị mặc định nếu không có
        }

        // Lấy assignmentId từ dropdown
        String assignmentIdParam = request.getParameter("assignmentId");
        if (assignmentIdParam != null && !assignmentIdParam.isEmpty()) {
            inputCriteria.setAssignmentId(Integer.parseInt(assignmentIdParam));
        }

        // Lấy trạng thái (Status) từ radio button
        String statusParam = request.getParameter("status");
        inputCriteria.setStatus("Active".equals(statusParam));
        // Ghi log và trả về đối tượng EvalCriteria để thêm vào database
        System.out.println("Adding EvalCriteria: " + inputCriteria);
        return inputCriteria;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EvalCriteria criteria = getInputForm(request);
        boolean result;
        Integer subjectId = (Integer) session.getAttribute("groupId");
        if (criteria.getId() != 0) {
            result = criteriaService.updateEvalCriteria(criteria); // Gọi service để cập nhật nếu có ID
            request.getSession().setAttribute("Message", "Updated successfully!");
        } else {
            result = criteriaService.addEvalCriteria(criteria); // Gọi service để thêm mới nếu không có ID
            request.getSession().setAttribute("Message", "Successfully added!");
        }

        if (!result) {
            // Nếu có lỗi, lấy thông báo lỗi từ service và chuyển về form
            Map<String, String> messages = criteriaService.getMessages();
            request.setAttribute("messages", messages);
            List<Assignment> assignments = criteriaService.getAssignmentBySubject(subjectId);
            // Đặt danh sách assignment vào request attribute để chuyển đến JSP
            request.setAttribute("assignments", assignments);
            request.setAttribute("criteria", criteria); // Giữ lại dữ liệu form để người dùng sửa
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspForm);
            dispatcher.forward(request, response);
        } else {
            // Nếu thành công, chuyển hướng về trang chủ hoặc trang mong muốn
            //String subjectId = request.getParameter("subjectId");
            response.sendRedirect(request.getContextPath() + "/subject?id=" + subjectId);
        }
    }

    private void changestatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("idStatus"));
        int newStatus = Integer.parseInt(request.getParameter("newStatus"));
        int groupId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        String activeTab = request.getParameter("activeTab");
        session.setAttribute("activeTab", activeTab);
        boolean isUpdated = criteriaService.updateStatusById(id, newStatus);

        request.getSession().setAttribute("Message", "Criteria has been " + (newStatus == 1 ? "activated" : "deactivated") + ".");

        // Kiểm tra nếu cập nhật thành công, tạo URL chuyển hướng
        String redirectUrl = request.getContextPath() + "/subject?id=" + groupId + "&tab=" + activeTab;

        // Chuyển hướng
        response.sendRedirect(redirectUrl);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
}
