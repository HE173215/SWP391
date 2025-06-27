package service;

import dao.FinalEvaluatedDAO;
import model.FinalEvaluated;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalEvaluatedService {

    private final FinalEvaluatedDAO finalEvaluatedDAO;

    public FinalEvaluatedService() {
        finalEvaluatedDAO = new FinalEvaluatedDAO();
    }

    // Method to get all final evaluated teams
    public List<FinalEvaluated> getAllFinalEvaluatedTeams() throws SQLException {
        return finalEvaluatedDAO.getAllFinalEvaluatedTeams();
    }

    // Method to get final evaluated team by ID
    public FinalEvaluated getFinalEvaluatedTeamById(int teamId) throws SQLException {
        return finalEvaluatedDAO.getFinalEvaluatedTeamById(teamId);
    }

    // Method to search final evaluated teams by name
    public List<FinalEvaluated> searchFinalEvaluatedTeamsByName(String teamName) throws SQLException {
        return finalEvaluatedDAO.searchFinalEvaluatedTeamsByName(teamName);
    }

    // Method to validate final evaluation info (if necessary)
    public Map<String, String> validateFinalEvaluationInfo(int teamId, int milestoneId) {
        Map<String, String> errorMessages = new HashMap<>();

        // Example validation rules, adjust according to your business logic
        if (teamId <= 0) {
            errorMessages.put("teamId", "Team ID must be valid.");
        }
        if (milestoneId <= 0) {
            errorMessages.put("milestoneId", "Milestone ID must be valid.");
        }

        return errorMessages;
    }

    // Additional methods can be added as needed for further functionality
}
