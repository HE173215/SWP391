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
import util.JDBCUtils;
import static util.JDBCUtils.getConnection;

/**
 *
 * @author admin
 */
public class AssignmentDAO {

    //Assignment List
    public List<Assignment> getAllAssignments(String sortBy, String sortOrder) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id  "
                    + "WHERE g.type = 'Subject' "
                    + "Order by " + sortBy + " " + sortOrder + " ";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));

                assigments.add(assigment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assigments;
    }

    //
    public Assignment getAssignmentsById(int id) {
        Assignment assigment = null;
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.detail, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE e.id = ? AND g.type = 'Subject'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setDetail(rs.getString("detail"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigment;
    }

    // search assigment full name
    public List<Assignment> searchAssignmentsByName(String name) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE e.name LIKE ? AND g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Sử dụng ký tự đại diện '%' cho việc tìm kiếm tên cài đặt
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assigments;
    }

    public List<Assignment> getAssignmentsByStatus(boolean status) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE e.status = ? AND g.type = 'Subject'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigments;
    }

    public List<Assignment> getAllCodes() {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT g.id, g.code FROM `group` g WHERE type = 'Subject'  ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setSubject_id(rs.getInt("id"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigments;
    }

    public List<Assignment> getAssignmentsByCode(String code) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE g.code = ? AND g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, code);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assigments;
    }

    public List<Assignment> getAssignmentsByCodeAndStatus(String code, boolean status) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE g.code = ? AND e.status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ps.setBoolean(2, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigments;
    }

    // Thêm danh mục
    public void addAssignment(Assignment assigment) {
        try (Connection con = getConnection()) { // Sử dụng try-with-resources để tự động đóng kết nối
            String sql = "INSERT INTO eval_assignment(name, weight, detail, final_assignment, status, subject_id) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, assigment.getName());
            ps.setInt(2, assigment.getWeight());
            ps.setString(3, assigment.getDetail());
            ps.setBoolean(4, assigment.isFinal_assignment());
            ps.setBoolean(5, assigment.isStatus());
            ps.setInt(6, assigment.getSubject_id());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Cập nhật danh mục
    public void updateAssignment(Assignment assigment) {
        try (Connection con = JDBCUtils.getConnection()) { // Sử dụng try-with-resources
            String sql = "UPDATE eval_assignment SET name = ?, weight = ?, detail = ?, final_assignment = ?, status = ?, subject_id = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, assigment.getName());
            ps.setInt(2, assigment.getWeight());
            ps.setString(3, assigment.getDetail());
            ps.setBoolean(4, assigment.isFinal_assignment());
            ps.setBoolean(5, assigment.isStatus());
            ps.setInt(6, assigment.getSubject_id());
            ps.setInt(7, assigment.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Activate/Deactivate a assigment
    public void updateAssignmentsStatus(int id, boolean status) {
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "UPDATE eval_assignment SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Assignment> getAssignmentsListBySubjectId(int subjectID) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, e.subject_id, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE g.id = ? AND g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, subjectID);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_id(rs.getInt("subject_id"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigments;
    }

    public List<Assignment> getAssignmentsBySubjectId( int subjectID) {
        List<Assignment> assigments = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT e.id, e.name, e.weight, e.final_assignment, e.status, e.subject_id, g.code "
                    + "FROM eval_assignment e "
                    + "JOIN `group` g ON e.subject_id = g.id "
                    + "WHERE g.id = ? AND g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, subjectID);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Assignment assigment = new Assignment();
                assigment.setId(rs.getInt("id"));
                assigment.setName(rs.getString("name"));
                assigment.setWeight(rs.getInt("weight"));
                assigment.setFinal_assignment(rs.getBoolean("final_assignment"));
                assigment.setStatus(rs.getBoolean("status"));
                assigment.setSubject_id(rs.getInt("subject_id"));
                assigment.setSubject_code(rs.getString("code"));
                assigments.add(assigment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assigments;
    }
    
}
