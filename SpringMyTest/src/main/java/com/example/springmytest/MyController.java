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

//// Тут ми створюємо Controller над Сервісами (в нашому випадку GeneralService), для взаємодії з Клієнтами

@Controller
//анотація, яка автоматично робить цей клас Контролером (який стає обробником запитів)
public class MyController {
    static final int DEFAULT_PROFESSION_ID = -1; //константа - заглушка для назви Професії Not Profession
    static final int ITEMS_PER_PAGE = 7; //константа - для виведення людей на одній сторінці

    private final GeneralService generalService; //інжектимо сюди наш Сервіс ContactService через конструктор, для роботи з базою даних
    private final PersonRepository personRepository; //і посилання на PersonRepository

    public MyController(GeneralService generalService, PersonRepository personRepository) { //конструктор класу
        this.generalService = generalService;
        this.personRepository = personRepository;
    }

    @GetMapping("/") //кореневий метод, з запитом на корінь
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") Integer page) { //параметр запиту - параметр page з типом
        //Integer з додатковими параметрами (required = false - означає, що параметр не обов'язковий, defaultValue = "0" - якщо немає параметра, то він = 0)
        if (page < 0) page = 0; //перевірка, щоб не було негативної сторінки

        List<Person> persons = generalService //звертаємось до Сервісу - дайте нам людей (вивантажить нам сторінку по номеру з 7-ми людьми)
                .findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id")); //PageRequest.of() - створити об'єкт з
        // параметрів (ті, що всередині дужок) - PageRequest.of(номер сторінки, КІЛЬКІСТЬ_ЛЮДЕЙ_НА_СТОРІНЦІ, сортування по спаданню,
        // назва поля в Entity, яке ми зараз отримуємо)

        //додаємо в модель атрибути для подальшої передачі на сторінку index
        model.addAttribute("professions", generalService.findProfessions()); //додаємо "professions" - витягуємо список
        // всіх професій для випадаючого меню
        model.addAttribute("persons", persons); //додаємо 7 людей, які завантажились
        model.addAttribute("allPages", getPageCount()); //додаємо кількість сторінок для малювання кнопок (тобто кількість
        //контактів, поділену на 7 (ITEMS_PER_PAGE) = кількість кнопок з номером сторінки, на які можна натискати)

