package com.example.springmytest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Репозиторій/DAO для роботи з таблицею Чоловік
public interface PersonRepository extends JpaRepository<Person, Long> {

    // метод для отримання всіх людей з певною професією, за допомогою анотації @Query
    @Query("SELECT p FROM Person p WHERE p.profession = :profession") //(Java Persistence Query Language) JPQL-запит
    List<Person> findByProfession(@Param("profession") Profession profession, Pageable pageable); // в результаті отримуємо список з таблиці Чоловік
    // на вхід передаємо profession, за якою потрібно знайти всіх людей, і за допомогою анотації @Param відбувається зв'язок методу findByProfession
    // з JPQL-запитом. Параметр Pageable pageable дозволяє методу підтримувати відображення на сторінках (пагінація)

    // метод для підрахунку кількості людей з певною професією
    @Query("SELECT COUNT(p) FROM Person p WHERE p.profession = :profession") //(Java Persistence Query Language) JPQL-запит
    long countByProfession(@Param("profession") Profession profession);

    // метод для пошуку людини за шаблоном у іменах людей ('%', :pattern, '%') 
    @Query("SELECT p FROM Person p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pattern, '%'))") //(Java Persistence Query Language) JPQL-запит (можна
    // доповнити - ...WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " + "LOWER(c.surname) LIKE LOWER(CONCAT('%', :pattern, '%'))"... і
    // буде шукати також і за прізвищем
    List<Person> findByPattern(@Param("pattern") String pattern, Pageable pageable); // отримуємо результат у вигляді списку з пагінацією
    // сторінок

}
