<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .notexcess {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h3>Meals</h3>
    <a href="meals?action=new">Add Meal</a>
    <table border="1" cellpadding="10" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${meals}" var="mealTo">
            <jsp:useBean id="mealTo" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${mealTo.excess ? 'excess' : 'notexcess'}">
            <td>
                <fmt:parseDate value="${mealTo.dateTime}" pattern="y-M-dd'T'H:m" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
            </td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>
                <a href="meals?action=edit&id=${mealTo.id}">Update</a>
                <a href="meals?action=delete&id=${mealTo.id}">Delete</a>
            </td>
        </tr>
        </c:forEach>
        <tbody>
    </table>
</section>
</body>
</html>