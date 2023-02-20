package com.example.springmytest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Persons")
@Getter //автогенерация Геттерами
@Setter //автогенерация Сеттерами
public class Person { //таблица Контакты в DataBase
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne //много человек могут иметь одну профессию
    @JoinColumn(name = "profession_id")
    private Profession profession;

    private String name;

    private String surname;

    public Person() {}

    public Person(Profession profession, String name, String surname) {
        this.profession = profession;
        this.name = name;
        this.surname = surname;
    }

}
