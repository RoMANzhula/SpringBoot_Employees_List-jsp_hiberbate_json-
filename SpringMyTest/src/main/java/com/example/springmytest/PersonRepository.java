package com.example.springmytest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Repository/DAO для работы с таблицей Человек
public interface PersonRepository extends JpaRepository<Person, Long> {

            //метод для получения всех людей с определенной профессией, с помощью аннотации @Query
    @Query("SELECT p FROM Person p WHERE p.profession = :profession") //(Java Persistence Query Language)JPQL-запрос
    List<Person> findByProfession(@Param("profession") Profession profession, Pageable pageable); //в результате получаем список из таблицы Человек'и :-)
    //на вход передаем profession, из которой нужно достать всех людей, и с помощью аннотации @Param идет связка метода findByProfession
    //с JPQL-запросом. Параметр Pageable pageable позволяет методу поддерживать view-запрос (для отображения на страницах) - пейдженацию

            //метод считает количество людей с определенной профессией
    @Query("SELECT COUNT(p) FROM Person p WHERE p.profession = :profession") //(Java Persistence Query Language)JPQL-запрос
    long countByProfession(@Param("profession") Profession profession);

            //метод для поиска человека по искуемой подстроке('%', :pattern, '%') в именах людей
    @Query("SELECT p FROM Person p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pattern, '%'))") //(Java Persistence Query Language)JPQL-запрос (можно
    //дополнить - ...WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " + "LOWER(c.surname) LIKE LOWER(CONCAT('%', :pattern, '%'))"... и
    // будет искать дополнительно и по фамилии тоже
    List<Person> findByPattern(@Param("pattern") String pattern, Pageable pageable); //в виде списка получаем результат с применением пейдженации
    //страниц

}
