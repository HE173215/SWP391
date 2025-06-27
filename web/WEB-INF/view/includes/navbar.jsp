<%-- 
    Document   : navbar
    Created on : Sep 18, 2024, 8:55:59 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<nav class="navbar navbar-expand bg-light navbar-light sticky-top px-4 py-0">
    <a href="index.html" class="navbar-brand d-flex d-lg-none me-4">
        <h2 class="text-primary mb-0"><i class="fa fa-hashtag"></i></h2>
    </a>
    <a href="#" class="sidebar-toggler flex-shrink-0">
        <i class="fa fa-bars"></i>
    </a>

    <div class="navbar-nav align-items-center ms-auto">
   

        <div class="nav-item dropdown">
              <c:set var="user" value="${sessionScope.currentUser}" />
            <a href="${pageContext.request.contextPath}/profile" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <img class="rounded-circle me-lg-2" src="${pageContext.request.contextPath}/uploads/${user.image}" alt="image"
                     style="width: 40px; height: 40px;">
                <span class="d-none d-lg-inline-flex">${user.userName}</span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <a href="${pageContext.request.contextPath}/profile" class="dropdown-item">My Profile</a>
                <a href="${pageContext.request.contextPath}/changepassword" class="dropdown-item">Change Password</a>
                <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">Log Out</a>
            </div>
        </div>
    </div>
</nav>
