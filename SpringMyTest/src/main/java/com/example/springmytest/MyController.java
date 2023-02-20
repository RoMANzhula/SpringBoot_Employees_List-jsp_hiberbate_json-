package com.example.springmytest;

import com.google.gson.Gson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.NoSuchElementException;

//// Тут мы создаем Controller над Сервисами (в нашем случае GeneralService), для взаимодействия с Клиентами

@Controller
//аннотация, которая автоматически делает данный класс Контроллером (который становится обработчиком запросов)
public class MyController {
    static final int DEFAULT_PROFESSION_ID = -1; //константа - заглушка для названия Профессии Not Profession
    static final int ITEMS_PER_PAGE = 7; //константа - для вывода людей на одной странице


    private final GeneralService generalService; //инжектим сюда наш Сервис ContactService через конструктор, для работы с DataBase
    private final PersonRepository personRepository; //и ссылку на PersonRepository

    public MyController(GeneralService generalService, PersonRepository personRepository) { //конструктор класса
        this.generalService = generalService;
        this.personRepository = personRepository;
    }

    @GetMapping("/") //корневой метод, с запросом в корень
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") Integer page) { //параметр запроса - параметр page с типом
        //Integer с доп.параметрами (required = false - значит параметр не обязательный, defaultValue = "0" - если нет параметра, то он = 0)
        if (page < 0) page = 0; //проверка, чтоб не было минусовой страницы

        List<Person> persons = generalService //обращаемся к Сервису - дай нам людей (выгрузит нам страницу по номеру с 7-ью людьми)
                .findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id")); //PageRequest.of() - создать обьект из
        // параметров(те, что внутри скобок) - PageRequest.of(номер страницы, КОЛИЧЕСТВО_ЧЕЛОВЕК_НА_СТРАНИЦЕ, сортировка по спаданию,
        // название поля в Entity, которое мы сейчас достаем)

        //добавляем в модель атрибуты для последеющей передачи на страничку index
        model.addAttribute("professions", generalService.findProfessions()); //добавляем "professions" - вытаскиваем список
        // всех профессий для выпадающего меню
        model.addAttribute("persons", persons); //добавляем 7 человек, которые подгрузились
        model.addAttribute("allPages", getPageCount()); //добавляем количество страниц для отрисовки кнопок (т.е. количество
        //контактов разделенное на 7(ITEMS_PER_PAGE) = количество кнопок с номером странички, на которые можно жмакать)

