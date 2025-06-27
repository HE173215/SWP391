<%-- 
    Document   : userlist
    Created on : Sep 20, 2024, 12:00:44 AM
    Author     : admin
--%>
<!DOCTYPE html>
<html lang="en">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html; charset=UTF-8" %>
    <head>
        <meta charset="UTF-8">
        <title>Review Councils</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <!--        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" integrity="sha384-k6RqeWeci5ZR/Lv4MR0sA0FfDOM7dPhN+53M1C7xOHe6M8D6k99r1c5ftz5I5G5" crossorigin="anonymous">-->

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
        <style>
            th a {
                color: inherit;
                text-decoration: none;
            }

            th a:hover {
                text-decoration: unset;
                cursor: pointer;
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

                <!-- Spinner Start -->
                <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center"></div>
                <!-- Spinner End -->

                <div class="container-fluid pt-4 px-4">
                    <!-- Title -->
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <h3 class="mb-0">Review Councils</h3>
                    </div>
                    <div class="bg-light text-center rounded mb-4">
                        <div class=" text-center rounded p-4 mb-4">

                            <!-- Filters, Search, and Add Button in the Same Row -->
                            <form action="council" method="get" class="mb-3">
                                <div class="row g-3 align-items-center">
                                    <!-- Classes Filter (Shorter Width) -->
                                    <div class="col-md-2">
                                        <div class="form-group">
                                            <select id="classesFilter" name="classesFilter" class="form-select" onchange="this.form.submit()">
                                                <option value="">All Classes</option>
                                                <c:forEach items="${classes}" var="c">
                                                    <option value="${c.classId}" ${c.classId == param.classesFilter ? 'selected' : ''}>
                                                        ${c.className}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <!-- Subject Filter (Shorter Width) -->
                                    <div class="col-md-2">
                                        <div class="form-group">
                                            <select id="subjectFilter" name="subjectFilter" class="form-select" onchange="this.form.submit()">
                                                <option value="">All Subjects</option>
                                                <c:forEach items="${subjects}" var="s">
                                                    <option value="${s.subjectCode}" ${s.subjectCode == param.subjectFilter ? 'selected' : ''}>
                                                        ${s.subjectCode}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>


                                    <!-- Status Filter (Shorter Width) -->
                                    <div class="col-md-2">
                                        <div class="form-group">
                                            <select id="statusFilter" name="statusFilter" class="form-select" onchange="this.form.submit()">
                                                <option value="">All Status</option>
                                                <option value="true" ${param.statusFilter == 'true' ? 'selected' : ''}>Active</option>
                                                <option value="false" ${param.statusFilter == 'false' ? 'selected' : ''}>Inactive</option>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- Search Bar (Takes more space) -->
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="searchQuery" placeholder="Enter keyword(s) to search" value="${param.searchQuery}">
                                            <button type="submit" class="btn btn-primary">Search</button>
                                        </div>
                                    </div>
                                    <c:if test="${currentUser.role == 'Subject Manager'}">
                                        <!-- Add Button (Short Width) -->
                                        <div class="col-md-2 d-flex justify-content-end">
                                            <button type="button" class="btn btn-link " style="font-size: 1.3em;" onclick="window.location.href = '${pageContext.request.contextPath}/council/add'">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </c:if>

                                </div> 
                            </form>
                        </div>
                    </div>
                </div>

                <!-- User List Table -->
                <div class="table-responsive mt-3" style="flex-grow: 1;
                     margin-left: 25px;
                     margin-right: 25px ">
                    <table class="table table-striped table-hover align-middle table-bordered mb-0">
                        <thead>
                            <tr class="text-dark">
                                <th scope="col">
                                    <a href="?sortBy=id&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        ID 
                                        <i class="fas ${param.sortBy == 'id' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">
                                    <a href="?sortBy=name&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Name 
                                        <i class="fas ${param.sortBy == 'name' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">
                                    <a href="?sortBy=class&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Class 
                                        <i class="fas ${param.sortBy == 'class' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">
                                    <a href="?sortBy=subject&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Subject 
                                        <i class="fas ${param.sortBy == 'subject' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">
                                    <a href="?sortBy=created_date&sortOrder=${param.sortBy == 'created_date' && param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Date 
                                        <i class="fas ${param.sortBy == 'created_date' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">
                                    <a href="?sortBy=status&sortOrder=${param.sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                                        Status 
                                        <i class="fas ${param.sortBy == 'status' ? (param.sortOrder == 'ASC' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                    </a>
                                </th>
                                <th scope="col">Action</th>
                            </tr>
                        </thead>


                        <tbody>
                            <c:forEach var="council" items="${councilsList}">
                                <tr>
                                    <td>${council.id}</td>
                                    <td>${council.name}</td>
                                    <td>${council.className}</td>
                                    <td>${council.subjectCode}</td>
                                    <td>${council.createdDate}</td>
                                    <td><c:out value="${council.status ? 'Active' : 'Inactive'}" /></td>
                                    <td>
<!--                                        <a href="${pageContext.request.contextPath}/council/details?id=${council.id}">
                                            <button type="button" class="btn btn-link"><i class="fas fa-solid fa-eye"></i></button>
                                        </a>
                                        -->
                                        <c:if test="${currentUser.role == 'Admin' || currentUser.role == 'Subject Manager'}">
                                            <button type="button" class="btn btn-link" onclick="openCouncilModal('${pageContext.request.contextPath}/council/details?id=${council.id}')">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </c:if>
                                        <c:if test="${currentUser.role == 'Subject Manager'}">
                                            | <a href="${pageContext.request.contextPath}/council/edit?id=${council.id}">
                                                <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                            </a> |

                                            <!-- Deactivate or Activate button -->
                                            <c:choose>
                                                <c:when test="${council.status}">
                                                    <button type="button" class="btn btn-link text-warning"
                                                            onclick="confirmAction('${pageContext.request.contextPath}/council/status?id=${council.id}&status=false', 'Deactivate')">
                                                        <i class="fas fa-ban"></i>
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-link text-success"
                                                            onclick="confirmAction('${pageContext.request.contextPath}/council/status?id=${council.id}&status=true', 'Activate')">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:if test="${empty councilsList}">
                    <p style="padding: 10px; margin-left: 30px">No Councils Found.</p>
                </c:if>
                <!-- Error and Success Alerts -->
                <c:if test="${not empty sessionScope.ErrorMessage}">
                    <script>
                        console.log("Error Message: ${sessionScope.ErrorMessage}");
                        Swal.fire({
                            icon: 'error',
                            text: '${sessionScope.ErrorMessage}',
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
                    <c:set var="ErrorMessage" value="" scope="session"/> 
                </c:if>

                <c:if test="${not empty sessionScope.Message}">
                    <script>
                        Swal.fire({
                            icon: 'success',
                            text: '${sessionScope.Message}',
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
                        ${sessionScope.Message = null}
                    </script>
                </c:if>
                <script>
                    function confirmAction(url, action) {
                        console.log('confirmAction called with URL:', url, 'and action:', action);
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
                <!-- Modal Structure -->
                <div class="modal fade" id="councilModal" tabindex="-1" aria-labelledby="councilModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="councilModalLabel">Council Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="modalContent">
                                <!-- Content will be loaded here dynamically -->
                            </div>
                        </div>
                    </div>
                </div>
                <script>
                    function openCouncilModal(url) {
                        // Load content via AJAX
                        fetch(url)
                                .then(response => response.text())
                                .then(html => {
                                    document.getElementById("modalContent").innerHTML = html;
                                    // Show the modal
                                    new bootstrap.Modal(document.getElementById('councilModal')).show();
                                })
                                .catch(error => console.error('Error loading modal content:', error));
                    }
                </script>


                <!-- Pagination -->
                <div class="d-flex justify-content-center mt-4">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="?page=${i}&typeFilter=${param.typeFilter}&searchQuery=${param.searchQuery}"
                           class="btn ${i == currentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1">${i}</a>
                    </c:forEach>
                </div>
            </div>
            <!-- Main Content End -->

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
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


            <!-- Template Javascript -->
            <script src="js/main.js"></script>
        </div>

    </body>
</html>
