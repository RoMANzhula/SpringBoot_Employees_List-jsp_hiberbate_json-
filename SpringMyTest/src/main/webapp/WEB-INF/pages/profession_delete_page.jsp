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
            <form role="form" class="form-horizontal" action="/profession/delete" method="post"> <!-- вся форма Post-запросом отправляется на
             end-point /profession/delete -->
                <h3>Delete Profession</h3> <!-- название контейнера -->
                <select class="selectpicker form-control form-group" name="profession"> <!-- select выбрать из перечня, внутрь которого for-each"ом отрисовываем  -->
                    <option value="-1">Not Profession</option> <!-- первая позиция в списке -->
                    <c:forEach items="${professions}" var="profession">
                        <option value="${profession.id}">${profession.name}</option> <!-- список профессий -->
                    </c:forEach>
                </select>
                <input type="submit" class="btn btn-primary" value="Delete"> <!-- добавляем кнопку для удаления с названием -->
            </form>
        </div>
        <script>
            $('.selectpicker').selectpicker(); <!-- скрипт для бутстрепа, чтоб работала программа(выше описанная) -->
        </script>
    </body>
</html>