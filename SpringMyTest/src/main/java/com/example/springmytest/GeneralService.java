package com.example.springmytest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

////Тут мы обьединяем Dao/Repository
@Service
public class GeneralService { //Главный Сервис (для управления Репозиториями/Дао)
    private final PersonRepository personRepository; //поле класса для работы с репозиториями класса Человек

    private final ProfessionRepository professionRepository; //поле класса для работы с репозиториями класса Профессия

    public GeneralService(PersonRepository personRepository, ProfessionRepository professionRepository) { //конструктор класса
        this.personRepository = personRepository;
        this.professionRepository = professionRepository;
    }

    @Transactional
    //аннотация из JTA (Java Transaction API) - автоматический Transaction (если ОК - commit, если Exception - rollback)
    public void addPerson(Person person) { //метод добавить человека в DataBase
        personRepository.save(person); //используется метод через Repository- его springDate-метод save()
    }


    @Transactional
    public void addProfession(Profession profession) { //метод добавить профессию в DataBase
        professionRepository.save(profession); //используется метод через Repository- его springDate-метод save()
    }

    @Transactional
    public void deletePersons(long[] listId) { //метод удалить человека по айдишнику (передаем на вход массив айдишников)
        for (long id : listId) { //проходим по массиву айдишников
            personRepository.deleteById(id); //используется метод через Repository- его springDate-метод deleteById()
        }
    }

    @Transactional
    public void deleteProfession(Profession profession) { //метод для удаления профессии по айдишнику
        professionRepository.deleteById(profession.getId());
    }

    @Transactional(readOnly = true)
    //@Transactional(только для чтения данных) - ускоряет работу, если для нашей задачи достаточно только чтения данных
    public List<Person> findPersons(Profession profession) { //метод для поиска человека по професии
        return professionRepository.findByProfession(profession); //метод, который мы создали в ProfessionRepository
    }


    @Transactional(readOnly = true)
    public List<Profession> findProfessions() { //метод - найти все профессии из таблицы
        return professionRepository.findAll(); //используется метод через Repository- его springDate-метод findAll()
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(Pageable pageable) { //метод вытащить все обьекты из таблицы (Pageable - это лимит-запрос)
        return personRepository.findAll(pageable).getContent(); //используется метод через Repository- его springDate-метод
        // findAll(pageable).getContent()
    }

    @Transactional(readOnly = true)
    public List<Person> findByProfession(Profession profession, Pageable pageable) { //метод для получения всех людей с определенной профессией
        return personRepository.findByProfession(profession, pageable); //метод, который мы создали в PersonRepository
    }

    @Transactional(readOnly = true)
    public long countByProfession(Profession profession) { //метод считает количество людей с определенной профессией
        return personRepository.countByProfession(profession); //метод, который мы создали в PersonRepository
    }

    @Transactional(readOnly = true)
    public List<Person> findByPattern(String pattern, Pageable pageable) { //метод для поиска человека по искомой подстроке('%', :pattern, '%')
        return personRepository.findByPattern(pattern, pageable); // метод, который мы создали в PersonRepository
    }

    @Transactional(readOnly = true)
    public long count() { //метод - посчитать людей в Репозитории
        return personRepository.count();
    }

    @Transactional(readOnly = true)
    public Profession findProfession(long id) { //метод - по айдишнику найти профессию в базе
        return professionRepository.findById(id).get();
    }

    @Transactional
    public void reset() { //метод - перезагрузка (с первоначальным заполнением базы данных)
        professionRepository.deleteAll(); //удалить все профессии
        personRepository.deleteAll(); //удалить всех людей

        //тут мы наполняем базу первичными данными (начальный вид приложения в браузере), к которым приводит нажатие кнопки reset
        Profession profession1 = new Profession("Java Junior Developer"); //создаем профессию
        Profession profession2 = new Profession("Java Middle Developer"); //создаем профессию
        Profession profession3 = new Profession("Java Senior Developer"); //создаем профессию
        Person person; //создаем контакты в группах

        addProfession(profession1); //добавляем профессию
        addProfession(profession2); //добавляем профессию
        addProfession(profession3); //добавляем профессию

        for (int i = 1; i < 6; i++) {
            person = new Person(profession1, "Genius" + i, "Ordinary" + i);
            addPerson(person);
        }
        for (int i = 6; i < 11; i++) {
            person = new Person(profession2, "Genius" + i, "Normal" + i);
            addPerson(person);
        }
        for (int i = 11; i < 16; i++) {
            person = new Person(profession3, "Genius" + i, "Advanced" + i);
            addPerson(person);
        }
    }
}
