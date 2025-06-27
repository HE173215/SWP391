/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SubjectSetting;
import util.JDBCUtils;

/**
 *
 * @author vqman
 */
public class SubjectSettingDAO {

    public List<SubjectSetting> getSubjectSettingsSubjectId(int groupId, int limit, int offset) {
        List<SubjectSetting> subjectSettings = new ArrayList<>();
        String sql = "SELECT ss.* "
                + "FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ? AND g.type = 'subject' "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, limit); // Số bản ghi cần lấy
            stmt.setInt(3, offset); // Bắt đầu từ vị trí nào

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubjectSetting subjectSetting = new SubjectSetting();
                    subjectSetting.setId(rs.getInt("id"));
                    subjectSetting.setName(rs.getString("name"));
                    subjectSetting.setType(rs.getString("type"));
                    subjectSetting.setValue(rs.getInt("value"));
                    subjectSetting.setStatus(rs.getBoolean("status"));
                    subjectSetting.setSubjectId(rs.getInt("subject_id"));
                    subjectSettings.add(subjectSetting);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectSettings;
    }

    public List<SubjectSetting> getSubjectSettingByType(int groupId, String type, int limit, int offset) {
        List<SubjectSetting> subjectSettings = new ArrayList<>();
        String sql = "SELECT ss.* "
                + "FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ? AND ss.type = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setString(2, type);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubjectSetting subjectSetting = new SubjectSetting();
                    subjectSetting.setId(rs.getInt("id"));
                    subjectSetting.setName(rs.getString("name"));
                    subjectSetting.setType(rs.getString("type"));
                    subjectSetting.setValue(rs.getInt("value"));
                    subjectSetting.setStatus(rs.getBoolean("status"));
                    subjectSetting.setSubjectId(rs.getInt("subject_id"));
                    subjectSettings.add(subjectSetting);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectSettings;
    }

    public List<SubjectSetting> getSubjectSettingByStatus(int groupId, boolean status, int limit, int offset) {
        List<SubjectSetting> subjectSettings = new ArrayList<>();
        String sql = "SELECT ss.* "
                + "FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ? AND ss.status = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setBoolean(2, status);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubjectSetting subjectSetting = new SubjectSetting();
                    subjectSetting.setId(rs.getInt("id"));
                    subjectSetting.setName(rs.getString("name"));
                    subjectSetting.setType(rs.getString("type"));
                    subjectSetting.setValue(rs.getInt("value"));
                    subjectSetting.setStatus(rs.getBoolean("status"));
                    subjectSetting.setSubjectId(rs.getInt("subject_id"));
                    subjectSettings.add(subjectSetting);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectSettings;
    }

    public List<SubjectSetting> searchSubjectSettingByName(int groupId, String name, int limit, int offset) {
        List<SubjectSetting> subjectSettings = new ArrayList<>();
        String sql = "SELECT ss.* "
                + "FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ? AND ss.name LIKE ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setString(2, "%" + name + "%");
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubjectSetting subjectSetting = new SubjectSetting();
                    subjectSetting.setId(rs.getInt("id"));
                    subjectSetting.setName(rs.getString("name"));
                    subjectSetting.setType(rs.getString("type"));
                    subjectSetting.setValue(rs.getInt("value"));
                    subjectSetting.setStatus(rs.getBoolean("status"));
                    subjectSetting.setSubjectId(rs.getInt("subject_id"));
                    subjectSettings.add(subjectSetting);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectSettings;
    }

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM subject_setting";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    public List<SubjectSetting> getSubjectSettingByTypeAndStatus(int groupId, String type, boolean status, int offset, int limit) {
        List<SubjectSetting> subjectSettings = new ArrayList<>();
        String sql = "SELECT ss.* "
                + "FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ? AND ss.type = ? AND ss.status = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setString(2, type);
            stmt.setBoolean(3, status);
            stmt.setInt(4, limit);
            stmt.setInt(5, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SubjectSetting subjectSetting = new SubjectSetting();
                    subjectSetting.setId(rs.getInt("id"));
                    subjectSetting.setName(rs.getString("name"));
                    subjectSetting.setType(rs.getString("type"));
                    subjectSetting.setValue(rs.getInt("value"));
                    subjectSetting.setStatus(rs.getBoolean("status"));
                    subjectSetting.setSubjectId(rs.getInt("subject_id"));
                    subjectSettings.add(subjectSetting);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectSettings;
    }

    public boolean addSubjectSetting(SubjectSetting subjectSetting) {
        String sql = "INSERT INTO subject_setting (name, type, value, status, subject_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Adding subject setting: " + subjectSetting); // In ra để kiểm tra

            ps.setString(1, subjectSetting.getName());
            ps.setString(2, subjectSetting.getType());
            ps.setInt(3, subjectSetting.getValue());
            ps.setBoolean(4, subjectSetting.isStatus());
            ps.setInt(5, subjectSetting.getSubjectId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean editSubjectSetting(SubjectSetting subjectSetting) {
        // Câu lệnh để cập nhật vào bảng subject_setting
        String updateSql = "UPDATE subject_setting SET name = ?, type = ?, value = ?, status = ? WHERE id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(updateSql)) {

            // Thiết lập các tham số cho câu lệnh SQL
            preparedStatement.setString(1, subjectSetting.getName());
            preparedStatement.setString(2, subjectSetting.getType());
            preparedStatement.setInt(3, subjectSetting.getValue());
            preparedStatement.setBoolean(4, subjectSetting.isStatus());
            preparedStatement.setInt(5, subjectSetting.getId()); // id của subjectSetting

            // Thực hiện cập nhật
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Nếu có ít nhất một dòng bị ảnh hưởng, trả về true

        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ (có thể log lỗi hoặc xử lý theo cách khác)
            return false; // Nếu có lỗi, trả về false
        }
    }

    public SubjectSetting getSubjectSettingById(int id) {
        SubjectSetting subjectSetting = null;
        String query = "SELECT ss.*, g.name AS subject_name "
                + "FROM subject_setting ss "
                + "LEFT JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE ss.id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                subjectSetting = new SubjectSetting();
                subjectSetting.setId(rs.getInt("id"));
                subjectSetting.setName(rs.getString("name"));
                subjectSetting.setType(rs.getString("type"));
                subjectSetting.setValue(rs.getInt("value"));
                subjectSetting.setStatus(rs.getBoolean("status"));
                subjectSetting.setSubjectId(rs.getInt("subject_id"));
                // Lưu tên subject vào một thuộc tính mới trong SubjectSetting
                subjectSetting.setSubjectName(rs.getString("subject_name")); // Giả sử bạn đã thêm thuộc tính này
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjectSetting;
    }

    public void updateSubjectSettingByStatus(int id, int newStatus) {
        String query = "UPDATE subject_setting SET status = ? WHERE id = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getGroupNameById(int groupId) {
        String groupName = null;
        String sql = "SELECT name FROM group WHERE id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            PreparedStatement ps = conn.prepareStatement(sql);

            preparedStatement.setInt(1, groupId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    groupName = rs.getString("name"); // Lấy tên của nhóm
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupName;
    }

    public int countSubjectSettings(int groupId, String type, Boolean status, String name) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM subject_setting ss "
                + "JOIN `group` g ON ss.subject_id = g.id "
                + "WHERE g.id = ?");

        // Điều kiện linh hoạt cho type, status, và name
        if (type != null) {
            sql.append(" AND ss.type = ?");
        }
        if (status != null) {
            sql.append(" AND ss.status = ?");
        }
        if (name != null) {
            sql.append(" AND ss.name LIKE ?");
        }

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, groupId);

            // Thiết lập các tham số dựa trên điều kiện
            if (type != null) {
                stmt.setString(paramIndex++, type);
            }
            if (status != null) {
                stmt.setBoolean(paramIndex++, status);
            }
            if (name != null) {
                stmt.setString(paramIndex, "%" + name + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public boolean isDuplicateNameAndType(String name, String type) {
        String sql = "SELECT COUNT(*) AS count "
                + "FROM subject_setting "
                + "WHERE name = ? AND type = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số
            stmt.setString(1, name);
            stmt.setString(2, type);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Kiểm tra nếu số lượng > 0 thì có sự trùng lặp
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
