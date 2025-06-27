<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                            <h3>Subject Details</h3>
                        </div>
                        <c:if test="${not empty messages['dup']}">
                            <div class="text-danger">${messages['dup']}</div>
                        </c:if>
                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/subject/save" method="post">
                                <input type="hidden" name="id" value="${subId.id}" />
                                <input type="hidden" name="subjectId" value="${sessionScope.groupId}" >
                                <input type="hidden" name="activeTab" value="sessionScope.activeTab">

                                <!-- Name Field -->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="name">Setting*</label>
                                        <input type="text" class="form-control" id="name" name="name" value="${subId.name}">
                                        <c:if test="${not empty messages['name']}">
                                            <div class="text-danger">${messages['name']}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label for="type">Type</label>
                                        <select class="form-control" id="type" name="type">
                                            <option value="Quality" ${subId.type == 'Quality' ? 'selected' : ''}>Quality</option>
                                            <option value="Complexity" ${subId.type == 'Complexity' ? 'selected' : ''}>Complexity</option>
                                        </select>
                                    </div>
                                </div>

                                <!-- Priority and Status Fields -->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="value">Value</label>
                                        <input type="number" class="form-control" id="value" name="value" value="${subId.value}" min="0">
                                        <c:if test="${not empty messages['value']}">
                                            <div class="text-danger">${messages['value']}</div>
                                        </c:if>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Status</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="active" name="status" value="1" 
                                                   ${subId.status == true || subId.status == null ? 'checked' : ''}>
                                            <label class="form-check-label" for="active">Active</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="inactive" name="status" value="0" 
                                                   ${subId.status == false ? 'checked' : ''}>
                                            <label class="form-check-label" for="inactive">Inactive</label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Description Field -->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="subject">Subject</label>
                                        <input type="hidden" id="subjectId" value="${sessionScope.groupId}">
                                        <input type="text" class="form-control" id="subject" name="subject" value="${groupName}" readonly>
                                    </div>
                                </div>

                                <!-- Submit Button -->
                                <button type="button" class="btn btn-secondary" onclick="history.back()">Back</button>
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
