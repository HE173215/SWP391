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
import util.JDBCUtils;

import model.Class;

/**
 *
 * @author Do Duan
 */
public class ClassDAO {

    private Connection con = null; // ket noi vs sql
    private PreparedStatement ps = null; // nhan cau lenh
    private ResultSet rs = null; // tra kq

    public int createClass(Class c) {
        String query = "insert into class(name,detail,status,semester_id,group_id)\n"
                + "values (?,?,?,?,?)";
        int result = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, c.getName());
                ps.setString(2, c.getDetail());
                ps.setInt(3, c.getStatus());
                ps.setInt(4, c.getSemesterID());
                ps.setInt(5, c.getGroupID());
                result = ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    public Class getClassByID(int id) {
        String query = "SELECT c.* ,g.code \n"
                + "FROM project_evaluation_system.class as c\n"
                + "join `group` as g on g.id= c.group_id\n"
                + "where c.id = ? ";
        Class c = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSubjectCode(rs.getString("code"));
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
        return c;
    }

    // group class list
    public List<Class> searchClass(int userID, int settingID, int groupID, String searchString,
            int itemPerPage, int pageNumber) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,g.code,g.name as subjectName, s.setting_name,u.user_name,u.role_id, s.status \n"
                + "FROM project_evaluation_system.class as c\n"
                + "               join `group` as g on c.group_id = g.id\n"
                + "               join setting as s on c.semester_id = s.id\n"
                + "                join class_member as cm on cm.class_id = c.id\n"
                + "                join user as u on cm.user_id = u.id \n"
                + "                where c.status = 1\n"
                + "AND s.status = 1\n"
                + "               AND u.id = ? \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("subjectName"));
                    classList.add(c);
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
        return classList;
    }

    // class list teacher
    public List<Class> getClassTeacher(int userID, int settingID, int groupID, String searchString,
            int itemPerPage, int pageNumber) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,g.code,g.name as subjectName, s.setting_name,u.user_name,u.role_id, s.status \n"
                + "                FROM project_evaluation_system.class as c\n"
                + "                            join `group` as g on c.group_id = g.id\n"
                + "                              join setting as s on c.semester_id = s.id\n"
                + "                             join class_teacher ct on ct.class_id = c.id\n"
                + "                              join user as u on ct.teacher_id = u.id                               \n"
                + "                                where c.status = 1\n"
                + "                AND s.status = 1\n"
                + "                             AND u.id = ? \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("subjectName"));
                    classList.add(c);
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
        return classList;
    }

    public List<Class> searchClass(int userID, int settingID, int groupID, String searchString
    ) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,\n"
                + "g.code,g.name,\n"
                + "s.setting_name,\n"
                + "u.user_name,u.role_id FROM project_evaluation_system.class as c\n"
                + "join `group` as g on c.group_id = g.id\n"
                + "join setting as s on c.semester_id = s.id\n"
                + "join user_group as ug on ug.group_id = g.id\n"
                + "join user as u on ug.user_id=u.id \n"
                + "where c.status = 1\n"
                + "AND u.id = ?\n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
