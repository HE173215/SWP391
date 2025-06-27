<%-- 
    Document   : details
    Created on : Sep 20, 2024
    Author     : admin
--%>
<!DOCTYPE html>
<html lang="en">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html; charset=UTF-8" %>
    <head>
        <meta charset="UTF-8">
        <title>User Details</title>
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
        <style>
            th a {
                color: inherit;
                text-decoration: none;
            }

            th a:hover {
                text-decoration: unset;
                cursor: pointer;
            }
            .swal2-toast {
                background-color: #f8f9fa;
                color: #333;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
            }

            .swal2-title {
                font-weight: bold;
                font-size: 1.2em;
            }

            .swal2-content {
                font-size: 0.9em;
            }
        </style>
    </head>

    <body>
        <div class="container-fluid position-relative bg-white d-flex p-0">
            <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            <!-- Sidebar End -->
            <!-- Main Content Start -->
            <div class="content" style="flex-grow: 1;">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->

                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-12 col-xl-10 d-flex justify-content-between align-items-center">
                            <h2 class="mb-0">User Details</h2>
                            <a href="${pageContext.request.contextPath}/user" class="btn btn-outline-secondary text-black">
                                <i class="bi bi-arrow-left"></i> Back
                            </a>
                        </div>

                        <div class="card-body text-black">                           
                            <form action="${pageContext.request.contextPath}/user/save" method="post">
                                <input type="hidden" name="id" value="${user != null ? user.id : ''}" />

                                <!-- Full Name and Email -->
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="fullName" class="form-label">Full Name*</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" value="${user.fullName}">
                                        <c:if test="${not empty validationErrors['fullName']}">
                                            <small class="text-danger">${validationErrors['fullName']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['fullNameLength']}">
                                            <small class="text-danger">${validationErrors['fullNameLength']}</small>
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="email" class="form-label">Email*</label>
                                        <input type="email" class="form-control" id="email" name="email" value="${user.email}" >
                                        <c:if test="${not empty validationErrors['email']}">
                                            <small class="text-danger">${validationErrors['email']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['emailRegex']}">
                                            <small class="text-danger">${validationErrors['emailRegex']}</small>
                                        </c:if>
                                    </div>

                                </div>

                                <!-- Mobile and Role-->
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="mobile" class="form-label">Mobile</label>
                                        <input type="text" class="form-control" id="mobile" name="mobile" value="${user.mobile}">
                                        <c:if test="${not empty validationErrors['mobile']}">
                                            <small class="text-danger">${validationErrors['mobile']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['mobileRegex']}">
                                            <small class="text-danger">${validationErrors['mobileRegex']}</small>
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="roleID" class="form-label">Role</label>
                                        <select id="roleID" name="roleID" class="form-select text-black">
                                            <c:forEach items="${roles}" var="role">
                                                <option value="${role.roleID}" ${role.roleID == user.roleID ? 'selected' : ''}>
                                                    ${role.roleName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty validationErrors['roleID']}">
                                            <small class="text-danger">${validationErrors['roleID']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Notes Field -->
                                <div class="mb-3">
                                    <label for="notes" class="form-label">Notes</label>
                                    <textarea class="form-control" id="notes" name="notes" rows="3">${user.notes}</textarea>
                                    <c:if test="${not empty validationErrors['notes']}">
                                        <small class="text-danger">${validationErrors['notes']}</small>
                                    </c:if>
                                    <c:if test="${not empty validationErrors['notesLength']}">
                                        <small class="text-danger">${validationErrors['notesLength']}</small>
                                    </c:if>
                                </div>

                                <!-- Status -->
                                <div class="mb-3">
                                    <label class="form-label" >Status</label><br>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="active" name="status" value="1" ${user.status == 1 ? 'checked' : ''} required>
                                        <label class="form-check-label" for="active">Active</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="inactive" name="status" value="0" ${user.status == 0 ? 'checked' : ''}>
                                        <label class="form-check-label" for="inactive">Inactive</label>
                                    </div>
                                </div>

                                <!-- Submit Button -->
                                <div class="d-flex justify-content-lg-start " style="margin-top: 30px;">
                                    <button type="submit" class="btn btn-outline-secondary text-black">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Main Content End -->
        </div>

        <!-- JavaScript Libraries -->
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
