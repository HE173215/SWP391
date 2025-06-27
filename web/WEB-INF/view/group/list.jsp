<!DOCTYPE html>
<html lang="en">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <head>
        <meta charset="UTF-8">
        <title>DASHMIN - Bootstrap Admin Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Include Toastify CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
        <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

        <!-- Include SweetAlert CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <!-- Libraries Stylesheet -->
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
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

                <!-- Spinner Start -->
                <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center"></div>
                <!-- Spinner End -->
                <!-- Group List Start -->
                <div class="container-fluid pt-4 px-4">                    
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <h3 class="mb-0">Group List</h3>
                        <c:if test="${currentUser.role == 'Admin'}">
                            <!-- Add Button -->
                            <button type="button" class="btn btn-success ms-auto" onclick="window.location.href = 'group/new'">Add</button>
                        </c:if>
                    </div>
                    <!-- Tab Navigation -->
                    <ul class="nav nav-tabs mb-3" id="myTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <a class="nav-link active" id="type1-tab" data-bs-toggle="tab" href="#type1" role="tab" aria-controls="type1" aria-selected="true">Subject</a>
                        </li>
                        <c:if test="${currentUser.role == 'Admin'}">
                            <li class="nav-item" role="presentation">
                                <a class="nav-link" id="type2-tab" data-bs-toggle="tab" href="#type2" role="tab" aria-controls="type2" aria-selected="false">Department</a>
                            </li>
                        </c:if>

                    </ul>
                    <div class="table-responsive mt-3">
                        <!-- Tab Content -->
                        <div class="tab-content" id="myTabContent">
                            <!-- Tab Type 1 - Student -->
                            <div class="tab-pane fade show active" id="type1" role="tabpanel" aria-labelledby="type1-tab">
                                <div class="table-responsive">
                                    <table class="table text-start align-middle table-bordered table-hover mb-0">
                                        <thead>
                                            <tr class="text-dark">
                                                <th scope="col">ID</th>
                                                <th scope="col">Code</th>
                                                <th scope="col">Name</th>
                                                <th scope="col">Details</th>
                                                <th scope="col">Status</th>
                                                <th scope="col">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <!-- L?c nh?m c? type l? 'Subject' -->
                                            <c:forEach var="slist" items="${groupList}">
                                                <c:if test="${slist.type == 'Subject'}">
                                                    <tr>
                                                        <td>${slist.id}</td>
                                                        <td>${slist.code}</td>
                                                        <td>${slist.name}</td>
                                                        <td>${slist.detail}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${slist.status == 1}">Active</c:when>
                                                                <c:otherwise>Inactive</c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:if test="${currentUser.role == 'Admin'}">
                                                                <a href="${pageContext.request.contextPath}/group/edit?id=${slist.id}&page=${param.page}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}">
                                                                    <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                                                </a> |
                                                                <c:choose>
                                                                    <c:when test="${slist.status == 1}">
                                                                        <form action="${pageContext.request.contextPath}/group/change-status" method="post" style="display:inline;">
                                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                                            <input type="hidden" name="newStatus" value="0" />
                                                                            <button type="submit" class="btn btn-link text-warning"><i class="fas fa-ban"></i></button>
                                                                        </form>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <form action="${pageContext.request.contextPath}/group/change-status" method="post" style="display:inline;">
                                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                                            <input type="hidden" name="newStatus" value="1" />
                                                                            <button type="submit" class="btn btn-link text-success"><i class="fas fa-check"></i></button>
                                                                        </form> 
                                                                    </c:otherwise>
                                                                </c:choose> 
                                                            </c:if>
                                                            <c:if test="${currentUser.role == 'Subject Manager'}">
                                                                <a href="${pageContext.request.contextPath}/subject?id=${slist.id} ">
                                                                    <button type="button" class="btn btn-link"><i class="fas fa-wrench"></i></button>
                                                                </a>
                                                            </c:if>

                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <c:if test="${currentUser.role == 'Admin'}">
                                <!-- Tab Type 2 - Department -->
                                <div class="tab-pane fade" id="type2" role="tabpanel" aria-labelledby="type2-tab">
                                    <div class="table-responsive">
                                        <table class="table text-start align-middle table-bordered table-hover mb-0">
                                            <thead>
                                                <tr class="text-dark">
                                                    <th scope="col">ID</th>
                                                    <th scope="col">Code</th>
                                                    <th scope="col">Name</th>
                                                    <th scope="col">Details</th>
                                                    <th scope="col">Status</th>
                                                    <th scope="col">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <!-- L?c nh?m c? type l? 'Department' -->
                                                <c:forEach var="slist" items="${groupList}">
                                                    <c:if test="${slist.type == 'Department'}">
                                                        <tr>
                                                            <td>${slist.id}</td>
                                                            <td>${slist.code}</td>
                                                            <td>${slist.name}</td>
                                                            <td>${slist.detail}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${slist.status == 1}">Active</c:when>
                                                                    <c:otherwise>Inactive</c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <a href="${pageContext.request.contextPath}/group/edit?id=${slist.id}&page=${param.page}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}">
                                                                    <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                                                </a> |
                                                                <c:choose>
                                                                    <c:when test="${slist.status == 1}">
                                                                        <form action="${pageContext.request.contextPath}/group/change-status" method="post" style="display:inline;">
                                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                                            <input type="hidden" name="newStatus" value="0" />
                                                                            <button type="submit" class="btn btn-link text-warning"> <i class="fas fa-ban"></i></button>
                                                                        </form>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <form action="${pageContext.request.contextPath}/group/change-status" method="post" style="display:inline;">
                                                                            <input type="hidden" name="id" value="${slist.id}" />
                                                                            <input type="hidden" name="newStatus" value="1" />
                                                                            <button type="submit" class="btn btn-link text-success"><i class="fas fa-check"></i></button>
                                                                        </form>
                                                                    </c:otherwise>
                                                                </c:choose>

                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:if>

                        </div>

                    </div>


                    <!-- Phan trang -->
                    <div class="d-flex justify-content-center mt-3">
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <a href="?page=${i}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}"
                               class="btn ${i == currentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1">${i}</a>
                        </c:forEach>
                    </div>


                </div>

                <!-- JavaScript Libraries -->
                <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                <script src="lib/chart/chart.min.js"></script>
                <script src="lib/easing/easing.min.js"></script>
                <script src="lib/waypoints/waypoints.min.js"></script>
                <script src="lib/owlcarousel/owl.carousel.min.js"></script>
                <script src="lib/tempusdominus/js/moment.min.js"></script>
                <script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
                <script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

                <!-- Template Javascript -->
                <script src="js/main.js"></script>
                </body>
                </html>