//                    c.setSemesterID(rs.getInt("semester_id"));
//                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("name"));
                    classList.add(c);
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
        return classList;
    }

    public int getTotalRecord(int userID, int settingID, int groupID, String searchString, int limit, int offset) {

        StringBuilder query = new StringBuilder(" SELECT COUNT(*) as total_records\n"
                + "FROM (\n"
                + "    SELECT c.id,\n"
                + "g.code,g.name,\n"
                + "s.setting_name,\n"
                + "u.user_name,u.role_id\n"
                + "    FROM project_evaluation_system.class as c\n"
                + "    JOIN `group` as g ON c.group_id = g.id\n"
                + "    JOIN setting as s ON c.semester_id = s.id\n"
                + "    JOIN user_group as ug ON ug.group_id = g.id\n"
                + "    JOIN user as u ON ug.user_id = u.id\n"
                + "    WHERE c.status = 1\n"
                + "    AND u.id = ? \n");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }
                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }
                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }
                query.append("LIMIT ? OFFSET ?");

                query.append(") as subquery");
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }
                ps.setInt(paramIndex++, limit);
                ps.setInt(paramIndex++, offset);

                // Thực thi truy vấn
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalRecords = rs.getInt("total_records");
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

    public int getTotalRecord(int userID, int settingID, int groupID, String searchString) {

        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records\n"
                + "FROM (\n"
                + "    SELECT c.*,g.code,g.name as subjectName, s.setting_name,u.user_name,u.role_id \n"
                + "FROM project_evaluation_system.class as c\n"
                + "               join `group` as g on c.group_id = g.id\n"
                + "               join setting as s on c.semester_id = s.id\n"
                + "                join class_member as cm on cm.class_id = c.id\n"
                + "                join user as u on cm.user_id = u.id \n"
                + "                where c.status = 1\n"
                + "               AND u.id = ? \n");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }
                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }
                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append(") as subquery");
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                // Thực thi truy vấn
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalRecords = rs.getInt("total_records");
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

    public int getTotalClass() {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records\n"
                + "FROM (\n"
                + "    SELECT c.* \n"
                + "FROM project_evaluation_system.class as c\n"
        );
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {

                query.append(") as subquery");
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                // Đặt giá trị cho tham số đầu tiên (userID)

                // Thực thi truy vấn
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalRecords = rs.getInt("total_records");
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

    public int getTotalClassByDept(int deptID, int settingID, int groupID, int status, String searchString) {

        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records\n"
                + "FROM (\n"
                + "    SELECT c.*,g.code,g.name as subjectName, s.setting_name\n"
                + "               FROM project_evaluation_system.class as c\n"
                + "                join `group` as g on c.group_id = g.id\n"
                + "                join setting as s on c.semester_id = s.id					\n"
                + "                join user_group as ug on ug.group_id = g.id\n"
                + "                where ug.user_id = ? ");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }
                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                if (status != -1) {
                    query.append(" and c.status = ?\n");
                }
                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append(") as subquery");
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, deptID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }
                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                // Thực thi truy vấn
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalRecords = rs.getInt("total_records");
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

    public int getTotalClassByTeacher(int deptID, int settingID, int groupID, int status, String searchString) {

        StringBuilder query = new StringBuilder("SELECT COUNT(*) as total_records\n"
                + "FROM (\n"
                + "    SELECT c.*,g.code,g.name as subjectName, s.setting_name\n"
                + "               FROM project_evaluation_system.class as c\n"
                + "                join `group` as g on c.group_id = g.id\n"
                + "                join setting as s on c.semester_id = s.id					\n"
                + "                join class_teacher as ct on ct.class_id = c.id\n"
                + "                where ct.teacher_id = ? ");
        int totalRecords = 0;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }
                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                if (status != -1) {
                    query.append(" and c.status = ?\n");
                }
                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                query.append(") as subquery");
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, deptID);

                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }
                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                // Thực thi truy vấn
                rs = ps.executeQuery();
                if (rs.next()) {
                    totalRecords = rs.getInt("total_records");
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

    public List<Class> getClassListByUserID(int userID, int subjectID) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.* FROM project_evaluation_system.group as g\n"
                + "join user_group as ug on g.id = ug.group_id\n"
                + "join class as c on c.group_id = ug.group_id\n"
                + "where type = 'Subject'\n"
                + "and ug.user_id = ? \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                if (subjectID != 0) {
                    query.append(" and ug.group_id = ? ");
                }
                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, userID);
                if (subjectID != 0) {
                    ps.setInt(2, subjectID);
                }
                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    classList.add(c);
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
        return classList;
    }

    public List<Class> getAllClass() {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT * FROM project_evaluation_system.class \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    classList.add(c);
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
        return classList;
    }

    public List<Class> getAllClass(int settingID, int groupID, int status, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,g.code,g.name as subjectName, s.setting_name\n"
                + "FROM project_evaluation_system.class as c\n"
                + "join `group` as g on c.group_id = g.id\n"
                + "join setting as s on c.semester_id = s.id \n"
        );

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                if (status != -1) {
                    query.append(" AND c.status = ? \n");
                }
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                if (sortBy != null && sortOrder != null) {
                    query.append("order by " + sortBy + " " + sortOrder + "\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
//                ps.setInt(paramIndex++, userID);
                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }
                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("subjectName"));
                    classList.add(c);
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
        return classList;

    }

    public List<Class> getClassListByDeptUserID(int userID, int settingID, int groupID, int status, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,g.code,g.name as subjectName, s.setting_name\n"
                + "FROM project_evaluation_system.class as c\n"
                + "join `group` as g on c.group_id = g.id\n"
                + "join setting as s on c.semester_id = s.id					\n"
                + "join user_group as ug on ug.group_id = g.id\n"
                + "where ug.user_id = ? \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                if (status != -1) {
                    query.append(" AND c.status = ? \n");
                }
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                if (sortBy != null && sortOrder != null) {
                    query.append("order by " + sortBy + " " + sortOrder + "\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }
                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("subjectName"));
                    classList.add(c);
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
        return classList;
    }

    public List<Class> getClassListByTeacherID(int userID, int settingID, int groupID, int status, String searchString,
            int itemPerPage, int pageNumber, String sortBy, String sortOrder) {
        List<Class> classList = new ArrayList<>();
        Class c = null;
        StringBuilder query = new StringBuilder("SELECT c.*,g.code,g.name as subjectName, s.setting_name\n"
                + "                FROM project_evaluation_system.class as c\n"
                + "                join `group` as g on c.group_id = g.id\n"
                + "                join setting as s on c.semester_id = s.id					\n"
                + "                join class_teacher as ct on ct.class_id = c.id\n"
                + "              where ct.teacher_id = ? \n");

        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                // Bắt đầu với tham số đầu tiên (userID)
                int paramIndex = 1;

                if (status != -1) {
                    query.append(" AND c.status = ? \n");
                }
                // Thêm điều kiện cho settingID nếu có
                if (settingID != 0) {
                    query.append(" AND s.id = ?\n");
                }

                // Thêm điều kiện cho groupID nếu có
                if (groupID != 0) {
                    query.append(" AND g.id = ?\n");
                }

                // Thêm điều kiện cho searchString nếu có
                if (!searchString.equals("")) {
                    query.append(" AND lower(c.name) LIKE ?\n");
                }

                if (sortBy != null && sortOrder != null) {
                    query.append("order by " + sortBy + " " + sortOrder + "\n");
                }

                query.append("LIMIT ? OFFSET ?");

                // Chuẩn bị PreparedStatement với câu truy vấn động
                ps = con.prepareStatement(query.toString());

                // Đặt giá trị cho tham số đầu tiên (userID)
                ps.setInt(paramIndex++, userID);

                if (status != -1) {
                    ps.setInt(paramIndex++, status);
                }
                // Nếu settingID != 0, thêm giá trị cho tham số
                if (settingID != 0) {
                    ps.setInt(paramIndex++, settingID);
                }

                // Nếu groupID != 0, thêm giá trị cho tham số
                if (groupID != 0) {
                    ps.setInt(paramIndex++, groupID);
                }

                // Nếu searchString không rỗng, thêm giá trị cho tham số
                if (!searchString.equals("")) {
                    ps.setString(paramIndex++, "%" + searchString.toLowerCase() + "%");
                }

                ps.setInt(paramIndex++, itemPerPage);
                ps.setInt(paramIndex++, pageNumber);

                // Thực thi truy vấn
                rs = ps.executeQuery();

                // Xử lý kết quả từ ResultSet
                while (rs.next()) {
                    // Xử lý dữ liệu ở đây
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
                    c.setSemester(rs.getString("setting_name"));
                    c.setSubjectCode(rs.getString("code"));
                    c.setSubjectName(rs.getString("subjectName"));
                    classList.add(c);
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
        return classList;
    }

    public int updateStatusClass(int id, int newStatus) {
        String query = "UPDATE `class` SET status = ? WHERE id = ?";
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

    public int updateClass(Class updatedClass) {
        String query = "UPDATE `project_evaluation_system`.`class` "
                + "SET `name` = ? , "
                + "`detail` = ? , "
                + "`status` = ?, "
                + "`semester_id` = ? , "
                + "`group_id` = ? "
                + "WHERE (`id` = ? ) ";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, updatedClass.getName());
                ps.setString(2, updatedClass.getDetail());
                ps.setInt(3, updatedClass.getStatus());
                ps.setInt(4, updatedClass.getSemesterID());
                ps.setInt(5, updatedClass.getGroupID());
                ps.setInt(6, updatedClass.getId());

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

    public Class getClassByMilestoneID(int id) {
        String query = "SELECT c.* FROM project_evaluation_system.class as c\n"
                + "join milestone as m on c.id = m.class_id\n"
                + "where m.id = ? ";
        Class c = null;
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c = new Class();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDetail(rs.getString("detail"));
                    c.setStatus(rs.getInt("status"));
                    c.setSemesterID(rs.getInt("semester_id"));
                    c.setGroupID(rs.getInt("group_id"));
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
        return c;
    }

    public boolean checkClassNameExist(String className, String subjectId, String semeterID) {
        String query = "select count(*)  from (\n"
                + "SELECT c.*\n"
                + "               FROM project_evaluation_system.class as c \n"
                + "                where c.name = ? \n"
                + "                and c.semester_id = ? \n"
                + "                and c.group_id = ? ) as subquery ";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, className);
                ps.setString(2, semeterID);
                ps.setString(3, subjectId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
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
        return false;
    }

    public int getClassID(String className, String subjectId, String semeterID) {
        String query = "SELECT c.id\n"
                + "FROM project_evaluation_system.class as c \n"
                + "where c.name = ?\n"
                + "and c.semester_id = ?\n"
                + " and c.group_id = ? \n";
        try {
            con = JDBCUtils.getConnection();
            if (con != null) {
                ps = con.prepareStatement(query);
                ps.setString(1, className);
                ps.setString(2, semeterID);
                ps.setString(3, subjectId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
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
        return 0;
    }

}
