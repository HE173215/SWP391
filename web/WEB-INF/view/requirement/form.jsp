<%-- 
    Document   : form
    Created on : Nov 2, 2024, 8:17:49 PM
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
                    <div class="col-lg-2 ">

                    </div>
                    <div class="card shadow-sm">

                        <div class="card-header">
                            <h3>${requestScope.pageTitle}</h3>
                            <a href="${pageContext.request.contextPath}/dashboard/detail?id=${sessionScope.classID}&tab=${requestScope.selectedTab}" class="btn btn-primary px-4" role="button">Back </a>
                        </div>
                        <div class="card-body">
                            <c:set var="c" value="${requestScope.currentClass}" />
                            <form action="${pageContext.request.contextPath}/requirement/save" method="get">

                                <input type="hidden" name="action" value="${action != 'Add' ? 'Edit' : 'Add'}" />

                                <input type="hidden" name="id" value="${requirement != null ? requirement.id : ''}" />

                                <input type="hidden" name="tab" value=${requestScope.selectedTab} />

                                <input type="hidden" name="currentOwner" value=${requestScope.requirement.ownerID} />


                                <!-- Name Field -->
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="name">Title*</label>
                                        <input type="text" class="form-control" id="name" name="tittle" value="${requirement.tittle}" required>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label for="type">Complexity</label>
                                        <select class="form-control" id="subject" name="complexityF">

                                            <c:forEach var="complexity" items="${requestScope.complexityList}">
                                                <option value="${complexity.id}" 
                                                        <c:if test="${complexity.name == requirement.complexity}">selected</c:if> >
                                                    ${complexity.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <input type="hidden" name="teamID" value="${requestScope.teamID}" />

                                <!-- Priority and Status Fields-->
                                <div class="form-row">

                                    <div class="form-group col-md-6">
                                        <label for="type">Owner</label>
                                        <select class="form-control" id="semester" name="ownerF" >
                                            <!--<option value="" >None</option>-->
                                            <c:forEach var="member" items="${teamMember}">
                                                <option value="${member.id}" 
                                                        <c:if test="${member.id == requirement.ownerID}">selected</c:if> >
                                                    ${member.userName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label for="type">Milestone</label>
                                        <select class="form-control" id="milestone" name="milestoneF" >
                                            <c:forEach var="m" items="${milestoneList}">
                                                <option value="${m.id}" 
                                                        <c:if test="${m.id == requirement.milestoneID}">selected</c:if> >
                                                    ${m.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label>Status</label><br>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="todo" name="statusF" value="0" ${requirement.status == 0 ? 'checked' : ''}>
                                            <label class="form-check-label" for="todo">To do</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="doing" name="statusF" value="1" ${requirement.status == 1 ? 'checked' : ''}>                             
                                            <label class="form-check-label" for="doing">Doing</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="done" name="statusF" value="2" ${requirement.status == 2 ? 'checked' : ''}>                             
                                            <label class="form-check-label" for="done">Done</label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Description Field -->
                                <div class="form-group">
                                    <label for="detail">Description</label>
                                    <textarea class="form-control" id="detail" name="detail" rows="3">${requirement.description}</textarea>                                   
                                </div>

                                <div class="form-group" style="color: red">
                                    ${requestScope.errMess}
                                </div>

                                <div class="form-group" style="color: green">
                                    ${requestScope.editSucces}
                                </div>

                                <div class="form-group" style="color: red">
                                    ${requestScope.editFail}
                                </div>

                                <!-- Submit Button -->
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </form>
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

        <!-- Template Javascript -->
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>

</html>
