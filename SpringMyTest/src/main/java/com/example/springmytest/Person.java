package com.example.springmytest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Persons")
@Getter //автогенерація Геттерами
@Setter //автогенерація Сеттерами
public class Person { //таблиця Контакти в DataBase
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne //багато людей може мати одну професію
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
