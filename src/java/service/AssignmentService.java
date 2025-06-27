/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AssignmentDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Assignment;

/**
 *
 * @author admin
 */
public class AssignmentService {

    private final AssignmentDAO assignmentDAO;

    public AssignmentService() {
        assignmentDAO = new AssignmentDAO();
    }

    public List<Assignment> getAllAssignments( String sortBy, String sortOrder) {
        return assignmentDAO.getAllAssignments( sortBy, sortOrder);
    }

    public Assignment getAssignmentsById(int id) {
        return assignmentDAO.getAssignmentsById(id);
    }

    public List<Assignment> searchAssignmentsByName(String name) {
        return assignmentDAO.searchAssignmentsByName(name);
    }

    public List<Assignment> getAssignmentsByStatus(boolean status) {
        return assignmentDAO.getAssignmentsByStatus(status);
    }

    public List<Assignment> getAllCodes() {
        return assignmentDAO.getAllCodes();
    }

    public List<Assignment> getAssignmentsByCode(String code) {
        return assignmentDAO.getAssignmentsByCode(code);
    }

    public List<Assignment> getAssignmentsByCodeAndStatus(String code, boolean status) {
        return assignmentDAO.getAssignmentsByCodeAndStatus(code, status);
    }

    public void addAssignment(Assignment assigment) {
        assignmentDAO.addAssignment(assigment);
    }

    public void updateAssignment(Assignment assigment) {
        assignmentDAO.updateAssignment(assigment);
    }

    public void updateAssignmentsStatus(int id, boolean status) {
        assignmentDAO.updateAssignmentsStatus(id, status);
    }

    public Map<String, String> validateAssignmentInfo(String name, int weight, String detail, boolean final_assignment, boolean status, int group_id) {
        Map<String, String> errorMessages = new HashMap<>();

        if (name == null || name.isEmpty()) {
            errorMessages.put("name", "Assignment name cannot be empty.");
        }
        if (weight <= 0 || weight > 100) {
            errorMessages.put("weight", "Assignment weight must be between 1 and 100.");
        }
        if (detail == null || detail.isEmpty()) {
            errorMessages.put("detail", "Assignment details cannot be left blank!");
        }

        if (name.length() > 45) {
            errorMessages.put("nameLength", "Assignment name cannot exceed 45 characters.");
        }
        if (detail.length() > 255) {
            errorMessages.put("detailLength", "Assignment details cannot exceed 255 characters.");
        }

        if (group_id <= 0) {
            errorMessages.put("group_id", "Group ID is not valid.");
        }

        return errorMessages;
    }
    
    public List<Assignment> getAssignmentsListBySubjectId(int subjectID) {
        return assignmentDAO.getAssignmentsListBySubjectId(subjectID);
    }
   public List<Assignment> getAssignmentsBySubjectId( int subjectID) {
        return assignmentDAO.getAssignmentsBySubjectId( subjectID);
    }


}
