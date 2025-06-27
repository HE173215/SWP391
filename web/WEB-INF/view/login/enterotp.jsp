<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nhập OTP</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                padding: 50px;
            }
            .container {
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
        <div class="container">
            <h2 class="text-center">Nhập OTP</h2>
            <form action="${pageContext.request.contextPath}/otp/submit" method="post">
                <div class="form-group">
                    <input type="text" class="form-control" name="otp" required placeholder="Nhập OTP">
                </div>
                <button type="submit" class="btn btn-primary btn-block">Xác nhận</button>
            </form>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger mt-3">${sessionScope.errorMessage}</div>
                <c:set var="errorMessage" value="${sessionScope.errorMessage}" />
                <c:remove var="errorMessage" />
            </c:if>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
