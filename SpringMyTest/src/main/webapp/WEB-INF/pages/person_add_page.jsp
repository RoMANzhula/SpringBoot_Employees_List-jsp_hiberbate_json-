<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Нова особа</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form role="form" class="form-horizontal" action="/person/add" method="post"> <!-- вся форма Post-запитом відправляється на
             end-point /person/add -->
        <h3>Нова особа</h3> <!-- надпис-назва блоку -->

        <input class="form-control form-group" type="text" name="name" placeholder="Ім'я"> <!-- рядок для вводу імені людини -->
        <input class="form-control form-group" type="text" name="surname" placeholder="Прізвище"> <!-- рядок для вводу прізвища людини -->
        <select class="selectpicker form-control form-group" name="profession"> <!-- select вибрати зі списку, всередину якого за допомогою for-each відображаємо -->
            <option value="-1">Без професії</option> <!-- перша позиція для списку професій -->
            <c:forEach items="${professions}" var="profession">
                <option value="${profession.id}">${profession.name}</option> <!-- список професій -->
            </c:forEach>
        </select>
        <input type="submit" class="btn btn-primary" value="Додати"> <!-- малюємо кнопку з назвою  -->
    </form>

    <form role="form" class="form-horizontal" action="/persons/importFromJSON" method="post"> <!-- вся форма Post-запитом відправляється на
             end-point /persons/importFromJSON-->
        <h3>Нова особа з JSON</h3> <!-- надпис-назва блоку -->

        <select class="selectpicker form-control form-group" name="person"> <!-- select вибрати зі списку, всередину якого за допомогою for-each відображаємо -->
            <option value="-1">Ці особи будуть додані до бази даних</option> <!-- перша позиція для списку людей -->
            <c:forEach items="${persons}" var="person"> <!-- проходимо по кожному елементу зі списку -->
                <option value="${person.id}">
                    ${person.name} <!-- відображаємо ім'я -->
                    ${person.surname} <!-- відображаємо прізвище -->
                </option>
            </c:forEach>
        </select>

        <br> <!-- порожній рядок між рядками -->

        <select class="selectpicker form-control form-group" name="profession"> <!-- select вибрати зі списку, всередину якого за допомогою for-each відображаємо -->
            <option value="-1">Виберіть професію</option> <!-- перша позиція для списку професій -->
            <c:forEach items="${professions}" var="profession">
                <option value="${profession.id}">${profession.name}</option> <!-- список професій -->
            </c:forEach>
        </select>

        <input type="submit" class="btn btn-primary" value="Додати"> <!-- малюємо кнопку з назвою  -->

    </form>
</div>

<script>
    $('.selectpicker').selectpicker(); <!-- скрипт для Bootstrap, щоб працювала програма (вище описана) -->
</script>
</body>
</html>
