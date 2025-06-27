<%-- 
    Document   : list
    Created on : Nov 2, 2024, 8:17:44 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

    <div class="tab-pane fade show " id="list" role="tabpanel">
        <form action="${pageContext.request.contextPath}/dashboard/detail" method="get" id="paginationForm">

            <!--Filter Start-->
            <div class="container-fluid pt-4 px-4">
                <!-- Form filter -->

                <input type="hidden" name="id" value="${sessionScope.classID}" />
                <input type="hidden" name="tab" value='team' />

                <div class="row g-4">
                    <!--Complexity-->
                    <div class="col-sm-12 col-xl-2">
                        <select class="form-select mb-3" aria-label="Default select example" name="complexity" >
                            <option value="0">Team : All</option>
                            <c:forEach var="team" items="${teams}">
                                <option value="${team.id}" 
                                        <c:if test="${team.id == teamIdFilter}">selected</c:if> >
                                    ${team.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <!--Subject-->
                    <div class="col-sm-12 col-xl-2">
                        <select class="form-select mb-3" aria-label="Default select example" name="owner">
                            <option value="0">All milestone</option>
                            <c:forEach var="m" items="${milestones}">
                                <option value="${m.id}" 
                                        <c:if test="${m.id == milestone}">selected</c:if> >
                                    ${m.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!--Status-->
                    <div class="col-sm-12 col-xl-2">
                        <select class="form-select mb-3" aria-label="Default select example" name="status">
                            <option value="-1">All Status</option>
                            <option value="0" ${requestScope.status_s == '0' ? 'selected' : ''}>To do</option>

                            <option value="1" ${requestScope.status_s == '1' ? 'selected' : ''}>Doing</option>
                            <option value="2" ${requestScope.status_s == '2' ? 'selected' : ''}>Done</option>

                        </select>
                    </div>

                    <!--Search input-->
                    <div class="col-sm-12 col-xl-2">
                        <input class="form-control border-1" name="searchString" value="${requestScope.searchStr_s}" type="search" placeholder="Search title">
                    </div>
                    <!--Search button-->
                    <div class="col-sm-12 col-xl-3">
                        <button type="submit" class="btn btn-primary mb-3">Filter</button>
                    </div>

                    <c:if test="${requestScope.teamRole == 9}">
                        <div class="col-sm-12 col-xl-1">
                            <button type="button" class="btn btn-success " onclick="window.location.href = '${pageContext.request.contextPath}/requirement?action=Add&teamID=${requestScope.teamID}&tab=team'">Add</button>
                        </div>
                    </c:if>

                </div>
            </div>
            <!--Filter End-->

            <div class="table-responsive" style="flex-grow: 1; margin-left: 25px;margin-right: 25px ">
                <table class="table table-bordered mt-3">
                    <thead>
                        <tr>
                            <!--<th scope="col" style="width: 5%;">#</th>-->
                            <th scope="col" style="width: 20%;">Title</th>
                            <th scope="col" style="width: 10%;">Complexity</th>
                            <th scope="col" style="width: 10%;">Value</th>
                            <th scope="col" style="width: 15%;">Owner</th>
                            <th scope="col" style="width: 10%;">Milestone</th>
                            <th scope="col" style="width: 15%;">Deadline</th>


                            <th scope="col" style="width: 10%;">Status</th>
                            <th scope="col" style="width: 10%;">Action</th>

                            <!--                            <th scope="col" style="width: 20%;">Actions</th>-->
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${teamRequirements}">
                            <tr>
                                <!--<td>${r.id}</td>-->
                                <td>${r.tittle}</td>
                                <td>${r.complexity}</td>
                                <td>${r.complexValue}</td>
                                <td>${r.owner}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${r.status == 0}">To do</c:when>
                                        <c:when test="${r.status == 1}">Doing</c:when>
                                        <c:otherwise>Done</c:otherwise>
                                    </c:choose>
                                </td>

                                <c:if test="${requestScope.teamRole == 9}">
                                    <!--                                    <td>
                                                                            <a href="${pageContext.request.contextPath}/requirement?action=Edit&teamID=${requestScope.teamID}&id=${r.id}&tab=team">
                                                                                <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                                                            </a>
                                                                        </td>-->

                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <!--Paging Start-->
                                                <!--<a href="${pageContext.request.contextPath}/dashboard/detail?id=${sessionScope.classID}&tab=team&page=${i}" class="btn ${i == currentPage ? 'btn-primary' : 'btn-info'}">${i}</a>-->

                <div class="container-fluid pt-4 px-4">
                    <div class="align-content-center">
                        <input type="hidden" name="page" id="pageInput" value="1">

                        <div class="btn-group" role="group" aria-label="Pagination group">
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <!-- Nút bấm cho từng trang, khi nhấn sẽ cập nhật giá trị của `pageInput` -->
                                <button type="button" class="btn ${i == currentPage ? 'btn-primary' : 'btn-info'}" 
                                        onclick="setPage(${i})">${i}</button>
                            </c:forEach>
                        </div>

                        <script>
                            function setPage(pageNumber) {
                                // Đặt giá trị cho input ẩn
                                document.getElementById("pageInput").value = pageNumber;
                                // Gửi form
                                document.getElementById("paginationForm").submit();
                            }
                        </script>
                    </div>
                </div> 
                <!--Paging End-->
            </div>
        </form>

    </div>

</html>

