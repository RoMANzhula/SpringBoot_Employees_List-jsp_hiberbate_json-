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
public class Profession { //таблиця Професії в DataBase

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "profession", cascade = CascadeType.ALL)
    //є колекція людина, яка до окремої професії
    private List<Person> persons = new ArrayList<Person>(); //список контаків для кажної окремої Групи

    public Profession() {
    }

    public Profession(String name) {
        this.name = name;
    }

}
