package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import model.User;
import service.UserService;

@WebServlet({"/register", "/register/submit", "/login", "/login/submit", "/logout", "/otp", "/otp/submit", "/otp/reset", "/otp/reset/submit", "/sendOtp", "/resetPassword",})
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService; // Khởi tạo UserService

    public void init() {
        userService = new UserService(); // Khởi tạo UserService khi servlet khởi động
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        switch (action) {
            case "/register":
                showRegisterForm(req, res);
                break;
            case "/login":
                showLoginForm(req, res);
                break;
            case "/logout":
                handleLogout(req, res);
                break;
            case "/otp":
                showOtpForm(req, res);
                break;
            case "/resetPassword":
                showResetPasswordForm(req, res);
                break;

        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();
        switch (action) {
            case "/register/submit":
                registerUser(req, res);
                break;
            case "/login/submit":
                authenticate(req, res);
                break;
            case "/otp/submit":
                handleOtpSubmission(req, res);
                break;
            case "/otp/reset/submit":
                handleOtpForPasswordReset(req, res);
                break;
            case "/sendOtp":
                sendOtp(req, res);
                break;

        }
    }

    private void showRegisterForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        req.setAttribute("email", email);
        forwardToPage(req, res, "WEB-INF/view/login/register.jsp");
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username").trim();
        String pass = req.getParameter("password").trim();
        String confirmPass = req.getParameter("confirmPass").trim();
        String email = req.getParameter("email").trim();
        String name = req.getParameter("name").trim();
        String phone = req.getParameter("mobile").trim();

        try {
            String errorMessage = userService.validateRegisterInfo(username, pass, confirmPass, email, name, phone, pass);
            if (!errorMessage.isEmpty()) {
                saveRegistrationInfoToSession(req, username, email, name, phone, errorMessage);
                res.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            if (userService.isUserExist(username, email)) {
                saveRegistrationInfoToSession(req, username, email, name, phone, "Tên người dùng hoặc email đã tồn tại!");
                res.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            String registeredEmail = userService.register(username, pass, email, name, phone);
            req.getSession().setAttribute("email", registeredEmail);
            res.sendRedirect(req.getContextPath() + "/otp");

        } catch (Exception e) {
            saveRegistrationInfoToSession(req, username, email, name, phone, "Có lỗi xảy ra: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/register");
        }
    }

    private void saveRegistrationInfoToSession(HttpServletRequest req, String username, String email, String name, String phone, String errorMessage) {
        req.getSession().setAttribute("username", username);
        req.getSession().setAttribute("email", email);
        req.getSession().setAttribute("name", name);
        req.getSession().setAttribute("phone", phone);
        req.getSession().setAttribute("errorMessage", errorMessage);
    }

    private void forwardToPage(HttpServletRequest req, HttpServletResponse res, String page) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        dispatcher.forward(req, res);
    }

    private void showLoginForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "WEB-INF/view/login/loginpage.jsp");
    }

    private void authenticate(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String enteredUsername = req.getParameter("username");
        String enteredPassword = req.getParameter("password");
        User user;

        try {
            user = userService.login(enteredUsername, enteredPassword);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi đăng nhập: " + e.getMessage());
            session.setAttribute("enteredUsername", enteredUsername);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (user != null) {
            if (user.getStatus() == 0) {
                session.setAttribute("errorMessage", "Truy cập bị từ chối!");
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                session.setAttribute("currentUser", user);
                session.setAttribute("userEmail", user.getEmail());
                res.sendRedirect(req.getContextPath() + "/dashboard");
            }
        } else {
            session.setAttribute("errorMessage", "Đăng nhập thất bại. Vui lòng thử lại.");
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        res.sendRedirect(req.getContextPath() + "/login");
    }

    private void showOtpForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "/WEB-INF/view/login/enterotp.jsp");
    }

    private void handleOtpSubmission(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("errorMessage");
        String email = (String) session.getAttribute("email");
        String enteredOtp = req.getParameter("otp");

        try {
            boolean isValidOtp = userService.verifyOTP(email, enteredOtp);
            if (isValidOtp) {
                userService.activateUser(email);
                User user = userService.getUserByEmail(email);
                session.setAttribute("currentUser", user);
                res.sendRedirect(req.getContextPath() + "/login");
            } else {
                session.setAttribute("errorMessage", "OTP không hợp lệ. Vui lòng thử lại.");
                res.sendRedirect(req.getContextPath() + "/otp"); // Chuyển hướng về trang nhập OTP
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/otp"); // Chuyển hướng về trang nhập OTP
        }

    }

    private void showResetPasswordForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        forwardToPage(req, res, "/WEB-INF/view/login/resetPassword.jsp");
    }

    private void handleOtpForPasswordReset(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String enteredOtp = req.getParameter("otp");
        String newPassword = req.getParameter("password");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("email");

        try {
            boolean isValidOtp = userService.verifyOTP(email, enteredOtp);
            if (isValidOtp) {
                // Validate mật khẩu mới trước khi cập nhật
                String passwordError = userService.validateNewPassword(newPassword);
                if (!passwordError.isEmpty()) {
                    session.setAttribute("resetError", passwordError);
                    forwardToResetPasswordPage(req, res);
                    return;
                }

                boolean isPasswordUpdated = userService.updatePassword(email, newPassword);
                if (isPasswordUpdated) {
                    session.setAttribute("resetMessage", "Mật khẩu đã được cập nhật thành công.");
                    session.removeAttribute("email");
                    res.sendRedirect(req.getContextPath() + "/resetPassword");
                } else {
                    session.setAttribute("resetError", "Có lỗi xảy ra khi cập nhật mật khẩu.");
                    forwardToResetPasswordPage(req, res);
                }
            } else {
                session.setAttribute("resetError", "OTP không hợp lệ. Vui lòng thử lại.");
                forwardToResetPasswordPage(req, res);
            }
        } catch (Exception e) {
            session.setAttribute("resetError", "Có lỗi xảy ra: " + e.getMessage());
            forwardToResetPasswordPage(req, res);
        }
    }

    private void forwardToResetPasswordPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/login/resetPassword.jsp");
        dispatcher.forward(req, res);
    }

    private void sendOtp(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email").trim();
        HttpSession session = req.getSession();

        boolean isSent = userService.sendOTP(email);
        if (isSent) {
            session.setAttribute("isOtpValid", false);
            session.setAttribute("otpMessage", "Mã OTP đã được gửi đến email của bạn.");
            session.setAttribute("email", email);
        } else {
            session.setAttribute("otpError", "Không thể gửi mã OTP. Vui lòng kiểm tra lại email.");
        }

        res.sendRedirect(req.getContextPath() + "/resetPassword");
    }

}
