/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AssignmentDAO;
import dao.MilestoneDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Assignment;
import model.Milestone;

/**
 *
 * @author Do Duan
 */
public class MilestoneService {

    private MilestoneDAO milestoneDAO;
    private AssignmentDAO assignmentDAO;

    public MilestoneService() {
        milestoneDAO = new MilestoneDAO();
        assignmentDAO = new AssignmentDAO();
    }

    public List<Milestone> getMilestoneListByClassID(String classID_raw) {
        try {
            int classID = Integer.parseInt(classID_raw);
            return milestoneDAO.getMilestoneListByClassID(classID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Milestone getMilestoneById(int milestoneId) {
        return milestoneDAO.getMilestoneById(milestoneId);
    }

    public List<Assignment> getAllNames() {
        return milestoneDAO.getAllNames();
    }

    public boolean updateMilestone(Milestone milestone) {
        try {
            milestoneDAO.updateMilestone(milestone);
            return true; // Return true if update succeeds
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }

    public boolean validateMilestone(Milestone milestone) {
        if (milestone.getCode() == null || milestone.getCode().isEmpty()) {
            System.out.println("Milestone code is required.");
            return false;
        }
        if (milestone.getName() == null || milestone.getName().isEmpty()) {
            System.out.println("Milestone name is required.");
            return false;
        }
        if (milestone.getWeight() <= 0) {
            System.out.println("Milestone weight must be greater than 0.");
            return false;
        }
        if (milestone.getEndDate() == null) {
            System.out.println("Milestone end date is required.");
            return false;
        }
        return true;
    }

    public Map<String, String> validateMilestonesInfo(String code, String name, int priority, int weight, String detail, int max_eval_value, Date endDate) {
        Map<String, String> errorMessages = new HashMap<>();

        // Code validation
        if (code == null || code.isEmpty()) {
            errorMessages.put("code", "Code cannot be empty.");
        } else if (code.length() > 45) {
            errorMessages.put("codeLength", "Code cannot exceed 45 characters.");
        }

        // Name validation
        if (name == null || name.isEmpty()) {
            errorMessages.put("name", "Name cannot be empty.");
        } else if (name.length() > 45) {
            errorMessages.put("nameLength", "Name cannot exceed 45 characters.");
        }

        // Detail validation
        if (detail == null || detail.isEmpty()) {
            errorMessages.put("detail", "Details cannot be left blank.");
        } else if (detail.length() > 255) {
            errorMessages.put("detailLength", "Details cannot exceed 255 characters.");
        }

        // Priority validation (assuming it should be a positive integer)
        if (priority <= 0) {
            errorMessages.put("priority", "Priority must be a positive integer.");
        }
        // Weight validation (assuming a range of 0 to 100)
        if (weight < 0 || weight > 100) {
            errorMessages.put("weight", "Weight must be between 0 and 100.");
        }

        // Max evaluation value validation (assuming a positive integer)
        if (max_eval_value <= 0) {
            errorMessages.put("max_eval_value", "Max evaluation value must be a positive integer.");
        }
        // End date validation
        if (endDate == null) {
            errorMessages.put("endDate", "End date cannot be empty.");
        } else {
            LocalDate parsedEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            if (parsedEndDate.isBefore(currentDate)) {
                errorMessages.put("endDate", "End date cannot be in the past.");
            }
        }

        return errorMessages;
    }

    public int cloneMilestoneToClass(String classID_raw) {
        int classID = Integer.parseInt(classID_raw);
        GroupService groupService = new GroupService();
        int subjectID = groupService.getSubjectByClassID(classID_raw).getId();

//        System.out.println(subjectID);
        List<Assignment> assignmentList = assignmentDAO.getAssignmentsListBySubjectId(subjectID);
        int count = 0;
        for (Assignment assignment : assignmentList) {
//            System.out.println(category.toString());
            if (milestoneDAO.insertMilestoneToClass(classID, assignment) > 0) {
                count++;
            }
        }
        return count;
    }

    public List<Milestone> getAllMilestones() throws SQLException {
        return milestoneDAO.getAllMilestones();
    }

    public List<Assignment> getAssignmentsBySubjectId(int subjectId) {
        return milestoneDAO.getAssignmentsBySubjectId(subjectId);
    }

    public List<Assignment> getAssignmentsForMilestone(int milestoneId) {
        Milestone milestone = milestoneDAO.getMilestonesById(milestoneId);
        if (milestone != null) {
            return getAssignmentsBySubjectId(milestone.getAssigmentId());
        }

        return new ArrayList<>();
    }
}