        return "index"; //весь результат передаємо на сторінку index
    }

    @GetMapping("/reset") //якщо прийшов GET на reset, то
    public String reset() {
        generalService.reset(); //робимо reset()
        return "redirect:/"; //це означає перенаправити користувача в корінь додатку, тобто призведе на сторінку index
    }

    @GetMapping("/person_add_page") //якщо прийшов Get-запит на /person_add_page
    public String personAddPage(Model model) { //метод - додавання нового людини
        model.addAttribute("professions", generalService.findProfessions()); //беремо список всіх професій, які
        // у нас є, і передаємо далі на return "person_add_page"


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

    @GetMapping("/profession_add_page") //якщо потрібно додати нову професію, то
    public String professionAddPage(Model model) {
        return "profession_add_page"; //підставити нову сторінку
    }

    @GetMapping("/profession/{id}") //універсальний обробник для відображення професій
    //в URL {id} - це змінна частина (яка змінюється в залежності від того, що ми туди напишемо)
    public String listProfession(
            @PathVariable(value = "id") long professionId, //з допомогою анотації, ми заміняємо частину "id" на частину professionId
            @RequestParam(required = false, defaultValue = "0") Integer page, Model model) //параметр запиту - параметр page з типом
    //Integer з додатковими параметрами (required = false - означає, що параметр не обов'язковий, defaultValue = "0" - якщо немає параметра, то він = 0)
    {
        Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; //по айдішнику
        // професії витягуємо професію з бази
        if (page < 0) page = 0; //перевіряємо, щоб не була мінусовою

        List<Person> persons = generalService //робимо вибірку по знайденій професії з PageRequest.of(для поточної сторінки,
                // КІЛЬКІСТЬ_ЛЮДЕЙ_НА_СТОРІНЦІ, сортування по спаданню, назва поля в Ентитеті (яке ми зараз отримуємо))
                .findByProfession(profession, PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("professions", generalService.findProfessions()); //передаємо на сторінку список всіх можливих професій
        model.addAttribute("persons", persons); //передаємо на сторінку тих людей, яких ми витягнули
        model.addAttribute("byProfessionPages", getPageCount(profession)); //передаємо скільки людей в конкретній професії, для
        //пейджинації
        model.addAttribute("professionId", professionId); //айдішник професії для якої ми зараз вивантажуємо людей

        return "index"; //малюємо головну сторінку
    }

    //обробник для пошуку по підстрічці-pattern
    @PostMapping(value = "/search") //звужуємо діапазон відображення запитів на основі Content-Type запиту
    public String search(@RequestParam String pattern, Model model) { //Post-запитом приходить рядок (String pattern) для пошуку
        model.addAttribute("professions", generalService.findProfessions()); //передаємо список професій
        model.addAttribute("persons", generalService.findByPattern(pattern, null)); //передаємо список людей,
        //які відповідають введеній підстрічці-pattern (з допомогою сервісу і його методу findByPattern(pattern, null (без пагінації)))

        return "index"; //перемальовуємо сторінку index
    }

    // Обробник запиту для видалення людини
@PostMapping(value = "/person/delete") // POST-запит на end-point /person/delete
public ResponseEntity<Void> delete( // Тип для відповіді клієнту за протоколом з дженериком <Void> - це для відповіді без тіла
                                    @RequestParam(value = "toDelete[]", required = false) long[] toDelete) { // Приходить необов'язковий
    // параметр, який ми десеріалізуємо в масив long[] (тобто значення параметра десеріалізується в масив ідентифікаторів,
    // яких потрібно видалити)
    if (toDelete != null && toDelete.length > 0) // Якщо цей масив не порожній, то ми його відправляємо в
        generalService.deletePersons(toDelete); // метод deletePersons(toDelete)

    return new ResponseEntity<>(HttpStatus.OK); // Але тут ми повертаємо статус-код ОК замість сторінки "index" (це щоб не відображати
    // сторінку, а просто виконати певні дії (в нашому випадку - видалити людей, оскільки працюємо з чекбоксами (квадратики для
    // галочок)). Тут у нас клієнтом виступає не браузер, а JavaScript-код (находиться на сторінці index 142-150 рядки коду)
    // всередині браузера, а коду немає сенсу віддавати сторінку, тому ми передаємо статус-код. А якщо клієнтом виступає
    // браузер - то ми повертаємо йому сторінки.
}

// 1) Створити кнопку для видалення вибраної групи з контактами
@GetMapping("/profession_delete_page") // Якщо приходить GET-запит
private String professionDeletePage(Model model) {
    model.addAttribute("professions", generalService.findProfessions()); // Виконуємо пошук усіх професій
    return "profession_delete_page";
}

@PostMapping(value = "/profession/delete") // Обробник для видалення професії з людьми, які мають цю професію
public String professionDelete(@RequestParam(value = "profession") long professionId) { // Приходить параметр, який ми десеріалізуємо
    // в ідентифікатор професії типу long
    try { // Щоб уникнути виключення при повному відсутності професій при їх послідовному видаленні
        Profession profession = generalService.findProfession(professionId); // Виконуємо пошук професії

        List<Person> persons = generalService.findPersons(profession); // Інжектимо список з знайденими людьми за запитуваною професією
        personRepository.deleteAll(persons); // Через репозиторій видаляємо весь список людей з однаковою професією
        generalService.deleteProfession(profession); // Через головний Сервіс видаляємо запитувану професію

    } catch (NoSuchElementException e) {
        e.printStackTrace();
    }

    return "redirect:/"; // Перерисовуємо вміст з урахуванням видалених об'єктів
}

// 2) Створити можливість завантажити список контактів з JSON файлу
@PostMapping("/persons/importFromJSON")
public String importPersons(@RequestParam(value = "profession") long professionId) {

    Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; // По id професії ми знаходимо
    // професію в базі (робимо це тільки якщо група не дефолтна)
    try {
        String json = new String(Files.readAllBytes(Paths.get("src/main/webapp/WEB-INF/static/persons_import.json"))); // Читаємо JSON
        // з вказаного документа
        Gson gson = new Gson(); // Об'єкт бібліотеки
        Person[] persons = gson.fromJson(json, Person[].class); // У масив типу Person записуємо зчитане з JSON-файлу

        for (Person p : persons) { // Проходимося по масиву людей з JSON
            p = new Person(profession, p.getName(), p.getSurname());
            generalService.addPerson(p); // Додаємо їх у базу
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "redirect:/"; // Перерисовуємо дані
}

// Тут додається людина
@PostMapping(value = "/person/add") // POST-запит на end-point /person/add
public String personAdd(@RequestParam(value = "profession") long professionId, // Ідентифікатор професії, куди додавати нового
                        // людину (тут назва параметра в Java (professionId) не збігається з назвою в формі HTML ("profession"))
                        @RequestParam String name, // Ім'я (тут назва параметра в Java збігається з назвою в формі HTML)
                        @RequestParam String surname) // Прізвище (тут назва параметра в Java збігається з назвою в формі HTML)
{
    Profession profession = (professionId != DEFAULT_PROFESSION_ID) ? generalService.findProfession(professionId) : null; // По id професії ми знаходимо
    // професію в базі (робимо це тільки якщо група не дефолтна)

    Person person = new Person(profession, name, surname); // Створюємо Entity людина з аргументами для конструктора
    generalService.addPerson(person); // За допомогою Сервісу зберігаємо людину в базі

    return "redirect:/"; // Перерисовуємо головну сторінку, з урахуванням того, що ми тільки що додали нового
}

// Тут обробник для додавання нової професії
@PostMapping(value = "/profession/add")
public String professionAdd(@RequestParam String name) { // Приходить просто рядок з форми HTML з назвою професії
    generalService.addProfession(new Profession(name)); // Створюємо професію та зберігаємо її в базі
    return "redirect:/"; // Перерисовуємо головну сторінку, з урахуванням того, що ми тільки що додали нову групу
}

private long getPageCount() { // Пейджинація - калькулятор сторінок для головної сторінки додатка
    long totalCount = generalService.count(); // Через головний Сервіс отримуємо кількість усіх рядків (об'єктів)
    return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0); // Всі рядки ділимо на кількість
    // елементів на сторінці (в нашому випадку 7), але оскільки ділення повертає ціле число без залишку - ми виконуємо додаткову операцію по
    // обчисленню залишку від ділення всіх рядків на кількість рядків на сторінці: якщо є залишок, то додаємо ще одну сторінку,
    // якщо залишку немає - не додаємо.
}

private long getPageCount(Profession profession) { // Пейджинація - калькулятор сторінок для відсортованих об'єктів (людей) за професією
    long totalCount = generalService.countByProfession(profession);
    return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
}
}

