<%-- 
    Document   : form
    Created on : Oct 7, 2024, 11:38:15 PM
    Author     : Do Duan
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Setting Details</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

        <!-- Google Web Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Bootstrap & Icon Font Stylesheets -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet"/>

        <!-- Customized Bootstrap Stylesheet -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">

        <!-- SweetAlert2 CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <!-- SweetAlert2 JS -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- Custom CSS -->
    </head>
    <body>
        <div class="wrapper">
            <!-- Sidebar Start -->
            <div class="sidebar">
                <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            </div>
            <!-- Sidebar End -->

            <!-- Main Content Start -->
            <div class="content">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->
                <div class="container-fluid mt-4">
                    <div class="card shadow-sm">
                        <div class="card-header">
                            <h3>${requestScope.action} Class</h3>
                        </div>
                        <div class="card-body">
                            <c:set var="c" value="${requestScope.currentClass}" />
                            <form action="${pageContext.request.contextPath}/class/detail" method="get">
                                <c:if test="${requestScope.action == 'Add new' || requestScope.action =='SaveNew'}">
                                    <input type="hidden" name="action" value="SaveNew" />

                                </c:if>
                                <c:if test="${requestScope.action == 'Edit' || requestScope.action =='Save'}">
                                    <input type="hidden" name="action" value="Save" />

                                </c:if>

                                <input type="hidden" name="id" value="${c != null ? c.id : ''}" />
                                <!-- Name and Subject Field-->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="name">Class Name*</label>
                                        <input type="text" class="form-control" id="name" name="name" value="${param.name != null ? param.name : c.name}" required>
                                    </div>

                                    <c:if test="${requestScope.action =='Add new' || requestScope.action =='SaveNew'}">
                                        <div class="form-group col-md-6">
                                            <label for="type">Subject</label>
                                            <select class="form-control" id="subject" name="subject">
                                                <c:forEach var="subject" items="${subjectList}">
                                                    <!--<option value="${param.subject != null ? param.subject : subject.id}"--> 
                                                    <option value="${ subject.id}" 
                                                            <c:if test="${subject.id == c.groupID  }">selected</c:if> >
                                                        ${subject.code }
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </c:if>

                                    <c:if test="${requestScope.action =='Edit' || requestScope.action =='Save'}">
                                        <div class="form-group col-md-6">
                                            <label for="type">Subject</label>
                                            <select class="form-control" id="subject" name="subject" disabled>
                                                <c:forEach var="subject" items="${subjectList}">
                                                    <option value="${ subject.id}" 
                                                            <c:if test="${subject.id == c.groupID}">selected</c:if> >
                                                        ${subject.code}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <!-- Hidden input để giữ giá trị khi submit form -->
                                            <input type="hidden" name="subject" value="${c.groupID}">
                                        </div>
                                    </c:if>


                                </div>
                                <!-- Semester and Status Fields-->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="type">Semester</label>
                                        <select class="form-control" id="semester" name="semester">
                                            <c:forEach var="semester" items="${semeterList}">
                                                <option value="${ param.semester != null ? param.semester : semester.id}" 
                                                        <c:if test="${semester.id == c.semesterID}">selected</c:if> >
                                                    ${semester.settingName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Status</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="active" name="status" value="1" ${c.status == 1 ? 'checked' : ''}>
                                            <label class="form-check-label" for="active">Active</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="inactive" name="status" value="0" ${c.status == 0 ? 'checked' : ''}>                             
                                            <label class="form-check-label" for="inactive">Inactive</label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Description Field -->
                                <div class="form-group">
                                    <label for="detail">Detail</label>
                                    <textarea class="form-control" id="detail" name="detail" rows="3">${param.detail != null ? param.detail : c.detail}</textarea>                                   
                                </div>

                                <div class="form-group" style="color: red">
                                    ${requestScope.errMess}
                                </div>

                                <div class="form-group" style="color: green">
                                    ${requestScope.editSucces}
                                </div>

                                <div class="form-group" style="color: red">
                                    ${requestScope.editFail}
                                </div>

                                <!-- Submit Button -->
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </form>
                        </div>
                    </div>
                    <!-- Main Content End -->
                </div>

                <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/chart/chart.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/moment.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/moment-timezone.min.js"></script>
                <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

                <!-- Bootstrap Scripts -->
                <script src="${pageContext.request.contextPath}/js/main.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
                </body>
                </html>
