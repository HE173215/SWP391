/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Requirement;
import util.JDBCUtils;
import java.sql.SQLException;
import model.User;
import java.sql.*;

/**
 *
 * @author Do Duan
 */
public class RequirementDAO {

    private Connection con = null; // ket noi vs sql
    private PreparedStatement ps = null; // nhan cau lenh
    private ResultSet rs = null; // tra kq

    public List<Requirement> getRequirement(int classID, int userID, int teamID, int complexityID, int status, String searchString,
            int limit, int offset) {
        StringBuilder query = new StringBuilder("SELECT r.id,r.tittle,r.description,r.status, ss.name as comlexity ,ss.value,t.id as teamID, t.name as 'team name',u.user_name as owner,r.owner_id,m.name as milestone,m.end_date\n"
                + "                FROM project_evaluation_system.requirement as r\n"
                + "                join subject_setting as ss on r.complexity_id = ss.id\n"
                + "               join team as t on t.id = r.team_id\n"
                + "                join user as u on u.id  = r.creator_id \n"
                + "                join work_eval we on we.req_id = r.id\n"
                + "                join milestone m on m.id = we.milestone_id\n"
                + "               where ss.type = 'Complexity'\n"
                + "                and t.class_id = ? \n"
                + "                and r.creator_id = ? \n"
                + "                and t.id = ? ");
        List<Requirement> requirementList = new ArrayList<>();
        Requirement r = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)

                // Thêm điều kiện cho settingID nếu có
                if (complexityID != 0) {
                    query.append(" and r.complexity_id = ? \n");
                }

