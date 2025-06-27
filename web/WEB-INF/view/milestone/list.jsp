<%-- 
    Document   : list
    Created on : Oct 21, 2024, 2:40:04 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <div class="tab-pane fade show active" id="milestones" role="tabpanel">
        <c:if test="${not empty milestoneList}"> 
            <table class="table table-bordered mt-3">
                <thead>
                    <tr>
                        <th scope="col">Id</th>
                        <th scope="col">Milestone</th>
                        <th scope="col">Assignment</th>
                        <th scope="col">Code</th>
                        <th scope="col">Priority</th>
                        <th scope="col">End date</th>
                        <th scope="col">Status</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${milestoneList}">
                        <tr>
                            <td>${m.id}</td>
                            <td>${m.name}</td>
                            <td>${m.assigment}</td>
                            <td>${m.code}</td>
                            <td>${m.priority}</td>
                            <td>${m.endDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${m.status == 0}">Closed</c:when>
                                    <c:when test="${m.status == 1}">In progress</c:when>
                                    <c:otherwise>Pending</c:otherwise>
                                </c:choose>
                            </td>

                            <td><a href="${pageContext.request.contextPath}/milestone/view?id=${m.id}"> 
                                <button type="button" class="btn btn-link"><i class="fas fa-eye"></i></button>
                                </a> | 
                                <a href="${pageContext.request.contextPath}/milestone/edit?id=${m.id}">
                                    <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                </a> 
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty milestoneList}">
            <p>No milestones found.</p>
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
    </div>

</html>
