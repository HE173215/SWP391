package dao;

import model.Team;
import model.User;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static util.JDBCUtils.getConnection;

public class TeamDAO extends JDBCUtils {

    // Method to get all teams
    public List<Team> getAllTeams() throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Team team = mapResultSetToTeam(rs);
                teamList.add(team);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching the team list: " + ex.getMessage(), ex);
        }

        return teamList;
    }

    // Method to get teams by a specific milestone ID
    public List<Team> getTeamsByMilestone(int milestoneId) throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "JOIN project_evaluation_system.team_milestone tm ON t.id = tm.team_id "
                + "WHERE tm.milestone_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, milestoneId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = mapResultSetToTeam(rs);
                    teamList.add(team);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching teams for milestone: " + ex.getMessage(), ex);
        }

        return teamList;
    }

    // Method to get teams by user ID
    public List<Team> getTeamsByUser(int userId) throws SQLException {
        List<Team> teams = new ArrayList<>();

        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.class_member cm ON t.class_id = cm.class_id "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "WHERE cm.user_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);  // Set the user ID parameter
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setClassId(rs.getInt("class_id"));
                    team.setClassName(rs.getString("class_name"));
                    team.setName(rs.getString("name"));
                    team.setDetail(rs.getString("detail"));
                    team.setTopic(rs.getString("topic"));

                    teams.add(team);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching teams for user: " + ex.getMessage(), ex);
        }

        return teams;
    }

    public List<Team> getTeamsByUserRole1(int userId, String searchQuery, String classId, String teamSearchQuery) throws SQLException {
        List<Team> teams = new ArrayList<>();

        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.team_member tm ON t.id = tm.team_id "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "WHERE tm.user_id = ?";

        // Thêm điều kiện tìm kiếm nếu có
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql += " AND (t.name LIKE ? OR t.topic LIKE ?)";
        }
        if (classId != null && !classId.isEmpty()) {
            sql += " AND t.class_id = ?";
        }
        if (teamSearchQuery != null && !teamSearchQuery.isEmpty()) {
            sql += " AND t.name LIKE ?";
        }

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);  // Set the user ID parameter

            // Set các tham số tìm kiếm nếu có
            int paramIndex = 2;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchQuery + "%");
                ps.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (classId != null && !classId.isEmpty()) {
                ps.setString(paramIndex++, classId);
            }
            if (teamSearchQuery != null && !teamSearchQuery.isEmpty()) {
                ps.setString(paramIndex++, "%" + teamSearchQuery + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setClassId(rs.getInt("class_id"));
                    team.setClassName(rs.getString("class_name"));
                    team.setName(rs.getString("name"));
                    team.setDetail(rs.getString("detail"));
                    team.setTopic(rs.getString("topic"));

                    teams.add(team);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching teams for user with role 1: " + ex.getMessage(), ex);
        }

        return teams;
    }

    public List<Team> getTeamsByUserRole2(int userId, String searchQuery, String classId, String teamSearchQuery) throws SQLException {
        List<Team> teams = new ArrayList<>();

        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.team_member tm ON t.id = tm.team_id "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "WHERE tm.user_id = ?";

        // Thêm điều kiện tìm kiếm nếu có
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql += " AND (t.name LIKE ? OR t.topic LIKE ?)";
        }
        if (classId != null && !classId.isEmpty()) {
            sql += " AND t.class_id = ?";
        }
        if (teamSearchQuery != null && !teamSearchQuery.isEmpty()) {
            sql += " AND t.name LIKE ?";
        }

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);  // Set the user ID parameter

            // Set các tham số tìm kiếm nếu có
            int paramIndex = 2;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchQuery + "%");
                ps.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (classId != null && !classId.isEmpty()) {
                ps.setString(paramIndex++, classId);
            }
            if (teamSearchQuery != null && !teamSearchQuery.isEmpty()) {
                ps.setString(paramIndex++, "%" + teamSearchQuery + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setClassId(rs.getInt("class_id"));
                    team.setClassName(rs.getString("class_name"));
                    team.setName(rs.getString("name"));
                    team.setDetail(rs.getString("detail"));
                    team.setTopic(rs.getString("topic"));

                    teams.add(team);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching teams for user with role 2: " + ex.getMessage(), ex);
        }

        return teams;
    }

    // Method to get a specific team by its ID
    public Team getTeamById(int teamId) throws SQLException {
        String sql = "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "WHERE t.id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeam(rs);
                } else {
                    throw new SQLException("Team not found with ID: " + teamId);
                }
            }
        }
    }

    // Method to get teams by a specific class ID
    public List<Team> getTeamsByClassId(String classId, String searchQuery) throws SQLException {
        List<Team> teamList = new ArrayList<>();

        // Base SQL query
        StringBuilder sql = new StringBuilder(
                "SELECT t.id, t.class_id, c.name AS class_name, t.name, t.detail, t.topic "
                + "FROM project_evaluation_system.team t "
                + "JOIN project_evaluation_system.class c ON t.class_id = c.id "
                + "WHERE t.class_id = ?"
        );

        // Initialize parameter index
        int paramIndex = 1;

        // Check for search query and modify SQL accordingly
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (t.name LIKE ? OR t.topic LIKE ?)"); // Search by team name or topic
        }

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            // Set the classId parameter
            ps.setString(paramIndex++, classId);

            // Set search parameters if present
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String searchPattern = "%" + searchQuery + "%";
                ps.setString(paramIndex++, searchPattern); // Set search for name (t.name LIKE ?)
                ps.setString(paramIndex++, searchPattern); // Set search for topic (t.topic LIKE ?)
            }

            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = mapResultSetToTeam(rs);
                    teamList.add(team);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching teams by class ID: " + ex.getMessage(), ex);
        }

        return teamList;
    }

    // Method to update a team's details
    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE project_evaluation_system.team SET class_id = ?, name = ?, detail = ?, topic = ? WHERE id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, team.getClassId());
            ps.setString(2, team.getName());
            ps.setString(3, team.getDetail());
            ps.setString(4, team.getTopic());
            ps.setInt(5, team.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error while updating team details: " + ex.getMessage(), ex);
        }
    }

    // Method to add a team to a milestone
    public void addTeamToMilestone(int teamId, int milestoneId) throws SQLException {
        String sql = "INSERT INTO project_evaluation_system.team_milestone (team_id, milestone_id) VALUES (?, ?)";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, milestoneId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error while adding team to milestone: " + ex.getMessage(), ex);
        }
    }

    // Method to remove a team from a milestone
    public void removeTeamFromMilestone(int teamId, int milestoneId) throws SQLException {
        String sql = "DELETE FROM project_evaluation_system.team_milestone WHERE team_id = ? AND milestone_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, milestoneId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error while removing team from milestone: " + ex.getMessage(), ex);
        }
    }

    // Method to map ResultSet to a Team object
    private Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setClassId(rs.getInt("class_id"));
        team.setClassName(rs.getString("class_name")); // Assuming class_name is fetched correctly in the SQL query
        team.setName(rs.getString("name"));
        team.setDetail(rs.getString("detail"));
        team.setTopic(rs.getString("topic"));
        return team;
    }

    // Method to create a new team and add the user to the team
    public void createTeam(Team team) throws SQLException {
        Connection con = null;
        PreparedStatement teamStmt = null;
        ResultSet rs = null;

        // SQL để thêm một team vào bảng team
        String teamInsertSql = "INSERT INTO project_evaluation_system.team (class_id, name, detail, topic) VALUES (?, ?, ?, ?)";

        try {
            con = getConnection();  // Lấy kết nối tới cơ sở dữ liệu
            con.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Thêm team mới vào bảng team
            teamStmt = con.prepareStatement(teamInsertSql, Statement.RETURN_GENERATED_KEYS);
            teamStmt.setInt(1, team.getClassId());  // Gán giá trị class_id
            teamStmt.setString(2, team.getName());  // Gán giá trị name
            teamStmt.setString(3, team.getDetail());  // Gán giá trị detail
            teamStmt.setString(4, team.getTopic());  // Gán giá trị topic
            teamStmt.executeUpdate();

            // 2. Lấy team ID tự động sinh ra từ quá trình thêm team
            rs = teamStmt.getGeneratedKeys();
            if (rs.next()) {
                int teamId = rs.getInt(1);  // Lấy team ID vừa được sinh
                // Bạn có thể lưu teamId hoặc thực hiện bất kỳ thao tác nào khác nếu cần
            }

            con.commit(); // Xác nhận giao dịch nếu việc thêm thành công
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();  // Quay lại giao dịch nếu có lỗi
                } catch (SQLException rollbackEx) {
                    throw new SQLException("Error during rollback: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new SQLException("Error while creating team: " + ex.getMessage(), ex);
        } finally {
            // Giải phóng tài nguyên
            if (rs != null) {
                rs.close();
            }
            if (teamStmt != null) {
                teamStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    // Method to check if a team is in a milestone
    public boolean isTeamInMilestone(int teamId, int milestoneId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.team_milestone WHERE team_id = ? AND milestone_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, milestoneId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Return true if the team is in the milestone
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while checking team-milestone association: " + ex.getMessage(), ex);
        }
        return false;
    }

    // Method to get team members by team ID
    public List<User> getTeamMembersByTeamId(int teamId) throws SQLException {
        List<User> members = new ArrayList<>();

        // Updated SQL query to include roleID
        String sql = "SELECT u.id, u.user_name, u.full_name, u.email, u.mobile, u.role_id, "
                + "tm.allocation_id, s.setting_name "
                + "FROM project_evaluation_system.team_member tm "
                + "JOIN project_evaluation_system.user u ON tm.user_id = u.id "
                + "JOIN project_evaluation_system.setting s ON tm.allocation_id = s.id "
                + "WHERE tm.team_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setMobile(rs.getString("mobile"));

                    // Fetch roleID from the ResultSet and set it in the User object
                    user.setRoleID(rs.getInt("role_id"));
                    user.setAllocationId(rs.getInt("allocation_id"));
                    user.setSettingName(rs.getString("setting_name"));

                    members.add(user);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching team members: " + ex.getMessage(), ex);
        }
        return members;
    }

    // Method to add a member to a team
    public boolean addMemberToTeam(int teamId, int userId) throws SQLException {
        String sql = "INSERT INTO project_evaluation_system.team_member (user_id, allocation_id, team_id) VALUES (?, 10, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, teamId);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new SQLException("Error while adding member to team: " + ex.getMessage(), ex);
        }
    }

    // Method to remove a member from a team
    public boolean removeMemberFromTeam(int teamId, int userId) throws SQLException {
        String sql = "DELETE FROM project_evaluation_system.team_member WHERE team_id = ? AND user_id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            throw new SQLException("Error while removing member from team: " + ex.getMessage(), ex);
        }
    }

    public boolean isTeamNameOrTopicExists(String name, String topic, int classId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.team WHERE class_id = ? AND (name = ? OR topic = ?)";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setString(2, name);
            ps.setString(3, topic);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, team with the same name or topic exists
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while checking for existing team name or topic: " + ex.getMessage(), ex);
        }

        return false; // No duplicate found
    }

    public boolean isTeamNameExists(String name, int classId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.team WHERE class_id = ? AND name = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setString(2, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu count > 0, tên team đã tồn tại
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while checking for existing team name: " + ex.getMessage(), ex);
        }

        return false; // Không tìm thấy tên team trùng lặp
    }

    public boolean deleteTeam(int teamId) throws SQLException {
        String deleteTeamMemberSQL = "DELETE FROM project_evaluation_system.team_member WHERE team_id = ?";
        String deleteTeamSQL = "DELETE FROM project_evaluation_system.team WHERE id = ?";

        try (Connection con = getConnection(); PreparedStatement deleteTeamMemberPS = con.prepareStatement(deleteTeamMemberSQL); PreparedStatement deleteTeamPS = con.prepareStatement(deleteTeamSQL)) {

            // Xóa tất cả các bản ghi trong team_member có liên kết với team_id
            deleteTeamMemberPS.setInt(1, teamId);
            deleteTeamMemberPS.executeUpdate();

            // Sau đó xóa đội trong bảng team
            deleteTeamPS.setInt(1, teamId);
            int rowsAffected = deleteTeamPS.executeUpdate();

            return rowsAffected > 0; // Trả về true nếu đội đã bị xóa thành công
        } catch (SQLException ex) {
            throw new SQLException("Error while deleting team with ID: " + teamId, ex);
        }
    }

    public void updateTeamMemberRole(int userId, int teamId) throws SQLException {
        String updateLeaderSql = "UPDATE project_evaluation_system.team_member "
                + "SET allocation_id = 9 "
                + "WHERE user_id = ? AND team_id = ?";

        String updateMembersSql = "UPDATE project_evaluation_system.team_member "
                + "SET allocation_id = 10 "
                + "WHERE user_id != ? AND team_id = ?";

        try (Connection con = getConnection(); PreparedStatement ps1 = con.prepareStatement(updateLeaderSql); PreparedStatement ps2 = con.prepareStatement(updateMembersSql)) {

            // Update the specified user to leader
            ps1.setInt(1, userId);
            ps1.setInt(2, teamId);
            ps1.executeUpdate();

            // Update all other team members to regular members
            ps2.setInt(1, userId);
            ps2.setInt(2, teamId);
            ps2.executeUpdate();
        }
    }

    public int getTeamAllocation(int userID, int teamID) throws SQLException {
        String sql = "SELECT allocation_id FROM project_evaluation_system.team_member\n"
                + "where user_id = ?\n"
                + "and team_id =? ";
        int result = 0;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, teamID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                   result = rs.getInt("allocation_id");
                    return result;
                } else {
                    throw new SQLException("Team not found with ID: ");
                }
            }
        }
    }
    
    public Team getTeam(int classID, int userID) throws SQLException {
        String sql = "SELECT * FROM project_evaluation_system.team as t\n"
                + "join team_member as tm on tm.team_id = t.id\n"
                + "where t.class_id = ?\n"
                + "and tm.user_id = ? ";
        Team t = null;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classID);
            ps.setInt(2, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new Team();
                    t.setId(rs.getInt("id"));
                    t.setName(rs.getString("name"));
                    t.setTopic(rs.getString("topic"));
                    t.setDetail(rs.getString("detail"));
                    return t;
                } else {
                    throw new SQLException("Team not found with ID: ");
                }
            }
        }
    }
}
