/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.List;
import java.util.Map;
import model.User;
import service.CommonService;
import service.UserService;

/**
 *
 * @author Do Duan
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB

@WebServlet({"/changepassword", "/user", "/user/add", "/user/edit", "/user/save", "/user/status", "/profile", "/profile/submit", "/profile/update", "/user/otp", "/user/otp/submit", "/user/submit"})
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String jsplist = "/WEB-INF/view/user/list.jsp";
    private final String jspdetails = "/WEB-INF/view/user/details.jsp";

    private UserService userService;

    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if ((action.equals("/user/add") || action.equals("/user/edit") || action.equals("/user/status")) && !isAdmin(currentUser)) {
            forwardToErrorPage(request, response, "Bạn không có quyền truy cập vào chức năng này.");
            return;
        }
        if (currentUser == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/common/accessDenied.jsp");
            dispatcher.forward(request, response);
//            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        switch (action) {
            case "/user":
                showUsersList(request, response);
                break;
            case "/user/add":
                showAddUsers(request, response);
                break;
            case "/user/edit":
                showEditUser(request, response);
                break;
            case "/user/save":
                save(request, response);
                break;
            case "/user/status":
                activateUser(request, response);
                break;
            case "/profile":
                showUserProfile(request, response);
                break;
            case "/profile/submit":
                submitUserProfile(request, response);
                break;
            case "/user/submit":
                submitUserChangePass(request, response);
                break;
            case "/changepassword":
                showUserChangePass(request, response);
                break;
        }
    }

    private boolean isAdmin(User user) {
        return user != null && "Admin".equals(user.getRole());
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/team/ErrorPage.jsp");
        dispatcher.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

        String action = request.getServletPath();
        switch (action) {
            case "/profile/submit":
                submitUserProfile(request, response);
                break;
            case "/profile/update":
                updateProfile(request, response);
                break;
            case "/user/otp":
                showOtpForm(request, response);
                break;
            case "/user/otp/submit":
                handleOtpSubmission(request, response);
                break;
        }

        doGet(request, response);

    }

    private void showUsersList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 8;

        // Lấy tham số trang nếu có
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        String roleFilter = request.getParameter("roleFilter");
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("statusFilter");

        String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "id";
        String sortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "ASC";

        List<User> userList;
        int totalRecords;

        if (statusFilter != null && !statusFilter.isEmpty()) {
            int status = Integer.parseInt(statusFilter);
            userList = userService.getUsersByStatus(status, offset, pageSize);
            totalRecords = userService.countUsersByStatus(status);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            userList = userService.searchUserByName(searchQuery, offset, pageSize);
            totalRecords = userService.countUserByName(searchQuery);
        } else if (roleFilter != null && !roleFilter.isEmpty()) {
            userList = userService.getUsersByRole(roleFilter, offset, pageSize);
            totalRecords = userService.countUsersByRole(roleFilter);
        } else {
            userList = userService.getAllUser(offset, pageSize, sortBy, sortOrder);
            totalRecords = userService.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("userList", userList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("userList", userList);

        List<User> roles = userService.getAllRole();
        request.setAttribute("roles", roles);

        RequestDispatcher dispatcher = request.getRequestDispatcher(jsplist);
        dispatcher.forward(request, response);
    }

    private void showAddUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> roles = userService.getAllRole();
        request.setAttribute("roles", roles);
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
        dispatcher.forward(request, response);
    }

    private void showEditUser(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User userByID = userService.getUserById(id);
        req.setAttribute("user", userByID);

        List<User> roles = userService.getAllRole();
        req.setAttribute("roles", roles);

        RequestDispatcher dispatcher = req.getRequestDispatcher(jspdetails);
        dispatcher.forward(req, res);
    }

    private User getInputForm(HttpServletRequest request) {
        User inputUser = new User();
        inputUser.setFullName(request.getParameter("fullName"));
        inputUser.setEmail(request.getParameter("email"));
        inputUser.setMobile(request.getParameter("mobile"));
        inputUser.setNotes(request.getParameter("notes"));

        // Handle status
        String status = request.getParameter("status");
        inputUser.setStatus("1".equals(status) ? 1 : 0);

        String roleIdParam = request.getParameter("roleID");
        if (roleIdParam != null && !roleIdParam.isEmpty()) {
            inputUser.setRoleID(Integer.parseInt(roleIdParam));
        } else {
            inputUser.setRoleID(0);
        }

        return inputUser;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        User inputUser = getInputForm(request);

        Map<String, String> validationErrors = userService.validateUserInfo(
                inputUser.getFullName(),
                inputUser.getEmail(),
                inputUser.getMobile(),
                inputUser.getRoleID(),
                inputUser.getNotes(),
                inputUser.getStatus()
        );

        if (!validationErrors.isEmpty()) {
            request.setAttribute("validationErrors", validationErrors);
            request.setAttribute("userID", inputUser);
            List<User> roles = userService.getAllRole();
            request.setAttribute("roles", roles);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
            return;
        }

        try {
            if (id == null || id.isEmpty()) {
                userService.addUser(inputUser);
                request.getSession().setAttribute("Message", "User added successfully!");
            } else {
                inputUser.setId(Integer.parseInt(id));
                userService.updateUser(inputUser);
                request.getSession().setAttribute("Message", "User updated successfully!");
            }
            response.sendRedirect(request.getContextPath() + "/user");
        } catch (Exception e) {
            request.setAttribute("ErrorMessage", "Error occurred: " + e.getMessage());
            request.setAttribute("userID", inputUser);
            List<User> roles = userService.getAllRole();
            request.setAttribute("roles", roles);
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspdetails);
            dispatcher.forward(request, response);
        }

    }

    private void activateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        userService.updateUserStatus(userId, status);

        request.getSession().setAttribute("Message", "User has been " + (status == 1 ? "activated" : "deactivated") + ".");
        response.sendRedirect(request.getContextPath() + "/user");
    }

    // profile
    private void showUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy current user từ session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String message = (String) session.getAttribute("message");
        request.setAttribute("message", message);

        if (message != null) {
            session.removeAttribute("message"); // Xóa message khỏi session sau khi sử dụng
            // Hiển thị thông báo lỗi
        }

        // Gọi service để lấy dữ liệu và đẩy vào request
        request.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(request, response);
    }

    // Đường dẫn thư mục lưu file upload
    private static final String UPLOAD_DIR = "uploads";

    // /profile/submit
    private void submitUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin người dùng hiện tại trong session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        int userID = currentUser.getId();

        // Lấy file từ form
        Part filePart = request.getPart("file");
        String filePath = userService.getUserImage(userID);
        if (filePart != null && filePart.getSize() > 0) {
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

            filePath = new CommonService().uploadFile(filePart, uploadPath);
        }

        // Lấy thông tin được gửi từ profile.jsp
        String userName = request.getParameter("userName");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String notes = request.getParameter("notes");

        try {
            // Validate thông tin người dùng 
            userService.checkUserName(userName);
            userService.checkFullName(fullName);
            userService.checkEmailFormat(email);
            userService.checkMobilePhone(mobile);

            // Kiểm tra lỗi (ví dụ đơn giản)
            String errMessage = null;
            if (userName == null || userName.trim().isEmpty()) {
                errMessage = "User Name is required.";
            } else if (!email.contains("@")) {
                errMessage = "Invalid email format.";
            }

            if (errMessage != null ) {
                // Nếu có lỗi, giữ lại dữ liệu đã nhập và chuyển hướng về trang JSP
                request.setAttribute("userName", userName);
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("mobile", mobile);
                request.setAttribute("notes", notes);
                request.setAttribute("errMessage", errMessage);
                request.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(request, response);
            } // Check email của người dùng hiện tại với email đc gửi về
            else if(!userService.isChangedName(userID, userName) && userService.checkEmailToUpdate(userID, email)){
                request.setAttribute("filePath", filePath);
                request.setAttribute("userName", userName);
                request.setAttribute("fullName", fullName);
                request.setAttribute("mobile", mobile);
                request.setAttribute("notes", notes);
                request.setAttribute("email", email);

                request.getRequestDispatcher("/profile/update").forward(request, response);
            }
            
            else if ((userService.checkEmailExists(email) && userService.checkEmailToUpdate(userID, email))
                    || userService.checkUserNameToUpdate(userID, userName)) { // Nếu email được gửi về trùng với email hiện tại thì cập nhật luôn
                request.setAttribute("filePath", filePath);
                request.setAttribute("userName", userName);
                request.setAttribute("fullName", fullName);
                request.setAttribute("mobile", mobile);
                request.setAttribute("notes", notes);
                request.setAttribute("email", email);

                request.getRequestDispatcher("/profile/update").forward(request, response);

                ////////
            } else if (!userService.checkEmailExists(email)) {  // Nếu email được gửi về ko trùng với email hiện tại thì xác thực email mới
                String registeredEmail = userService.authenticateEmailToUpdate(email, currentUser.getEmail());
                // Đẩy thông tin cập nhật vào session
                session.setAttribute("email", registeredEmail);
                session.setAttribute("userName", userName);
                session.setAttribute("fullName", fullName);
                session.setAttribute("mobile", mobile);
                session.setAttribute("notes", notes);
                session.setAttribute("filePath", filePath);

                request.getRequestDispatcher("/user/otp").forward(request, response);

//                response.sendRedirect(request.getContextPath() + "/user/otp"); // Chuyển đến trang nhập OTP
            } else {
                request.setAttribute("errMessage", "Email already exist");
                request.getRequestDispatcher("/profile").forward(request, response);

            }
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            request.setAttribute("errMessage", message); // Lưu message vào session
            // Lấy đường dẫn gốc của ứng dụng,sau đó thêm /profile vào URL 
            request.getRequestDispatcher("/profile").forward(request, response);
//            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }

    // /profile/update
    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy userID của người dùng hiện tại
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        int userID = currentUser.getId();

        // Lấy thông tin được cập nhật
        String filePath = (String) request.getAttribute("filePath");
        String userName = (String) request.getAttribute("userName");
        String fullName = (String) request.getAttribute("fullName");
        String email = (String) request.getAttribute("email");
        String mobile = (String) request.getAttribute("mobile");
        String notes = (String) request.getAttribute("notes");

        if (userService.updateUserProfileInUpdate(userName, fullName, email, mobile, filePath, notes, userID) != 0) {
            User updatedUser = userService.getUserByID(userID);
            session.setAttribute("currentUser", updatedUser);
            request.setAttribute("messSucces", "Update profile successfully");
        } else {
            request.setAttribute("messFail", "Update profile un-successfully");
        }
        request.getRequestDispatcher("/profile").forward(request, response);
    }

    // user/otp
    private void showOtpForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Chuyển đến trang nhập OTP
        String errMess = req.getParameter("errorMessage");
        req.setAttribute("errorMessage", errMess);
        req.getRequestDispatcher("/WEB-INF/view/user/enterotp.jsp").forward(req, res);
    }

    // user/otp/submit
    private void handleOtpSubmission(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        session.removeAttribute("errorMessage");

        String filePath = (String) session.getAttribute("file");
        String userName = (String) session.getAttribute("userName");
        String fullName = (String) session.getAttribute("fullName");
        String mobile = (String) session.getAttribute("mobile");
        String notes = (String) session.getAttribute("notes");
        String email = (String) session.getAttribute("email");
        String enteredOtp = req.getParameter("otp");

        try {
            boolean isValidOtp = userService.verifyEnteredOTP(currentUser.getEmail(), enteredOtp); // Kiểm tra OTP từ cơ sở dữ liệu
            if (isValidOtp) { // OTP hợp lệ
                req.setAttribute("email", email);
                req.setAttribute("filePath", filePath);
                req.setAttribute("userName", userName);
                req.setAttribute("fullName", fullName);
                req.setAttribute("mobile", mobile);
                req.setAttribute("notes", notes);

                // Chuyển dữ liệu về trang /profile/update
                req.getRequestDispatcher("/profile/update").forward(req, res);

//                res.sendRedirect(req.getContextPath() + "/profile/update"); // Chuyển đến trang thành công
            } else {
                req.setAttribute("errorMessage", "OTP không hợp lệ. Vui lòng thử lại.");
                showOtpForm(req, res); // Hiển thị lại form nhập OTP mà không kích hoạt tài khoản
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            showOtpForm(req, res); // Hiển thị lại form với thông báo lỗi
        }
    }

    private void submitUserChangePass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy thông tin mật khẩu từ form
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Lấy email từ session (đã được lưu khi người dùng đăng nhập)
        String email = (String) session.getAttribute("userEmail");

        // In ra console để kiểm tra
        System.out.println("Email: " + email);
        System.out.println("Old Password: " + oldPassword);
        System.out.println("New Password: " + newPassword);
        System.out.println("Confirm Password: " + confirmPassword);

        if (email != null) {
            if (newPassword.equals(confirmPassword)) {
                // Gọi service để thực hiện việc đổi mật khẩu thông qua email, mật khẩu cũ và mật khẩu mới
                boolean success = userService.changePassword(email, oldPassword, newPassword);

                if (success) {
                    request.setAttribute("message", "Đổi mật khẩu thành công!");
                } else {
                    request.setAttribute("error", "Mật khẩu cũ không đúng!");
                }
            } else {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            }
        } else {
            // Nếu không có session hoặc email, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Chuyển tiếp về trang profile
        request.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(request, response);
    }

    private void showUserChangePass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            // Nếu đã đăng nhập, chuyển hướng đến trang đổi mật khẩu
            request.getRequestDispatcher("/WEB-INF/view/user/ChangePassword.jsp").forward(request, response);
        } else {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
