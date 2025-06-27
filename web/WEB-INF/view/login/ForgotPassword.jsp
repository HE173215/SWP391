<%-- 
    Document   : ForgotPassword
    Created on : Sep 18, 2024, 11:06:35 PM
    Author     : Admin
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            display: flex;
            justify-content: center;
            align-items: flex-start; /* Align items to the start of the container */
            height: 90vh; /* Reduce height to shift the form up */
            margin: 0;
            padding-top: 40px; /* Add some padding at the top */
        }
        .form-container {
            width: 300px;
            background-color: #fff;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        h2 {
            color: #333;
            margin-bottom: 20px;
        }
        label {
            margin-bottom: 10px;
            display: block;
            color: #333;
        }
        input[type="email"], input[type="submit"] {
            width: calc(100% - 20px);
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            border: 1px solid #ddd;
            box-sizing: border-box;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            cursor: pointer;
            border: none;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        p {
            color: #ff0000; /* Red color for error messages */
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Quên mật khẩu</h2>
        
        <form action="ForgotPassword" method="post">
            <label for="email">Email của bạn:</label>
            <input type="email" id="email" name="email" required>
            
            <input type="submit" value="Gửi OTP">
        </form>
        
        <p>${sessionScope.resetMessage}</p>
        <p>${sessionScope.resetError}</p>
    </div>
</body>
</html>

