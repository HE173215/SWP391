package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import util.JDBCUtils;
import service.BcryptService;
import service.EmailUtils;
import static util.JDBCUtils.getConnection;

public class UserDAO extends JDBCUtils {

    // Phương thức lấy danh sách tất cả người dùng
    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT id, user_name, full_name, email, mobile, password, status, role_id, image, notes, otp, otp_expiry FROM project_evaluation_system.user";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setPassword(rs.getString("password"));
                user.setStatus(rs.getInt("status"));
                user.setRoleID(rs.getInt("role_id"));
                user.setImage(rs.getString("image"));
                user.setNotes(rs.getString("notes"));
                user.setOtp(rs.getString("otp"));
                user.setOtp_expiry(rs.getTimestamp("otp_expiry")); // Sửa từ getDate thành getTimestamp
                userList.add(user);
            }
        } catch (SQLException ex) {
            throw new SQLException("Lỗi khi lấy danh sách người dùng: " + ex.getMessage(), ex);
        }

        return userList;
    }

    // Phương thức đăng nhập người dùng
    public User getUserLogin(String username, String password) throws Exception {
        String query = "SELECT id, user_name, full_name, email, mobile, password, status, role_id, notes, image, otp, otp_expiry "
                + "FROM project_evaluation_system.user WHERE user_name = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserName(resultSet.getString("user_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setMobile(resultSet.getString("mobile"));
                    user.setStatus(resultSet.getInt("status"));
                    user.setRoleID(resultSet.getInt("role_id"));
                    user.setNotes(resultSet.getString("notes"));
                    user.setImage(resultSet.getString("image"));
                    user.setOtp(resultSet.getString("otp"));
                    user.setOtp_expiry(resultSet.getTimestamp("otp_expiry"));

                    // Kiểm tra mật khẩu
                    String hashedPassword = resultSet.getString("password");
                    if (BcryptService.checkPassword(password, hashedPassword)) {
                        return user; // Trả về user nếu mật khẩu đúng
                    } else {
                        throw new Exception("Mật khẩu không đúng."); // Mật khẩu sai
                    }
                } else {
                    throw new Exception("Không tìm thấy người dùng."); // Không tìm thấy người dùng
                }
            }
        } catch (SQLException e) {
            throw new Exception("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // Phương thức đăng ký người dùng mới
    public String registerUser(String username, String pass, String email, String name, String phone) {
        String sql = "INSERT INTO project_evaluation_system.user (user_name, password, email, full_name, mobile, role_id, status) VALUES (?, ?, ?, ?, ?, 1, 0)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String hashedPassword = BcryptService.hashPassword(pass);

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, name);
            ps.setString(5, phone);

            ps.executeUpdate();

            // Tạo mã OTP
            String otp = EmailUtils.generateOTP();

            // Lưu mã OTP vào cơ sở dữ liệu
            String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
            Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

            try (PreparedStatement otpPs = conn.prepareStatement(updateOtpSql)) {
                otpPs.setString(1, otp);
                otpPs.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
                otpPs.setString(3, email);
                otpPs.executeUpdate();
            }

            // Gửi email không đồng bộ trong một luồng riêng
            new Thread(() -> {
                try {
                    EmailUtils.sendTextEmail(email, "Your OTP for Verification", "Dear User,\n\nYour OTP is: " + otp + ".\nIt will expire in 5 minutes.\n\nBest regards,\nYour Company Name");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            return email; // Trả về email để sử dụng trong xác thực OTP
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String authenticateEmail(String email) {
        try (Connection conn = getConnection()) {
            // Tạo mã OTP
            String otp = EmailUtils.generateOTP();

            // Lưu mã OTP vào cơ sở dữ liệu trước
            String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
            Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

            try (PreparedStatement otpPs = conn.prepareStatement(updateOtpSql)) {
                otpPs.setString(1, otp);
                otpPs.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
                otpPs.setString(3, email);
                otpPs.executeUpdate(); // Cập nhật mã OTP và thời gian hết hạn
            }

            // Tạo và gửi email với mã OTP trong một luồng riêng
            new Thread(() -> {
                try {
                    EmailUtils.sendTextEmail(
                            email,
                            "Your OTP for Verification",
                            "Dear User,\n\nYour OTP is: " + otp + ".\nIt will expire in 5 minutes.\n\nBest regards,\nYour Company Name"
                    );
                } catch (Exception e) {
                    e.printStackTrace(); // Xử lý lỗi nếu việc gửi email gặp vấn đề
                }
            }).start(); // Khởi động luồng gửi email

            return email; // Trả về email để sử dụng trong xác thực OTP

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi xảy ra
        }
    }

    public String authenticateEmailToUpdate(String sendedEmail, String currenEmail) {
        try (Connection conn = getConnection()) {
            // Tạo mã OTP
            String otp = EmailUtils.generateOTP();

            // Lưu mã OTP vào cơ sở dữ liệu trước
            String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
            Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

            try (PreparedStatement otpPs = conn.prepareStatement(updateOtpSql)) {
                otpPs.setString(1, otp);
                otpPs.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
                otpPs.setString(3, currenEmail);
                otpPs.executeUpdate();
            }

            // Tạo một luồng riêng để gửi email với nội dung OTP
            new Thread(() -> {
                try {
                    EmailUtils.sendTextEmail(
                            sendedEmail,
                            "Your OTP for Verification",
                            "Dear User,\n\nYour OTP is: " + otp + ".\nIt will expire in 5 minutes.\n\nBest regards,\nYour Company Name"
                    );
                } catch (Exception e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu việc gửi email gặp lỗi
                }
            }).start(); // Bắt đầu chạy luồng để gửi email

            return sendedEmail; // Trả về email để sử dụng trong xác thực OTP
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi xảy ra
        }
    }

    // Các phương thức khác giữ nguyên nhưng cập nhật kiểm tra mật khẩu cũ và thay đổi mật khẩu bằng Bcrypt
    public boolean checkOldPassword(String username, String oldPassword) {
        String sql = "SELECT password FROM project_evaluation_system.user WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BcryptService.checkPassword(oldPassword, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkOldPasswordByEmail(String email, String oldPassword) {
        String sql = "SELECT password FROM project_evaluation_system.user WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BcryptService.checkPassword(oldPassword, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changePassword(String email, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword));
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword));
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Kiểm tra số lượng bản ghi trả về
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy email
    }

    public boolean sendOTP(String email) {
        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không
        if (!checkEmailExists(email)) {
            return false; // Nếu không tồn tại, trả về false
        }

        // Tạo mã OTP mới
        String otp = String.format("%06d", new Random().nextInt(999999));
        Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // Thời gian hết hạn 5 phút

        // Cập nhật mã OTP và thời gian hết hạn vào cơ sở dữ liệu
        String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(updateOtpSql)) {
            ps.setString(1, otp);
            ps.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
            ps.setString(3, email);
            ps.executeUpdate(); // Thực hiện cập nhật

            // Gửi email chứa mã OTP bất đồng bộ
            CompletableFuture.runAsync(() -> {
                try {
                    EmailUtils.sendTextEmail(
                            email,
                            "Your OTP for Verification",
                            "Dear User,\n\nYour OTP is: " + otp + ".\nIt will expire in 5 minutes.\n\nBest regards,\nYour Company Name"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    // Bạn có thể thêm logic để ghi log hoặc xử lý lỗi tại đây.
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }

        return true; // Trả về true nếu cập nhật thành công và việc gửi email đã được xử lý bất đồng bộ
    }

    // Method to verify OTP
    public boolean verifyOTP(String email, String submittedOtp) {
        String sql = "SELECT otp, otp_expiry FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedOtp = rs.getString("otp");
                    java.sql.Timestamp otpExpiry = rs.getTimestamp("otp_expiry");
                    Date currentTime = new Date(System.currentTimeMillis());

                    return storedOtp.equals(submittedOtp) && currentTime.before(otpExpiry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByMobile(String phone) throws Exception {
        String sql = "SELECT * FROM project_evaluation_system.user WHERE mobile = ?";
        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, phone);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("user_name")); // Sử dụng đúng tên trường
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name")); // Sử dụng đúng tên trường
                    user.setMobile(rs.getString("mobile")); // Sử dụng đúng tên trường
                    user.setRoleID(rs.getInt("role_id")); // Sử dụng đúng tên trường
                    user.setStatus(rs.getInt("status")); // Sử dụng đúng tên trường
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Lỗi khi lấy thông tin người dùng theo số điện thoại: " + ex.getMessage(), ex);
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE user_name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Kiểm tra số lượng bản ghi trả về
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy username
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        String sql = "SELECT otp, otp_expiry FROM project_evaluation_system.user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String otp = rs.getString("otp");
                java.sql.Timestamp otpExpiry = rs.getTimestamp("otp_expiry");
                java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());

                // In ra để kiểm tra giá trị
                System.out.println("OTP từ DB: " + otp);
                System.out.println("OTP nhập vào: " + enteredOtp);
                System.out.println("Thời gian hết hạn OTP: " + otpExpiry);
                System.out.println("Thời gian hiện tại: " + currentTime);

                // Kiểm tra null trước khi so sánh
                if (otp != null && otpExpiry != null) {
                    // Kiểm tra nếu OTP khớp và chưa hết hạn
                    if (otp.trim().equals(enteredOtp.trim()) && otpExpiry.after(currentTime)) {
                        return true; // OTP hợp lệ
                    } else {
                        System.out.println("OTP không hợp lệ hoặc đã hết hạn");
                    }
                } else {
                    System.out.println("OTP hoặc thời gian hết hạn bị null");
                }
            } else {
                System.out.println("Không tìm thấy người dùng với email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // OTP không hợp lệ
    }

    public void activateUser(String email) {
        String sql = "UPDATE project_evaluation_system.user SET status = 1 WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate(); // Kích hoạt tài khoản người dùng
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExist(String username, String email) {
        String sql = "SELECT COUNT(*) FROM project_evaluation_system.user WHERE user_name = ? OR email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu tồn tại tài khoản
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM project_evaluation_system.user WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Giả sử bạn có hàm map dữ liệu từ ResultSet sang đối tượng User
                    user = mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE project_evaluation_system.user SET password = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BcryptService.hashPassword(newPassword)); // Mã hóa mật khẩu mới
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate(); // Số hàng được cập nhật
            return rowsUpdated > 0; // Trả về true nếu có hàng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

// Hàm này có thể giúp bạn map từ ResultSet sang User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUserName(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setMobile(rs.getString("mobile"));
        user.setRoleID(rs.getInt("role_id"));
        user.setStatus(rs.getInt("status"));
        user.setOtp(rs.getString("otp"));
        user.setOtp_expiry(rs.getTimestamp("otp_expiry"));
        return user;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO project_evaluation_system.user (user_name, password, email, full_name, mobile, role_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String hashedPassword = BcryptService.hashPassword(user.getPassword()); // Hash mật khẩu

            // Đặt các tham số cho PreparedStatement
            ps.setString(1, user.getUserName());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getMobile());
            ps.setInt(6, user.getRoleID()); // Role ID
            ps.setInt(7, user.getStatus()); // Status

            ps.executeUpdate(); // Thực thi câu truy vấn
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getEmailByUserID(int userID) {
        String query = "SELECT email FROM project_evaluation_system.user\n"
                + "where id = ?;";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserNameByUserID(int userID) {
        String query = "SELECT user_name FROM project_evaluation_system.user\n"
                + "where id = ?;";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateUserProfile(String userName, String fullName, String email, String mobile, String image, String notes, int userID) {
        String query = "UPDATE `project_evaluation_system`.`user` "
                + "SET `user_name` = ? , "
                + "`full_name` = ?, "
                + "`email` = ?, "
                + "`mobile` = ?, "
                + "`notes` = ?,"
                + " `image` = ? "
                + "WHERE (`id` = ?);";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            // Cập nhật thông tin người dùng
            ps.setString(1, userName);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, mobile);
            ps.setString(5, notes);
            ps.setString(6, image);
            ps.setInt(7, userID);

            ps.executeUpdate(); // Thực hiện cập nhật thông tin

            // Tạo mã OTP
            String otp = EmailUtils.generateOTP();
            Date otpExpiry = new Date(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 phút

            // Lưu mã OTP vào cơ sở dữ liệu
            String updateOtpSql = "UPDATE project_evaluation_system.user SET otp = ?, otp_expiry = ? WHERE email = ?";
            try (PreparedStatement otpPs = conn.prepareStatement(updateOtpSql)) {
                otpPs.setString(1, otp);
                otpPs.setTimestamp(2, new java.sql.Timestamp(otpExpiry.getTime()));
                otpPs.setString(3, email);
                otpPs.executeUpdate(); // Lưu OTP và thời gian hết hạn vào cơ sở dữ liệu
            }

            // Gửi email OTP trong một luồng riêng
            new Thread(() -> {
                try {
                    EmailUtils.sendTextEmail(
                            email,
                            "Your OTP Code",
                            "Dear User,\n\nYour OTP is: " + otp + ".\nIt will expire in 5 minutes.\n\nBest regards,\nYour Company Name"
                    );
                } catch (Exception e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu việc gửi email gặp lỗi
                }
            }).start(); // Khởi động luồng gửi email

            return email; // Trả về email để sử dụng trong xác thực OTP

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    public int updateUserProfileInUpdate(String userName, String fullName, String mobile, String email, String image, String notes, int userID) {
        int rowsUpdated = 0;
        String query = "UPDATE `project_evaluation_system`.`user` "
                + "SET `user_name` = ? , "
                + "`full_name` = ?, "
                + "`email` = ?, "
                + "`mobile` = ?, "
                + "`notes` = ?,"
                + " `image` = ? "
                + "WHERE (`id` = ?);";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userName);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, mobile);
            ps.setString(5, notes);
            ps.setString(6, image);
            ps.setInt(7, userID);

            rowsUpdated = ps.executeUpdate(); // Số hàng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated;
    }

    public User getUserByID(int id) {
        String query = "SELECT * FROM project_evaluation_system.user\n"
                + "where id = ? ";
        User user = null;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("user_name"));
                user.setEmail(resultSet.getString("email"));
                user.setFullName(resultSet.getString("full_name"));
                user.setMobile(resultSet.getString("mobile"));
                user.setStatus(resultSet.getInt("status"));
                user.setRoleID(resultSet.getInt("role_id"));
                user.setNotes(resultSet.getString("notes"));
                user.setImage(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getUserImage(int id) {
        String query = "SELECT image FROM project_evaluation_system.user\n"
                + "where id = ? ";
        String image = null;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                image = resultSet.getString("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    //User List
    public List<User> getAllUser(int offset, int pageSize, String sortBy, String sortOrder) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT u.id, u.full_name, u.email, u.mobile, u.status, u.image, s.setting_name AS role_name "
                    + "FROM user u "
                    + "JOIN setting s ON u.role_id = s.id "
                    + "WHERE s.type = 'role' AND s.setting_name != 'Admin'"
                    + "Order by " + sortBy + " " + sortOrder + " "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);

            // Thiết lập giá trị cho LIMIT và OFFSET
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_Name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setRoleName(rs.getString("role_name"));
                user.setStatus(rs.getInt("status"));
                user.setImage(rs.getString("image"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    //
    public User getUserById(int id) {
        User user = null;
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "SELECT u.id, u.user_name, u.full_name, u.email, u.mobile, u.notes, u.status, s.setting_name AS role_name "
                    + "FROM user u "
                    + "JOIN setting s ON u.role_id = s.id "
                    + "WHERE u.id = ? AND s.type = 'role'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setNotes(rs.getString("notes"));
                user.setStatus(rs.getInt("status"));
                user.setRoleName(rs.getString("role_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // search user full name
    public List<User> searchUserByName(String name, int offset, int pageSize) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT u.id, u.user_name, u.full_name, u.email, u.mobile, u.notes, u.status, s.setting_name AS role_name "
                    + "FROM user u "
                    + "JOIN setting s ON u.role_id = s.id "
                    + "WHERE u.full_name LIKE ? AND s.type = 'role' "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Sử dụng ký tự đại diện '%' cho việc tìm kiếm tên cài đặt
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setNotes(rs.getString("notes"));
                user.setStatus(rs.getInt("status"));
                user.setRoleName(rs.getString("role_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<User> getUsersByStatus(int status, int offset, int pageSize) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT u.id, u.user_name, u.full_name, u.email, u.mobile, u.notes, u.status, s.setting_name AS role_name "
                    + "FROM user u "
                    + "JOIN setting s ON u.role_id = s.id "
                    + "WHERE u.status = ? AND s.type = 'role' "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, status);  // Đặt giá trị status
            ps.setInt(2, pageSize); // Đặt kích thước trang
            ps.setInt(3, offset);   // Đặt giá trị offset

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setNotes(rs.getString("notes"));
                user.setStatus(rs.getInt("status"));
                user.setRoleName(rs.getString("role_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllRoles() {
        List<User> roles = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT id, setting_name as role_name FROM setting WHERE type = 'role' AND setting_name != 'Admin' ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User role = new User();
                role.setRoleID(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public List<User> getUsersByRole(String role, int offset, int pageSize) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT u.id, u.user_name, u.full_name, u.email, u.mobile, u.notes, u.status, s.setting_name AS role_name "
                    + "FROM user u "
                    + "JOIN setting s ON u.role_id = s.id "
                    + "WHERE u.role_id = ? AND s.type = 'role' "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, role);
            preparedStatement.setInt(2, pageSize);
            preparedStatement.setInt(3, offset);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setNotes(rs.getString("notes"));
                user.setStatus(rs.getInt("status"));
                user.setRoleName(rs.getString("role_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

// count all user
    public int countAllUser() {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM user";
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

// count user name
    public int countUserByName(String name) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM user WHERE full_name LIKE ?";
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
    public int countUsersByStatus(int status) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM user WHERE status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
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

    //count role
    public int countUsersByRole(String role) {
        int totalRecords = 0;
        try {
            Connection con = getConnection();
            String sql = "SELECT COUNT(*) AS total FROM user WHERE role_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, role);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

////Add user
//    public void addUser(User user) {
//        try {
//            Connection con = getConnection();
//            String sql = "INSERT INTO user(full_name, email, mobile, notes, status, role_id) VALUES(?, ?, ?, ?, ?, ?)";
//            PreparedStatement ps = con.prepareStatement(sql);
//
//            ps.setString(1, user.getFullName());
//            ps.setString(2, user.getEmail());
//            ps.setString(3, user.getMobile());
////            ps.setString(4, user.getPassword());
//            ps.setString(4, user.getNotes());
//            ps.setInt(5, user.getStatus());
//            ps.setInt(6, user.getRoleID());
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
    public void addUser(User user) {
        try {
            Connection con = getConnection();
            String sql = "INSERT INTO user(full_name, email, mobile, user_name, password, notes, status, role_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            String username = EmailUtils.generateUsername(user.getEmail());
            String password = EmailUtils.generatePassword();
            String hashedPassword = BcryptService.hashPassword(password);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getMobile());
            ps.setString(4, username);
            ps.setString(5, hashedPassword);
            ps.setString(6, user.getNotes());
            ps.setInt(7, user.getStatus());
            ps.setInt(8, user.getRoleID());

            ps.executeUpdate();

            String subject = "Your Account Details";
            String body = "Dear " + user.getFullName() + ",\n\n"
                    + "Your account has been created successfully. Here are your details:\n"
                    + "Username: " + username + "\n"
                    + "Password: " + password + "\n\n"
                    + "Please keep your password secure.\n\n"
                    + "Best regards,\nYour Company Name";

            EmailUtils.sendTextEmail(user.getEmail(), subject, body);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//Update user
    public void updateUser(User user) {
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "UPDATE user SET full_name = ?, email = ?, mobile = ?, notes = ?, status = ?, role_id = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getMobile());
            ps.setString(4, user.getNotes());
            ps.setInt(5, user.getStatus());
            ps.setInt(6, user.getRoleID());
            ps.setInt(7, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Activate/Deactivate a user
    public void updateUserStatus(int id, int status) {
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "UPDATE user SET status = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsersByPage(int page, int pageSize) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = getConnection();
            String sql = "SELECT * FROM user LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (page - 1) * pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setNotes(rs.getString("notes"));
                user.setStatus(rs.getInt("status"));
                user.setRoleName(rs.getString("role_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int getUserRole(int userId) throws SQLException {
        String sql = "SELECT * FROM project_evaluation_system.user WHERE id = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);  // Set the user ID parameter

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");  // Return the user's role
                } else {
                    throw new SQLException("User not found with ID: " + userId);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching user role: " + ex.getMessage(), ex);
        }
    }

    public List<User> getMembersByClassId(int classId) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.id, u.user_name, u.full_name, u.email, u.role_id "
                + "FROM project_evaluation_system.user u "
                + "JOIN project_evaluation_system.class_member cm ON u.id = cm.user_id "
                + "LEFT JOIN project_evaluation_system.team_member tm ON u.id = tm.user_id "
                + "WHERE cm.class_id = ? AND tm.team_id IS NULL"; // Exclude users with a team

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRoleID(rs.getInt("role_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving members by class ID: " + e.getMessage(), e);
        }

        return users;
    }
}
