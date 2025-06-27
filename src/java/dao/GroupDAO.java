package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Group;
import util.JDBCUtils;

public class GroupDAO {

    public List<Group> getAllGroups(int offset, int pageSize) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM `group` ORDER BY id ASC LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho LIMIT và OFFSET
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getInt("id"));
                    group.setName(rs.getString("name"));
                    group.setType(rs.getString("type"));
                    group.setCode(rs.getString("code"));
                    group.setStatus(rs.getInt("status"));
                    group.setDetail(rs.getString("detail"));
                    groups.add(group);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    // Đếm tổng số bản ghi
    public int countAll() {
        String sql = "SELECT COUNT(*) AS total FROM `group`";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public Group getGroupById(int id) {
        Group group = null;
        String query = "SELECT * FROM `group` WHERE id = ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setType(rs.getString("type"));
                group.setCode(rs.getString("code"));
                group.setStatus(rs.getInt("status"));
                group.setDetail(rs.getString("detail"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return group;
    }

    public List<Group> searchGroupByName(String name, int offset, int pageSize) {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM `group` WHERE name LIKE ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setType(rs.getString("type"));
                group.setCode(rs.getString("code"));
                group.setStatus(rs.getInt("status"));
                group.setDetail(rs.getString("detail"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public int countGroupsByName(String name) {
        String query = "SELECT COUNT(*) AS total FROM `group` WHERE name LIKE ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public void addGroup(Group group) {
        String query = "INSERT INTO `group` (code, name, detail, status, type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, group.getCode());
            preparedStatement.setString(2, group.getName());
            preparedStatement.setString(3, group.getDetail());
            preparedStatement.setInt(4, group.getStatus());
            preparedStatement.setString(5, group.getType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(Group group) {
        String query = "UPDATE `group` SET code = ?, name = ?, detail = ?, status = ?, type = ? WHERE id = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, group.getCode());
            preparedStatement.setString(2, group.getName());
            preparedStatement.setString(3, group.getDetail());
            preparedStatement.setInt(4, group.getStatus());
            preparedStatement.setString(5, group.getType());
            preparedStatement.setInt(6, group.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM `group`";

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

    public List<Group> getGroupsByType(String type, int offset, int pageSize) {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM `group` WHERE type = ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setType(rs.getString("type"));
                group.setCode(rs.getString("code"));
                group.setStatus(rs.getInt("status"));
                group.setDetail(rs.getString("detail"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public int countGroupsByType(String type) {
        String query = "SELECT COUNT(*) AS total FROM `group` WHERE type = ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, type);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public List<Group> getGroupsByStatus(int status, int offset, int pageSize) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM `group` WHERE status = ? LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setType(rs.getString("type"));
                group.setCode(rs.getString("code"));
                group.setStatus(rs.getInt("status"));
                group.setDetail(rs.getString("detail"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public int countGroupsByStatus(int status) {
        String sql = "SELECT COUNT(*) AS total FROM `group` WHERE status = ?";
        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    public List<Group> getGroupsByPage(int page, int pageSize) {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM `group` LIMIT ? OFFSET ?";

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (page - 1) * pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setType(rs.getString("type"));
                group.setCode(rs.getString("code"));
                group.setStatus(rs.getInt("status"));
                group.setDetail(rs.getString("detail"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    // Đếm tổng số bản ghi trong bảng group
    public int getTotalGroups() {
        String query = "SELECT COUNT(*) FROM group";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Group> filterGroups(String typeFilter, String statusFilter, int offset, int pageSize) {
        List<Group> groups = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM `group` WHERE 1=1");

        if (typeFilter != null && !typeFilter.isEmpty()) {
            query.append(" AND type = ?");
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            query.append(" AND status = ?");
        }

        query.append(" LIMIT ? OFFSET ?");

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (typeFilter != null && !typeFilter.isEmpty()) {
                preparedStatement.setString(paramIndex++, typeFilter);
            }

            if (statusFilter != null && !statusFilter.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(statusFilter));
            }

            preparedStatement.setInt(paramIndex++, pageSize);
            preparedStatement.setInt(paramIndex++, offset);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setName(rs.getString("name"));
                    group.setType(rs.getString("type"));
                    group.setCode(rs.getString("code"));
                    group.setStatus(rs.getInt("status"));
                    group.setDetail(rs.getString("detail"));
                    groups.add(group);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public int countGroupsByFilters(String typeFilter, String statusFilter) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) AS total FROM `group` WHERE 1=1");

        if (typeFilter != null && !typeFilter.isEmpty()) {
            query.append(" AND type = ?");
        }

        if (statusFilter != null && !statusFilter.isEmpty()) {
            query.append(" AND status = ?");
        }

        int totalRecords = 0;

        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (typeFilter != null && !typeFilter.isEmpty()) {
                preparedStatement.setString(paramIndex++, typeFilter);
            }

            if (statusFilter != null && !statusFilter.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(statusFilter));
            }

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public void updateGroupByStatus(int id, int newStatus) {
        String query = "UPDATE `group` SET status = ? WHERE id = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isNameExists(String groupName) {
        String query = "SELECT COUNT(*) FROM `group` WHERE name = ?";
        try (Connection conn = JDBCUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, groupName);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isGroupNameExists(String groupName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Group> getGroupsByType(String type) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Connection con = null; // ket noi vs sql
    PreparedStatement ps = null; // nhan cau lenh
    ResultSet rs = null; // tra kq

    public List<Group> getSubjectList() {
        String query = "SELECT * FROM project_evaluation_system.group\n"
                + "where type = 'Subject';";

        List<Group> subjectList = new ArrayList<>();
        Group g = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    g = new Group();
                    g.setId(rs.getInt("id"));
                    g.setCode(rs.getString("code"));
                    g.setName(rs.getString("name"));
                    g.setDetail(rs.getString("detail"));
                    g.setStatus(rs.getInt("status"));
                    subjectList.add(g);
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
        return subjectList;
    }

    public List<Group> getSubjectListByUserID(int userID) {
        String query = "SELECT g.* FROM project_evaluation_system.group as g\n"
                + "join user_group as ug on g.id = ug.group_id\n"
                + "where type = 'Subject'\n"
                + "and ug.user_id = ? ";

        List<Group> subjectList = new ArrayList<>();
        Group g = null;
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;

        try {
            // Mở kết nối
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Chuẩn bị câu truy vấn
                ps = con.prepareStatement(query);
                ps.setInt(1, userID);
                // Thực thi câu truy vấn và nhận kết quả
                rs = ps.executeQuery();

                // Duyệt qua ResultSet
                while (rs.next()) {
                    g = new Group();
                    g.setId(rs.getInt("id"));
                    g.setCode(rs.getString("code"));
                    g.setName(rs.getString("name"));
                    g.setDetail(rs.getString("detail"));
                    g.setStatus(rs.getInt("status"));
                    subjectList.add(g);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // Đóng ResultSet
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Đóng PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Đóng Connection
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return subjectList;
    }

    public Group getSubjectByClassID(int classID) {
        String query = "SELECT g.* FROM project_evaluation_system.group as g\n"
                + "join class as c on c.group_id = g.id\n"
                + "where type = 'Subject'\n"
                + "and c.id = ? ";
        Group g = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, classID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    g = new Group();
                    g.setId(rs.getInt("id"));
                    g.setCode(rs.getString("code"));
                    g.setName(rs.getString("name"));
                    g.setDetail(rs.getString("detail"));
                    g.setStatus(rs.getInt("status"));
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
        return g;        
    }

}
