package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.UserDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO
    }

    // Lấy danh sách tất cả người dùng
    public ArrayList<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng trong trường hợp có lỗi
        }
    }

    // Đăng nhập người dùng
    public User login(String username, String password) throws Exception {
        return userDAO.getUserLogin(username, password);
    }

    // Đăng ký người dùng mới
    public String register(String username, String pass, String email, String name, String phone) {
        return userDAO.registerUser(username, pass, email, name, phone);
    }

    // Kiểm tra mật khẩu cũ
    public boolean checkOldPassword(String username, String oldPassword) {
        return userDAO.checkOldPassword(username, oldPassword);
    }

    // Thay đổi mật khẩu
    public void changePassword(String email, String newPassword) {
        userDAO.changePassword(email, newPassword);
    }

    public void changePasswordByUsername(String username, String newPassword) {
        userDAO.changePasswordByUsername(username, newPassword);
    }

    // Kiểm tra sự tồn tại của email
    public boolean checkEmailExists(String email) {
        return userDAO.checkEmailExists(email);
    }

    // Gửi OTP
    public boolean sendOTP(String email) {
        return userDAO.sendOTP(email);
    }

    // Xác thực OTP
    public boolean verifyOTP(String email, String submittedOtp) {
        return userDAO.verifyOtp(email, submittedOtp);
    }

    // Kích hoạt người dùng
    public void activateUser(String email) {
        userDAO.activateUser(email);
    }

    // Kiểm tra sự tồn tại của người dùng
    public boolean isUserExist(String username, String email) {
        return userDAO.isUserExist(username, email);
    }

    // Lấy người dùng theo email
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        return userDAO.updatePassword(email, newPassword);
    }

    // Lấy người dùng theo số điện thoại
    public User getUserByMobile(String phone) throws Exception {
        return userDAO.getUserByMobile(phone);
    }

    // Kiểm tra sự tồn tại của tên đăng nhập
    public boolean checkUsernameExists(String username) {
        return userDAO.checkUsernameExists(username);
    }

    public String validateRegisterInfo(String username, String pass, String confirmPass, String email, String name, String phone, String newPassword) {
        StringBuilder errorMessages = new StringBuilder();

        // Kiểm tra các trường có bị bỏ trống hay không
        if (username.isEmpty() || pass.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            errorMessages.append("Tất cả các trường đều bắt buộc! ");
        }

        // Kiểm tra chiều dài tối đa
        if (username.length() > 63 || name.length() > 255 || email.length() > 255 || phone.length() != 10) {
            errorMessages.append("Đầu vào vượt quá chiều dài tối đa! ");
        }

        // Kiểm tra mật khẩu xác nhận có khớp không
        if (!pass.equals(confirmPass)) {
            errorMessages.append("Mật khẩu xác nhận không khớp! ");
        }

        // Kiểm tra mật khẩu có độ dài lớn hơn 6 và chứa chữ hoa, chữ thường, chữ số
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{7,}$";
        if (!pass.matches(passwordRegex)) {
            errorMessages.append("Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và lớn hơn 6 ký tự! ");
        }

        // Kiểm tra định dạng email
        String mailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!email.matches(mailRegex)) {
            errorMessages.append("Định dạng email không hợp lệ! ");
        }

        // Kiểm tra định dạng số điện thoại
        String mobileRegex = "(09|03|07|08|05)+([0-9]{8})";
        if (!phone.matches(mobileRegex)) {
            errorMessages.append("Định dạng số điện thoại không hợp lệ! ");
        }

        return errorMessages.toString();
    }

    public String validateNewPassword(String newPassword) {
        StringBuilder errorMessages = new StringBuilder();
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{7,}$";
        if (!newPassword.matches(passwordRegex)) {
            errorMessages.append("Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và lớn hơn 6 ký tự! ");
        }
        return errorMessages.toString();
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        // Kiểm tra mật khẩu cũ
        try {
            if (userDAO.checkOldPasswordByEmail(email, oldPassword)) {
                // Gọi phương thức updatePassword để cập nhật mật khẩu mới
                return userDAO.updatePassword(email, newPassword);
            } else {
                System.out.println("Mật khẩu cũ không chính xác."); // Thông báo nếu mật khẩu cũ không đúng
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ
        }
        return false; // Trả về false nếu có lỗi hoặc mật khẩu cũ không chính xác
    }

    public int updateUserProfileInUpdate(String userName, String fullName, String email,
            String mobile, String image, String notes, int userID) throws IllegalArgumentException {
        return userDAO.updateUserProfileInUpdate(userName, fullName, mobile, email, image, notes, userID);
    }

    // Update profile without email
    public void updateUserProfile(String userName, String fullName,
            String mobile, String image, String notes, int userID) throws IllegalArgumentException {
        checkUserName(userName);
        checkFullName(fullName);
        checkMobilePhone(mobile);
    }

    public void checkUserProfile(String userName, String fullName, String email,
            String mobile) throws IllegalArgumentException {
        checkUserName(userName);
        checkFullName(fullName);
        checkEmailFormat(email);
        checkMobilePhone(mobile);

    }

    public void checkUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User Name cannot be empty or null");
        }
        if (userName.length() > 45) {
            throw new IllegalArgumentException("User Name must be less than 45 chars");
        }
        
    }
    
    public boolean isChangedName(int id,String inputName){
        User u = userDAO.getUserByID(id);
        if(inputName.trim().equals(inputName.trim())){
            return false;
        }
        return true;
    }

    public void checkFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full Name cannot be empty or null");
        }
        if (fullName.length() > 45) {
            throw new IllegalArgumentException("Full Name must be less than 45 chars");
        }
    }

    public boolean checkEmailFormat(String email) {
        if (email.length() > 50) {
            throw new IllegalArgumentException("Email must be less than 50 chars");
        }
        String mailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!email.matches(mailRegex)) {
            throw new IllegalArgumentException("Email format is invalid! ");
        }
        return true;
    }

    public boolean checkEmailExist(String email) {
        if (userDAO.checkEmailExists(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
        return true;
    }

    public void checkMobilePhone(String mobile) {
        if (mobile.length() > 11 || mobile.length() < 9) {
            throw new IllegalArgumentException("Mobile must be less than 11 and more 9 chars");
        }
        String phoneRegex = "^(03|05|07|08|09)\\d{8}$";
        if (!mobile.matches(phoneRegex)) {
            throw new IllegalArgumentException("Phone number format is invalid! ");
        }
    }

    // If email entered is not different from email in database
    public boolean checkEmailToUpdate(int userID, String email) {
        userDAO = new UserDAO();
        if (email.trim().equalsIgnoreCase(userDAO.getEmailByUserID(userID))) {
            return true; // Same
        }
        return false; // Different
    }

    public boolean checkUserNameToUpdate(int userID, String userName) {
        userDAO = new UserDAO();
        if (userName.trim().equalsIgnoreCase(userDAO.getUserNameByUserID(userID))) {
            return true; // Same
        }
        return false; // Different
    }

    public String authenticateEmail(String email) {
        userDAO = new UserDAO();
        return userDAO.authenticateEmail(email);
    }

    public String authenticateEmailToUpdate(String sendedEmail, String currenEmail) {
        userDAO = new UserDAO();
        return userDAO.authenticateEmailToUpdate(sendedEmail, currenEmail);
    }

    public boolean verifyEnteredOTP(String email, String enteredOtp) {
        userDAO = new UserDAO();
        return userDAO.verifyOtp(email, enteredOtp);
    }

    public User getUserByID(int id) {
        userDAO = new UserDAO();
        return userDAO.getUserByID(id);
    }

    public String getUserImage(int id) {
        userDAO = new UserDAO();
        return userDAO.getUserImage(id);
    }

    public List<User> getAllUser(int offset, int pageSize, String sortBy, String sortOrder) {
        return userDAO.getAllUser(offset, pageSize, sortBy, sortOrder);
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public List<User> searchUserByName(String name, int offset, int pageSize) {
        return userDAO.searchUserByName(name, offset, pageSize);
    }

    public int countUserByName(String name) {
        return userDAO.countUserByName(name);
    }

    public List<User> getUsersByRole(String role, int offset, int pageSize) {
        return userDAO.getUsersByRole(role, offset, pageSize);
    }

    public int countUsersByRole(String role) {
        return userDAO.countUsersByRole(role);
    }

    public List<User> getUsersByStatus(int status, int offset, int pageSize) {
        return userDAO.getUsersByStatus(status, offset, pageSize);
    }

    public int countUsersByStatus(int status) {
        return userDAO.countUsersByStatus(status);
    }

    public void addUser(User user) {
        userDAO.addUser(user);
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public void updateUserStatus(int id, int status) {
        userDAO.updateUserStatus(id, status);
    }

    public List<User> getAllRole() {
        return userDAO.getAllRoles();
    }

    public int countAll() {
        return userDAO.countAllUser();
    }

    public int getUserRole(int userId) throws SQLException {
        return userDAO.getUserRole(userId);
    }

    public Map<String, String> validateUserInfo(String fullName, String email, String mobile, int roleID, String notes, int status) {
        Map<String, String> errorMessages = new HashMap<>();

        // Full name validation
        if (fullName == null || fullName.isEmpty()) {
            errorMessages.put("fullName", "Full name cannot be empty.");
        } else if (fullName.length() > 45) {
            errorMessages.put("fullNameLength", "Full name cannot exceed 45 characters.");
        }

        // Email validation
        if (email == null || email.isEmpty()) {
            errorMessages.put("email", "Email cannot be empty.");
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errorMessages.put("emailRegex", "Invalid email format.");
        }

        // Mobile validation
        if (mobile == null || mobile.isEmpty()) {
            errorMessages.put("mobile", "Mobile cannot be empty.");
        } else if (!mobile.matches("^(09|03|07|08|05)\\d{8}$")) {
            errorMessages.put("mobileRegex", "Invalid mobile number format.");
        }

        // Notes validation
        if (notes == null || notes.isEmpty()) {
            errorMessages.put("notes", "Notes cannot be left blank.");
        } else if (notes.length() > 255) {
            errorMessages.put("notesLength", "Notes cannot exceed 255 characters.");
        }

        // Role ID validation
        if (roleID <= 0) {
            errorMessages.put("roleID", "Role ID is not valid.");
        }

        return errorMessages;
    }
}
//commit sua code
