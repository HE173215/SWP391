package service;

import dao.SubjectSettingDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.SubjectSetting;

/**
 *
 * @author vqman
 */
public class SubjectSettingService {

    private final SubjectSettingDAO subjectSettingDAO;
    private Map<String, String> messages = new HashMap<>();

    // Constructor để khởi tạo DAO
    public SubjectSettingService() {
        this.subjectSettingDAO = new SubjectSettingDAO();
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    // Get SubjectSettings by groupId with pagination
    public List<SubjectSetting> getSubjectSettings(int groupId, int limit, int offset) {
        return subjectSettingDAO.getSubjectSettingsSubjectId(groupId, limit, offset);
    }

    // Get SubjectSettings by type and groupId with pagination
    public List<SubjectSetting> getSubjectSettingByType(int groupId, String type, int limit, int offset) {
        return subjectSettingDAO.getSubjectSettingByType(groupId, type, limit, offset);
    }

    // Get SubjectSettings by groupId and status with pagination
    public List<SubjectSetting> getSubjectSettingByStatus(int groupId, boolean status, int limit, int offset) {
        return subjectSettingDAO.getSubjectSettingByStatus(groupId, status, limit, offset);
    }

    // Search SubjectSettings by name with pagination
    public List<SubjectSetting> searchSubjectSettingByName(int groupId, String name, int limit, int offset) {
        return subjectSettingDAO.searchSubjectSettingByName(groupId, name, limit, offset);
    }

    // Get all distinct types of SubjectSettings
    public List<String> getAllTypes() {
        return subjectSettingDAO.getAllTypes();
    }

    // Get SubjectSettings by type and status with pagination
    public List<SubjectSetting> getSubjectSettingsFilter(int groupId, String type, boolean status, int offset, int limit) {
        try {
            // Call the DAO method to fetch the data
            List<SubjectSetting> subjectSettings = subjectSettingDAO.getSubjectSettingByTypeAndStatus(groupId, type, status, offset, limit);
            // Additional business logic if needed
            return subjectSettings;
        } catch (Exception e) {
            // Log error and handle exception accordingly
            System.err.println("Error fetching SubjectSettings: " + e.getMessage());
            // Depending on the context, you could throw a custom exception here
            throw new RuntimeException("Failed to fetch subject settings", e);
        }
    }

    public boolean addSubjectSetting(SubjectSetting subjectSetting) {
        messages.clear(); // Xóa thông báo lỗi trước đó
        boolean isValid = true;
        // Kiểm tra tên không được để trống
        if (subjectSetting.getName() == null || subjectSetting.getName().isEmpty()) {
            messages.put("name", "Tên của setting không được để trống.");
            isValid = false;
        }

        // Kiểm tra độ dài tên trong khoảng từ 1 đến 45 ký tự
        if (subjectSetting.getName().length() < 3 || subjectSetting.getName().length() > 45) {
            messages.put("name", "Tên của setting phải nằm trong khoảng từ 3 đến 45 ký tự.");
            isValid = false;
        }

        // Kiểm tra trùng lặp tên và loại
        if (subjectSettingDAO.isDuplicateNameAndType(subjectSetting.getName(), subjectSetting.getType())) {
            messages.put("dup", "Tên và loại của setting đã tồn tại.");
            isValid = false;
        }
        // Nếu có bất kỳ lỗi nào, trả về false mà không thêm dữ liệu
        if (!isValid) {
            return false;
        }

        // Thực hiện thêm nếu tất cả điều kiện thỏa mãn
        return subjectSettingDAO.addSubjectSetting(subjectSetting);
    }

    public boolean editSubjectSetting(SubjectSetting subjectSetting) {
        messages.clear(); // Xóa thông báo lỗi trước đó
        boolean isValid = true;
        // Kiểm tra tên không được để trống
        if (subjectSetting.getName() == null || subjectSetting.getName().isEmpty()) {
            messages.put("name", "Tên của setting không được để trống.");
            isValid = false;
        }

        // Kiểm tra độ dài tên trong khoảng từ 1 đến 45 ký tự
        if (subjectSetting.getName().length() < 3 || subjectSetting.getName().length() > 45) {
            messages.put("name", "Tên của setting phải nằm trong khoảng từ 3 đến 45 ký tự.");
            isValid = false;
        }
        if (!isValid) {
            return false;
        }
        // Không kiểm tra trùng lặp khi chỉnh sửa nếu cần cập nhật bản ghi hiện tại
        return subjectSettingDAO.editSubjectSetting(subjectSetting);
    }

    // Get a SubjectSetting by its ID
    public SubjectSetting getSubjectSettingById(int id) {
        return subjectSettingDAO.getSubjectSettingById(id);
    }

    // Update the status of a SubjectSetting
    public void updateSubjectSettingStatus(int id, int newStatus) {
        subjectSettingDAO.updateSubjectSettingByStatus(id, newStatus);
    }

    // Get the group name by its ID
    public String getGroupNameById(int id) {
        return subjectSettingDAO.getGroupNameById(id);
    }

    // Count methods for pagination purposes
    // Count all SubjectSettings by groupId
    public int countSubjectSettingsByGroupId(int groupId) {
        return subjectSettingDAO.countSubjectSettings(groupId, null, null, null);
    }

    // Count SubjectSettings by type and groupId
    public int countSubjectSettingsByType(int groupId, String type) {
        return subjectSettingDAO.countSubjectSettings(groupId, type, null, null);
    }

    // Count SubjectSettings by groupId and status
    public int countSubjectSettingsByStatus(int groupId, boolean status) {
        return subjectSettingDAO.countSubjectSettings(groupId, null, status, null);
    }

    // Count SubjectSettings by name and groupId
    public int countSubjectSettingsByName(int groupId, String name) {
        return subjectSettingDAO.countSubjectSettings(groupId, null, null, name);
    }

    // Count SubjectSettings by type, status, and groupId
    public int countSubjectSettingsByTypeAndStatus(int groupId, String type, boolean status) {
        return subjectSettingDAO.countSubjectSettings(groupId, type, status, null);
    }
}
