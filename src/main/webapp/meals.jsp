<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="/WEB-INF/functions.tld" %>

<html lang="ru">

<head>
    <title>Meals</title>
</head>
<body>
<c:set var="meals" value="${requestScope.mealsToFilteredList}"/>
<h3><a href="index.html">Home</a></h3>
<h3><a href="users">Users</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Date/time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <c:set value="green" var="color"></c:set>
        <c:if test="${meal.excess}">
            <c:set value="red" var="color"></c:set>
        </c:if>
        <tr style="color: ${color}">
            <td><c:out value="${fn:convertDateTimeFormat(meal.dateTime)}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>