        return "index"; //весь результат передаем на страничку index
    }

    @GetMapping("/reset") //если пришел Гет на reset, то
    public String reset() {
        generalService.reset(); //делаем reset()
        return "redirect:/"; //это значит перенаправить пользователя в корень приложения, т.е. приведет в index
    }

    @GetMapping("/person_add_page") //если пришел Get-запрос на /person_add_page
    public String personAddPage(Model model) { //метод - добавление нового человека
        model.addAttribute("professions", generalService.findProfessions()); //берем список всех профессий, которые
        // у нас есть и передаем дальше на return "person_add_page"


        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/webapp/WEB-INF/static/persons_import.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        Person[] persons = gson.fromJson(json, Person[].class);
        model.addAttribute("persons", persons);

        return "person_add_page";
    }

    @GetMapping("/profession_add_page") //если нужно добавить новую профессию, то
    public String professionAddPage(Model model) {
        return "profession_add_page"; //подставить новую страничку
    }


    @GetMapping("/profession/{id}") //универсальный обработчик для отображения профессий
    //в URL {id} - это переменная часть (которая меняется в зависимости от того, что мы туда напишем)
    public String listProfession(
            @PathVariable(value = "id") long professionId, //с помощью аннотации, мы заменяем часть "id" на часть professionId
            @RequestParam(required = false, defaultValue = "0") Integer page, Model model) //параметр запроса - параметр page с типом
    //Integer с доп.параметрами (required = false - значит параметр не обязательный, defaultValue = "0" - если нет параметра, то он = 0)
    {
        Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; //по айдишнику
        // профессии вытаскиваем профессию из базы
        if (page < 0) page = 0; //проверяем, чтоб не была минусовой

        List<Person> persons = generalService //делаем выборку по найденной профессии с PageRequest.of(для текущей страницы,
                // КОЛИЧЕСТВО_ЧЕЛОВЕК_НА_СТРАНИЦЕ, сортировка по спаданию, название поля в Энтети(которое мы сейчас достаем))
                .findByProfession(profession, PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("professions", generalService.findProfessions()); //передаем на страницу список всех возможных профессий
        model.addAttribute("persons", persons); //передаем на страницу тех людей, которых мы вытащили
        model.addAttribute("byProfessionPages", getPageCount(profession)); //передаем сколько человек в конкретной профессией, для
        //пейдженации
        model.addAttribute("professionId", professionId); //айдишник профессии для которой мы сейчас выгружаем людей

        return "index"; //рисуем заглавную страницу
    }

    //обработчик для поиска по подстроке-pattern
    @PostMapping(value = "/search") //сужаем диапазон отображения запросов на основе Content-Type запроса
    public String search(@RequestParam String pattern, Model model) { //Post-запросом прилетвет строка (String patter) для поиска
        model.addAttribute("professions", generalService.findProfessions()); //передаем список профессий
        model.addAttribute("persons", generalService.findByPattern(pattern, null)); //передаем список людей,
        //которые соответствкют введенной подстроке-pattern (с помощью сервиса и его метода findByPattern(pattern, null(без постраничного вывода)))

        return "index"; //перерисовывем страницу index
    }

    //обработчик запросов для удаления человека
    @PostMapping(value = "/person/delete") //POST-запросом на end-point /person/delete
    public ResponseEntity<Void> delete( //тип для ответа клиенту по протоколу с дженериком <Void> - это для ответа без тела
                                        @RequestParam(value = "toDelete[]", required = false) long[] toDelete) { //прилетает необязательный
        //параметр, который мы десериализуем в массив long[] (т.е. значение параметра дисиреализируется в массив айдишников,
        //которые нужно удалить)
        if (toDelete != null && toDelete.length > 0) //если этот массив не пустой, то мы его отправляем в
            generalService.deletePersons(toDelete); //метод deletePersons(toDelete)

        return new ResponseEntity<>(HttpStatus.OK); //но тут возвращаем СтатусКод ОК вместо страницы "index" (это чтобы не отображать
        //страницу, а просто выполнить какие-то действия(в нашем случае - удалить людей, т.к. работаем с чек-боксами(квадратики для
        // галочек)). Тут у нас клиентом выступает не браузер, а java-script-код(находится на странице index 142-150 строчки кода)
        // внутри браузера, а коду нет никакого смысла отдавать страницу, поэтому мы передаем СтатусКод. А если клиентом выступает
        // браузер - то мы возвращаем ему странички.
    }

    //1) Сделать кнопку для удаления выбранной группы с контактами
    @GetMapping("/profession_delete_page") //если приходит Get-запрос
    private String professionDeletePage(Model model) {
        model.addAttribute("professions", generalService.findProfessions()); //выполняем поиск всех профессий
        return "profession_delete_page";
    }

    @PostMapping(value = "/profession/delete") //обработчик для удаления профессии с людьми, у которых данная профессия
    public String professionDelete(@RequestParam(value = "profession") long professionId) { //прилетает параметр, который мы дисиреализуем
        //в айдишник с типом long
        try { //во избежание исключения с полным отсутствием профессий при их последовательном удалении
            Profession profession = generalService.findProfession(professionId); //выполняем поиск профессиии

            List<Person> persons = generalService.findPersons(profession); //инжектим список с найденными людьми по запрошенной профессии
            personRepository.deleteAll(persons); //через репозиторий удаляем весь список людей с одинаковой профессией
            generalService.deleteProfession(profession); //через главныый Сервис удаляем запрошенную профессию

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return "redirect:/"; //перерисовываем содержимое с учетом удаленных обьектов
    }

    // 2) Сделать возможность загрузить список контактов из JSON файла
    @PostMapping("/persons/importFromJSON")
    public String importPersons(@RequestParam(value = "profession") long professionId) {

        Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; //по айдишнику в профессии мы находим
        // профессию в Базе (делаем это только если группа не дефолтная)
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/webapp/WEB-INF/static/persons_import.json"))); //читаем JSON
            //из указанного документа
            Gson gson = new Gson(); //обьект библиотеки
            Person[] persons = gson.fromJson(json, Person[].class); //в массив типа Person пишем считанное с JSON-файла

            for (Person p : persons) { //проходимся по массиву людей из JSON
                p = new Person(profession, p.getName(), p.getSurname());
                generalService.addPerson(p); //добавляем их в базу
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/"; //перертсовываем данные
    }

    //тут добавляется человек
    @PostMapping(value = "/person/add") //Post-запрос на end-point /person/add
    public String personAdd(@RequestParam(value = "profession") long professionId,//айдишник профессии куда добавлять нового человека (тут
                            // название параметра в Java(professionId) не совпадает с названием в форме HTML("profession") - поэтому пишем (value = "profession"))
                            @RequestParam String name, //имя (тут название параметра в Java совпадает с названием в форме HTML)
                            @RequestParam String surname) //фамилия (тут название параметра в Java совпадает с названием в форме HTML)
    {
        Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; //по айдишнику в профессии мы находим
        // профессию в Базе (делаем это только если группа не дефолтная)

        Person person = new Person(profession, name, surname); //создаем Entity человек с аргументами для конструктора
        generalService.addPerson(person); //с помощью Сервиса сохраняем человека в Базу

        return "redirect:/"; //перерисовываем заглавную страницу, с учето того, что мы только что добавили нового человека
    }

    //тут обработчик для добавления новой профессии
    @PostMapping(value = "/profession/add")
    public String professionAdd(@RequestParam String name) { //прилетает просто строка с формы HTML с названием профессии
        generalService.addProfession(new Profession(name)); //мы создаем профессию и сохраняем ее в базу
        return "redirect:/"; //перерисовываем заглавную страницу, с учето того, что мы только что добавили новую группу
    }

    private long getPageCount() { //пейдженация - калькулятор страниц для главной страници приложения
        long totalCount = generalService.count(); //через главный Сервис получаем количество всех строк (обьектов)
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0); //все строки делим на количество
        //элементов на странице (в нашем случае 7), но т.к. деление возвращает целое число без хвоста - мы выполняяем доп.операцию по
        //вычислению отсатка от деления всех строк на количество строк на странице: если есть остаток, то добавляем еще одну страницу,
        //если остатка нет - не добавляем.
    }

    private long getPageCount(Profession profession) { //пейдженация - калькулятор страниц для отсортированных обьектов(людей) по профессии
        long totalCount = generalService.countByProfession(profession);
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }
}
