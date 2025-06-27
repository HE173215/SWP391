<%-- 
    Document   : detail
    Created on : Nov 2, 2024, 7:59:52 PM
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

                <div class="container-fluid pt-4 px-4">

                    <div class="container-fluid pt-4 px-4">
                        <div class="row g-4">
                            <p class="h3">Class : ${requestScope.className} - ${requestScope.subject}</p>
                        </div>
                    </div>

                    <!-- Error and Success Alerts -->
                    <c:if test="${requestScope.Message != null}">
                        <script>
                        console.log("Message: ${requestScope.Message}");
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: '${requestScope.Message}',
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
                        </script>
                        <c:set var="Message" value="" scope="request"/>
                    </c:if>
                        
                    <ul class="nav nav-tabs" id="myTab" role="tablist">

                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${requestScope.selectedTab == 'team' ? 'active' : ''}" id="list-tab" data-bs-toggle="tab" data-bs-target="#list" type="button" role="tab">Team's Requirements</button>
                        </li>

                        <li class="nav-item" role="presentation">
                            <button class="nav-link ${requestScope.selectedTab == 'personal' ? 'active' : ''}" id="personal-tab" data-bs-toggle="tab" data-bs-target="#personal" type="button" role="tab">My Requirements</button>
                        </li>                                         
                    </ul>
                    <!-- Combine Filter, Search, and Add Button in One Row -->
                    <div class=" align-items-center justify-content-between mb-3">

                        <!-- Tab Content -->
                        <div class="tab-content" id="myTabContent">

                            <!--My Requirement Content-->

                            <div class="tab-pane fade ${requestScope.selectedTab == 'team' ? 'show active' : ''}" id="list" role="tabpanel">
                                <jsp:include page="/WEB-INF/view/requirement/list.jsp" />
                            </div>

                            <!-- My Requirements -->
                            <div class="tab-pane fade ${requestScope.selectedTab == 'personal' ? 'show active' : ''}" id="personal" role="tabpanel">
                                <jsp:include page="/WEB-INF/view/requirement/personal.jsp" />
                            </div>            

                        </div>



                    </div>
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


