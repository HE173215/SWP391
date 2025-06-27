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
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import model.Assignment;
import model.Milestone;
import model.Class;
import model.ReviewCouncil;
import model.User;
import service.ClassService;
import service.MilestoneService;

/**
 *
 * @author admin
 */
@WebServlet(urlPatterns = {"/milestone/view", "/milestone/edit", "/milestone/save"})
public class MilestoneController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jsplist = "/WEB-INF/view/class/configs.jsp";
    private final String jspdetails = "/WEB-INF/view/milestone/details.jsp";

    private MilestoneService milestoneService;
    private ClassService classService;

    public void init() {
        milestoneService = new MilestoneService();
        classService = new ClassService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/milestone/view":
                showViewMilestone(request, response);
                break;
            case "/milestone/edit":
                showEditMilestone(request, response);
                break;
            case "/milestone/save":
                saveMilestone(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        saveMilestone(request, response);
    }

    private void showViewMilestone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String milestoneId = request.getParameter("id");
        Milestone milestone = milestoneService.getMilestoneById(Integer.parseInt(milestoneId));
        List<Assignment> assignments = milestoneService.getAllNames();
        Class c = classService.getClassByMilestoneID(milestoneId);

        request.setAttribute("assignments", assignments);
        request.setAttribute("c", c);

        if (milestone != null) {
            request.setAttribute("milestone", milestone);
            request.setAttribute("action", "view");
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("error", "Milestone not found.");
            request.getRequestDispatcher(jsplist).forward(request, response);
        }
    }

    private void showEditMilestone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String milestoneId = request.getParameter("id");
        Milestone milestone = milestoneService.getMilestoneById(Integer.parseInt(milestoneId));
        List<Assignment> assignments = milestoneService.getAllNames();
        Class c = classService.getClassByMilestoneID(milestoneId);

        request.setAttribute("milestone", milestone);
        request.setAttribute("assignments", assignments);
        request.setAttribute("c", c);
        request.setAttribute("action", "edit");

        RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
        dispatcher.forward(request, response);
    }

    private Milestone getInputForm(HttpServletRequest request) {
        Milestone inputMilestone = new Milestone();
        inputMilestone.setId(Integer.parseInt(request.getParameter("id")));
        inputMilestone.setCode(request.getParameter("code"));
        inputMilestone.setName(request.getParameter("name"));
        inputMilestone.setPriority(Integer.parseInt(request.getParameter("priority")));
        inputMilestone.setWeight(Integer.parseInt(request.getParameter("weight")));
        inputMilestone.setDetail(request.getParameter("detail"));
        inputMilestone.setMax_eval_value(Integer.parseInt(request.getParameter("max_eval_value")));

        String endDateString = request.getParameter("endDate");
        if (endDateString != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date endDate = dateFormat.parse(endDateString);
                inputMilestone.setEndDate(endDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        inputMilestone.setStatus(Integer.parseInt(request.getParameter("status")));

//        String assignmentId = request.getParameter("assignment");
//        if (assignmentId != null && !assignmentId.isEmpty()) {
//            inputMilestone.setAssigmentId(Integer.parseInt(assignmentId));
//        } else {
//            inputMilestone.setAssigmentId(0);
//        }

        return inputMilestone;
    }

    private void saveMilestone(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Milestone inputMilestone = getInputForm(request);
        String id = request.getParameter("id");
        String classId = request.getParameter("c");

        Map<String, String> validationErrors = milestoneService.validateMilestonesInfo(inputMilestone.getCode(), inputMilestone.getName(),
                inputMilestone.getPriority(), inputMilestone.getWeight(), inputMilestone.getDetail(), inputMilestone.getMax_eval_value(), inputMilestone.getEndDate());

        if (!validationErrors.isEmpty()) {
            request.setAttribute("validationErrors", validationErrors);
            request.setAttribute("milestole", inputMilestone);
            List<Assignment> milestones = milestoneService.getAllNames();
            request.setAttribute("milestones", milestones);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
            return;
        }

        if (id == null || id.isEmpty()) {
            boolean isValid = milestoneService.validateMilestone(inputMilestone);
            if (isValid) {
                milestoneService.updateMilestone(inputMilestone);
            }
        } else {
            inputMilestone.setId(Integer.parseInt(id));
            boolean isValid = milestoneService.validateMilestone(inputMilestone);
            if (isValid) {
                milestoneService.updateMilestone(inputMilestone);
                request.getSession().setAttribute("Message", "Milestone updated successfully!");
            }
        }

        response.sendRedirect(request.getContextPath() + "/class/configs?id=" + classId);
    }

}
