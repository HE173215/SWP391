package dao;

import model.FinalEvaluated;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinalEvaluatedDAO extends JDBCUtils {

    // Method to get all final evaluations for all teams
    public List<FinalEvaluated> getAllFinalEvaluatedTeams() throws SQLException {
        List<FinalEvaluated> finalEvaluatedList = new ArrayList<>();
        String sql = """
        SELECT 
            t.id AS team_id,
            t.name AS team_name,
            m.id AS milestone_id,
            m.name AS milestone_name,
            r.id AS requirement_id,
            MAX(ss_complexity.value) AS complexity_value,
            MAX(ss_quality.value) AS quality_value,
            m.weight AS milestone_weight
        FROM 
            project_evaluation_system.team AS t
        JOIN 
            project_evaluation_system.team_milestone tm ON t.id = tm.team_id
        JOIN 
            project_evaluation_system.work_eval we ON tm.milestone_id = we.milestone_id
        JOIN 
            project_evaluation_system.requirement r ON we.req_id = r.id
        JOIN 
            project_evaluation_system.subject_setting ss_complexity 
            ON r.complexity_id = ss_complexity.id AND TRIM(ss_complexity.type) = 'Complexity'
        JOIN 
            project_evaluation_system.subject_setting ss_quality 
            ON we.quality_id = ss_quality.id AND TRIM(ss_quality.type) = 'Quality'
        JOIN 
            project_evaluation_system.milestone m ON tm.milestone_id = m.id
        GROUP BY 
            t.id, t.name, m.id, m.name, r.id, m.weight
        ORDER BY 
            t.id, m.id, r.id;  -- Order by team ID, milestone ID, and requirement ID
    """;

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FinalEvaluated finalEvaluated = mapResultSetToFinalEvaluated(rs);
                finalEvaluatedList.add(finalEvaluated);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching final evaluations: " + ex.getMessage(), ex);
        }

        return finalEvaluatedList;
    }

    public FinalEvaluated getFinalEvaluatedTeamById(int teamId) throws SQLException {
        String sql = """
        SELECT 
            t.id AS team_id,
            t.name AS team_name,
            m.id AS milestone_id,
            m.name AS milestone_name,
            r.id AS requirement_id,
            MAX(ss_complexity.value) AS complexity_value,
            MAX(ss_quality.value) AS quality_value,
            m.weight AS milestone_weight
        FROM 
            project_evaluation_system.team AS t
        JOIN 
            project_evaluation_system.team_milestone tm ON t.id = tm.team_id
        JOIN 
            project_evaluation_system.work_eval we ON tm.milestone_id = we.milestone_id
        JOIN 
            project_evaluation_system.requirement r ON we.req_id = r.id
        JOIN 
            project_evaluation_system.subject_setting ss_complexity 
            ON r.complexity_id = ss_complexity.id AND TRIM(ss_complexity.type) = 'Complexity'
        JOIN 
            project_evaluation_system.subject_setting ss_quality 
            ON we.quality_id = ss_quality.id AND TRIM(ss_quality.type) = 'Quality'
        JOIN 
            project_evaluation_system.milestone m ON tm.milestone_id = m.id
        WHERE 
            t.id = ?
        GROUP BY 
            t.id, t.name, m.id, m.name, r.id, m.weight
    """;

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FinalEvaluated finalEvaluated = mapResultSetToFinalEvaluated(rs);
                    return finalEvaluated;
                } else {
                    throw new SQLException("Final evaluation not found for team ID: " + teamId);
                }
            }
        }
    }

    public List<FinalEvaluated> searchFinalEvaluatedTeamsByName(String teamName) throws SQLException {
        List<FinalEvaluated> finalEvaluatedList = new ArrayList<>();
        String sql = """
        SELECT 
            t.id AS team_id,
            t.name AS team_name,
            m.id AS milestone_id,
            m.name AS milestone_name,
            r.id AS requirement_id,
            MAX(ss_complexity.value) AS complexity_value,
            MAX(ss_quality.value) AS quality_value,
            m.weight AS milestone_weight
        FROM 
            project_evaluation_system.team AS t
        JOIN 
            project_evaluation_system.team_milestone tm ON t.id = tm.team_id
        JOIN 
            project_evaluation_system.work_eval we ON tm.milestone_id = we.milestone_id
        JOIN 
            project_evaluation_system.requirement r ON we.req_id = r.id
        JOIN 
            project_evaluation_system.subject_setting ss_complexity 
            ON r.complexity_id = ss_complexity.id AND TRIM(ss_complexity.type) = 'Complexity'
        JOIN 
            project_evaluation_system.subject_setting ss_quality 
            ON we.quality_id = ss_quality.id AND TRIM(ss_quality.type) = 'Quality'
        WHERE 
            t.name LIKE ? 
        GROUP BY 
            t.id, t.name, m.id, m.name, r.id, m.weight
    """;

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + teamName + "%"); // Set the team name parameter
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FinalEvaluated finalEvaluated = mapResultSetToFinalEvaluated(rs);
                    finalEvaluatedList.add(finalEvaluated);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while searching final evaluations by team name: " + ex.getMessage(), ex);
        }

        return finalEvaluatedList;
    }

    // Method to map ResultSet to FinalEvaluated object
    private FinalEvaluated mapResultSetToFinalEvaluated(ResultSet rs) throws SQLException {
        FinalEvaluated finalEvaluated = new FinalEvaluated();
        finalEvaluated.setTeamId(rs.getInt("team_id"));
        finalEvaluated.setTeamName(rs.getString("team_name"));
        finalEvaluated.setMilestoneId(rs.getInt("milestone_id"));
        finalEvaluated.setMilestoneName(rs.getString("milestone_name"));
        finalEvaluated.setRequirementId(rs.getInt("requirement_id"));
        finalEvaluated.setComplexityValue(rs.getDouble("complexity_value"));
        finalEvaluated.setQualityValue(rs.getDouble("quality_value"));
        finalEvaluated.setMilestoneWeight(rs.getDouble("milestone_weight"));
        return finalEvaluated;
    }

}
