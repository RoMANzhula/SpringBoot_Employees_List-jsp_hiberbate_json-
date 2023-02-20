package com.example.springmytest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Repository/DAO для работы с таблицей Профессии
public interface ProfessionRepository extends JpaRepository<Profession, Long> { //для сохранения Java-обьектов в DataBase, в удобном для
    //нас виде, мы используем систему Java Persistence API (JPA), поэтому наследуемся от интерфейса JpaRepository и в дженерике
    //указываем первый параметр- Entity, а второй параметр - тип первичного ключа в этом Entity - таким образом мы получаем
    //ряд методов для обьектов Группы из DataBase

           //метод для поиска человека(людей) по профессии
    @Query("SELECT p FROM Person p WHERE p.profession = :profession") //(Java Persistence Query Language)JPQL-запрос
    List<Person> findByProfession(@Param("profession") Profession profession); //в виде списка получаем перечень людей с определенной
    // профессией с применением пейдженации страниц

}
