/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ReviewCouncilDAO;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ReviewCouncil;

/**
 *
 * @author admin
 */
public class ReviewCouncilService {

    private final ReviewCouncilDAO councilDAO;

    public ReviewCouncilService() {
        councilDAO = new ReviewCouncilDAO();
    }

    public List<ReviewCouncil> getAllCouncils(int offset, int pageSize, String sortBy, String sortOrder) {
        return councilDAO.getAllCouncils(offset, pageSize, sortBy, sortOrder);
    }

    public ReviewCouncil getCouncilsById(int id) {
        return councilDAO.getCouncilsById(id);
    }

    public List<ReviewCouncil> searchCouncilsByName(String name, int offset, int pageSize) {
        return councilDAO.searchCouncilsByName(name, offset, pageSize);
    }

    public List<ReviewCouncil> getCouncilsByStatus(boolean status, int offset, int pageSize) {
        return councilDAO.getCouncilsByStatus(status, offset, pageSize);
    }

    public List<ReviewCouncil> getAllClasses() {
        return councilDAO.getAllClasses();
    }

    public List<ReviewCouncil> getAllSubjects() {
        return councilDAO.getAllSubjects();
    }

    public List<ReviewCouncil> getCouncilsByClass(String className, int offset, int pageSize) {
        return councilDAO.getCouncilsByClass(className, offset, pageSize);
    }

    public List<ReviewCouncil> getCouncilsBySubject(String subjectCode, int offset, int pageSize) {
        return councilDAO.getCouncilsBySubject(subjectCode, offset, pageSize);
    }

    public int countAllCouncils() {
        return councilDAO.countAllCouncils();
    }

    public int countCouncilsByName(String name) {
        return councilDAO.countCouncilsByName(name);
    }

    public int countCouncilsByStatus(boolean status) {
        return councilDAO.countCouncilsByStatus(status);
    }

    public int countCouncilsByClass(String className) {
        return councilDAO.countCouncilsByClass(className);
    }

    public int countCouncilsBySubject(String subjectCode) {
        return councilDAO.countCouncilsBySubject(subjectCode);
    }

    public void addCouncils(ReviewCouncil council) {
        councilDAO.addCouncils(council);
    }

    public void updateCouncils(ReviewCouncil council) {
        councilDAO.updateCouncils(council);
    }

    public void updateCouncilStatus(int id, boolean status) {
        councilDAO.updateCouncilStatus(id, status);
    }

    public List<ReviewCouncil> getCouncilsByPage(int page, int pageSize) {
        return councilDAO.getCouncilsByPage(page, pageSize);
    }


    public Map<String, String> validateCouncilsInfo(String name, String description, boolean status,Date createDate ,int classId) {
        Map<String, String> errorMessages = new HashMap<>();

        //Name validation
        if (name == null || name.isEmpty()) {
            errorMessages.put("name", "Name cannot be empty.");
        } else if (name.length() > 45) {
            errorMessages.put("nameLength", "Name cannot exceed 45 characters.");
        }

        // Description validation
        if (description == null || description.isEmpty()) {
            errorMessages.put("description", "Description cannot be left blank.");
        } else if (description.length() > 255) {
            errorMessages.put("descriptionLength", "Description cannot exceed 255 characters.");
        }
        
        // End date validation
        if (createDate == null) {
            errorMessages.put("createDate", "End date cannot be empty.");
        } else {
            LocalDate parsedEndDate = new java.sql.Date(createDate.getTime()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            if (parsedEndDate.isBefore(currentDate)) {
                errorMessages.put("createDate", "End date cannot be in the past.");
            }
        }

        // Class ID validation
        if (classId <= 0) {
            errorMessages.put("classId", "Class ID is not valid.");
        }

        return errorMessages;
    }
}
