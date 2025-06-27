<%-- 
    Document   : dashboard
    Created on : Sep 15, 2024, 9:16:28 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">

    <head>
        <meta charset="utf-8">
        <title>Project Evaluation System</title>
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


                <!--Body Start-->

                <!--Filter Start-->
                <div class="container-fluid pt-4 px-4">
                    <!-- Form filter -->
                    <form action="dashboard" method="get">
                        <div class="row g-4">
                            <!--Semester-->
                            <div class="col-sm-12 col-xl-3">
                                <select class="form-select mb-3" aria-label="Default select example" name="semesterID" >
                                    <option value="0">Semester: All</option>
                                    <c:forEach var="semester" items="${semesterList}">
                                        <option value="${semester.id}" 
                                                <c:if test="${semester.id == selectedSemesterID}">selected</c:if> >
                                            ${semester.settingName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!--Subject-->
                            <div class="col-sm-12 col-xl-3">
                                <select class="form-select mb-3" aria-label="Default select example" name="subjectID">
                                    <option value="0">All subject</option>
                                    <c:forEach var="subject" items="${subjectList}">
                                        <option value="${subject.id}" 
                                                <c:if test="${subject.id == selectedSubjectID}">selected</c:if> >
                                            ${subject.code}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!--Search input-->
                            <div class="col-sm-12 col-xl-3">
                                <input class="form-control border-1" name="searchString" value="${param.searchString}" type="search" placeholder="Search">
                            </div>
                            <!--Search button-->
                            <div class="col-sm-12 col-xl-3">
                                <button type="submit" class="btn btn-primary mb-3">Search</button>
                            </div>
                        </div>
                    </form>
                </div>
                <!--Filter End-->

                <!-- Assigned Classes -->
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <p class="h3">Assigned Classes</p>
                    </div>
                </div>

                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <!-- Kiểm tra xem có classList hay không -->
                        <c:if test="${not empty classList}">
                            <c:forEach var="c" items="${classList}">
                                <div class="col-sm-12 col-md-6 col-xl-4">
                                    <div class="h-100 bg-light rounded p-4">
                                        <div class="d-flex align-items-center justify-content-between mb-4">
                                            <h5 class="mb-0">${c.name}</h5>
                                        </div>
                                        <div>
                                            <p>Subject: ${c.subjectCode} - ${c.subjectName}</p>
                                            <p>Semester: ${c.semester}</p>
                                            <p>Status: Ongoing</p>
                                            <a href="dashboard/detail?id=${c.id}" class="btn btn-sm btn-primary">Detail</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty classList}">
                            <p>No classes found.</p>
                        </c:if>
                    </div>
                </div>


                <!--Paging Start-->
                <div class="d-flex justify-content-center mt-4">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="?page=${i}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}"
                           class="btn ${i == currentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1">${i}</a>
                    </c:forEach>
                </div>
                <!--Paging End-->

                <!--Body End-->

                <!-- Footer Start -->
                <jsp:include page="/WEB-INF/view/includes/footer.jsp"/>
                <!--         Footer End-->
            </div>
            <!-- Content End -->

            <!-- Back to Top -->
            <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
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
