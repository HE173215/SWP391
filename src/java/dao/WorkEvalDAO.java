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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.SubjectSetting;
import model.Team;
import model.WorkEval;
import util.JDBCUtils;

/**
 * DAO class for WorkEval
 */
public class WorkEvalDAO extends JDBCUtils {

    // Method to get all WorkEvals from work_eval table
    public List<WorkEval> getAllWorkEvals() throws SQLException {
        List<WorkEval> workEvalList = new ArrayList<>();
        String sql = "SELECT we.*, "
                + "ss_complexity.value AS complexity_value, "
                + "ss_quality.value AS quality_value, "
                + "m.name AS milestone_name, "
                + "r.tittle AS req_name, "
                + "ss_quality.name AS quality_name, "
                + "ss_complexity.name AS complexity_name, "
                + "m.max_eval_value "
                + "FROM work_eval we "
                + "LEFT JOIN subject_setting ss_complexity ON we.complexity_id = ss_complexity.id "
                + "LEFT JOIN subject_setting ss_quality ON we.quality_id = ss_quality.id "
                + "LEFT JOIN milestone m ON we.milestone_id = m.id "
                + "LEFT JOIN requirement r ON we.req_id = r.id";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                WorkEval workEval = new WorkEval();
                workEval.setId(rs.getInt("id"));
                workEval.setReqId(rs.getInt("req_id"));
                workEval.setMilestoneId(rs.getInt("milestone_id"));
                workEval.setComplexityId(rs.getInt("complexity_id"));
                workEval.setQualityId(rs.getInt("quality_id"));
                workEval.setIsFinal(rs.getBoolean("is_final"));
                workEval.setQuanlityName(rs.getString("quality_name"));
                workEval.setComplexityName(rs.getString("complexity_name"));
                workEval.setMilestoneName(rs.getString("milestone_name"));
                workEval.setReqName(rs.getString("req_name"));
                double complexityValue = rs.getDouble("complexity_value");
                double qualityValue = rs.getDouble("quality_value");
                double maxEvalValue = rs.getDouble("max_eval_value");
                workEval.setComplexityValue(complexityValue);
                workEval.setQualityValue(qualityValue);
                if (maxEvalValue != 0) {
                    double grade = ((complexityValue * (qualityValue / 100)) / maxEvalValue) * 10;
                    grade = Math.round(grade * 10.0) / 10.0;
                    workEval.setGrade(grade);
                } else {
                    workEval.setGrade(0.0);
                }
                workEvalList.add(workEval);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return workEvalList;
    }

    public WorkEval getWorkEvalById(int id) throws SQLException {
        String sql = "SELECT * FROM work_eval WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    WorkEval workEval = new WorkEval();
                    workEval.setId(rs.getInt("id"));
                    workEval.setReqId(rs.getInt("req_id")); // Đúng tên cột từ cơ sở dữ liệu
                    workEval.setMilestoneId(rs.getInt("milestone_id"));
                    workEval.setComplexityId(rs.getInt("complexity_id"));
                    workEval.setQualityId(rs.getInt("quality_id"));
                    workEval.setIsFinal(rs.getBoolean("is_final"));
                    return workEval;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex; // Optional: re-throw the exception if you want to handle it at a higher level
        }
        return null; // Return null if no record is found
    }

    public boolean updateWorkEvalById(int id, WorkEval updatedWorkEval) {
        String sql = "UPDATE work_eval "
                + "SET req_id = ?, milestone_id = ?, complexity_id = ?, quality_id = ?, is_final = ? "
                + "WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, updatedWorkEval.getReqId());
            ps.setInt(2, updatedWorkEval.getMilestoneId());
            ps.setInt(3, updatedWorkEval.getComplexityId());
            ps.setInt(4, updatedWorkEval.getQualityId());
            ps.setBoolean(5, updatedWorkEval.isIsFinal());
            ps.setInt(6, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<SubjectSetting> getComplexities() throws SQLException {
        List<SubjectSetting> complexities = new ArrayList<>();
        String sql = "SELECT id, name FROM subject_setting WHERE TRIM(type) = 'Complexity';"; // Giả sử `type = 'complexity'` để phân biệt

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SubjectSetting complexity = new SubjectSetting();
                complexity.setId(rs.getInt("id"));
                complexity.setName(rs.getString("name"));
                complexities.add(complexity);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return complexities;
    }

    public List<SubjectSetting> getQualities() throws SQLException {
        List<SubjectSetting> qualities = new ArrayList<>();
        String sql = "SELECT id, name FROM subject_setting WHERE type = 'quality'"; // Giả sử `type = 'quality'` để phân biệt

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SubjectSetting quality = new SubjectSetting();
                quality.setId(rs.getInt("id"));
                quality.setName(rs.getString("name"));
                qualities.add(quality);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return qualities;
    }

    public List<Team> getTeamsFromTeamMilestone() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT tm.id AS teamMilestoneId, t.name AS teamName "
                + "FROM team_milestone tm "
                + "INNER JOIN team t ON tm.team_id = t.id"; // Giả sử `team_id` là khóa ngoại

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("teamMilestoneId"));
                team.setName(rs.getString("teamName"));
                teams.add(team);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return teams;
    }

    public String getReqNameByReqId(int reqId) throws SQLException {
        String reqName = null;
        String sql = "SELECT tittle FROM requirement WHERE id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reqId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reqName = rs.getString("tittle");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex; // Re-throw if you need to handle the exception at a higher level
        }
        return reqName;
    }

}
