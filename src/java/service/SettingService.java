package service;

import dao.SettingDAO;
import java.util.List;
import model.Setting;

/**
 *
 * @author vqman
 */
public class SettingService {

    private SettingDAO settingDAO;

    // Constructor khởi tạo SettingDAO
    public SettingService() {
        this.settingDAO = new SettingDAO();
    }

    // Lấy tất cả cài đặt với phân trang
    public List<Setting> getAllSettings(int offset, int pageSize) {
        return settingDAO.getAllSettings(offset, pageSize);
    }

    // Lấy thông tin cài đặt dựa vào ID
    public Setting getSettingById(int id) {
        return settingDAO.getSettingById(id);
    }

    // Tìm kiếm cài đặt dựa theo tên với phân trang
    public List<Setting> searchSettingByName(String name, int offset, int pageSize) {
        return settingDAO.searchSettingByName(name, offset, pageSize);
    }

    // Đếm tổng số bản ghi khi tìm kiếm theo tên
    public int countSettingsByName(String name) {
        return settingDAO.countSettingsByName(name);
    }

    // Lấy cài đặt theo loại với phân trang
    public List<Setting> getSettingsByType(String type, int offset, int pageSize) {
        return settingDAO.getSettingsByType(type, offset, pageSize);
    }

    public List<Setting> getSettingsByType(String type) {
        return settingDAO.getSettingsByType(type);
    }

    public List<Setting> getSettingsByType(String type, int status) {
        return settingDAO.getSettingsByType(type, status);
    }

    // Đếm tổng số bản ghi khi lọc theo loại
    public int countSettingsByType(String type) {
        return settingDAO.countSettingsByType(type);
    }

    public List<Setting> getSettingsByStatus(int status, int offset, int pageSize) {
        return settingDAO.getSettingsByStatus(status, offset, pageSize);
    }

    public int countSettingsByStatus(int status) {
        return settingDAO.countSettingsByStatus(status);
    }

    // Thêm mới cài đặt với kiểm tra hợp lệ
    public String addSetting(Setting setting) {
        String validationError = validateSetting(setting);
        if (validationError == null) {
            settingDAO.addSetting(setting);
            return null;  // Không có lỗi
        } else {
            return validationError; // Trả về thông báo lỗi cụ thể
        }
    }

    // Cập nhật cài đặt với kiểm tra hợp lệ
    public String updateSetting(Setting setting) {
        String validationError = validateSetting(setting);
        if (setting != null && setting.getId() > 0 && validationError == null) {
            settingDAO.updateSetting(setting);
            return null;  // Không có lỗi
        } else {
            return validationError != null ? validationError : "Dữ liệu không hợp lệ để cập nhật."; // Trả về thông báo lỗi cụ thể
        }
    }

    // Phương thức kiểm tra hợp lệ cho cài đặt, trả về chuỗi lỗi nếu có
    private String validateSetting(Setting setting) {
        if (setting == null) {
            return "Cài đặt không được rỗng.";
        }

        // Kiểm tra tên cài đặt không được rỗng
        if (setting.getSettingName()== null || setting.getSettingName().isEmpty()) {
            return "Tên cài đặt không được để trống.";
        }

        // Kiểm tra độ dài của tên cài đặt
        if (setting.getSettingName().length() < 3 || setting.getSettingName().length() > 50) {
            return "Độ dài của tên cài đặt phải nằm trong khoảng từ 3 đến 50 ký tự.";
        }

        if (isSettingExists(setting.getSettingName(), setting.getType())) {
            return "Tên cài đặt đã tồn tại.";
        }

        // Kiểm tra loại không được rỗng
        if (setting.getType() == null || setting.getType().isEmpty()) {
            return "Loại cài đặt không được để trống.";
        }

        // Các kiểm tra hợp lệ khác nếu cần
        return null; // Trả về null nếu không có lỗi

    }

    public void updateSettingStatus(int id, int newStatus) {
        settingDAO.updateSettingByStatus(id, newStatus);
    }

    public List<Setting> filterSettings(String typeFilter, String statusFilter, int offset, int pageSize) {
        // Bạn có thể thêm các kiểm tra và xử lý nghiệp vụ tại đây nếu cần
        return settingDAO.filterSettings(typeFilter, statusFilter, offset, pageSize);
    }

    public int countSettingsByFilters(String typeFilter, String statusFilter) {
        // Bạn có thể thêm các kiểm tra và xử lý nghiệp vụ tại đây nếu cần
        return settingDAO.countSettingsByFilters(typeFilter, statusFilter);
    }

    // Lấy tất cả các loại (types) cài đặt
    public List<String> getAllTypes() {
        return settingDAO.getAllTypes();
    }

    // Đếm tổng số bản ghi
    public int countAll() {
        return settingDAO.countAll();
    }

    public boolean isSettingExists(String settingName, String type) {
        return settingDAO.isSettingExists(settingName, type);
    }

}