                if (status != -1) {
                    query.append(" and r.status = ? \n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, classID);
                ps.setInt(2, userID);
                ps.setInt(3, teamID);
                int paramIndex = 4;

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (complexityID != 0) {
                    ps.setInt(paramIndex++, complexityID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, limit);
                ps.setInt(paramIndex++, offset);

                rs = ps.executeQuery();
                while (rs.next()) {
                    r = new Requirement();
                    r.setId(rs.getInt("id"));
                    r.setTittle(rs.getString("tittle"));
                    r.setDescription(rs.getString("description"));
                    r.setStatus(rs.getInt("status"));
                    r.setComplexity(rs.getString("comlexity"));
                    r.setComplexValue(rs.getInt("value"));
                    r.setTeamID(rs.getInt("teamID"));
                    r.setTeam(rs.getString("team name"));
                    if (rs.getString("owner") == null) {
                        r.setOwner(rs.getString("none"));
                    } else {
                        r.setOwner(rs.getString("owner"));
                    }
                    r.setOwnerID(rs.getInt("owner_id"));
                    r.setMilestone(rs.getString("milestone"));

                    requirementList.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return requirementList;
    }

    public int getTotalRecordPersonal(int classID, int userID, int teamID, int complexityID, int status, String searchString) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records from (\n"
                + "SELECT r.id,r.tittle,r.description,r.status, ss.name as comlexity ,ss.value,t.id as teamID, t.name as 'team name',u.user_name as owner,m.name as milestone,m.end_date\n"
                + "                FROM project_evaluation_system.requirement as r\n"
                + "                join subject_setting as ss on r.complexity_id = ss.id\n"
                + "               join team as t on t.id = r.team_id\n"
                + "                join user as u on u.id  = r.creator_id \n"
                + "                join work_eval we on we.req_id = r.id\n"
                + "                join milestone m on m.id = we.milestone_id\n"
                + "               where ss.type = 'Complexity'\n"
                + "                and t.class_id = ? \n"
                + "                and r.creator_id = ? \n"
                + "                and t.id = ? ");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)

                // Thêm điều kiện cho settingID nếu có
                if (complexityID != 0) {
                    query.append(" and r.complexity_id = ? \n");
                }

                if (status != -1) {
                    query.append(" and r.status = ? \n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }

                query.append(") as subquery");
//                query.append("LIMIT ? OFFSET ?");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, classID);
                ps.setInt(2, userID);
                ps.setInt(3, teamID);
                int paramIndex = 4;

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (complexityID != 0) {
                    ps.setInt(paramIndex++, complexityID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                rs = ps.executeQuery();
                if (rs.next()) {
                    // Thực thi truy vấn
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        totalRecords = rs.getInt("total_records");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return totalRecords;
    }

    public List<Requirement> getRequirementByTeamID(int teamID, int complexityID, int ownerID, int status, String searchString,
            int limit, int offset) {
        StringBuilder query = new StringBuilder("SELECT r.id,r.tittle,r.description,r.status, ss.name as comlexity ,ss.value,t.id as teamID, t.name as 'team name',u.user_name as owner, r.owner_id, m.name as milestone\n"
                + "                FROM project_evaluation_system.requirement as r\n"
                + "                join subject_setting as ss on r.complexity_id = ss.id\n"
                + "                join team as t on t.id = r.team_id\n"
                + "                join user as u on u.id  = r.owner_id\n"
                + "                join work_eval we on we.req_id = r.id\n"
                + "                join milestone m on m.id = we.milestone_id\n"
                + "                where ss.type = 'Complexity'\n"
                + "              and t.id = ? ");
        List<Requirement> requirementList = new ArrayList<>();
        Requirement r = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)

                // Thêm điều kiện cho settingID nếu có
                if (complexityID != 0) {
                    query.append(" and r.complexity_id = ? \n");
                }

                if (ownerID != 0) {
                    query.append(" and r.owner_id = ? \n");
                }

                if (status != -1) {
                    query.append(" and r.status = ? \n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, teamID);

                int paramIndex = 2;

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (complexityID != 0) {
                    ps.setInt(paramIndex++, complexityID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (ownerID != 0) {
                    ps.setInt(paramIndex++, ownerID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }
                ps.setInt(paramIndex++, limit);
                ps.setInt(paramIndex++, offset);

                rs = ps.executeQuery();
                while (rs.next()) {
                    r = new Requirement();
                    r.setId(rs.getInt("id"));
                    r.setTittle(rs.getString("tittle"));
                    r.setDescription(rs.getString("description"));
                    r.setStatus(rs.getInt("status"));
                    r.setComplexity(rs.getString("comlexity"));
                    r.setComplexValue(rs.getInt("value"));
                    r.setTeamID(rs.getInt("teamID"));
                    r.setTeam(rs.getString("team name"));
                    if (rs.getString("owner") == null) {
                        r.setOwner(rs.getString("none"));
                    } else {
                        r.setOwner(rs.getString("owner"));
                    }
                    r.setOwnerID(rs.getInt("owner_id"));
                    r.setMilestone(rs.getString("milestone"));

                    requirementList.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return requirementList;
    }

    public int getTotalRecordTeam(int teamID, int complexityID, int ownerID, int status, String searchString) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records from (\n"
                + "SELECT r.id,r.tittle,r.description,r.status, ss.name as comlexity ,ss.value,t.id as teamID, t.name as 'team name',u.user_name as owner,m.name as milestone\n"
                + "                FROM project_evaluation_system.requirement as r\n"
                + "                join subject_setting as ss on r.complexity_id = ss.id\n"
                + "                join team as t on t.id = r.team_id\n"
                + "                join user as u on u.id  = r.owner_id\n"
                + "                join work_eval we on we.req_id = r.id\n"
                + "                join milestone m on m.id = we.milestone_id\n"
                + "                where ss.type = 'Complexity'\n"
                + "              and t.id = ?");
        int result = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)

                // Thêm điều kiện cho settingID nếu có
                if (complexityID != 0) {
                    query.append(" and r.complexity_id = ? \n");
                }

                if (ownerID != 0) {
                    query.append(" and r.owner_id = ? \n");
                }

                if (status != -1) {
                    query.append(" and r.status = ? \n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }
                query.append(") as subquery");
//                query.append("LIMIT ? OFFSET ?");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, teamID);

                int paramIndex = 2;

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (complexityID != 0) {
                    ps.setInt(paramIndex++, complexityID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (ownerID != 0) {
                    ps.setInt(paramIndex++, ownerID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                rs = ps.executeQuery();
                if (rs.next()) {
                    // Thực thi truy vấn
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        result = rs.getInt("total_records");
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public Requirement getRequirementByID(int id) {
        String query = "SELECT r.id,r.tittle,r.description,r.status, ss.name as comlexity ,ss.value,t.id as teamID, t.name as 'team name',u.user_name as owner,r.owner_id,we.milestone_id\n"
                + "                FROM project_evaluation_system.requirement as r\n"
                + "                join subject_setting as ss on r.complexity_id = ss.id\n"
                + "                join team as t on t.id = r.team_id\n"
                + "              join user as u on u.id  = r.owner_id\n"
                + "              join work_eval we on we.req_id = r.id\n"
                + "                where ss.type = 'Complexity'\n"
                + "                and r.id= ? ";
        Requirement r = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    r = new Requirement();
                    r.setId(rs.getInt("id"));
                    r.setTittle(rs.getString("tittle"));
                    r.setDescription(rs.getString("description"));
                    r.setStatus(rs.getInt("status"));
                    r.setComplexity(rs.getString("comlexity"));
                    r.setComplexValue(rs.getInt("value"));
                    r.setTeamID(rs.getInt("teamID"));
                    r.setTeam(rs.getString("team name"));
                    r.setOwnerID(rs.getInt("owner_id"));
                    r.setMilestoneID(rs.getString("milestone_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return r;
    }

    public User getMemberByRequirementID(int reqID) {
        String query = "SELECT u.id,u.user_name,u.status FROM project_evaluation_system.user as u\n"
                + "join requirement as r on r.owner_id = u.id\n"
                + "where u.status = 1\n"
                + "and r.id = ?; ";
        User u = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, reqID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUserName(rs.getString("user_name"));
                    u.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return u;
    }

    public int createRequirement(Requirement r) throws SQLException {
        String query = "INSERT INTO `project_evaluation_system`.`requirement` (`tittle`, `description`, `status`, `team_id`, `complexity_id`, `owner_id`, `creator_id`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = JDBCUtils.getConnection(); PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Thiết lập các giá trị cho các tham số trong câu lệnh SQL
            ps.setString(1, r.getTittle());
            ps.setString(2, r.getDescription());
            ps.setInt(3, r.getStatus());
            ps.setInt(4, r.getTeamID());
            ps.setInt(5, r.getComplexityID());
            if (r.getOwnerID() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, r.getOwnerID());
            }
            ps.setInt(7, r.getCreatorID());

            // Thực thi câu lệnh SQL và lấy ID của bản ghi vừa được tạo
            int result = ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy được ID của bản ghi vừa tạo.");
                }
            }
        }
    }

    public int putReqToWorkEval(int reqID, int milestoneID) {
        String query = "INSERT INTO `project_evaluation_system`.`work_eval` (`req_id`, `milestone_id`)"
                + " VALUES (?, ?);";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, reqID);
                ps.setInt(2, milestoneID);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    public int updateStatusRequirement(int id, int newStatus) {
        String query = "UPDATE `project_evaluation_system`.`requirement`"
                + " SET `status` = ? "
                + " WHERE (`id` = ? );";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, newStatus);
                ps.setInt(2, id);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    public int updateRequirement(Requirement r) {
        String query = "UPDATE `project_evaluation_system`.`requirement`"
                + " SET `tittle` = ? ,"
                + "`description` = ?,"
                + " `status` = ? ,"
                + " `team_id` = ? ,"
                + " `complexity_id` = ? ,"
                + " `owner_id` = ? "
                + " WHERE (`id` = ? ); ";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, r.getTittle());
                ps.setString(2, r.getDescription());
                ps.setInt(3, r.getStatus());
                ps.setInt(4, r.getTeamID());
                ps.setInt(5, r.getComplexityID());
                if (r.getOwnerID() == 0) {
                    ps.setNull(6, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(6, r.getOwnerID());
                }
                ps.setInt(7, r.getId());
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    public int updateMilestone(int reqID, String milestoneID) {
        String query = "UPDATE `project_evaluation_system`.`work_eval` "
                + "SET `milestone_id` = ? "
                + " WHERE (`req_id` = ?);";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, milestoneID);
                ps.setInt(2, reqID);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    public int updateStatus(int id, int newStatus) {
        String query = "UPDATE `project_evaluation_system`.`requirement` SET `status` = ? WHERE (`id` = ?);";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, newStatus);
                ps.setInt(2, id);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    public List<Requirement> getClassRequirement(int classID, String teamID, String milestoneID, String status, String search,
            int limit, int offset) {
        StringBuilder query = new StringBuilder("SELECT r.*,m.name,u.user_name as owner,t.name as team_name,m.id as mID,m.name as milestone\n"
                + "FROM project_evaluation_system.requirement r\n"
                + "join work_eval we on we.req_id = r.id\n"
                + "join milestone m on m.id = we.milestone_id\n"
                + "join team t on t.id = r.team_id\n"
                + "join class c on t.class_id = c.id\n"
                + "join class_teacher ct on c.id = ct.class_id\n"
                + "join user u on u.id = r.owner_id \n"
                + "where c.id = ? ");
        List<Requirement> requirementList = new ArrayList<>();
        Requirement r = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {

                if (!teamID.equals("")) {
                    query.append(" and r.team_id = ? \n");
                }

                if (!milestoneID.equals("")) {
                    query.append(" and m.id = ? \n");
                }

                if (!status.equals("")) {
                    query.append(" and r.status = ? \n");
                }

                if (!search.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, classID);
//                ps.setString(2, teacherID);

                int paramIndex = 2;

                if (!teamID.equals("")) {
                    ps.setString(paramIndex++, teamID);
                }

                if (!milestoneID.equals("")) {
                    ps.setString(paramIndex++, milestoneID);

                }

                if (!status.equals("")) {
                    ps.setString(paramIndex++, status);

                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!search.equals("")) {
                    ps.setString(paramIndex++, "%" + search.toLowerCase() + "%");
                }
                ps.setInt(paramIndex++, limit);
                ps.setInt(paramIndex++, offset);

                rs = ps.executeQuery();
                while (rs.next()) {
                    r = new Requirement();
                    r.setId(rs.getInt("id"));
                    r.setTittle(rs.getString("tittle"));
                    r.setDescription(rs.getString("description"));
                    r.setStatus(rs.getInt("status"));

//                    r.setComplexity(rs.getString("comlexity"));
//                    r.setComplexValue(rs.getInt("value"));
                    r.setTeamID(rs.getInt("team_id"));
                    r.setTeam(rs.getString("team_name"));
                    r.setOwnerID(rs.getInt("owner_id"));
                    if (rs.getString("owner") == null) {
                        r.setOwner(rs.getString("none"));
                    } else {
                        r.setOwner(rs.getString("owner"));
                    }
                    r.setMilestoneID(rs.getString("mID"));
                    r.setMilestone(rs.getString("milestone"));

                    requirementList.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return requirementList;

    }

    public int getTotalRecordClassRequirement(int classID, String teamID, String milestoneID, String status, String search) {
        StringBuilder query = new StringBuilder("select count(*) as total_records from(\n"
                + "SELECT r.*,m.name,u.user_name as owner,t.name as team_name,m.id as mID,m.name as milestone\n"
                + "FROM project_evaluation_system.requirement r\n"
                + "join work_eval we on we.req_id = r.id\n"
                + "join milestone m on m.id = we.milestone_id\n"
                + "join team t on t.id = r.team_id\n"
                + "join class c on t.class_id = c.id\n"
                + "join class_teacher ct on c.id = ct.class_id\n"
                + "join user u on u.id = r.owner_id \n"
                + "where c.id = ? ");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {

                if (!teamID.equals("")) {
                    query.append(" and r.team_id = ? \n");
                }

                if (!milestoneID.equals("")) {
                    query.append(" and m.id = ? \n");
                }

                if (!status.equals("")) {
                    query.append(" and r.status = ? \n");
                }

                if (!search.equals("")) {
                    query.append(" AND lower(r.tittle) LIKE ?\n");
                }
                query.append(") as subquery");

                ps = con.prepareStatement(query.toString());
                ps.setInt(1, classID);

                int paramIndex = 2;

                if (!teamID.equals("")) {
                    ps.setString(paramIndex++, teamID);
                }

                if (!milestoneID.equals("")) {
                    ps.setString(paramIndex++, milestoneID);

                }

                if (!status.equals("")) {
                    ps.setString(paramIndex++, status);

                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!search.equals("")) {
                    ps.setString(paramIndex++, "%" + search.toLowerCase() + "%");
                }

                rs = ps.executeQuery();
                if (rs.next()) {
                    // Thực thi truy vấn
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        totalRecords = rs.getInt("total_records");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return totalRecords;

    }

}
