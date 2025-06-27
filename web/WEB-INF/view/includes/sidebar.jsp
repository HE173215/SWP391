<%-- 
    Document   : sidebar
    Created on : Sep 18, 2024, 8:58:47 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<c:set var="activeMenu" value="${sessionScope.activeMenu}" />

<div class="sidebar pe-4 pb-3">
    <nav class="navbar bg-light navbar-light">
        <c:set var="user" value="${sessionScope.currentUser}" />
        <a href="${pageContext.request.contextPath}/dashboard" class="navbar-brand mx-4 mb-3">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo" style="height: 60px; margin-right: 10px;">
        </a>
        <div class="d-flex align-items-center ms-4 mb-4">
            <div class="position-relative">
                <img class="rounded-circle" src="${pageContext.request.contextPath}/uploads/${user.image}" alt="image" style="width: 40px; height: 40px;">
                <div class="bg-success rounded-circle border border-2 border-white position-absolute end-0 bottom-0 p-1"></div>
            </div>
            <div class="ms-3">
                <h6 class="mb-0">${user.userName}</h6>
                <span>${user.role}</span>
            </div>
        </div>
        <div class="navbar-nav w-100">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item nav-link ${activeMenu == 'dashboard' ? 'active' : ''}">
                <i class="fa fa-tachometer-alt me-2"></i>Dashboard
            </a>

            <c:if test="${user.roleID == 6 }">
                <a href="${pageContext.request.contextPath}/setting" class="nav-item nav-link ${activeMenu == 'setting' ? 'active' : ''}">
                    <i class="fa fa-th me-2"></i>Setting List
                </a>
                <a href="${pageContext.request.contextPath}/user" class="nav-item nav-link ${activeMenu == 'user' ? 'active' : ''}">
                    <i class="fa fa-keyboard me-2"></i>User List
                </a>
            </c:if>

            <c:if test="${user.roleID == 2 || user.roleID == 4|| user.roleID == 5 || user.roleID == 6}">
                <a href="${pageContext.request.contextPath}/class" class="nav-item nav-link ${activeMenu == 'class' ? 'active' : ''}">
                    <i class="fa fa-keyboard me-2"></i>Class List
                </a>
                <a href="${pageContext.request.contextPath}/group" class="nav-item nav-link ${activeMenu == 'group' ? 'active' : ''}">
                    <i class="fa fa-th me-2"></i>Group List
                </a>
                <li class="nav-item dropdown">
                    <a href="#" class="nav-link dropdown-toggle" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fa fa-th me-2"></i>Evaluation
                    </a>
                    <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0" aria-labelledby="evaluatedDropdown">
                        <a href="#" class="dropdown-item">Class Evaluated</a>
                        <a href="#" class="dropdown-item">Team Evaluated</a>
                        <a href="${pageContext.request.contextPath}/workEval" class="dropdown-item">Work Evaluated</a>
                    </div>
                </li>
            </c:if>        
            <c:if test="${user.roleID == 4|| user.roleID == 6}">               
                <a href="${pageContext.request.contextPath}/council" class="nav-item nav-link ${activeMenu == 'council' ? 'active' : ''}">
                    <i class="fa fa-th me-2"></i>Review Council
                </a>
            </c:if>
        </div>
    </nav>

    <script>
        // Lấy tất cả các mục menu
        const navLinks = document.querySelectorAll('.nav-link');

        // Lặp qua các mục và thêm sự kiện click
        navLinks.forEach(link => {
            link.addEventListener('click', function (event) {
                // Chặn hành vi mặc định của liên kết (chuyển trang ngay lập tức)
                event.preventDefault();

                // Xóa class 'active' khỏi tất cả các mục menu
                navLinks.forEach(item => item.classList.remove('active'));

                // Thêm class 'active' cho mục menu được click
                this.classList.add('active');

                // Lấy giá trị href mà không có context path
                const activeMenu = this.getAttribute('href').replace('${pageContext.request.contextPath}/', '');

                // Gửi yêu cầu AJAX để lưu trạng thái menu
                fetch('${pageContext.request.contextPath}/setActiveMenu?menu=' + activeMenu)
                        .then(response => {
                            if (response.ok) {
                                // Chuyển hướng đến liên kết sau khi lưu trạng thái thành công
                                window.location.href = this.getAttribute('href');
                            }
                        });
            });
        });
    </script>

</div>
