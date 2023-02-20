package com.example.springmytest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Professions")
@Getter
@Setter
public class Profession { //таблица Профессии в DataBase

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "profession", cascade = CascadeType.ALL)
    //есть коллекция человек, которая принадлежит отдельной профессии
    private List<Person> persons = new ArrayList<Person>(); //список контаков для каждой отлдельной Группы

    public Profession() {
    }

    public Profession(String name) {
        this.name = name;
    }

}
