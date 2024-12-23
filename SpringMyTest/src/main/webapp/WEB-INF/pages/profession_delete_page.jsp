<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Delete Profession</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <form role="form" class="form-horizontal" action="/profession/delete" method="post"> <!-- вся форма Post-запитом відправляється на
             end-point /profession/delete -->
                <h3>Delete Profession</h3> <!-- назва контейнера -->
                <select class="selectpicker form-control form-group" name="profession"> <!-- select обрати з переліку, в середину якого for-each"ем відмалбовуваємо  -->
                    <option value="-1">Not Profession</option> <!-- перша позиція в списку -->
                    <c:forEach items="${professions}" var="profession">
                        <option value="${profession.id}">${profession.name}</option> <!-- список професій -->
                    </c:forEach>
                </select>
                <input type="submit" class="btn btn-primary" value="Delete"> <!-- додаємо кнопку для видалення з назвою -->
            </form>
        </div>
        <script>
            $('.selectpicker').selectpicker(); <!-- скрипт для бутстрепа, щоб працювала програма(вище описана) -->
        </script>
    </body>
</html>
