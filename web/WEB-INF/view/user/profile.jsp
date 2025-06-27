<%-- 
    Document   : profile
    Created on : Sep 18, 2024, 8:40:26 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">


        <title>profile</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            body {
                background: #f7f7ff;
                margin-top: 20px;
            }

            .card {
                position: relative;
                display: flex;
                flex-direction: column;
                min-width: 0;
                word-wrap: break-word;
                background-color: #fff;
                background-clip: border-box;
                border: 0 solid transparent;
                border-radius: .25rem;
                margin-bottom: 1.5rem;
                box-shadow: 0 2px 6px 0 rgb(218 218 253 / 65%), 0 2px 6px 0 rgb(206 206 238 / 54%);
            }

            .me-2 {
                margin-right: .5rem !important;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="main-body">
                <div class="row">
                    <div class="col-lg-2 ">
                        <div class="card">
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary px-4" role="button">Back home</a>

                        </div>
                    </div>

                    <div class="col-lg-12">
                        <form action="${pageContext.request.contextPath}/profile/submit" method="post" enctype="multipart/form-data">
                            <div class="card">
                                <c:set var="user" value="${sessionScope.currentUser}" />

                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">
                                        <img src='${pageContext.request.contextPath}/uploads/${user.image}' alt="User" class="rounded-circle p-1 bg-primary" width="110" height="110">
                                        <div class="mb-3">
                                            <input class="form-control" type="file" id="formFile" name="file" accept="image/*">
                                        </div>
                                        <div class="mt-3">
                                            <h4>${user.userName}</h4>
                                            <span>${user.role}</span>

                                        </div>

                                    </div>
                                    <hr class="my-4">                                              
                                </div>

                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="row mb-3">
                                                <div class="col-sm-3">
                                                    <h6 class="mb-0">User Name</h6>
                                                </div>
                                                <div class="col-sm-9 text-secondary">
                                                    <input type="text" class="form-control" id="userName" name="userName" 
                                                           value="${param.userName != null ? param.userName : user.userName}" >
                                                </div>
                                            </div>
                                            <div class="row mb-3">
                                                <div class="col-sm-3">
                                                    <h6 class="mb-0">Full Name</h6>
                                                </div>
                                                <div class="col-sm-9 text-secondary">
                                                    <input type="text" class="form-control" value="${param.fullName != null ? param.fullName : user.fullName}" name="fullName">
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <div class="col-sm-3">
                                                    <h6 class="mb-0">Mobile</h6>
                                                </div>
                                                <div class="col-sm-9 text-secondary">
                                                    <input type="text" class="form-control" value="${param.mobile != null ? param.mobile : user.mobile}" name="mobile">
                                                </div>
                                            </div>

                                        </div>


                                        <div class="col-sm-6">
                                            <div class="row mb-3">
                                                <div class="col-sm-2">
                                                    <h6 class="mb-0">Email</h6>
                                                </div>
                                                <div class="col-sm-8 text-secondary">
                                                    <input type="text" class="form-control" value="${param.email != null ? param.email : user.email}" name="email">
                                                </div>

<!--                                                <div class="col-sm-2">
                                                    <button type="button" class="btn btn-primary" onclick="showOtpInput()">Edit</button>
                                                </div>-->


                                                <!--                                                <div class="col-sm-1">
                                                                                                    <button type="button" class="btn btn-primary" onclick="showOtpInput()">Edit</button>
                                                                                                </div>-->
                                            </div>

                                            <!-- Ô nhập OTP (ẩn ban đầu) -->
<!--                                            <div class="row mb-3" id="otpRow" style="display: none;">
                                                <div class="col-sm-2"></div>


                                                <div class="col-sm-2">
                                                    <h6 class="mb-0">OTP</h6>
                                                </div>
                                                <div class="col-sm-4 text-secondary">
                                                    <input type="text" class="form-control" name="otp" id="otp">
                                                </div>
                                                <div class="col-sm-2">
                                                    <button type="button" class="btn btn-primary ms-2" >Verify</button>
                                                </div>
                                            </div>-->
                                        </div>
                                    </div>

                                    <!-- Hiển thị thông báo lỗi -->
                                    <div>
                                        <p style="color: red">
                                            ${errMessage}
                                        </p>
                                    </div>
                                    <!-- Hiển thị thông báo thành công -->
                                    <div>
                                        <p style="color: green">
                                            ${messSucces}
                                        </p>
                                    </div>
                                    <!--<div class="row">-->
                                    <div class="col-sm-3"></div>
                                    <div class="col-sm-9 text-secondary">
                                        <input type="submit" class="btn btn-primary px-4" value="Save Changes">
                                        <a href="${pageContext.request.contextPath}/changepassword" class="btn btn-primary px-4" role="button">Change Password</a>
                                    </div>
                                    <!--</div>-->

                                </div>
                            </div>
                        </form>
                    </div>


                </div>
            </div>
        </div>
        <script>
            function showOtpInput() {
                // Hiển thị ô nhập OTP khi nhấn nút Edit
                document.getElementById('otpRow').style.display = 'flex';
            }
        </script>
        <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript">

        </script>
    </body>
</html>
