<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                            <h2 class="mb-0">Councils Details</h2>
                            <a href="${pageContext.request.contextPath}/council" class="btn btn-outline-secondary text-black">
                                <i class="bi bi-arrow-left"></i> Back
                            </a>
                        </div>
                        <div class="card-body text-black">     
                            <form action="${pageContext.request.contextPath}/council/save" method="post">
                                <!-- Hidden Field for Council ID (only used for editing) -->
                                <c:if test="${not empty council}">
                                    <input type="hidden" name="id" value="${council.id}">
                                </c:if>

                                <!-- Council Name Field -->
                                <div class="mb-3">
                                    <label for="name" class="form-label">Council Name</label>
                                    <input type="text" class="form-control" id="name" name="name" 
                                           value="${council != null ? council.name : ''}" placeholder="">
                                    <c:if test="${not empty validationErrors['name']}">
                                        <small class="text-danger">${validationErrors['name']}</small>
                                    </c:if>
                                    <c:if test="${not empty validationErrors['nameLength']}">
                                        <small class="text-danger">${validationErrors['nameLength']}</small>
                                    </c:if>
                                </div>

                                <!-- Description Field -->
                                <div class="mb-3">
                                    <label for="description" class="form-label">Description</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              rows="3">${council != null ? council.description : ''}</textarea>
                                    <c:if test="${not empty validationErrors['description']}">
                                        <small class="text-danger">${validationErrors['description']}</small>
                                    </c:if>
                                    <c:if test="${not empty validationErrors['descriptionLength']}">
                                        <small class="text-danger">${validationErrors['descriptionLength']}</small>
                                    </c:if>
                                </div>

                                <!-- Class Selection Field -->
                                <div class="mb-3">
                                    <label for="class_id" class="form-label">Class</label>
                                    <select class="form-select" id="class_id" name="class_id">
                                        <option value="" disabled ${classes == null ? 'selected' : ''}>Select a class</option>
                                        <c:forEach items="${classes}" var="c">
                                            <option value="${c.classId}" 
                                                    ${council != null && c.classId == council.classId ? 'selected' : ''}>
                                                ${c.className}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty validationErrors['class_id']}">
                                        <small class="text-danger">${validationErrors['class_id']}</small>
                                    </c:if>
                                </div>

                                <!-- Create Date Field -->
                                <div class="mb-3">
                                    <label for="createDate" class="form-label">Date</label>
                                    <input type="date" class="form-control" id="createDate" name="createDate"
                                           value="${council != null ? council.createdDate : ''}">
                                    <c:if test="${not empty validationErrors['createDate']}">
                                        <small class="text-danger">${validationErrors['createDate']}</small>
                                    </c:if>
                                </div>

                                <!-- Status Field -->
                                <div class="col d-flex align-items-center">
                                    <label class="form-label me-4">Status:</label>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="active" name="status" value="1" 
                                               ${council != null && council.status ? 'checked' : ''}>
                                        <label class="form-check-label" for="active">Active</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="inactive" name="status" value="0" 
                                               ${council != null && !council.status ? 'checked' : ''}>
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