<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="/WEB-INF/functions.tld" %>

<html lang="ru">

<head>
    <title>Meals</title>
</head>
<body style="background-color: #808080;">
<c:set var="meals" value="${requestScope.mealsToFilteredList}"/>
<h3><a href="index.html">Home</a></h3>
<h3><a href="users">Users</a></h3>
<hr>
<h2>Meals</h2>
<h3><a href="meal.jsp">Add meal</a></h3>
<table border="1">
    <tr>
        <th>Date/time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <c:set var="color" value="${meal.excess ? 'red' : 'green'}"/>
        <c:if test="${!meal.isDeleted}">
            <tr style="color: ${color}">
                <td>${fn:convertDateTimeFormat(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.mealToId}"/>">Update</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${meal.mealToId}"/>">Delete</a></td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>