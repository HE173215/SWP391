/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.WorkEvalDAO;
import model.WorkEval;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.SubjectSetting;
import model.Team;

/**
 * Service class for WorkEval
 */
public class WorkEvalService {

    private WorkEvalDAO workEvalDAO;

    // Constructor
    public WorkEvalService() {
        this.workEvalDAO = new WorkEvalDAO(); // Initialize the DAO
    }

    // Method to get all WorkEvals or filter by team milestone ID
    public List<WorkEval> getAllWorkEvals() {
        List<WorkEval> workEvalList = new ArrayList<>();
        try {
            workEvalList = workEvalDAO.getAllWorkEvals(); // Call the DAO method without parameters
        } catch (SQLException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Consider logging using a logger for better error management and tracking
            System.err.println("Error retrieving WorkEvals: " + e.getMessage());
        }
        return workEvalList; // Return an empty list if an exception occurs
    }

    public WorkEval getWorkEvalById(int id) {
        try {
            return workEvalDAO.getWorkEvalById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately, such as returning null or throwing a custom exception
        }
    }

    // Method to update a WorkEval by ID
    public boolean updateWorkEvalById(int id, WorkEval workEval) {
        return workEvalDAO.updateWorkEvalById(id, workEval);
    }

    public List<SubjectSetting> getComplexities() throws SQLException {
        return workEvalDAO.getComplexities();
    }

    public List<SubjectSetting> getQualities() throws SQLException {
        return workEvalDAO.getQualities();
    }

    public List<Team> getTeamsFromTeamMilestone() throws SQLException {
        return workEvalDAO.getTeamsFromTeamMilestone();
    }

    public String getReqNameByReqId(int reqId) {
        try {
            return workEvalDAO.getReqNameByReqId(reqId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle error appropriately (e.g., return a default value or throw a custom exception)
        }
    }

}
