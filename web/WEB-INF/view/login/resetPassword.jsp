<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding: 50px;
        }
        .form-container {
            max-width: 400px;
            margin: auto;
            padding: 20px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2 class="text-center">Khôi phục mật khẩu</h2>

        <c:if test="${empty sessionScope.isOtpValid}">
            <form action="${pageContext.request.contextPath}/sendOtp" method="post">
                <div class="form-group">
                    <label for="email">Nhập email của bạn:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Gửi mã OTP</button>
            </form>

            <c:if test="${not empty sessionScope.otpMessage}">
                <p class="text-success">${sessionScope.otpMessage}</p>
            </c:if>
            <c:if test="${not empty sessionScope.otpError}">
                <p class="text-danger">${sessionScope.otpError}</p>
            </c:if>
        </c:if>

        <c:if test="${not empty sessionScope.isOtpValid}">
            <form action="${pageContext.request.contextPath}/otp/reset/submit" method="post">
                <div class="form-group">
                    <label for="otp">Nhập mã OTP:</label>
                    <input type="text" class="form-control" id="otp" name="otp" required>
                </div>
                <div class="form-group">
                    <label for="password">Mật khẩu mới:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu:</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
                <button type="submit" class="btn btn-success btn-block">Đặt lại mật khẩu</button>
            </form>
            <c:if test="${not empty sessionScope.resetMessage}">
                <p class="text-success">${sessionScope.resetMessage}</p>
            </c:if>
            <c:if test="${not empty sessionScope.resetError}">
                <p class="text-danger">${sessionScope.resetError}</p>
            </c:if>
            <c:if test="${not empty sessionScope.passwordError}">
                <p class="text-danger">${sessionScope.passwordError}</p>
            </c:if>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
