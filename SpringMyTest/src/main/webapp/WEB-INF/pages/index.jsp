<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello All</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <h1><img height="60" width="65" src="<c:url value="/static/anon.png"/>"/> Employees List</h1>
    <!-- станавливаем картинку и надпись рядом с ней -->
    <a href="/"><small><h6> click to return to first page</h6></small></a> <!-- строка-ссыла под картинкой для возврата на главную страницу -->

    <nav class="navbar navbar-default"> <!-- панель навигации по-умолчанию -->
        <div class="container-fluid"> <!-- изменчивый контейнер -->
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul id="groupList" class="nav navbar-nav">
                    <li>
                        <button type="button" id="add_person" class="btn btn-default navbar-btn">Add Person</button> <!-- кнопка для добавления человека -->
                    </li>
                    <li>
                        <button type="button" id="delete_person" class="btn btn-default navbar-btn">Delete Person</button> <!-- кнопка для удаления человека -->
                    </li>
                    <li>
                        <button type="button" id="add_profession" class="btn btn-default navbar-btn">Add Profession</button> <!-- кнопка для добавления профессии -->
                    </li>
                    <li>
                        <button type="button" id="delete_profession" class="btn btn-default navbar-btn">Delete Profession</button> <!-- кнопка для удаления профессии -->
                    </li>

                    <li>
                        <button type="button" id="reset" class="btn btn-default navbar-btn">Reset</button> <!-- кнопка для возврата к изночальному виду -->
                    </li>
                    <li class="dropdown"> <!-- выпадающая вниз кнопка для демонстрации всех профессий -->
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">View Profession<span class="caret"></span></a> <!-- название кнопки -->
                        <ul class="dropdown-menu"> <!-- генерируем выпадающий список в Рантайме -->
                            <li><a href="/">All Profession</a></li>
                            <!-- tag a ссылка всегда ведущая в корень("/") - группа показать все  -->
                            <c:forEach items="${professions}"
                                       var="profession"> <!-- остальные группы мы рисуем с помощью тэга for-each.
                                Данные из модели(Model model) нам доступны как одноименные переменные "professions" - т.е. запускаем for-each по всем обьектам, которые лежат в
                                professions, к каждому обьекту в цикле мы можем обращаться через переменную "profession" -->
                                <li><a href="/profession/${profession.id}">${profession.name}</a></li>
                                <!-- для каждого обьект profession рисуем пункт в выподающем списке, во внутрь
                                кладем {profession.name}(это вызов геттера Profession getName(), имя profession оборачиваем ссылкой, в корень которой добавляем вызов геттера Profession getId() -->
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-left" role="search" action="/search" method="post">
                    <div class="form-group">
                        <input type="text" class="form-control" name="pattern" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                    <!-- устанавливаем кнопку для отправки данных формы на сервер -->
                </form>
            </div>
        </div>
    </nav>

    <!-- следующая динамическая часть - табличка с данными -->
    <table class="table table-striped">
        <!-- шапка таблички с названиями колонок -->
        <thead>
        <tr>
            <td></td>
            <td><b>Name</b></td>
            <td><b>Surname</b></td>
            <td><b>Profession</b></td>
        </tr>
        </thead>

        <!-- содержимое, которые генерируем динамически -->
        <c:forEach items="${persons}" var="person"> <!-- запускаем for-each по людям -->
            <tr> <!-- новая строка с добавлением колонок-->
                <td><input type="checkbox" name="toDelete[]" value="${person.id}" id="checkbox_${person.id}"/></td> <!-- первая колонка - это
                         чек-бокс(квадратик для галочки) которым мы можем выбирать (для их отличия мы value= Person getId(), напротив которого этот чек-бокс находится -->
                <td>${person.name}</td> <!-- подставляем имя - Person getName() -->
                <td>${person.surname}</td> <!-- подставляем фамилия - Person getSurname() -->

                <c:choose>
                    <c:when test="${person.profession ne null}"> <!-- если у человека профессия не пустая(т.е. она есть) -->
                        <td>${person.profession.name}</td>
                        <!-- то в последнюю колонку подставляем название этой профессии -->
                    </c:when>
                    <c:otherwise> <!-- иначе -->
                        <td>Not Profession</td>
                        <!-- подставляем название профессии Not Profession -->
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </table>

    <nav aria-label="Page navigation"> <!-- управление отрисовкой страниц по ссылкам -->
        <ul class="pagination"> <!-- пейдженизация -->
            <c:if test="${allPages ne null}"> <!-- пейдженация для главной страницы если есть параметр allPages(это кол-во страниц) в моделе(Model model) -->
                <c:forEach var="i" begin="1"
                           end="${allPages}"> <!-- запускаем for-each по индексу от 1 до количества страниц -->
                    <li><a href="/?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                    <!-- для каждой кнопочки во внутрь подставляем значение индекса, а
                    это значение заворачиваем в ссылку на корень с параметром page = (значение индекса - 1) -->
                </c:forEach>
            </c:if>
            <c:if test="${byProfessionPages ne null}"> <!-- пейдженация для конкретной профессии -->
                <c:forEach var="i" begin="1"
                           end="${byProfessionPages}"> <!-- запускаем for-each по индексу от 1 до количества страниц для конкретной профессии -->
                    <li><a href="/profession/${professionId}?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                    <!-- для каждой кнопочки во внутрь подставляем значение индекса, а
                    это значение заворачиваем в ссылку на корень с параметром переменным значением {professionId} и page = (значение индекса - 1) -->
                </c:forEach>
            </c:if>
        </ul>
    </nav>
</div>

<!-- java-script-cod используем библиотеку j-query -->
<script>
    $('#delete_profession').click(function () {
        window.location.href = '/profession_delete_page'
    });

    $('.dropdown-toggle').dropdown();

    $('#add_person').click(function () {
        window.location.href = '/person_add_page';
    });

    $('#add_profession').click(function () {
        window.location.href = '/profession_add_page';
    });

    $('#reset').click(function () {
        window.location.href = '/reset';
    });

    $('#delete_person').click(function () { <!-- селектор (выбираем определенный элемент по айдишнику).приНажатиииНаНего(выполнить следующий код() { -->
        var data = {'toDelete[]': []}; <!-- создаем обьект data -->
        $(":checked").each(function () { <!-- селектор(находим все чекнутые чек-боксы на данной странице).дляКаждогоЧекнутого(выполнить следующий код() { -->
            data['toDelete[]'].push($(this).val()); <!-- в data добавляем первичный ключ айдишника чекнутого человека :-) -->
        });
        $.post("/person/delete", data, function (data, status) { <!-- после Post-запросом на end-point отправляем data (это наш  @PostMapping(value = "/person/delete")) -->
            window.location.reload(); <!-- после выполняется данная функция - перезагрузить ту страницу на которой мы сейчас находимся (по сути - это запрос в корень) -->
        });
    });
</script>
</body>
</html>