/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ClassDAO;
import java.util.List;
import model.Class;

/**
 *
 * @author Do Duan
 */
public class ClassService {

    private ClassDAO classDAO;

    public Class searchClassByID(int id) {
        classDAO = new ClassDAO();
        if (classDAO.getClassByID(id) == null) {
            throw new IllegalArgumentException("Class not found");
        }
        return classDAO.getClassByID(id);
    }

    public boolean addClass(String name, String detail, String status_raw, String semesterID_raw, String groupID_raw) {

        Class c = new Class();
        int status = 0;
        if (status_raw != null && !status_raw.equals("")) {
            status = Integer.parseInt(status_raw);
        }
        c.setName(name);
        c.setDetail(detail);
        c.setStatus(status);
        int subjectID = Integer.parseInt(groupID_raw);
        int semesterID = Integer.parseInt(semesterID_raw);
        c.setSemesterID(semesterID);
        c.setGroupID(subjectID);
        classDAO = new ClassDAO();
        if (classDAO.createClass(c) != 0) {
            return true;
        }
        return false;
    }

    public List<Class> getClassList(int userID, String settingID_raw, String groupID_raw, String searchString,
            int itemPerPage, int pageNumber) {

        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (searchString == null) {
            searchString = "";
        }
        classDAO = new ClassDAO();
        try {
            int settingID = Integer.parseInt(settingID_raw);
            int groupID = Integer.parseInt(groupID_raw);
            return classDAO.searchClass(userID, settingID, groupID, searchString, itemPerPage, pageNumber);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Class> getClassList(int userID, int settingID, int groupID, String searchString
    ) {
        classDAO = new ClassDAO();
        return classDAO.searchClass(userID, settingID, groupID, searchString);
    }

    public int getTotalRecords(int userID, String settingID_raw, String groupID_raw, String searchString, int limit, int offset) {
        classDAO = new ClassDAO();
        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (searchString == null) {
            searchString = "";
        }
        int settingID = Integer.parseInt(settingID_raw);
        int groupID = Integer.parseInt(groupID_raw);

        return classDAO.getTotalRecord(userID, settingID, groupID, searchString, limit, offset);
    }

    public int getTotalRecords(int userID, String settingID_raw, String groupID_raw, String searchString) {
        classDAO = new ClassDAO();
        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (searchString == null) {
            searchString = "";
        }
        int settingID = Integer.parseInt(settingID_raw);
        int groupID = Integer.parseInt(groupID_raw);

        return classDAO.getTotalRecord(userID, settingID, groupID, searchString);
    }

    public int getTotalClassByDept(int userID, String settingID_raw, String groupID_raw, String status_raw, String searchString) {
        classDAO = new ClassDAO();
        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (status_raw == null) {
            status_raw = "-1";
        }
        if (searchString == null) {
            searchString = "";
        }
        int settingID = Integer.parseInt(settingID_raw);
        int groupID = Integer.parseInt(groupID_raw);
        int status = Integer.parseInt(status_raw);
        return classDAO.getTotalClassByDept(userID, settingID, groupID, status, searchString);
    }

    public int getTotalClassByTeacher(int userID, String settingID_raw, String groupID_raw, String status_raw, String searchString) {
        classDAO = new ClassDAO();
        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (status_raw == null) {
            status_raw = "-1";
        }
        if (searchString == null) {
            searchString = "";
        }
        int settingID = Integer.parseInt(settingID_raw);
        int groupID = Integer.parseInt(groupID_raw);
        int status = Integer.parseInt(status_raw);
        return classDAO.getTotalClassByTeacher(userID, settingID, groupID, status, searchString);
    }

    public int getTotalClass() {
        classDAO = new ClassDAO();
        return classDAO.getTotalClass();
    }

    public List<Class> getClassListByUserID(int userID, int subjectID) {
//        int subjectID = 0;
//        if (subjectID_raw != null) {
//            subjectID = Integer.parseInt(subjectID_raw);
//        }
        classDAO = new ClassDAO();
        return classDAO.getClassListByUserID(userID, subjectID);
    }

    public List<Class> getAllClass() {
        classDAO = new ClassDAO();
        return classDAO.getAllClass();
    }

    public List<Class> getAllClass(String settingID_raw, String groupID_raw, String status_raw, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {
        classDAO = new ClassDAO();

        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (status_raw == null) {
            status_raw = "-1";
        }
        if (searchString == null) {
            searchString = "";
        }
        classDAO = new ClassDAO();
        try {
            int settingID = Integer.parseInt(settingID_raw);
            int groupID = Integer.parseInt(groupID_raw);
            int status = Integer.parseInt(status_raw);
            return classDAO.getAllClass(settingID, groupID, status, searchString, itemPerPage, pageNumber, sortBy, sortOrder);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Class> getClassListByDeptUserID(int userID, String settingID_raw, String groupID_raw, String status_raw, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {

        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (status_raw == null) {
            status_raw = "-1";
        }
        if (searchString == null) {
            searchString = "";
        }

        classDAO = new ClassDAO();
        try {
            int settingID = Integer.parseInt(settingID_raw);
            int groupID = Integer.parseInt(groupID_raw);
            int status = Integer.parseInt(status_raw);
            return classDAO.getClassListByDeptUserID(userID, settingID, groupID, status, searchString, itemPerPage, pageNumber, sortBy, sortOrder);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Class> getClassListByTeacherID(int userID, String settingID_raw, String groupID_raw, String status_raw, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {

        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (status_raw == null) {
            status_raw = "-1";
        }
        if (searchString == null) {
            searchString = "";
        }

        classDAO = new ClassDAO();

        try {
            int settingID = Integer.parseInt(settingID_raw);
            int groupID = Integer.parseInt(groupID_raw);
            int status = Integer.parseInt(status_raw);
            return classDAO.getClassListByTeacherID(userID, settingID, groupID, status, searchString, itemPerPage, pageNumber, sortBy, sortOrder);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateStatusClass(int id, int newStatus) {
        classDAO = new ClassDAO();
        return classDAO.updateStatusClass(id, newStatus);
    }

    public boolean updateClass(Class updatedClass) {

        classDAO = new ClassDAO();
        if (classDAO.updateClass(updatedClass) > 0) {
            return true;
        }
        return false;
    }

    public Class getClassByMilestoneID(String id_raw) {
        classDAO = new ClassDAO();
        int id = Integer.parseInt(id_raw);
        return classDAO.getClassByMilestoneID(id);
    }

    public boolean checkClassIsExist(String className, String subjectId, String semeterID) {
        classDAO = new ClassDAO();
        if (classDAO.checkClassNameExist(className, subjectId, semeterID)) {
            return true;
        }
        return false;
    }

    public int getClassID(String className, String subjectId, String semeterID) {
        classDAO = new ClassDAO();
        return classDAO.getClassID(className, subjectId, semeterID);
    }

    public List<Class> getClassTeacher(int userID, String settingID_raw, String groupID_raw, String searchString,
            int itemPerPage, int pageNumber) {
        if (settingID_raw == null) {
            settingID_raw = "0";
        }
        if (groupID_raw == null) {
            groupID_raw = "0";
        }
        if (searchString == null) {
            searchString = "";
        }
        classDAO = new ClassDAO();
        try {
            int settingID = Integer.parseInt(settingID_raw);
            int groupID = Integer.parseInt(groupID_raw);
            classDAO.getClassListByTeacherID(userID, settingID, groupID, 1, searchString, itemPerPage, pageNumber, "id", "ASC");
            return classDAO.getClassTeacher(userID, settingID, groupID, searchString, itemPerPage, pageNumber);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
