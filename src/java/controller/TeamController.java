package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.TeamService;
import model.Team;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import service.ClassService;

@WebServlet({"/teams", "/teams/details", "/teams/update", "/teams/add", "/teams/addMember", "/teams/removeMember", "/teams/delete", "/teams/setRole"})
public class TeamController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TeamService teamService;
    private ClassService classService;

    @Override
    public void init() {
        teamService = new TeamService(); // Initialize TeamService when servlet starts
        classService = new ClassService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        switch (action) {
            case "/teams":
                showTeams(req, res);
                break;
            case "/teams/details":
                showTeamDetails(req, res);
                break;
            case "/teams/add":
                if (isTeacher(currentUser)) {
                    showAddTeamForm(req, res); // Only teachers can add teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to add a team.");
                }
                break;
            case "/teams/delete":
                if (isTeacher(currentUser)) {
                    deleteTeam(req, res); // Only teachers can delete teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to delete a team.");
                }
                break;
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        switch (action) {
            case "/teams/update":
                if (isTeacher(currentUser)) {
                    updateTeamDetails(req, res); // Only teachers can update teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to update team information.");
                }
                break;
            case "/teams/add":
                if (isTeacher(currentUser)) {
                    addNewTeam(req, res); // Only teachers can add new teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to add a team.");
                }
                break;
            case "/teams/addMember":
                if (isTeacher(currentUser)) {
                    addMemberToTeam(req, res); // Only teachers can add members to teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to add members to the team.");
                }
                break;
            case "/teams/removeMember":
                if (isTeacher(currentUser)) {
                    removeMemberFromTeam(req, res); // Only teachers can remove members from teams
                } else {
                    forwardToErrorPage(req, res, "You do not have permission to remove members from the team.");
                }
                break;
            case "/teams/setRole":
                if (isTeacher(currentUser)) {
                    updateTeamMemberRole(req, res);
                } else {
                    forwardToErrorPage(req, res, "You do not have permission.");
                }
                break;
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // Check if the user is a teacher (role 2)
    private boolean isTeacher(User user) {
        String role = user.getRole();
        return "Teacher".equals(role) || "Admin".equals(role);
    }

    // Method to forward to the error page
    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/team/ErrorPage.jsp");
        dispatcher.forward(req, res);
    }

    // Show the list of teams
    private void showTeams(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Team> teams = teamService.getAllTeams();
            req.setAttribute("teams", teams);
            forwardToPage(req, res, "/WEB-INF/view/class/configs.jsp");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "An error occurred while retrieving the list of teams.");
            forwardToPage(req, res, "/WEB-INF/view/class/configs.jsp");
        }
    }

    // Show teams by milestone
    private void showTeamsByMilestone(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String milestoneIdStr = req.getParameter("milestoneId");
        try {
            int milestoneId = Integer.parseInt(milestoneIdStr);
            List<Team> teams = teamService.getTeamsByMilestone(milestoneId);

            req.setAttribute("teams", teams);
            req.setAttribute("milestoneId", milestoneId);
            forwardToPage(req, res, "/WEB-INF/view/team/formTeam.jsp");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Milestone ID is invalid.");
        }
    }

    // Show the form to add a new team
    private void showAddTeamForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the session from the request
        HttpSession session = req.getSession();

        // Get classID from session
        String selectedClassIDStr = (String) session.getAttribute("selectedClassID");

        if (selectedClassIDStr != null) {
            try {
                // Convert classID from String to Integer
                int selectedClassID = Integer.parseInt(selectedClassIDStr);

                // Call service to get class information based on classId
                model.Class selectedClass = classService.searchClassByID(selectedClassID);

                if (selectedClass != null) {
                    // Store the class name into the request attribute
                    req.setAttribute("currentClassName", selectedClass.getName());
                } else {
                    req.setAttribute("error", "Class not found.");
                }
            } catch (NumberFormatException e) {
                // Handle if the string cannot be converted to an integer
                req.setAttribute("error", "Invalid class ID format.");
            }
        } else {
            req.setAttribute("error", "No class ID found in session.");
        }

        // Forward to JSP to show add team form
        forwardToPage(req, res, "/WEB-INF/view/team/addTeam.jsp");
    }

    // Add a new team
    private void addNewTeam(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String activeTab = req.getParameter("activeTab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "milestones"; // Default to the milestones tab if no tab is specified
        }

        // Get parameters from request
        String name = req.getParameter("name").trim();
        String detail = req.getParameter("detail").trim();
        String topic = req.getParameter("topic").trim();
        String classIdStr = req.getParameter("classId");

        try {
            int classId = Integer.parseInt(classIdStr); // Convert classId from String to int

            // Create a team object
            Team team = new Team();
            team.setName(name);
            team.setDetail(detail);
            team.setTopic(topic);
            team.setClassId(classId);

            // Check for duplicate team name or topic in the same class
            if (teamService.isTeamNameOrTopicExists(name, topic, classId)) {
                req.getSession().setAttribute("Message", "Team name or topic already exists in this class.");
                req.setAttribute("team", team);
                forwardToPage(req, res, "/WEB-INF/view/team/addTeam.jsp");
                return;
            }

            // Validate team information
            String errorMessage = teamService.validateTeamInfo(name, detail, topic, classId);
            if (!errorMessage.isEmpty()) {
                req.getSession().setAttribute("errorMessage", errorMessage);
                req.setAttribute("team", team);
                forwardToPage(req, res, "/WEB-INF/view/team/addTeam.jsp");
                return;
            }

            // Call service to create team
            boolean added = teamService.createTeam(team); // Just pass the team object
            if (added) {
                // Get classID from session to redirect to URL containing classID
                String classID = (String) req.getSession().getAttribute("selectedClassID");

                // Check if classID exists
                if (classID != null) {
                    req.getSession().setAttribute("successMessage", "New team added successfully.");
                    // Redirect with classID in URL
                    res.sendRedirect(req.getContextPath() + "/class/configs?classID=" + classID);
                    req.setAttribute("activeTab", activeTab);
                } else {
                    // If classID is not found in session, redirect to configs page
                    req.getSession().setAttribute("errorMessage", "Class ID not found in session.");
                    res.sendRedirect(req.getContextPath() + "/class/configs");
                }
            } else {
                // If unable to add team, show error
                req.getSession().setAttribute("errorMessage", "Unable to add new team. Please try again.");
                req.setAttribute("team", team);
                forwardToPage(req, res, "/WEB-INF/view/team/addTeam.jsp");
            }
        } catch (NumberFormatException e) {
            // If class ID is invalid
            req.getSession().setAttribute("errorMessage", "Class ID is invalid.");
            forwardToPage(req, res, "/WEB-INF/view/team/addTeam.jsp");
        }
    }

    // Delete team
    private void deleteTeam(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teamIdStr = req.getParameter("teamId");
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        // Only allow teachers to delete teams
        if (!isTeacher(currentUser)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to delete a team.");
            return;
        }

        try {
            int teamId = Integer.parseInt(teamIdStr);

            // Check if the team exists
            Team team = teamService.getTeamById(teamId);
            if (team != null) {
                // Delete the team
                boolean isDeleted = teamService.deleteTeam(teamId);
                if (isDeleted) {
                    req.getSession().setAttribute("successMessage", "Team deleted successfully.");
                } else {
                    req.getSession().setAttribute("errorMessage", "Unable to delete team.");
                }
            } else {
                req.getSession().setAttribute("errorMessage", "Team does not exist.");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("errorMessage", "Team ID is invalid.");
        }

        // Get classID from session to redirect back to team list with classID
        HttpSession session = req.getSession();
        String classID = (String) session.getAttribute("selectedClassID");

        // Check if classID exists, redirect with classID in URL
        if (classID != null) {
            res.sendRedirect(req.getContextPath() + "/class/configs?classID=" + classID);
        } else {
            // If classID is not found in session, redirect to configs page without classID
            res.sendRedirect(req.getContextPath() + "/class/configs");
        }
    }

    // Show details of a specific team
    private void showTeamDetails(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teamIdStr = req.getParameter("teamId");
        HttpSession session = req.getSession();
        String selectedClassIDStr = (String) session.getAttribute("selectedClassID");

        try {
            int teamId = Integer.parseInt(teamIdStr);
            Team team = teamService.getTeamById(teamId);

            if (team != null) {
                loadTeamMembers(req, teamId); // Load paginated team members

                // Get the paginated team members list and create a set of their IDs
                List<User> teamMembers = (List<User>) req.getAttribute("teamMembers");
                Set<Integer> teamMemberIds = teamMembers.stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

                if (selectedClassIDStr != null) {
                    loadClassDetails(req, selectedClassIDStr); // Load class details

                    try {
                        int classId = Integer.parseInt(selectedClassIDStr);
                        List<User> classMembers = teamService.getMembersByClassId(classId);

                        // Filter out team members from the class members list
                        List<User> availableClassMembers = classMembers.stream()
                                .filter(member -> !teamMemberIds.contains(member.getId()))
                                .collect(Collectors.toList());

                        req.setAttribute("classMembers", availableClassMembers); // Pass filtered class members to JSP
                    } catch (NumberFormatException e) {
                        req.setAttribute("errorMessage", "Class ID is invalid.");
                    }
                } else {
                    req.setAttribute("error", "No class ID found in session.");
                }

                req.setAttribute("team", team);
                forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
            } else {
                req.setAttribute("Message", "Team does not exist.");
                forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("Message", "Team ID is invalid.");
            forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
        }
    }

    // Update details of a specific team
    private void updateTeamDetails(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String teamIdStr = req.getParameter("teamId");
        String name = req.getParameter("name").trim();
        String detail = req.getParameter("detail").trim();
        String topic = req.getParameter("topic").trim();
        String classIdStr = req.getParameter("classId");

        try {
            int teamId = Integer.parseInt(teamIdStr);
            int classId = Integer.parseInt(classIdStr);

            Team team = teamService.getTeamById(teamId);
            if (team != null) {
                team.setName(name);
                team.setDetail(detail);
                team.setTopic(topic);
                team.setClassId(classId);

                // Check for duplicate team name
                if (teamService.isTeamNameExists(name, classId)) {
                    req.getSession().setAttribute("Message", "Team name already exists in this class.");

                    // Reload member list and class information
                    loadTeamMembers(req, teamId);
                    loadClassDetails(req, classIdStr); // Reload class name

                    req.setAttribute("team", team);
                    forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
                    return;
                }

                // Validate other potential errors
                String errorMessage = teamService.validateTeamInfo(name, detail, topic, classId);
                if (!errorMessage.isEmpty()) {
                    req.getSession().setAttribute("Message", errorMessage);

                    // Reload member list and class information
                    loadTeamMembers(req, teamId);
                    loadClassDetails(req, classIdStr); // Reload class name

                    req.setAttribute("team", team);
                    forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
                    return;
                }

                // Update the team
                boolean updated = teamService.updateTeam(team);
                if (updated) {
                    req.getSession().setAttribute("Message", "Team information updated successfully.");
                    res.sendRedirect(req.getContextPath() + "/teams/details?teamId=" + teamId);
                } else {
                    req.getSession().setAttribute("Message", "Update failed. Please try again.");

                    // Reload member list and class information
                    loadTeamMembers(req, teamId);
                    loadClassDetails(req, classIdStr); // Reload class name

                    req.setAttribute("team", team);
                    forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
                }
            } else {
                req.setAttribute("Message", "Team does not exist.");
                forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("Message", "Team ID or Class ID is invalid.");
            forwardToPage(req, res, "/WEB-INF/view/team/detailTeam.jsp");
        }
    }

    // Method to load members and paginate
    private void loadTeamMembers(HttpServletRequest req, int teamId) throws ServletException, IOException {
        List<User> teamMembers = teamService.getTeamMembers(teamId);

        // Paginate the member list
        List<User> filteredTeamMembers = teamMembers.stream()
                .filter(member -> "Student".equals(member.getRole()))
                .collect(Collectors.toList());

        int membersPerPage = 4;
        int totalMembers = filteredTeamMembers.size();
        int totalPages = (int) Math.ceil((double) totalMembers / membersPerPage);
        String pageStr = req.getParameter("page");
        int currentPage = (pageStr == null) ? 1 : Integer.parseInt(pageStr);

        int start = (currentPage - 1) * membersPerPage;
        int end = Math.min(start + membersPerPage, totalMembers);
        List<User> paginatedMembers = filteredTeamMembers.subList(start, end);

        req.setAttribute("teamMembers", paginatedMembers);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
    }

    // Method to load class information
    private void loadClassDetails(HttpServletRequest req, String classIdStr) {
        try {
            int classId = Integer.parseInt(classIdStr);
            model.Class selectedClass = classService.searchClassByID(classId);

            if (selectedClass != null) {
                req.setAttribute("currentClassName", selectedClass.getName());
            } else {
                req.setAttribute("error", "Class not found.");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid class ID format.");
        }
    }

    // Add a member to the team
    private void addMemberToTeam(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teamIdStr = req.getParameter("teamId");
        String[] selectedMemberIds = req.getParameterValues("selectedMembers"); // Retrieve selected member IDs

        try {
            int teamId = Integer.parseInt(teamIdStr);

            if (selectedMemberIds != null) { // Ensure at least one member is selected
                boolean allAdded = true; // Track if all members were added successfully

                for (String userIdStr : selectedMemberIds) {
                    int userId = Integer.parseInt(userIdStr);
                    boolean added = teamService.addMemberToTeam(teamId, userId);

                    if (!added) {
                        allAdded = false; // Track if any addition failed
                    }
                }

                if (allAdded) {
                    req.getSession().setAttribute("Message", "Members added to the team successfully.");
                } else {
                    req.getSession().setAttribute("Message", "Unable to add some members. They may already be in the team or an error occurred.");
                }
            } else {
                req.getSession().setAttribute("Message", "Please select at least one member to add.");
            }

            // Redirect to the team details page
            res.sendRedirect(req.getContextPath() + "/teams/details?teamId=" + teamId);

        } catch (NumberFormatException e) {
            req.getSession().setAttribute("Message", "Team ID or member ID is invalid.");
            res.sendRedirect(req.getContextPath() + "/teams/details?teamId=" + teamIdStr);
        }
    }

    // Remove a member from the team
    private void removeMemberFromTeam(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teamIdStr = req.getParameter("teamId");
        String userEmail = req.getParameter("userEmail").trim();

        try {
            int teamId = Integer.parseInt(teamIdStr);
            int userId = teamService.getUserIdByEmail(userEmail);

            if (userId != -1) {
                boolean removed = teamService.removeMemberFromTeam(teamId, userId);
                if (removed) {
                    req.setAttribute("Message", "Member removed from the team successfully.");
                } else {
                    req.setAttribute("Message", "Unable to remove member from the team.");
                }
            } else {
                req.setAttribute("Message", "User not found with email: " + userEmail);
            }
            showTeamDetails(req, res);
        } catch (NumberFormatException e) {
            req.setAttribute("Message", "Team ID is invalid.");
            showTeamDetails(req, res);
        }
    }

    private void updateTeamMemberRole(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teamIdStr = req.getParameter("teamId");
        String userEmail = req.getParameter("userEmail").trim();

        try {
            int teamId = Integer.parseInt(teamIdStr);
            int userId = teamService.getUserIdByEmail(userEmail);

            if (userId != -1) {
                // Update the team member role (promote to leader)
                teamService.updateTeamMemberRole(userId, teamId);
                req.setAttribute("Message", "Member promoted to leader successfully.");
            } else {
                req.setAttribute("Message", "User not found with email: " + userEmail);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("Message", "Team ID is invalid.");
        } catch (Exception e) {
            req.setAttribute("Message", "An error occurred: " + e.getMessage());
        } finally {
            showTeamDetails(req, res); // Display team details regardless of the outcome
        }
    }

    // Method to forward to the JSP page
    private void forwardToPage(HttpServletRequest req, HttpServletResponse res, String page) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        dispatcher.forward(req, res);
    }
}
