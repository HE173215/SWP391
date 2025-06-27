<%-- 
    Document   : personal
    Created on : Nov 3, 2024, 12:53:46 PM
    Author     : Do Duan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

    <div class="tab-pane fade show " id="personal" role="tabpanel">
        <!--Filter Start-->
        <div class="container-fluid pt-4 px-4">
            <!-- Form filter -->
            <form action="${pageContext.request.contextPath}/dashboard/detail" method="get">

                <input type="hidden" name="id" value="${sessionScope.classID}" />
                <input type="hidden" name="tab" value='personal' />

                <div class="row g-4">
                    <!--Complexity-->
                    <div class="col-sm-12 col-xl-2">
                        <select class="form-select mb-3" aria-label="Default select example" name="complexity" >
                            <option value="0">Complexity : All</option>
                            <c:forEach var="complexity" items="${complexities}">
                                <option value="${complexity.id}" 
                                        <c:if test="${complexity.id == complexityID_s}">selected</c:if> >
                                    ${complexity.name}
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

                    <!--                    <div class="col-sm-12 col-xl-1">
                                            <button type="button" class="btn btn-success " onclick="window.location.href = '${pageContext.request.contextPath}/requirement?action=Add&teamID=${requestScope.teamID}'">Add</button>
                                        </div>-->
                </div>
            </form>
        </div>
        <!--Filter End-->

        <div class="table-responsive" style="flex-grow: 1; margin-left: 25px;margin-right: 25px ">
            <table class="table table-bordered mt-3">
                <thead>
                    <tr>
                        <th scope="col" style="width: 10%;">ID</th>
                        <th scope="col" style="width: 25%;">Title</th>
                        <th scope="col" style="width: 20%;">Complexity</th>
                        <th scope="col" style="width: 10%;">Value</th>
                        <th scope="col" style="width: 15%;">Milestone</th>
                        <th scope="col" style="width: 10%;">Status</th>
                        <th scope="col" style="width: 10%;">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${requirementList}">
                        <tr>
                            <td>${r.id}</td>
                            <td>${r.tittle}</td>
                            <td>${r.complexity}</td>
                            <td>${r.complexValue}</td>
                            <td>${r.milestone}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${r.status == 0}">To do</c:when>
                                    <c:when test="${r.status == 1}">Doing</c:when>
                                    <c:otherwise>Done</c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <!--<a href="" >Change status</a>-->
                                <!--<a href="${pageContext.request.contextPath}/requirement?action=Edit&teamID=${requestScope.teamID}&id=${r.id}&tab=personal">Edit</a>--> 
                                <a href="${pageContext.request.contextPath}/requirement?action=Edit&teamID=${requestScope.teamID}&id=${r.id}&tab=personal">
                                    <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                </a>

                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <!--Paging Start-->
            <div class="container-fluid pt-4 px-4">
                <div class="align-content-center">
                    <div class="btn-group" role="group" aria-label="Pagination group">
                        <c:forEach begin="1" end="${requestScope.totalPagesPerson}" var="i">
                            <a href="${pageContext.request.contextPath}/dashboard/detail?id=${sessionScope.classID}&tab=personal&page=${i}" class="btn ${i == currentPagePerson ? 'btn-primary' : 'btn-info'}">${i}</a>
                        </c:forEach>
                    </div>
                </div>
            </div> 
            <!--Paging End-->
        </div>
    </div>

</html>
