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
import model.Assignment;
import model.EvalCriteria;
import util.JDBCUtils;

/**
 *
 * @author vqman
 */
public class EvalCriteriaDAO {

    public List<EvalCriteria> getEvalCriteriaByPage(int groupId, int limit, int offset) {
        List<EvalCriteria> criteriaList = new ArrayList<>();
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status, ea.name "
                + "FROM eval_criteria ec "
                + "JOIN eval_assignment ea ON ec.assignment_id = ea.id "
                + "JOIN `group` g ON g.id = ea.subject_id "
                + "WHERE g.id = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, limit);  // Số lượng bản ghi trên mỗi trang
            stmt.setInt(3, offset); // Vị trí bắt đầu

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvalCriteria criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteria.setAssignmentName(rs.getString("ea.name"));
                    criteriaList.add(criteria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return criteriaList;
    }

    public int countEvalCriteria(int groupId) {
        String sql = "SELECT COUNT(*) FROM eval_criteria ec "
                + "JOIN eval_assignment ea ON ec.assignment_id = ea.id "
                + "JOIN `group` g ON g.id = ea.subject_id "
                + "WHERE g.id = ?";
        int count = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
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

    public boolean insertEvalCriteria(EvalCriteria criteria) {
        String sql = "INSERT INTO eval_criteria (name, detail, weight, assignment_id, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        boolean isInserted = false;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số
            stmt.setString(1, criteria.getName());
            stmt.setString(2, criteria.getDetail());
            stmt.setInt(3, criteria.getWeight());
            stmt.setInt(4, criteria.getAssignmentId());
            stmt.setBoolean(5, criteria.isStatus());

            // Thực thi câu lệnh và kiểm tra xem có bản ghi nào được thêm
            isInserted = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isInserted;
    }

    public boolean updateEvalCriteria(EvalCriteria criteria) {
        String sql = "UPDATE eval_criteria SET name = ?, detail = ?, weight = ?, assignment_id = ?, status = ? "
                + "WHERE id = ?";
        boolean isUpdated = false;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số
            stmt.setString(1, criteria.getName());
            stmt.setString(2, criteria.getDetail());
            stmt.setInt(3, criteria.getWeight());
            stmt.setInt(4, criteria.getAssignmentId());
            stmt.setBoolean(5, criteria.isStatus());
            stmt.setInt(6, criteria.getId()); // ID của EvalCriteria cần cập nhật

            // Thực thi câu lệnh và kiểm tra xem có bản ghi nào được cập nhật
            isUpdated = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public List<EvalCriteria> filterByStatus(boolean status, int limit, int offset) {
        List<EvalCriteria> criteriaList = new ArrayList<>();
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status, ea.name "
                + "FROM eval_criteria ec "
                + "JOIN eval_assignment ea ON ec.assignment_id = ea.id "
                + "WHERE ec.status = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số status và phân trang
            stmt.setBoolean(1, status);
            stmt.setInt(2, limit);   // Số lượng bản ghi cần lấy
            stmt.setInt(3, offset);  // Vị trí bắt đầu

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvalCriteria criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteria.setAssignmentName(rs.getString("ea.name"));
                    criteriaList.add(criteria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return criteriaList;
    }

    public List<EvalCriteria> searchByName(String name, int limit, int offset) {
        List<EvalCriteria> criteriaList = new ArrayList<>();
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status, ea.name "
                + "FROM eval_criteria ec "
                + "JOIN eval_assignment ea ON ec.assignment_id = ea.id "
                + "WHERE ec.name LIKE ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số tìm kiếm và phân trang
            stmt.setString(1, "%" + name + "%");
            stmt.setInt(2, limit);   // Số lượng bản ghi cần lấy
            stmt.setInt(3, offset);  // Vị trí bắt đầu

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvalCriteria criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteria.setAssignmentName(rs.getString("ea.name"));
                    criteriaList.add(criteria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return criteriaList;
    }

    public List<EvalCriteria> filterByAssignmentId(int assignmentId, int limit, int offset) {
        List<EvalCriteria> criteriaList = new ArrayList<>();
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status "
                + "FROM eval_criteria ec WHERE ec.assignment_id = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số assignmentId và phân trang
            stmt.setInt(1, assignmentId);
            stmt.setInt(2, limit);   // Số lượng bản ghi cần lấy
            stmt.setInt(3, offset);  // Vị trí bắt đầu

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvalCriteria criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteriaList.add(criteria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return criteriaList;
    }

    public List<EvalCriteria> filterByStatusAndAssignmentId(boolean status, int assignmentId, int limit, int offset) {
        List<EvalCriteria> criteriaList = new ArrayList<>();
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status "
                + "FROM eval_criteria ec "
                + "WHERE ec.status = ? AND ec.assignment_id = ? "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số status, assignmentId, limit và offset
            stmt.setBoolean(1, status);
            stmt.setInt(2, assignmentId);
            stmt.setInt(3, limit);   // Số lượng bản ghi cần lấy
            stmt.setInt(4, offset);  // Vị trí bắt đầu

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvalCriteria criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteriaList.add(criteria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return criteriaList;
    }

    public List<Assignment> getAssignmentBySubject(int subjectId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT ea.name,ea.id "
                + "FROM eval_assignment ea "
                + "JOIN `group` g ON ea.subject_id = g.id "
                + "WHERE g.id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số subjectId
            stmt.setInt(1, subjectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setId(rs.getInt("id"));
                    assignment.setName(rs.getString("name"));
                    assignments.add(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public EvalCriteria getCriteriaById(int id) {
        EvalCriteria criteria = null;
        String sql = "SELECT ec.id, ec.name, ec.detail, ec.weight, ec.assignment_id, ec.status, ea.name AS assignment_name "
                + "FROM eval_criteria ec "
                + "JOIN eval_assignment ea ON ec.assignment_id = ea.id "
                + "WHERE ec.id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số id
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    criteria = new EvalCriteria();
                    criteria.setId(rs.getInt("id"));
                    criteria.setName(rs.getString("name"));
                    criteria.setDetail(rs.getString("detail"));
                    criteria.setWeight(rs.getInt("weight"));
                    criteria.setAssignmentId(rs.getInt("assignment_id"));
                    criteria.setStatus(rs.getBoolean("status"));
                    criteria.setAssignmentName(rs.getString("assignment_name")); // Tên assignment
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return criteria;
    }

    public boolean updateStatusById(int id, int newStatus) {
        String sql = "UPDATE eval_criteria SET status = ? WHERE id = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newStatus);
            stmt.setInt(2, id);

            // Thực thi câu lệnh và trả về kết quả cập nhật
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDuplicateCriteria(String name, int assignmentId) {
        String sql = "SELECT COUNT(*) AS count FROM eval_criteria WHERE name = ? AND assignment_id = ?;";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho các tham số
            stmt.setString(1, name);
            stmt.setInt(2, assignmentId);

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

    public int countSearchByName(String name) {
        String sql = "SELECT COUNT(*) FROM eval_criteria ec WHERE ec.name LIKE ?";
        int count = 0;
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
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

    public int countByStatus(boolean status) {
        String sql = "SELECT COUNT(*) FROM eval_criteria ec WHERE ec.status = ?";
        int count = 0;
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
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

    public int countByAssignmentId(int assignmentId) {
        String sql = "SELECT COUNT(*) FROM eval_criteria ec WHERE ec.assignment_id = ?";
        int count = 0;
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assignmentId);
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

    public int countByStatusAndAssignmentId(boolean status, int assignmentId) {
        String sql = "SELECT COUNT(*) FROM eval_criteria ec WHERE ec.status = ? AND ec.assignment_id = ?";
        int count = 0;
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setInt(2, assignmentId);
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
}
