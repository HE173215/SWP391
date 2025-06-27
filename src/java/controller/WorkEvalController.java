package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.WorkEvalService;
import model.WorkEval;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Milestone;
import model.SubjectSetting;
import model.Team;
import service.MilestoneService;

@WebServlet({"/workEval", "/workEval/update"})
public class WorkEvalController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jspList = "/WEB-INF/view/workEval/list.jsp";
    private final String jspAddForm = "/WEB-INF/view/workEval/add.jsp";
    private final String jspEditForm = "/WEB-INF/view/workEval/form.jsp";

    private WorkEvalService workEvalService;
    private MilestoneService milestoneService;

    public void init() {
        workEvalService = new WorkEvalService();
        milestoneService = new MilestoneService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/workEval":
                showWorkEvalList(request, response);
                break;
            case "/workEval/update":
                showWorkEvalForUpdate(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/workEval");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/workEval/update":
                updateWorkEval(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/workEval");
                break;
        }
    }

    private void showWorkEvalList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Default page and size handling
        int currentPage = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1; // Fallback to default if invalid
            }
        }

        String sizeParam = request.getParameter("size");
        if (sizeParam != null) {
            try {
                pageSize = Integer.parseInt(sizeParam);
            } catch (NumberFormatException e) {
                pageSize = 10; // Fallback to default if invalid
            }
        }

        // Fetch data
        List<WorkEval> fullWorkEvalList = workEvalService.getAllWorkEvals(); // Removed unnecessary parameter
        int totalItems = fullWorkEvalList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startItem = Math.max((currentPage - 1) * pageSize, 0);
        int endItem = Math.min(startItem + pageSize, totalItems);
        List<WorkEval> paginatedWorkEvalList = fullWorkEvalList.subList(startItem, endItem);

        // Fetch additional data

        // Set attributes for request
        request.setAttribute("workEvalList", paginatedWorkEvalList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspList);
        dispatcher.forward(request, response);
    }

    private void showWorkEvalForUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Fetch necessary data
            List<SubjectSetting> complexities = workEvalService.getComplexities();
            List<SubjectSetting> qualities = workEvalService.getQualities();
            List<Milestone> milestones = milestoneService.getAllMilestones();
            List<WorkEval> workEvalList = workEvalService.getAllWorkEvals();

            // Set attributes for dropdowns
            request.setAttribute("workEvalList", workEvalList);
            request.setAttribute("milestones", milestones);
            request.setAttribute("complexities", complexities);
            request.setAttribute("qualities", qualities);

            // Retrieve WorkEval by ID
            int id = Integer.parseInt(request.getParameter("id"));
            WorkEval workEval = workEvalService.getWorkEvalById(id);

            if (workEval != null) {
                String reqName = workEvalService.getReqNameByReqId(workEval.getReqId());
                workEval.setReqName(reqName);
                request.setAttribute("workEval", workEval);
                RequestDispatcher dispatcher = request.getRequestDispatcher(jspEditForm);
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "WorkEval not found.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid WorkEval ID.");
        } catch (SQLException e) {
            throw new ServletException("Database error occurred while fetching WorkEval for update.", e);
        }
    }

    private void updateWorkEval(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            WorkEval workEval = createWorkEvalFromRequest(request);
            boolean success = workEvalService.updateWorkEvalById(id, workEval);
            if (success) {
                request.getSession().setAttribute("successMessage", "Evaluated successfully.");
                response.sendRedirect(request.getContextPath() + "/workEval");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update WorkEval.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
        }
    }
    private WorkEval createWorkEvalFromRequest(HttpServletRequest request) {
        WorkEval workEval = new WorkEval();
        workEval.setReqId(Integer.parseInt(request.getParameter("reqId")));
        workEval.setMilestoneId(Integer.parseInt(request.getParameter("milestoneId")));
        workEval.setComplexityId(Integer.parseInt(request.getParameter("complexityId")));
        workEval.setQualityId(Integer.parseInt(request.getParameter("qualityId")));
        workEval.setIsFinal(Boolean.parseBoolean(request.getParameter("isFinal")));
        return workEval;
    }

    private void handleException(HttpServletResponse response, SQLException ex)
            throws IOException {
        ex.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + ex.getMessage());
    }
}
