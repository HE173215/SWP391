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

        <!-- Include SweetAlert CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>

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
                    <div class="container-fluid pt-4 px-4">
                        <div class="row g-4">
                            <p class="h3">Class Config</p>
                        </div>
                    </div>

                    <div class="container-fluid pt-4 px-4">
                        <div class="bg-light text-center rounded p-4 mb-4">
                            <!-- Combine Filter, Search, and Add Button in One Row -->
                            <div class="d-flex align-items-end mb-3">
                                <!-- Filter Form -->
                                <form id="filterForm" action="${pageContext.request.contextPath}/class/configs" method="get" class="d-flex align-items-end w-100">
                                    <!-- Dropdown Subject -->
                                    <div class="form-group me-2">
                                        <label for="subjectSelect" class="form-label">Subject</label>
                                        <select id="subjectSelect" class="form-select" name="subjectID" onchange="this.form.submit()">

                                            <c:forEach var="subject" items="${subjectList}">
                                                <option value="${subject.id}" <c:if test="${subject.id == selectedSubjectID}">selected</c:if>>
                                                    ${subject.code}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Dropdown Class -->
                                    <div class="form-group me-2">
                                        <label for="classSelect" class="form-label">Class</label>
                                        <select id="classSelect" class="form-select" name="classID" onchange="this.form.submit()">

                                            <c:forEach var="c" items="${classList}">
                                                <option value="${c.id}" <c:if test="${c.id == selectedClassID}">selected</c:if>>
                                                    ${c.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>




                    <div class="container-fluid pt-4 px-4">
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="milestones-tab" data-bs-toggle="tab" data-bs-target="#milestones" type="button" role="tab">Milestone</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="evalcriteria-tab" data-bs-toggle="tab" data-bs-target="#evalcriteria" type="button" role="tab">Eval Criteria</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="members-tab" data-bs-toggle="tab" data-bs-target="#members" type="button" role="tab">Members</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="teams-tab" data-bs-toggle="tab" data-bs-target="#teams" type="button" role="tab">Team</button>
                            </li>
                        </ul>
                        <!-- Combine Filter, Search, and Add Button in One Row -->
                        <div class=" align-items-center justify-content-between mb-3">

                            <c:if test="${not empty classList}"> 
                                <!-- Tab Content -->
                                <div class="tab-content" id="myTabContent">

                                    <!--Milestone Content-->
                                    <jsp:include page="/WEB-INF/view/milestone/list.jsp" />

                                    <!-- Additional tabs for Eval Criteria, Members, and Teams 
                                    <!--Eval Criteria - Thái -->
                                    <!--<div class="tab-pane fade" id="evalcriteria" role="tabpanel">Eval Criteria Content</div>-->

                                    <!--Members - Thái -->
<!--                                    <div class="tab-pane fade" id="members" role="tabpanel">Members Content</div>-->

                                    <!--Team - Mạnh -->
                                <div class="tab-pane fade" id="teams" role="tabpanel">
                                    <!-- Add Team button aligned to the top right with proper spacing -->
                                    <div class="d-flex justify-content-between align-items-center mb-4">
                                        <h4 class="mb-0">Teams</h4>
                                        <a href="${pageContext.request.contextPath}/teams/add" class="btn btn-primary">Add Team</a>
                                    </div>

                                    <!-- Search and Filter form -->
                                    <form action="${pageContext.request.contextPath}/class/configs" method="get" class="mb-4">
                                        <div class="input-group">
                                            <!-- Ô tìm kiếm với tên team hoặc chủ đề -->
                                            <input type="text" name="search" value="${param.search}" class="form-control" placeholder="Search by team name or topic">

                                            <!-- Trường ẩn để giữ giá trị classID và activeTab -->
                                            <input type="hidden" name="classID" value="${selectedClassID}">
                                            <input type="hidden" name="activeTab" value="teams">

                                            <!-- Nút tìm kiếm có thêm icon -->
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i> <!-- Icon tìm kiếm -->
                                                Search
                                            </button>
                                        </div>
                                    </form>

                                    <!-- Notification area for error and success messages -->
                                    <div class="alert-container">
                                        <!-- Error Message -->
                                        <c:if test="${not empty errorMessage}">
                                            <div class="alert alert-danger" role="alert" id="errorAlert">
                                                <strong>Error:</strong> ${errorMessage}
                                            </div>
                                            <!-- Reset the errorMessage after displaying -->
                                            <c:set var="errorMessage" value="" scope="session"/>
                                        </c:if>

                                        <!-- Success Message -->
                                        <c:if test="${not empty successMessage}">
                                            <div class="alert alert-success" role="alert" id="successAlert">
                                                <strong>Success:</strong> ${successMessage}
                                            </div>
                                            <!-- Reset the successMessage after displaying -->
                                            <c:set var="successMessage" value="" scope="session"/>
                                        </c:if>
                                    </div>

                                    <!-- Teams Table -->
                                    <table class="table table-bordered mt-3">
                                        <thead>
                                            <tr>
                                                <th scope="col">ID</th>
                                                <th scope="col">Name</th>
                                                <th scope="col">Topic</th>
                                                <th scope="col">Class Name</th>
                                                <th scope="col">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test="${not empty teams}">
                                                <c:forEach var="team" items="${teams}">
                                                    <tr>
                                                        <td>${team.id}</td>
                                                        <td>${team.name}</td>
                                                        <td>${team.topic}</td>
                                                        <td>${team.className}</td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/teams/details?teamId=${team.id}">View Details</a> | 
                                                            <a href="${pageContext.request.contextPath}/teams/delete?teamId=${team.id}" onclick="return confirm('Are you sure you want to delete this team?')">Delete</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${empty teams}">
                                                <tr>
                                                    <td colspan="5">No teams available.</td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </c:if>
                            <c:if test="${empty classList}">
                                <p>${requestScope.emptyClassListMess}</p>
                            </c:if>
                        </div>
                    </div>
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
