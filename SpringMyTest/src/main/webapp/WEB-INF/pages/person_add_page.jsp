<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Person</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form role="form" class="form-horizontal" action="/person/add" method="post"> <!-- вся форма Post-запросом отправляется на
             end-point /person/add -->
        <h3>New Person</h3> <!-- надпись-название блока -->

        <input class="form-control form-group" type="text" name="name" placeholder="Name"> <!-- строка для ввода имени человека -->
        <input class="form-control form-group" type="text" name="surname" placeholder="Surname"> <!-- строка для ввода фамилиии человека -->
        <select class="selectpicker form-control form-group" name="profession"> <!-- select выбрать из перечня, внутрь которого for-each"ом отрисовываем  -->
            <option value="-1">Not Profession</option> <!-- первая позиция для списка профессий -->
            <c:forEach items="${professions}" var="profession">
                <option value="${profession.id}">${profession.name}</option> <!-- список профессий -->
            </c:forEach>
        </select>
        <input type="submit" class="btn btn-primary" value="Add"> <!-- рисуем кнопку с названием  -->
    </form>


    <form role="form" class="form-horizontal" action="/persons/importFromJSON" method="post"> <!-- вся форма Post-запросом отправляется на
             end-point /persons/importFromJSON-->
        <h3>New Person From JSON</h3> <!-- надпись-название блока -->

        <select class="selectpicker form-control form-group"  name="person"> <!-- select выбрать из перечня, внутрь которого for-each"ом отрисовываем  -->
            <option value="-1">Еhese persons will be added to the database</option> <!-- первая позиция для списка людей -->
            <c:forEach items="${persons}" var="person"> <!-- проходим по каждому элементу из списка -->
                <option value="${person.id}">
                    ${person.name} <!-- рисуем имя -->
                    ${person.surname} <!-- рисуем фамилию -->
                </option>
            </c:forEach>
        </select>

        <br> <!-- пустая строка между строками -->

        <select class="selectpicker form-control form-group" name="profession"> <!-- select выбрать из перечня, внутрь которого for-each"ом отрисовываем  -->
            <option value="-1">Select Profession</option> <!-- первая позиция для списка профессий -->
            <c:forEach items="${professions}" var="profession">
                <option value="${profession.id}">${profession.name}</option> <!-- список профессий -->
            </c:forEach>
        </select>

        <input type="submit" class="btn btn-primary" value="Add"> <!-- рисуем кнопку с названием  -->

    </form>
</div>

<script>
    $('.selectpicker').selectpicker(); <!-- скрипт для бутстрепа, чтоб работала программа(выше описанная) -->
</script>
</body>
</html>