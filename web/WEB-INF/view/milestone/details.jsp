<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Milestone Details</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

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
            <!-- Spinner Start -->
            <div id="spinner"
                 class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
                <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
            <!-- Spinner End -->
            <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            <!-- Sidebar End -->

            <!-- Content Start -->
            <div class="content">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->

                <!--  Class List -->
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-12 col-xl-10 d-flex justify-content-between align-items-center">
                            <h3 class="mb-0">Milestone Details</h3>
                            <a  href="javascript:window.history.back();" class="btn btn-outline-secondary text-black">
                                <i class="bi bi-arrow-left"></i> Back
                            </a>
                        </div>

                        <c:if test="${not empty message}">
                            <div class="alert alert-success">${message}</div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <div class="card-body ">
                            <form action="${pageContext.request.contextPath}/milestone/save" method="post">
                                <input type="hidden" name="c" value="${c.id}"/>
                                <input type="hidden" name="id" value="${milestone != null ? milestone.id : ''}" />

                                <div class="row mb-3">
                                    <!-- Code -->
                                    <div class="col-md-6">
                                        <label for="code" class="form-label">Code*</label>
                                        <input type="text" class="form-control" id="code" name="code" 
                                               value="${milestone.code}" 
                                               ${action == 'view' ? 'disabled' : ''} >
                                        <c:if test="${not empty validationErrors['code']}">
                                            <small class="text-danger">${validationErrors['code']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['codeLength']}">
                                            <small class="text-danger">${validationErrors['codeLength']}</small>
                                        </c:if>
                                    </div>
                                    <!-- Name -->
                                    <div class="col-md-6">
                                        <label for="name" class="form-label">Name*</label>
                                        <input type="text" class="form-control" id="name" name="name" 
                                               value="${milestone.name}" 
                                               ${action == 'view' ? 'disabled' : ''} >
                                        <c:if test="${not empty validationErrors['name']}">
                                            <small class="text-danger">${validationErrors['name']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['nameLength']}">
                                            <small class="text-danger">${validationErrors['nameLength']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <!--                           Assignment 
                                                                   <div class="col-md-6">
                                                                    <label for="assignment" class="form-label">Assignment*</label>
                                                                        <select id="assignment" class="form-control" name="assignment"
                                    ${action == 'view' ? 'disabled' : ''}>
                                    <c:forEach var="assignment" items="${assignments}">
                                        <option value="${assignment.id}"
                                        ${milestone.assigmentId != null && milestone.assigmentId == assignment.id ? 'selected' : ''}>
                                        ${assignment.name}
                                    </option>
                                    </c:forEach>
                                </select>
                            </div>-->
                                </div>

                                <div class="row mb-3">
                                    <!-- Priority -->
                                    <div class="col-md-6">
                                        <label for="priority" class="form-label">Priority*</label>
                                        <input type="number" class="form-control" id="priority" name="priority" 
                                               value="${milestone.priority}" 
                                               ${action == 'view' ? 'disabled' : ''} >
                                        <c:if test="${not empty validationErrors['priority']}">
                                            <small class="text-danger">${validationErrors['priority']}</small>
                                        </c:if>
                                    </div>
                                    <!-- Weight -->
                                    <div class="col-md-6">
                                        <label for="weight" class="form-label">Weight*</label>
                                        <input type="number" class="form-control" id="weight" name="weight" 
                                               value="${milestone.weight}" 
                                               ${action == 'view' ? 'disabled' : ''} >
                                        <c:if test="${not empty validationErrors['weight']}">
                                            <small class="text-danger">${validationErrors['weight']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="row mb-3">
                                    <!-- Max Eval Value -->
                                    <div class="col-md-6">
                                        <label for="weight" class="form-label">Max Eval Value*</label>
                                        <input type="number" class="form-control" id="max_eval_value" name="max_eval_value" 
                                               value="${milestone.max_eval_value}" 
                                               ${action == 'view' ? 'disabled' : ''} >
                                        <c:if test="${not empty validationErrors['max_eval_value']}">
                                            <small class="text-danger">${validationErrors['max_eval_value']}</small>
                                        </c:if>

                                    </div>
                                    <!-- Details -->
                                    <div class="col">
                                        <label for="detail" class="form-label">Detail:</label>
                                        <textarea id="detail" class="form-control" name="detail" rows="3">${milestone != null ? milestone.detail : ''}</textarea>
                                        <c:if test="${not empty validationErrors['detail']}">
                                            <small class="text-danger">${validationErrors['detail']}</small>
                                        </c:if>
                                        <c:if test="${not empty validationErrors['detailLength']}">
                                            <small class="text-danger">${validationErrors['detailLength']}</small>
                                        </c:if>
                                    </div>

                                </div>

                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Status</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="closed" name="status" value="0" 
                                                   ${milestone.status == 0 ? 'checked' : ''} 
                                                   ${action == 'view' ? 'disabled' : ''}>
                                            <label class="form-check-label" for="closed">Closed</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="inProgress" name="status" value="1" 
                                                   ${milestone.status == 1 ? 'checked' : ''} 
                                                   ${action == 'view' ? 'disabled' : ''}>
                                            <label class="form-check-label" for="inProgress">In progress</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="pending" name="status" value="2" 
                                                   ${milestone.status == 2 ? 'checked' : ''} 
                                                   ${action == 'view' ? 'disabled' : ''}>
                                            <label class="form-check-label" for="pending">Pending</label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="endDate" class="form-label">End Date*</label>
                                        <input type="date" class="form-control" id="endDate" name="endDate"
                                               value="${milestone.endDate != null ? milestone.endDate : ''}"
                                               ${action == 'view' ? 'readonly' : ''}>
                                        <c:if test="${not empty validationErrors['endDate']}">
                                            <small class="text-danger">${validationErrors['endDate']}</small>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Submit Button -->
                                <c:if test="${milestone != null && action == 'edit'}">
                                    <div class="d-flex justify-content-lg-start">
                                        <button type="submit" class="btn btn-outline-secondary text-black">Save Changes</button>
                                    </div>
                                </c:if>
                            </form>
                        </div>
                    </div>
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

            <!-- Template Javascript -->
            <script src="${pageContext.request.contextPath}/js/main.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>


