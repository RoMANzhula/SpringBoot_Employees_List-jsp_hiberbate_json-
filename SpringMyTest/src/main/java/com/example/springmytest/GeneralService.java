package com.example.springmytest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

////Тут ми об'єднуємо Dao/Repository
@Service
public class GeneralService { //Головний сервіс (для управління репозиторіями/DAO)
    private final PersonRepository personRepository; //Поле класу для роботи з репозиторіями класу Людина

    private final ProfessionRepository professionRepository; //Поле класу для роботи з репозиторіями класу Професія

    public GeneralService(PersonRepository personRepository, ProfessionRepository professionRepository) { //Конструктор класу
        this.personRepository = personRepository;
        this.professionRepository = professionRepository;
    }

    @Transactional
    //Анотація з JTA (Java Transaction API) - автоматична транзакція (якщо все ок - commit, якщо помилка - rollback)
    public void addPerson(Person person) { //Метод для додавання людини в базу даних
        personRepository.save(person); //Використовується метод через Repository - його SpringData-метод save()
    }

    @Transactional
    public void addProfession(Profession profession) { //Метод для додавання професії в базу даних
        professionRepository.save(profession); //Використовується метод через Repository - його SpringData-метод save()
    }

    @Transactional
    public void deletePersons(long[] listId) { //Метод для видалення людей за ідентифікатором (передаємо масив ID)
        for (long id : listId) { //Проходимо по масиву ID
            personRepository.deleteById(id); //Використовується метод через Repository - його SpringData-метод deleteById()
        }
    }

    @Transactional
    public void deleteProfession(Profession profession) { //Метод для видалення професії за ID
        professionRepository.deleteById(profession.getId());
    }

    @Transactional(readOnly = true)
    //@Transactional (тільки для читання даних) - прискорює роботу, якщо для нашого завдання достатньо тільки читання даних
    public List<Person> findPersons(Profession profession) { //Метод для пошуку людей за професією
        return professionRepository.findByProfession(profession); //Метод, який ми створили в ProfessionRepository
    }

    @Transactional(readOnly = true)
    public List<Profession> findProfessions() { //Метод - знайти всі професії з таблиці
        return professionRepository.findAll(); //Використовується метод через Repository - його SpringData-метод findAll()
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(Pageable pageable) { //Метод для витягування всіх об'єктів з таблиці (Pageable - це ліміт-запит)
        return personRepository.findAll(pageable).getContent(); //Використовується метод через Repository - його SpringData-метод findAll(pageable).getContent()
    }

    @Transactional(readOnly = true)
    public List<Person> findByProfession(Profession profession, Pageable pageable) { //Метод для отримання всіх людей з певною професією
        return personRepository.findByProfession(profession, pageable); //Метод, який ми створили в PersonRepository
    }

    @Transactional(readOnly = true)
    public long countByProfession(Profession profession) { //Метод, що підраховує кількість людей з певною професією
        return personRepository.countByProfession(profession); //Метод, який ми створили в PersonRepository
    }

    @Transactional(readOnly = true)
    public List<Person> findByPattern(String pattern, Pageable pageable) { //Метод для пошуку людей за патерном (підстрічка)
        return personRepository.findByPattern(pattern, pageable); //Метод, який ми створили в PersonRepository
    }

    @Transactional(readOnly = true)
    public long count() { //Метод - порахувати кількість людей в репозиторії
        return personRepository.count();
    }

    @Transactional(readOnly = true)
    public Profession findProfession(long id) { //Метод - знайти професію за ID в базі
        return professionRepository.findById(id).get();
    }

    @Transactional
    public void reset() { //Метод - перезавантаження (з первинним заповненням бази даних)
        professionRepository.deleteAll(); //Видалити всі професії
        personRepository.deleteAll(); //Видалити всіх людей

        //Тут ми заповнюємо базу початковими даними (першочатковий вигляд застосунку в браузері), які з'являються після натискання кнопки reset
        Profession profession1 = new Profession("Java Junior Developer"); //Створюємо професію
        Profession profession2 = new Profession("Java Middle Developer"); //Створюємо професію
        Profession profession3 = new Profession("Java Senior Developer"); //Створюємо професію
        Person person; //Створюємо контакти в групах

        addProfession(profession1); //Додаємо професію
        addProfession(profession2); //Додаємо професію
        addProfession(profession3); //Додаємо професію

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
