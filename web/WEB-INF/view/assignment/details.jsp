<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${assignmentByID != null ? 'Edit Assignment' : 'Add Assignment'}</title>

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
        <div class="container-fluid position-relative bg-light p-0">
            <!-- Spinner Start -->

            <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            <!-- Sidebar End -->
            <!-- Main Content Start -->
            <div class="content" style="flex-grow: 1;">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->

                <!-- Form Content Start -->
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-12 col-xl-10 d-flex justify-content-between align-items-center">
                            <h3 class="mb-0">${assignmentByID != null ? 'Edit Assigment' : 'Add Assigment'}</h3>
                            <a href="javascript:window.history.back();" class="btn btn-outline-secondary text-black">
                                <i class="bi bi-arrow-left"></i> Back
                            </a>
                        </div>

                        <div class="card-body text-black">
                            <form action="${pageContext.request.contextPath}/assignment/save" method="post">
                                <input type="hidden" name="id" value="${assignmentByID != null ? assignmentByID.id : ''}"/>

                                <div class="row mb-3 " >
                                    <div class="col">
                                        <label for="name" class="form-label">Name:</label>
                                        <input type="text" class="form-control" id="name" name="name" value="${assignmentByID != null ? assignmentByID.name : ''}"/>
                                        <c:if test="${not empty validationErrors['name']}">
                                            <small class="text-danger">${validationErrors['name']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['nameLength']}">
                                            <small class="text-danger">${validationErrors['nameLength']}</small>
                                        </c:if>
                                    </div>
                                    <div class="col">
                                        <label for="weight" class="form-label">Weight:</label>
                                        <input type="number" class="form-control" id="weight" name="weight" value="${assignmentByID != null ? assignmentByID.weight : ''}"/>
                                        <c:if test="${not empty validationErrors['weight']}">
                                            <small class="text-danger">${validationErrors['weight']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <div class="col">
                                        <label for="group_id" class="form-label">Code:</label>
                                        <select id="group_id" class="form-control" name="group_id">
                                            <c:forEach var="code" items="${codes}">
                                                <option value="${code.subject_id}" ${assignmentByID != null && assignmentByID.subject_id == code.id ? 'selected' : ''}>
                                                    ${code.subject_code}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty validationErrors['group_id']}">
                                            <small class="text-danger">${validationErrors['group_id']}</small>
                                        </c:if>
                                    </div>
                                    <div class="col">
                                        <label for="detail" class="form-label">Detail:</label>
                                        <textarea id="detail" class="form-control" name="detail" rows="3">${assignmentByID != null ? assignmentByID.detail : ''}</textarea>
                                        <c:if test="${not empty validationErrors['detail']}">
                                            <small class="text-danger">${validationErrors['detail']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['detailLength']}">
                                            <small class="text-danger">${validationErrors['detailLength']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Final Assignment and Status as Radio Buttons -->
                                <div class="row mb-3">
                                    <div class="col">
                                        <label class="form-label">Final Assignment:</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="final_yes" name="final_assignment" value="1" ${assignmentByID != null && assignmentByID.final_assignment ? 'checked' : ''}>
                                            <label class="form-check-label" for="final_yes">Yes</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="final_no" name="final_assignment" value="0" ${assignmentByID != null && !assignmentByID.final_assignment ? 'checked' : ''}>
                                            <label class="form-check-label" for="final_no">No</label>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <label class="form-label">Status:</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="active" name="status" value="1" ${assignmentByID != null && assignmentByID.status ? 'checked' : ''} required>
                                            <label class="form-check-label" for="active">Active</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="inactive" name="status" value="0" ${assignmentByID != null && !assignmentByID.status ? 'checked' : ''}>
                                            <label class="form-check-label" for="inactive">Inactive</label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Submit Button -->
                                <div class="d-flex justify-content-lg-start" style="margin-top: 30px;">
                                    <button type="submit" class="btn btn-outline-secondary text-black">Save Changes</button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
                <!-- Form Content End -->
            </div>
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
