<%-- 
    Document   : configs
    Created on : Oct 9, 2024, 7:04:37 PM
    Author     : Do Duan
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">

    <head>
        <meta charset="utf-8">
        <title>Subject Config</title>
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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">        

        <!-- Include SweetAlert CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>

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

                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <p class="h3">Subject Config</p>
                    </div>
                </div>


                <div class="container-fluid pt-4 px-4">
                    <div class="bg-light text-center rounded p-4 mb-4">
                        <!-- Combine Filter, Search, and Add Button in One Row -->
                        <div class="d-flex align-items-end mb-3">

                        </div>
                    </div>
                </div>
                <div class="container-fluid pt-4 px-4">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${session.activeTab == 'subjectsetting' ? 'active' : ''}" 
                                    id="subjectsetting-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#subjectsetting" 
                                    type="button" 
                                    role="tab">Subject Setting</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${session.activeTab == 'SubjectUser' ? 'active' : ''}" 
                                    id="subjectuser-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#subjectuser" 
                                    type="button" 
                                    role="tab">Subject User</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${session.activeTab == 'EvalAssignment' ? 'active' : ''}" 
                                    id="evalassignment-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#evalassignment" 
                                    type="button" 
                                    role="tab">Eval Assignment</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${session.activeTab == 'evalcriteria' ? 'active' : ''}" 
                                    id="evalcriteria-tab" 
                                    data-bs-toggle="tab" 
                                    data-bs-target="#evalcriteria" 
                                    type="button" 
                                    role="tab">Eval Criteria</button>
                        </li>
                    </ul>
                </div>
                <!-- Combine Filter, Search, and Add Button in One Row -->
                <div class=" align-items-center justify-content-between mb-3">


                    <!-- Tab Content -->
                    <div class="tab-content" id="myTabContent">

                        <!--SubjectSetting Content-->
                        <div class="tab-pane fade ${sessionScope.activeTab == 'subjectsetting' ? 'show active' : ''}" id="subjectsetting" role="tabpanel">
                            <!-- Header and Add New Button aligned on the same row -->
                            <div class="d-flex justify-content-between align-items-center mb-4" style="margin-left: 0.8cm; padding: 10px;">
                                <h4 class="mb-0">Subject Settings</h4>
                                <a href="${pageContext.request.contextPath}/subject/add" class="btn btn-primary btn-sm" >
                                    <i class="fas fa-plus"></i> Add New
                                </a>
                            </div>

                            <!-- Filter, Status, and Search form all in one row -->
                            <form method="GET" action="${pageContext.request.contextPath}/subject" class="d-flex align-items-center mb-3" style="width: 99%; gap: 1rem;">
                                <input type="hidden" id="activeTab" name="activeTab" value="${param.tab}" />
                                <input type="hidden" id="groupId" name="id" value="${param.id}" />

                                <!-- Filter by Type -->
                                <div class="form-group" style="margin-left: 0.8cm;">
                                    <label for="typeFilter" class="form-label me-1"></label>
                                    <select id="typeFilter" name="typeFilter" class="form-select form-select-sm" onchange="this.form.submit()">
                                        <option value="">All Types</option>
                                        <c:forEach items="${types}" var="type">
                                            <option value="${type}" ${type == param.typeFilter ? 'selected' : ''}>${type}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- Filter by Status -->
                                <div class="form-group" style="flex: 0 0 auto;">
                                    <label for="statusFilter" class="form-label me-1"></label>
                                    <select id="statusFilter" name="statusFilter" class="form-select form-select-sm" onchange="this.form.submit()">
                                        <option value="">All Status</option>
                                        <option value="true" ${param.statusFilter == 'true' ? 'selected' : ''}>Active</option>
                                        <option value="false" ${param.statusFilter == 'false' ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </div>

                                <!-- Search Input -->
                                <div class="form-group">
                                    <label for="searchQuery" class="form-label me-1"></label>
                                    <div class="input-group input-group-sm">
                                        <input type="text" class="form-control" id="searchQuery" name="searchQuery" placeholder="What do you need?" value="${param.searchQuery}">
                                        <button type="submit" class="btn btn-primary">SEARCH</button>
                                    </div>
                                </div>
                            </form>

                            <c:if test="${not empty subjectSettingList}"> 
                                <div class="table-responsive mt-3" style="margin-left: 30px; margin-right: 30px">
                                    <table  class="table table-striped table-hover align-middle table-bordered mb-0">
                                        <thead>
                                            <tr>
                                                <th scope="col" style="width: 5%;">ID</th>
                                                <th scope="col" style="width: 20%;">Setting</th>
                                                <th scope="col" style="width: 15%;">Type</th>
                                                <th scope="col" style="width: 10%;">Value</th>
                                                <th scope="col" style="width: 10%;">Status</th>
                                                <th scope="col" style="width: 5%;">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="subject" items="${subjectSettingList}">
                                                <tr>
                                                    <td>${subject.id}</td>
                                                    <td>${subject.name}</td>
                                                    <td>${subject.type}</td>
                                                    <td>${subject.value}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${subject.status == false}">Inactive</c:when>
                                                            <c:when test="${subject.status == true}">Active</c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/subject/edit?id=${subject.id}" class="btn btn-link btn-sm">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        <c:choose>
                                                            <c:when test="${subject.status == true}">
                                                                <form action="${pageContext.request.contextPath}/subject/status" method="post" style="display:inline;">
                                                                    <input type="hidden" name="idStatus" value="${subject.id}" />
                                                                    <input type="hidden" id="groupId" name="id" value="${param.id}" />
                                                                    <input type="hidden" name="newStatus" value="0" />
                                                                    <button type="submit" class="btn btn-link text-warning btn-sm" title="Deactivate">
                                                                        <i class="fas fa-ban"></i>
                                                                    </button>
                                                                </form>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <form action="${pageContext.request.contextPath}/subject/status" method="post" style="display:inline;">
                                                                    <input type="hidden" name="idStatus" value="${subject.id}" />
                                                                    <input type="hidden" id="groupId" name="id" value="${param.id}" />
                                                                    <input type="hidden" name="newStatus" value="1" />
                                                                    <button type="submit" class="btn btn-link text-success btn-sm" title="Activate">
                                                                        <i class="fas fa-check"></i>
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
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
                                        </tbody>
                                    </table>
                                </div>
                                <!-- Pagination for Subject Setting Tab -->
                                <div class="d-flex justify-content-center mt-3">
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <a href="${pageContext.request.contextPath}/subject?id=${subjectId}&page=${i}&activeTab=subjectsetting"
                                           class="btn ${i == currentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1">${i}</a>
                                    </c:forEach>
                                </div>
                            </c:if>
                            <c:if test="${empty subjectSettingList}">
                                <p>No subject settings available.</p>
                            </c:if>
                        </div>
                        <!--Subject User -->
                        <div class="tab-pane fade" id="subjectuser" role="tabpanel">Eval Criteria Content</div>

                        <!--Eval Asignment -->
                        <jsp:include page="/WEB-INF/view/assignment/list.jsp" />


                        <!--Eval Criteria -->
                        <div class="tab-pane fade ${sessionScope.activeTab == 'evalcriteria' ? 'show active' : ''}" id="evalcriteria" role="tabpanel">
                            <div class="d-flex justify-content-between align-items-center mb-4" style="margin-left: 0.8cm; padding: 10px;">
                                <h4 class="mb-0">Eval Criteria</h4>
                                <a href="${pageContext.request.contextPath}/evalcriteria/add" class="btn btn-primary btn-sm">
                                    <i class="fas fa-plus"></i> Add New
                                </a>
                            </div>

                            <!-- Filter Form -->
                            <form method="GET" action="${pageContext.request.contextPath}/subject" style="display: inline-block; margin-right: 1rem;">
                                <input type="hidden" id="activeTab" name="activeTab" value="${sessionScope.activeTab}" />
                                <input type="hidden" id="groupId" name="id" value="${param.id}" />

                                <!-- Filter by Status -->
                                <div class="form-group">
                                    <label for="statusFilter" class="form-label me-1"></label>
                                    <select id="statusFilter" name="statusFilterCriteria" class="form-select form-select-sm" onchange="this.form.submit()">
                                        <option value="">All Status</option>
                                        <option value="true" ${param.statusFilterCriteria == 'true' ? 'selected' : ''}>Active</option>
                                        <option value="false" ${param.statusFilterCriteria == 'false' ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </div>
                            </form>

                            <!-- Search Form -->
                            <form method="GET" action="${pageContext.request.contextPath}/subject" style="display: inline-block;">
                                <input type="hidden" id="activeTab" name="activeTab" value="${sessionScope.activeTab}" />
                                <input type="hidden" id="groupId" name="id" value="${param.id}" />

                                <!-- Search Input -->
                                <div class="form-group">
                                    <label for="searchQuery" class="form-label me-1"></label>
                                    <div class="input-group input-group-sm">
                                        <input type="text" class="form-control" id="searchQuery" name="searchName" placeholder="Search by Name" value="${param.searchName}">
                                        <button type="submit" class="btn btn-primary">SEARCH</button>
                                    </div>
                                </div>
                            </form>


                            <!-- Hidden field to store active tab -->
                            <input type="hidden" id="activeTab" name="activeTab" value="${sessionScope.activeTab}" />
                            <input type="hidden" id="groupId" name="id" value="${param.id}" />
                            <c:if test="${not empty criteriaList}">
                                <div class="table-responsive mt-3" style="margin-left: 30px; margin-right: 30px">
                                    <table class="table table-striped table-hover align-middle table-bordered mb-0">
                                        <thead>
                                            <tr>
                                                <th scope="col" style="width: 5%;">Id</th>
                                                <th scope="col" style="width: 20%">Name</th>
                                                <th scope="col" style="width: 10%;">Weight</th>
                                                <th scope="col" style="width: 10%;">Assignment Name</th>
                                                <th scope="col" style="width: 5%;">Status</th>
                                                <th scope="col" style="width: 5%;">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="criteria" items="${criteriaList}">
                                                <tr>
                                                    <td>${criteria.id}</td>
                                                    <td>${criteria.name}</td>
                                                    <td>${criteria.weight}</td>
                                                    <td>${criteria.assignmentName}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${criteria.status == false}">Inactive</c:when>
                                                            <c:when test="${criteria.status == true}">Active</c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/evalcriteria/edit?id=${criteria.id}&activeTab=${sessionScope.activeTab}"class="btn btn-link">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        <c:choose>
                                                            <c:when test="${criteria.status == true}">
                                                                <form action="${pageContext.request.contextPath}/evalcriteria/status" method="post" style="display:inline;">
                                                                    <input type="hidden" name="idStatus" value="${criteria.id}" />
                                                                    <input type="hidden" id="groupId" name="id" value="${param.id}" />
                                                                    <input type="hidden" name="activeTab" value="${sessionScope.activeTab}">
                                                                    <input type="hidden" name="newStatus" value="0" />

                                                                    <button type="submit" class="btn btn-link text-warning btn-sm" title="Deactivate">
                                                                        <i class="fas fa-ban"></i>
                                                                    </button>
                                                                </form>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <form action="${pageContext.request.contextPath}/evalcriteria/status" method="post" style="display:inline;">
                                                                    <input type="hidden" name="idStatus" value="${criteria.id}" />
                                                                    <input type="hidden" id="groupId" name="id" value="${param.id}" />
                                                                    <input type="hidden" name="newStatus" value="1" />
                                                                    <button type="submit" class="btn btn-link text-success btn-sm" title="Activate">
                                                                        <i class="fas fa-check"></i>
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
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
                                        </tbody>
                                    </table>
                                </c:if>
                                <!-- Phân trang cho EvalCriteria -->
                                <div class="d-flex justify-content-center mt-3">
                                    <c:forEach var="i" begin="1" end="${criteriaTotalPages}">
                                        <a href="${pageContext.request.contextPath}/subject?id=${subjectId}&criteriaPage=${i}&activeTab=evalcriteria"
                                           class="btn ${i == criteriaCurrentPage ? 'btn-primary' : 'btn-light'} btn-sm me-1">${i}</a>
                                    </c:forEach>
                                </div>
                                <c:if test="${empty criteriaList}">
                                    <p>No C available.</p>
                                </c:if>
                            </div>
                            <!--Body End-->

                            <!-- Footer Start -->
                            <jsp:include page="/WEB-INF/view/includes/footer.jsp" />
                            <!--         Footer End-->
                        </div>
                        <!-- Content End -->

                        <!-- Back to Top -->
                        <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
                    </div>

                    <script>
                        function openTab(evt, tabName) {
                            var i, tabcontent, tablinks;
                            tabcontent = document.getElementsByClassName("tabcontent");
                            for (i = 0; i < tabcontent.length; i++) {
                                tabcontent[i].style.display = "none";
                            }
                            tablinks = document.getElementsByClassName("tablinks");
                            for (i = 0; i < tablinks.length; i++) {
                                tablinks[i].className = tablinks[i].className.replace(" active", "");
                            }
                            document.getElementById(tabName).style.display = "block";
                            evt.currentTarget.className += " active";
                        }

                        // Open the default tab (Milestones)
                        document.getElementById("defaultOpen").click();
                    </script>

                    <script>
                        // Store the active tab when it's clicked
                        document.querySelectorAll('.nav-link').forEach(tab => {
                            tab.addEventListener('click', function () {
                                document.getElementById('activeTab').value = this.getAttribute('data-bs-target').substring(1); // Get the tab ID and set it in the hidden input
                            });
                        });

                        // When the page loads, set the correct tab as active based on the value of the hidden field
                        window.onload = function () {
                            const activeTab = "${activeTab}";
                            if (activeTab) {
                                const activeTabButton = document.querySelector(`[data-bs-target="#${activeTab}"]`);
                                if (activeTabButton) {
                                    activeTabButton.click(); // Trigger a click to set the correct tab as active
                                }
                            }
                        };
                    </script>



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
                    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                    <!-- Template Javascript -->
                    <script src="${pageContext.request.contextPath}/js/main.js"></script>
                    </body>

                    </html>
