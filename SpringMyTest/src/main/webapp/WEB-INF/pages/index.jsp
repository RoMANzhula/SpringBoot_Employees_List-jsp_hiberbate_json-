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
    <h1><img height="60" width="65" src="<c:url value="/static/anon.png"/>"/> Список працівників</h1>
    <!-- вставляємо картинку і надпис поруч з нею -->
    <a href="/"><small><h6> натисніть, щоб повернутися на головну сторінку</h6></small></a> <!-- посилання під картинкою для повернення на головну сторінку -->

    <nav class="navbar navbar-default"> <!-- панель навігації за замовчуванням -->
        <div class="container-fluid"> <!-- змінний контейнер -->
            <!-- Збираємо посилання на навігацію, форми та інший контент для перемикання -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul id="groupList" class="nav navbar-nav">
                    <li>
                        <button type="button" id="add_person" class="btn btn-default navbar-btn">Додати особу</button> <!-- кнопка для додавання особи -->
                    </li>
                    <li>
                        <button type="button" id="delete_person" class="btn btn-default navbar-btn">Видалити особу</button> <!-- кнопка для видалення особи -->
                    </li>
                    <li>
                        <button type="button" id="add_profession" class="btn btn-default navbar-btn">Додати професію</button> <!-- кнопка для додавання професії -->
                    </li>
                    <li>
                        <button type="button" id="delete_profession" class="btn btn-default navbar-btn">Видалити професію</button> <!-- кнопка для видалення професії -->
                    </li>

                    <li>
                        <button type="button" id="reset" class="btn btn-default navbar-btn">Скинути</button> <!-- кнопка для повернення до початкового вигляду -->
                    </li>
                    <li class="dropdown"> <!-- випадаюча кнопка для демонстрації всіх професій -->
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">Переглянути професії<span class="caret"></span></a> <!-- назва кнопки -->
                        <ul class="dropdown-menu"> <!-- генеруємо випадаючий список в рантаймі -->
                            <li><a href="/">Всі професії</a></li>
                            <!-- посилання для показу всіх професій -->
                            <c:forEach items="${professions}"
                                       var="profession"> <!-- інші професії генеруємо через тег for-each.
                                Дані з моделі (Model model) доступні як одноіменні змінні "professions" - запускаємо цикл по всіх об'єктах в
                                professions, кожен об'єкт у циклі доступний через змінну "profession" -->
                                <li><a href="/profession/${profession.id}">${profession.name}</a></li>
                                <!-- для кожного об'єкта profession створюємо пункт в випадаючому списку, всередині
                                виводимо {profession.name} (це виклик геттера Profession getName()), обгортаємо професію посиланням, де в корінь додаємо виклик геттера Profession getId() -->
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-left" role="search" action="/search" method="post">
                    <div class="form-group">
                        <input type="text" class="form-control" name="pattern" placeholder="Пошук">
                    </div>
                    <button type="submit" class="btn btn-default">Відправити</button>
                    <!-- кнопка для відправки форми на сервер -->
                </form>
            </div>
        </div>
    </nav>

    <!-- наступна динамічна частина - таблиця з даними -->
<table class="table table-striped">
    <!-- шапка таблиці з назвами колонок -->
    <thead>
    <tr>
        <td></td>
        <td><b>Ім'я</b></td>
        <td><b>Прізвище</b></td>
        <td><b>Професія</b></td>
    </tr>
    </thead>

    <!-- вміст, який генеруємо динамічно -->
    <c:forEach items="${persons}" var="person"> <!-- запускаємо for-each по людям -->
        <tr> <!-- новий рядок з додаванням колонок-->
            <td><input type="checkbox" name="toDelete[]" value="${person.id}" id="checkbox_${person.id}"/></td> <!-- перша колонка - це
                     чек-бокс (квадратик для галочки), яким ми можемо вибирати (для їх відмінності ми value= Person getId(), напроти якого цей чек-бокс знаходиться) -->
            <td>${person.name}</td> <!-- підставляємо ім'я - Person getName() -->
            <td>${person.surname}</td> <!-- підставляємо прізвище - Person getSurname() -->

            <c:choose>
                <c:when test="${person.profession ne null}"> <!-- якщо у людини професія не порожня (т.е. вона є) -->
                    <td>${person.profession.name}</td>
                    <!-- то в останню колонку підставляємо назву цієї професії -->
                </c:when>
                <c:otherwise> <!-- інакше -->
                    <td>Без професії</td>
                    <!-- підставляємо назву професії "Без професії" -->
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>
</table>

<nav aria-label="Переміщення по сторінках"> <!-- управління відображенням сторінок за посиланнями -->
    <ul class="pagination"> <!-- пагінація -->
        <c:if test="${allPages ne null}"> <!-- пагінація для головної сторінки, якщо є параметр allPages (це кількість сторінок) в моделі (Model model) -->
            <c:forEach var="i" begin="1" end="${allPages}"> <!-- запускаємо for-each по індексах від 1 до кількості сторінок -->
                <li><a href="/?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                <!-- для кожної кнопки всередину підставляємо значення індекса, а
                це значення обгортаємо в посилання на корінь з параметром page = (значення індекса - 1) -->
            </c:forEach>
        </c:if>
        <c:if test="${byProfessionPages ne null}"> <!-- пагінація для конкретної професії -->
            <c:forEach var="i" begin="1" end="${byProfessionPages}"> <!-- запускаємо for-each по індексах від 1 до кількості сторінок для конкретної професії -->
                <li><a href="/profession/${professionId}?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                <!-- для кожної кнопки всередину підставляємо значення індекса, а
                це значення обгортаємо в посилання на корінь з параметром змінним значенням {professionId} і page = (значення індекса - 1) -->
            </c:forEach>
        </c:if>
    </ul>
</nav>
</div>

<!-- використовуємо бібліотеку j-query -->
<script>
    $('#delete_profession').click(function () {
        window.location.href = '/profession_delete_page'; // при натисканні перенаправляє на сторінку видалення професії
    });

    $('.dropdown-toggle').dropdown(); // ініціалізуємо випадаюче меню

    $('#add_person').click(function () {
        window.location.href = '/person_add_page'; // при натисканні перенаправляє на сторінку додавання особи
    });

    $('#add_profession').click(function () {
        window.location.href = '/profession_add_page'; // при натисканні перенаправляє на сторінку додавання професії
    });

    $('#reset').click(function () {
        window.location.href = '/reset'; // при натисканні перенаправляє на сторінку скидання
    });

    $('#delete_person').click(function () { <!-- селектор (вибираємо певний елемент по айдішнику). при натисканні на нього виконуємо наступний код -->
        var data = {'toDelete[]': []}; <!-- створюємо об'єкт data -->
        $(":checked").each(function () { <!-- селектор (знаходимо всі відмічені чекбокси на цій сторінці). для кожного відміченого виконуємо наступний код -->
            data['toDelete[]'].push($(this).val()); <!-- в data додаємо первинний ключ айдішника відміченого елемента -->
        });
        $.post("/person/delete", data, function (data, status) { <!-- після Post-запиту на endpoint "/person/delete" відправляємо data (це наш @PostMapping(value = "/person/delete")) -->
            window.location.reload(); <!-- після виконання запиту перезавантажуємо поточну сторінку (по суті це запит на корінь) -->
        });
    });
</script>
</body>
</html>
