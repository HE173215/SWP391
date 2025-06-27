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
                            <h3>Criteria Details</h3>
                        </div>
                        <div class="card-body">
                            <!-- Thông báo lỗi nếu có -->
                            <c:if test="${not empty messages['dup']}">
                                <div class="text-danger">${messages['dup']}</div>
                            </c:if>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/evalcriteria/save" method="post">
                                    <input type="hidden" name="id" value="${criId.id}" />
                                    <input type="hidden" name="subjectId" value="${sessionScope.groupId}" />
                                    <input type="hidden" name="activeTab" value="${sessionScope.activeTab}" />
                                    <!-- Name Field -->
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="name">Name*</label>
                                            <input type="text" class="form-control" id="name" name="name" value="${criId.name}">
                                            <c:if test="${not empty messages['name']}">
                                            <div class="text-danger">${messages['name']}</div>
                                        </c:if>
                                        </div>

                                        <!-- Detail Field -->
                                        <div class="form-group col-md-6">
                                            <label for="detail">Detail</label>
                                            <input type="text" class="form-control" id="detail" name="detail" value="${criId.detail}" >
                                        </div>
                                    </div>

                                    <!-- Weight Field -->
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="weight">Weight*</label>
                                            <input type="number" class="form-control" id="weight" name="weight" value="${criId.weight}" min="0">
                                             <c:if test="${not empty messages['weight']}">
                                            <div class="text-danger">${messages['weight']}</div>
                                        </c:if>
                                        </div>

                                        <!-- Assignment ID Field (Dropdown) -->
                                        <div class="form-group col-md-6">
                                            <label for="assignmentId">Assignment*</label>
                                            <select class="form-control" id="assignmentId" name="assignmentId" required>
                                                <c:forEach var="assignment" items="${assignments}">
                                                    <option value="${assignment.id}" ${assignment.id == criId.assignmentId ? 'selected' : ''}>${assignment.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- Status Field (Radio Button) -->
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label>Status*</label><br>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="statusActive" name="status" value="Active" 
                                                       ${criId.status == true || criId.status == null ? 'checked' : ''} required>
                                                <label class="form-check-label" for="statusActive">Active</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="statusInactive" name="status" value="Inactive" 
                                                       ${criId.status == false ? 'checked' : ''} required>
                                                <label class="form-check-label" for="statusInactive">Inactive</label>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Submit and Back Buttons -->
                                    <button type="button" class="btn btn-secondary" onclick="history.back()">Back</button>
                                    <button type="submit" class="btn btn-primary">Submit</button>
                                </form>
                            </div>
                        </div>
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
