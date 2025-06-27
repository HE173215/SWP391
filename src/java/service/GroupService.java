/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.GroupDAO;
import java.util.List;
import model.Group;

/**
 *
 * @author vqman
 */
public class GroupService {

    private GroupDAO groupDAO;

    // Constructor khởi tạo GroupDAO
    public GroupService() {
        this.groupDAO = new GroupDAO();
    }

    // Lấy tất cả cài đặt với phân trang
    public List<Group> getAllGroups(int offset, int pageSize) {
        return groupDAO.getAllGroups(offset, pageSize);
    }

    // Lấy thông tin cài đặt dựa vào ID
    public Group getGroupById(int id) {
        return groupDAO.getGroupById(id);
    }

    // Tìm kiếm cài đặt dựa theo tên với phân trang
    public List<Group> searchGroupByName(String name, int offset, int pageSize) {
        return groupDAO.searchGroupByName(name, offset, pageSize);
    }

    // Đếm tổng số bản ghi khi tìm kiếm theo tên
    public int countGroupsByName(String name) {
        return groupDAO.countGroupsByName(name);
    }

    // Lấy cài đặt theo loại với phân trang
    public List<Group> getGroupsByType(String type, int offset, int pageSize) {
        return groupDAO.getGroupsByType(type, offset, pageSize);
    }
    
    public List<Group> getGroupsByType(String type) {
        return groupDAO.getGroupsByType(type);
    }

    // Đếm tổng số bản ghi khi lọc theo loại
    public int countGroupsByType(String type) {
        return groupDAO.countGroupsByType(type);
    }

    public List<Group> getGroupsByStatus(int status, int offset, int pageSize) {
        return groupDAO.getGroupsByStatus(status, offset, pageSize);
    }

    public int countGroupsByStatus(int status) {
        return groupDAO.countGroupsByStatus(status);
    }

    // Thêm mới cài đặt
    public void addGroup(Group group) {
        groupDAO.addGroup(group);
    }

    // Cập nhật cài đặt
    public void updateGroup(Group group) {
        if (group != null && group.getId() > 0) {
            // Gọi phương thức cập nhật từ DAO
            groupDAO.updateGroup(group);
        } else {
            System.out.println("Dữ liệu không hợp lệ để cập nhật.");
        }
    }

    public void updateGroupStatus(int id, int newStatus) {
        groupDAO.updateGroupByStatus(id, newStatus);
    }

    public List<Group> filterGroups(String typeFilter, String statusFilter, int offset, int pageSize) {
        // Bạn có thể thêm các kiểm tra và xử lý nghiệp vụ tại đây nếu cần
        return groupDAO.filterGroups(typeFilter, statusFilter, offset, pageSize);
    }

    public int countGroupsByFilters(String typeFilter, String statusFilter) {
        // Bạn có thể thêm các kiểm tra và xử lý nghiệp vụ tại đây nếu cần
        return groupDAO.countGroupsByFilters(typeFilter, statusFilter);
    }

    // Lấy tất cả các loại (types) cài đặt
    public List<String> getAllTypes() {
        return groupDAO.getAllTypes();
    }

    // Đếm tổng số bản ghi
    public int countAll() {
        return groupDAO.countAll();
    }

    public boolean isGroupNameExists(String groupName) {
        return groupDAO.isGroupNameExists(groupName);
    }
    
     public List<Group> getSubjectList(){
        groupDAO = new GroupDAO();
        return groupDAO.getSubjectList();
    }
     
     public List<Group> getSubjectListByUserID(int userID) {
         return groupDAO.getSubjectListByUserID(userID);
     }
     
     public Group getSubjectByClassID(String classID_raw){
         if(classID_raw == null){
             throw new IllegalArgumentException("ClassID null");
         }
         try {
             int classID = Integer.parseInt(classID_raw);
             return groupDAO.getSubjectByClassID(classID);
         } catch (NumberFormatException e) {
             e.printStackTrace();
         }
         return null;
     }
}
