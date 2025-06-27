<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nhập OTP</title>
        <style>
            body {
                font-family: Arial, sans-serif;
            }
            h2 {
                font-size: 24px;
            }
            input {
                padding: 10px;
                font-size: 16px;
            }
            button {
                padding: 10px 15px;
                font-size: 16px;
                cursor: pointer;
            }
            div {
                margin-top: 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <h2>Nhập OTP</h2>
        <form action="${pageContext.request.contextPath}/user/otp/submit" method="post">
            <input type="text" name="otp" required placeholder="Nhập OTP">
            <button type="submit">Xác nhận</button>
        </form>

        <c:if test="${not empty requestScope.errorMessage}">
            <div class="alert alert-danger">${requestScope.errorMessage}</div>
            <c:set var="errorMessage" value="${requestScope.errorMessage}" />
            <c:remove var="errorMessage" />
        </c:if> 

    </body>
</html>
