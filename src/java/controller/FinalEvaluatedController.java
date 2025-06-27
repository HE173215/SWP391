package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.FinalEvaluatedService;
import model.FinalEvaluated;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/finalEvaluated", "/finalEvaluated/view", "/finalEvaluated/search"})
public class FinalEvaluatedController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jspList = "/WEB-INF/view/finalEvaluated/list.jsp";
    private final String jspDetails = "/WEB-INF/view/finalEvaluated/details.jsp";

    private FinalEvaluatedService finalEvaluatedService;

    public void init() {
        finalEvaluatedService = new FinalEvaluatedService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/finalEvaluated":
                    showFinalEvaluatedList(request, response);
                    break;
                case "/finalEvaluated/view":
                    showFinalEvaluatedById(request, response);
                    break;
                case "/finalEvaluated/search":
                    showSearchForm(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/finalEvaluated");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/finalEvaluated/search":
                    searchFinalEvaluatedTeams(request, response);
                    break;
                // Add additional POST actions here if needed
                default:
                    response.sendRedirect(request.getContextPath() + "/finalEvaluated");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error: " + ex.getMessage(), ex);
        }
    }

    private void showFinalEvaluatedList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<FinalEvaluated> finalEvaluatedList = finalEvaluatedService.getAllFinalEvaluatedTeams();
        request.setAttribute("finalEvaluatedList", finalEvaluatedList);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspList);
        dispatcher.forward(request, response);
    }

    private void showFinalEvaluatedById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int teamId = Integer.parseInt(request.getParameter("id"));
        FinalEvaluated finalEvaluated = finalEvaluatedService.getFinalEvaluatedTeamById(teamId);
        request.setAttribute("finalEvaluated", finalEvaluated);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspDetails);
        dispatcher.forward(request, response);
    }

    private void showSearchForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display the search form, can be a simple JSP with a search input
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspList); // You can change to a search JSP if available
        dispatcher.forward(request, response);
    }

    private void searchFinalEvaluatedTeams(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String teamName = request.getParameter("teamName");
        List<FinalEvaluated> finalEvaluatedList = finalEvaluatedService.searchFinalEvaluatedTeamsByName(teamName);
        request.setAttribute("finalEvaluatedList", finalEvaluatedList);
        request.setAttribute("searchQuery", teamName);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspList);
        dispatcher.forward(request, response);
    }
}
