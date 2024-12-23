package com.example.springmytest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Repository/DAO для роботи з таблицею Професії
public interface ProfessionRepository extends JpaRepository<Profession, Long> { // для збереження Java-об'єктів в базі даних у зручному для нас вигляді,
    використовуємо систему Java Persistence API (JPA), тому успадковуємо інтерфейс JpaRepository і в дженеріку вказуємо перший параметр - Entity, а
    другий параметр - тип первинного ключа в цьому Entity - таким чином ми отримуємо ряд методів для об'єктів Групи з бази даних.

           // метод для пошуку людини(людей) за професією
    @Query("SELECT p FROM Person p WHERE p.profession = :profession") // (Java Persistence Query Language) JPQL-запит
    List<Person> findByProfession(@Param("profession") Profession profession); // у вигляді списку отримуємо перелік людей з певною професією з застосуванням пагінації сторінок

}

