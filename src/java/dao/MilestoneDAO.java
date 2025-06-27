/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import model.Assignment;
import model.Milestone;
import util.JDBCUtils;

/**
 *
 * @author Do Duan
 */
public class MilestoneDAO {

    private Connection con = null; // ket noi vs sql
    private PreparedStatement ps = null; // nhan cau lenh
    private ResultSet rs = null; // tra kq

//    public List<Milestone> getMilestoneListByClassID(int classID) {
//        String sql = "SELECT m.id,m.code,m. name, m.priority, m.weight, m.detail, m.final_milestone, m.end_date, m.status, ec.name as assignment\n"
//                + "FROM project_evaluation_system.milestone as m\n"
//                + "join eval_assignment as ec on m.assignment_id = ec.id\n"
//                + "join class as c on c.id = m.class_id\n"
//                + "where c.id = ? ";
//        List<Milestone> milestoneList = new ArrayList<>();
//        try {
//            con = JDBCUtils.getConnection();
//            if (con != null) {
//                Milestone m = new Milestone();
//                ps = con.prepareStatement(sql);
//                ps.setInt(1, classID);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    m.setId(rs.getInt("id"));
//                    m.setCode(rs.getString("code"));
//                    m.setName(rs.getString("name"));
//                    m.setPriority(rs.getInt("priority"));
//                    m.setDetail(rs.getString("detail"));
//                    m.setFinalMilestone(rs.getInt("final_milestone"));
//                    m.setEndDate(rs.getDate("end_date"));
//                    m.setStatus(rs.getInt("status"));
//                    m.setAssigment(rs.getString("assignment"));
//                    milestoneList.add(m);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                if (con != null) {
//                    con.close();
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return milestoneList;
//    }
    public List<Milestone> getMilestoneListByClassID(int classID) {
        String sql = "SELECT m.id, m.code, m.name, m.priority, m.weight, m.detail, m.final_milestone,m.max_eval_value , "
                + "m.end_date, m.status, ec.name as assignment "
                + "FROM project_evaluation_system.milestone as m "
                + "JOIN eval_assignment as ec on m.assignment_id = ec.id "
                + "JOIN class as c on c.id = m.class_id "
                + "WHERE c.id = ?";
        List<Milestone> milestoneList = new ArrayList<>();
        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Milestone m = new Milestone();
                    m.setId(rs.getInt("id"));
                    m.setCode(rs.getString("code"));
                    m.setName(rs.getString("name"));
                    m.setPriority(rs.getInt("priority"));
                    m.setDetail(rs.getString("detail"));
                    m.setFinalMilestone(rs.getInt("final_milestone"));
                    m.setMax_eval_value(rs.getInt("max_eval_value"));
                    m.setEndDate(rs.getDate("end_date"));
                    m.setStatus(rs.getInt("status"));
                    m.setAssigment(rs.getString("assignment"));
                    milestoneList.add(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return milestoneList;
    }

    public boolean isAssignmentIdValid(int assignmentId) {
        String sql = "SELECT COUNT(*) FROM eval_assignment WHERE id = ?";
        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateMilestone(Milestone milestone) {

        String sql = "UPDATE milestone SET code = ?, name = ?, priority = ?, weight = ?, detail = ?, max_eval_value = ?, end_date = ?,  "
                + "status = ? WHERE id = ?";

        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, milestone.getCode());
            ps.setString(2, milestone.getName());
            ps.setInt(3, milestone.getPriority());
            ps.setInt(4, milestone.getWeight());
            ps.setString(5, milestone.getDetail());
            ps.setInt(6, milestone.getMax_eval_value());
            ps.setDate(7, new java.sql.Date(milestone.getEndDate().getTime()));
            ps.setInt(8, milestone.getStatus());
            ps.setInt(9, milestone.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Milestone getMilestoneById(int milestoneId) {
        Milestone milestone = null;
        String sql = "SELECT m.id, m.code, m.name, m.priority, m.weight, m.detail, m.final_milestone, m.max_eval_value, "
                + "m.end_date, m.status, ec.name as assignment "
                + "FROM milestone m "
                + "JOIN eval_assignment ec ON m.assignment_id = ec.id "
                + "WHERE m.id = ?";

        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, milestoneId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    milestone = new Milestone();
                    milestone.setId(rs.getInt("id"));
                    milestone.setCode(rs.getString("code"));
                    milestone.setName(rs.getString("name"));
                    milestone.setPriority(rs.getInt("priority"));
                    milestone.setWeight(rs.getInt("weight"));
                    milestone.setDetail(rs.getString("detail"));
                    milestone.setFinalMilestone(rs.getInt("final_milestone"));
                    milestone.setMax_eval_value(rs.getInt("max_eval_value"));
                    milestone.setEndDate(rs.getDate("end_date"));
                    milestone.setStatus(rs.getInt("status"));
                    milestone.setAssigment(rs.getString("assignment"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return milestone;
    }

    public List<Assignment> getAssignmentsBySubjectId(int subjectId) {
        List<Assignment> assignments = new ArrayList<>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT id, name FROM eval_assignment WHERE subject_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, subjectId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setId(rs.getInt("id"));
                assignment.setName(rs.getString("name"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public List<Assignment> getAllNames() {
        List<Assignment> names = new ArrayList<>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT id, name  FROM eval_assignment ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Assignment name = new Assignment();
                name.setId(rs.getInt("id"));
                name.setName(rs.getString("name"));
                names.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

//    public void updateMilestone(Milestone milestone) throws ParseException {
//        String sql = "UPDATE milestone SET code = ?, name = ?, priority = ?, weight = ?, end_date = ?, status = ?, assignment_id = ? WHERE id = ?";
//        try {
//            con = JDBCUtils.getConnection();
//            if (con != null) {
//                ps = con.prepareStatement(sql);
//                ps.setString(1, milestone.getCode());
//                ps.setString(2, milestone.getName());
//                ps.setInt(3, milestone.getPriority());
//                ps.setInt(4, milestone.getWeight());
//                // Convert String to java.sql.Date
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format to match your date format
//                java.util.Date parsedDate = sdf.parse(milestone.getEndDate());
//                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
//                ps.setDate(5, sqlDate);
//                ps.setInt(6, milestone.getStatus());
//                ps.setInt(7, milestone.getAssigmentId());
//                ps.setInt(8, milestone.getId());
//                ps.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public int insertMilestoneToClass(int classID, Assignment assignment) {
        String sql = "insert into milestone(weight,final_milestone,status,assignment_id,class_id) values\n"
                + "(?,?,?,?,?)";

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, assignment.getWeight());
                if (assignment.isFinal_assignment()) {
                    ps.setInt(2, 1);
                } else {
                    ps.setInt(2, 0);
                }

                if (assignment.isStatus()) {
                    ps.setInt(3, 1);
                } else {
                    ps.setInt(3, 2);
                }

                ps.setInt(4, assignment.getId());
                ps.setInt(5, classID);

                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Milestone> getAllMilestones() throws SQLException {
        List<Milestone> milestones = new ArrayList<>();
        String sql = "SELECT id, name FROM milestone";
        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Milestone milestone = new Milestone();
                milestone.setId(rs.getInt("id"));
                milestone.setName(rs.getString("name"));
                milestones.add(milestone);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return milestones;
    }

    public Milestone getMilestonesById(int milestoneId) {
        Milestone milestone = null;
        String sql = "SELECT m.id, m.code, m.name, m.priority, m.weight, m.detail, m.final_milestone, m.max_eval_value, \n"
                + "m.end_date, m.status, ec.name AS assignment, ec.subject_id\n"
                + "FROM milestone m \n"
                + "JOIN eval_assignment ec ON m.assignment_id = ec.id \n"
                + "WHERE m.id = ?";

        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, milestoneId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    milestone = new Milestone();
                    milestone.setId(rs.getInt("id"));
                    milestone.setCode(rs.getString("code"));
                    milestone.setName(rs.getString("name"));
                    milestone.setPriority(rs.getInt("priority"));
                    milestone.setWeight(rs.getInt("weight"));
                    milestone.setDetail(rs.getString("detail"));
                    milestone.setFinalMilestone(rs.getInt("final_milestone"));
                    milestone.setMax_eval_value(rs.getInt("max_eval_value"));
                    milestone.setEndDate(rs.getDate("end_date"));
                    milestone.setStatus(rs.getInt("status"));
                    milestone.setAssigment(rs.getString("assignment"));
                    milestone.setAssigmentId(rs.getInt("subject_id")); // Thiết lập subject_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return milestone;
    }

}
