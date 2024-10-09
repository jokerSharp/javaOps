<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="/WEB-INF/functions.tld" %>

<html lang="ru">

<head>
    <title>Meals</title>
</head>
<body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
<c:set var="meal" value="${requestScope.meal}"/>
<h3><a href="index.html">Home</a></h3>
<h3><a href="users">Users</a></h3>
<h3><a href="meals">Meals</a></h3>
<hr>
<h2>Meal edit form</h2>
<form method="POST" action='meals' name="Add meal">
    <input hidden="" type="text" name="mealId" value="${meal.id}"/>
    Date and time: <input type="datetime-local" name="date" value="${meal.dateTime}"/> <br/>
    Description : <input type="text" name="description" value="${meal.description}"/> <br/>
    Calories : <input type="number" name="calories" value="${meal.calories}"/><br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>