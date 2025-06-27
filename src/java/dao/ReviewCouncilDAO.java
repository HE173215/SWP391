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
import model.ReviewCouncil;
import model.ReviewMember;
import util.JDBCUtils;
import static util.JDBCUtils.getConnection;

/**
 *
 * @author admin
 */
public class ReviewCouncilDAO extends JDBCUtils {

    public List<ReviewCouncil> getAllCouncils(int offset, int pageSize, String sortBy, String sortOrder) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE g.type = 'Subject'"
                    + "Order by " + sortBy + " " + sortOrder + " "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);
            // Thiết lập giá trị cho LIMIT và OFFSET
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public ReviewCouncil getCouncilsById(int id) {
        ReviewCouncil council = null;
        try {
            Connection con = getConnection();
            String sql = "SELECT r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE r.id = ? AND g.type = 'Subject'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return council;
    }

    public List<ReviewCouncil> searchCouncilsByName(String name, int offset, int pageSize) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE r.name LIKE ? AND g.type = 'Subject'"
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public List<ReviewCouncil> getCouncilsByStatus(boolean status, int offset, int pageSize) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE r.status = ? AND g.type = 'Subject'"
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, status);  // Đặt giá trị status
            ps.setInt(2, pageSize); // Đặt kích thước trang
            ps.setInt(3, offset);   // Đặt giá trị offset
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public List<ReviewCouncil> getAllClasses() {
        List<ReviewCouncil> classes = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT DISTINCT c.id ,c.name as `class`, g.code as `subject`\n"
                    + "From `class` c \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE  g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setClassId(rs.getInt("id"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));
                classes.add(council);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public List<ReviewCouncil> getAllSubjects() {
        List<ReviewCouncil> subjects = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT DISTINCT g.code AS subject "
                    + "FROM `class` c "
                    + "JOIN `group` g ON c.group_id = g.id "
                    + "WHERE g.type = 'Subject'";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setSubjectCode(rs.getString("subject"));
                subjects.add(council);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public List<ReviewCouncil> getCouncilsByClass(String classes, int offset, int pageSize) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id \n"
                    + "Join `group` g On c.group_id = g.id\n"
                    + "WHERE r.class_id = ? AND g.type = 'Subject'"
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, classes);
            ps.setInt(2, pageSize); // Đặt kích thước trang
            ps.setInt(3, offset);   // Đặt giá trị offset
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public List<ReviewCouncil> getCouncilsBySubject(String subjectCode, int offset, int pageSize) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT DISTINCT  r.id, r.name, r.description, r.created_date, r.status, c.name as class, g.code as subject\n"
                    + "FROM `review_council` r\n"
                    + "JOIN `class` c ON r.class_id = c.id\n"
                    + "JOIN `group` g ON c.group_id = g.id\n"
                    + "WHERE g.code = ? AND g.type = 'Subject'\n"
                    + "LIMIT ? OFFSET ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, subjectCode);
            ps.setInt(2, pageSize); // Set page size
            ps.setInt(3, offset);    // Set offset

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public int countAllCouncils() {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM review_council";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    public int countCouncilsByName(String name) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM review_council WHERE name LIKE ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
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

    //count status
    public int countCouncilsByStatus(boolean status) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM review_council WHERE status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    //count class
    public int countCouncilsByClass(String classes) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM review_council WHERE class_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, classes);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    public int countCouncilsBySubject(String subjectCode) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total\n"
                    + "FROM review_council r\n"
                    + "JOIN class c ON r.class_id = c.id\n"
                    + "JOIN `group` g ON c.group_id = g.id\n"
                    + "WHERE g.code = ? AND g.type = 'Subject'";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, subjectCode);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    //Add councils
    public void addCouncils(ReviewCouncil council) {
        try {
            Connection con = getConnection();
            String sql = "INSERT INTO review_council(name, description, status, created_date, class_id) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, council.getName());
            ps.setString(2, council.getDescription());
            ps.setBoolean(3, council.isStatus());
            ps.setDate(4, new java.sql.Date(council.getCreatedDate().getTime()));
            ps.setInt(5, council.getClassId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Update councils
    public void updateCouncils(ReviewCouncil council) {
        try {
            Connection con = getConnection();
            String sql = "UPDATE review_council SET name = ?, description = ?, status = ?, created_date = ?, class_id = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, council.getName());
            ps.setString(2, council.getDescription());
            ps.setBoolean(3, council.isStatus());
            ps.setDate(4, new java.sql.Date(council.getCreatedDate().getTime()));
            ps.setInt(5, council.getClassId());
            ps.setInt(6, council.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Activate/Deactivate a councils
    public void updateCouncilStatus(int id, boolean status) {
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "UPDATE review_council SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReviewCouncil> getCouncilsByPage(int page, int pageSize) {
        List<ReviewCouncil> councils = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT * FROM review_council LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (page - 1) * pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ReviewCouncil council = new ReviewCouncil();
                council.setId(rs.getInt("id"));
                council.setName(rs.getString("name"));
                council.setDescription(rs.getString("description"));
                council.setStatus(rs.getBoolean("status"));
                council.setCreatedDate(rs.getDate("created_date"));
                council.setClassName(rs.getString("class"));
                council.setSubjectCode(rs.getString("subject"));

                councils.add(council);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return councils;
    }

    public boolean isCouncilNameExists(String name) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM review_council WHERE name = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

}
