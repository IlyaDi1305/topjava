<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal Form</title>
</head>
<body>
<h1>${meal == null ? "Add Meal" : "Edit Meal"}</h1>
<form form action="meals" method="post" accept-charset="UTF-8">
    <input type="hidden" name="uuid" value="${meal.uuid}">
    <table>
        <tr>
            <td>Date Time:</td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}"></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input type="text" name="description" value="${meal.description}"></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input type="number" name="calories" value="${meal.calories}"></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="${meal == null ? "Add" : "Update"}">
                <input type="hidden" name="action" value="${meal == null ? "add" : "update"}">
                <button onclick="window.history.back()" type="button">Cancel</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>