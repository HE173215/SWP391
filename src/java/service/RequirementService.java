/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.RequirementDAO;
import java.util.List;
import model.Requirement;
import model.User;

/**
 *
 * @author Do Duan
 */
public class RequirementService {

    private RequirementDAO requirementDAO;

    public List<Requirement> getRequirement(int classID, int userID, int teamID, String complexityID_raw, String status_raw, String searchStr,
            int limit, int offset) {

        requirementDAO = new RequirementDAO();
        int complexityID = 0;
        if (complexityID_raw != null) {
            complexityID = Integer.parseInt(complexityID_raw);

        }

        int status = -1;
        if (status_raw != null) {
            status = Integer.parseInt(status_raw);
        }

        if (searchStr == null) {
            searchStr = "";
        }

        return requirementDAO.getRequirement(classID, userID, teamID, complexityID, status, searchStr, limit, offset);
    }

    public int getTotalRecordPersonal(int classID, int userID, int teamID, String complexityID_raw, String status_raw, String searchStr) {
        requirementDAO = new RequirementDAO();
        int complexityID = 0;
        if (complexityID_raw != null) {
            complexityID = Integer.parseInt(complexityID_raw);

        }

        int status = -1;
        if (status_raw != null) {
            status = Integer.parseInt(status_raw);
        }

        if (searchStr == null) {
            searchStr = "";
        }
        return requirementDAO.getTotalRecordPersonal(classID, userID, teamID, complexityID, status, searchStr);
    }

    public List<Requirement> getTeamRequirements(int teamID, String complexityID_raw, String ownerID_raw, String status_raw, String searchStr,
            int limit, int offset) {
        requirementDAO = new RequirementDAO();
        int complexityID = 0;
        if (complexityID_raw != null && !complexityID_raw.equals("")) {
            complexityID = Integer.parseInt(complexityID_raw);

        }
        int ownerID = 0;
        if (ownerID_raw != null && !ownerID_raw.equals("")) {
            ownerID = Integer.parseInt(ownerID_raw);

        }
        int status = -1;
        if (status_raw != null && !status_raw.equals("")) {
            status = Integer.parseInt(status_raw);
        }

        if (searchStr == null) {
            searchStr = "";
        }
        return requirementDAO.getRequirementByTeamID(teamID, complexityID, ownerID, status, searchStr, limit, offset);
    }

    public int getTotalRecordTeam(int teamID, String complexityID_raw, String ownerID_raw, String status_raw, String searchStr) {
        requirementDAO = new RequirementDAO();
        int complexityID = 0;
        if (complexityID_raw != null) {
            complexityID = Integer.parseInt(complexityID_raw);

        }
        int ownerID = 0;
        if (ownerID_raw != null) {
            ownerID = Integer.parseInt(ownerID_raw);

        }
        int status = -1;
        if (status_raw != null) {
            status = Integer.parseInt(status_raw);
        }

        if (searchStr == null) {
            searchStr = "";
        }
        return requirementDAO.getTotalRecordTeam(teamID, complexityID, ownerID, status, searchStr);
    }

    public Requirement getRequirementByID(String id_raw) {
        requirementDAO = new RequirementDAO();
        try {
            int id = Integer.parseInt(id_raw);
            return requirementDAO.getRequirementByID(id);

        } catch (Exception e) {
        }
        return null;
    }

    public User getMemberByRequirementID(String reqID_raw) {
        requirementDAO = new RequirementDAO();
        try {
            int reqID = Integer.parseInt(reqID_raw);
            return requirementDAO.getMemberByRequirementID(reqID);

        } catch (Exception e) {
        }
        return null;
    }

    public boolean validateInput(String tittle) {
        if (tittle.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public int addNewRequirement(String tittle, String complexityID_raw, String teamID_raw, String ownerID_raw, String status_raw, String description, String creatorID_raw, String milestoneID_raw) {
        requirementDAO = new RequirementDAO();
        Requirement r = new Requirement();
        r.setTittle(tittle);
        int complexityID = Integer.parseInt(complexityID_raw);
        r.setComplexityID(complexityID);
        if (!ownerID_raw.equals("") && ownerID_raw != null && !ownerID_raw.isBlank()) {
            int ownerID = Integer.parseInt(ownerID_raw);
            r.setOwnerID(ownerID);
        }
//        else {
//            r.setOwnerID(0);
//        }
        int teamID = Integer.parseInt(teamID_raw);
        r.setTeamID(teamID);
        int status = 0;
        if (status_raw != null && !status_raw.equals("")) {
            status = Integer.parseInt(status_raw);
        }
        r.setStatus(status);
        r.setDescription(description);
        if (creatorID_raw != null) {
            int creatorID = Integer.parseInt(creatorID_raw);
            r.setCreatorID(creatorID);
        }
        try {
            int reqID = requirementDAO.createRequirement(r);
            int milestoneID = Integer.parseInt(milestoneID_raw);
            return requirementDAO.putReqToWorkEval(reqID, milestoneID);
        } catch (Exception e) {
        }
        return 0;
    }

    public int updateRequirement(String id_raw, String tittle, String complexityID_raw, String teamID_raw, String ownerID_raw, String status_raw, String description,String milestoneID_raw) {
        requirementDAO = new RequirementDAO();
        Requirement r = new Requirement();

        int id = Integer.parseInt(id_raw);
        r.setId(id);
        r.setTittle(tittle);
        int complexityID = Integer.parseInt(complexityID_raw);
        r.setComplexityID(complexityID);
        if (!ownerID_raw.equals("")) {
            int ownerID = Integer.parseInt(ownerID_raw);
            r.setOwnerID(ownerID);
        }
        int teamID = Integer.parseInt(teamID_raw);
        r.setTeamID(teamID);
        int status = 0;
        if (status_raw != null && !status_raw.equals("")) {
            status = Integer.parseInt(status_raw);
        }
        r.setStatus(status);
        r.setDescription(description);
         requirementDAO.updateRequirement(r);
        return requirementDAO.updateMilestone(id, milestoneID_raw);
    }

    public int updateStatus(String id_raw, String newStatus_raw) {
        requirementDAO = new RequirementDAO();
        int id = Integer.parseInt(id_raw);
        int newStatus = Integer.parseInt(newStatus_raw);
        return requirementDAO.updateStatus(id, newStatus);
    }

    public List<Requirement> getClassRequirement(int classID, String teamID, String milestoneID, String status, String search,
            int limit, int offset) {
        requirementDAO = new RequirementDAO();
        if (teamID == null) {
            teamID = "";
        }
        if (milestoneID == null) {
            milestoneID = "";
        }
        if (status == null) {
            status = "";
        }
        if (search == null) {
            search = "";
        }
        return requirementDAO.getClassRequirement(classID, teamID, milestoneID, status, search, limit, offset);
    }

    public int getTotalRecordClassRequirement(int classID, String teamID, String milestoneID, String status, String search) {
        requirementDAO = new RequirementDAO();

        if (teamID == null) {
            teamID = "";
        }
        if (milestoneID == null) {
            milestoneID = "";
        }
        if (status == null) {
            status = "";
        }
        if (search == null) {
            search = "";
        }

        return requirementDAO.getTotalRecordClassRequirement(classID, teamID, milestoneID, status, search);
    } 

}
