<%-- 
    Document   : list
    Created on : Oct 7, 2024, 11:38:06 PM
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

        <!-- SweetAlert2 CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <!-- SweetAlert2 JS -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <!-- Include Toastify CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
        <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

        <!-- Include SweetAlert CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
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

                <!--Body Start-->

                <!--  Class List -->
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-12 col-xl-10">
                            <p class="h3">Class List</p>
                        </div>
                    </div>
                </div>

                <!--Filter Start-->
                <div class="container-fluid pt-4 px-4">
                    <!-- Form filter -->
                    <form action="${pageContext.request.contextPath}/class" method="get">
                        <div class="row g-4">
                            <!--Semester-->
                            <div class="col-sm-12 col-xl-2">
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
                            <div class="col-sm-12 col-xl-2">
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

                            <!--Status-->
                            <div class="col-sm-12 col-xl-2">
                                <select class="form-select mb-3" aria-label="Default select example" name="status">
                                    <option value="-1">All Status</option>
                                    <option value="1" ${requestScope.selectedStatus == '1' ? 'selected' : ''}>Active</option>
                                    <option value="0" ${requestScope.selectedStatus == '0' ? 'selected' : ''}>Inactive</option>
                                </select>
                            </div>

                            <!--Search input-->
                            <div class="col-sm-12 col-xl-2">
                                <input class="form-control border-1" name="searchString" value="${param.searchString}" type="search" placeholder="Search class name">
                            </div>
                            <!--Search button-->
                            <div class="col-sm-12 col-xl-3">
                                <button type="submit" class="btn btn-primary mb-3">Filter</button>
                            </div>

                            <!--Add buuton-->
                            <c:if test="${sessionScope.currentUser.roleID == 5}">

                                <div class="col-sm-12 col-xl-1">
                                    <button type="button" class="btn btn-success " onclick="window.location.href = '${pageContext.request.contextPath}/class/detail?action=New'">Add</button>
                                </div>
                            </c:if>

                        </div>
                    </form>
                </div>
                <!--Filter End-->



                <div class="table-responsive" style="flex-grow: 1; margin-left: 25px;margin-right: 25px ">
                    <table class="table text-start align-middle table-bordered table-hover mb-0">
                        <thead>

                            <tr class="text-dark">
                                <th scope="col" style="width: 10%;">
                                    <a href="?sortBy=id&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        ID 
                                        <i class="fas ${param.sortBy == 'id' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col" style="width: 15%;">
                                    <a href="?sortBy=name&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Class Name
                                        <i class="fas ${param.sortBy == 'name' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>

                                </th>
                                <th scope="col" style="width: 35%;">
                                    <a href="?sortBy=code&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Subject
                                        <i class="fas ${param.sortBy == 'code' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>

                                </th>
                                <th scope="col" style="width: 15%;">
                                    <a href="?sortBy=setting_name&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Semester
                                        <i class="fas ${param.sortBy == 'setting_name' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>

                                </th>
                                <th scope="col" style="width: 10%;">
                                    <a href="?sortBy=status&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Status
                                        <i class="fas ${param.sortBy == 'status' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>

                                </th>
                                <c:if test="${sessionScope.currentUser.roleID == 2 || sessionScope.currentUser.roleID == 5}">
                                    <th scope="col" style="width: 15%;">Action</th>
                                    </c:if>

                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty classList}">
                                <c:forEach var="c" items="${classList}">
                                    <tr>
                                        <td>${c.id}</td>
                                        <td>${c.name}</td>
                                        <td>${c.subjectCode} - ${c.subjectName}</td>
                                        <td>${c.semester}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${c.status == 1}">Active</c:when>
                                                <c:otherwise>Inactive</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <c:if test="${sessionScope.currentUser.roleID == 2 || sessionScope.currentUser.roleID == 5}">                                                                                    
                                            <td>
                                                <!--Edit-->
                                                <c:if test="${ sessionScope.currentUser.roleID ==5}">


                                                    <a href="${pageContext.request.contextPath}/class/detail?action=Edit&id=${c.id}">
                                                        <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                                    </a> |

                                                    <!--Change Status-->
                                                    <c:choose>
                                                        <c:when test="${c.status == 1}">

                                                            <button type="button" class="btn btn-link text-warning"
                                                                    onclick="confirmAction('${pageContext.request.contextPath}/class/changeStatus?id=${c.id}&newStatus=0', 'deactivate')">
                                                                <i class="fas fa-user-slash"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>

                                                            <button type="button" class="btn btn-link text-success"
                                                                    onclick="confirmAction('${pageContext.request.contextPath}/class/changeStatus?id=${c.id}&newStatus=1', 'activate')">
                                                                <i class="fas fa-check"></i>
                                                            </button>   
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>

                                                <c:if test="${sessionScope.currentUser.roleID == 2}">
                                                    <!--Config-->
                                                    <a href="${pageContext.request.contextPath}/class/configs?classID=${c.id}">
                                                        <button type="button" class="btn btn-secondary btn-sm"><i class="fas fa-wrench"></i></button>
                                                    </a>
                                                </c:if>



                                                <script>
                                                    function confirmAction(url, action) {
                                                        Swal.fire({
                                                            title: 'Are you sure?',
                                                            text: 'Do you want to ' + action + ' this user?',
                                                            icon: 'question',
                                                            showCancelButton: true,
                                                            confirmButtonColor: '#3085d6',
                                                            cancelButtonColor: '#d33',
                                                            confirmButtonText: 'Yes, ' + action + ' it!',
                                                            cancelButtonText: 'No'
                                                        }).then((result) => {
                                                            if (result.isConfirmed) {
                                                                window.location.href = url;
                                                            }
                                                        });
                                                    }

                                                </script>
                                                <script>
                                                    <c:if test="${not empty requestScope.Mess}">
                                                    Swal.fire({
                                                        icon: 'success',
                                                        title: 'Success',
                                                        text: '${requestScope.Mess}',
                                                        toast: true,
                                                        position: 'top-right',
                                                        showConfirmButton: false,
                                                        showCloseButton: true,
                                                        timer: 5000,
                                                        timerProgressBar: true,
                                                        didOpen: (toast) => {
                                                            toast.addEventListener('mouseenter', Swal.stopTimer);
                                                            toast.addEventListener('mouseleave', Swal.resumeTimer);
                                                        }
                                                    });
                                                        ${requestScope.Mess == null}
                                                    </c:if>
                                                </script>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>

                    <!--Paging Start-->
                    <div class="d-flex justify-content-center mt-4">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="${pageContext.request.contextPath}/class?page=${i}"  class="btn ${i == currentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1"">${i}</a>
                        </c:forEach>
                    </div>
                    
                    <!--Paging End-->
                    
                    <c:if test="${empty classList}">
                        <p>No classes found.</p>
                    </c:if>

                    <!-- Error and Success Alerts -->
                    <c:if test="${not empty sessionScope.messFail}">
                        <script>
                            console.log("Error Message: ${sessionScope.messFail}");
                            Swal.fire({
                                icon: 'error',
                                text: '${sessionScope.messFail}',
                                toast: true,
                                position: 'top-right',
                                showConfirmButton: false,
                                showCloseButton: true,
                                timer: 5000,
                                timerProgressBar: true,
                                customClass: {
                                    popup: 'swal-toast-position-below-navbar swal-error'
                                },
                                didOpen: (toast) => {
                                    toast.addEventListener('mouseenter', Swal.stopTimer);
                                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                                }
                            });
                        </script>
                        <c:set var="messFail" value="" scope="session"/> 
                    </c:if>

                    <c:if test="${not empty sessionScope.messSucces}">
                        <script>
                            Swal.fire({
                                icon: 'success',
                                text: '${sessionScope.messSucces}',
                                toast: true,
                                position: 'top-right',
                                showConfirmButton: false,
                                showCloseButton: true,
                                timer: 5000,
                                timerProgressBar: true,
                                customClass: {
                                    popup: 'swal-toast-position-below-navbar swal-success'
                                },
                                didOpen: (toast) => {
                                    toast.addEventListener('mouseenter', Swal.stopTimer);
                                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                                }
                            });
                            ${sessionScope.messSucces = null}
                        </script>
                    </c:if>
                </div>
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
        <script src="${pageContext.request.contextPath}/lib/chart/chart.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/moment.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/moment-timezone.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

        <!-- Template Javascript -->
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>

</html>
