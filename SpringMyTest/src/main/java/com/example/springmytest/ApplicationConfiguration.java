package com.example.springmytest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //цей метод дозволяє при Get-запитах
        registry
                .addResourceHandler("/static/**") //на endpoint - /static/** (слеш-статик-слеш-що завгодно (дві зірочки
                //означають що завгодно) прив'язати які-небудь дані
                .addResourceLocations("/WEB-INF/static/"); //ці дані (/**) потрібно шукати в папці /WEB-INF/static/
    }

    @Bean
    public CommandLineRunner demo(final GeneralService generalService) { //описуємо Bean, тип якого CommandLineRunner - в demo
        // інжектимо Сервіс-ContactService contactService

        return new CommandLineRunner() { //створюємо об'єкт анонімного класу, у якого є метод run()

            @Override //перевизначаємо метод run(), в який
            public void run(String... strings) throws Exception { //передаються всі параметри, що були в main(), цей метод
                //виконується один раз при старті додатку

                    //пишемо код, який може використовувати Beans

                //тут ми заповнюємо базу первинними даними (початковий вигляд додатку в браузері)
                Profession profession1 = new Profession("Java Junior Developer"); //створюємо професію
                Profession profession2 = new Profession("Java Middle Developer"); //створюємо професію
                Profession profession3 = new Profession("Java Senior Developer"); //створюємо професію
                Person person; //створюємо посилання типу людина

                generalService.addProfession(profession1); //через Сервіс додаємо професію
                generalService.addProfession(profession2); //через Сервіс додаємо професію
                generalService.addProfession(profession3); //через Сервіс додаємо професію

                for (int i = 1; i < 6; i++) { //через цикл створюємо 5 осіб
                    person = new Person(profession1, "Genius" + i, "Ordinary" + i);
                    generalService.addPerson(person); //через Сервіс додаємо їх
                }
                for (int i = 6; i < 11; i++) {
                    person = new Person(profession2, "Genius" + i, "Normal" + i);
                    generalService.addPerson(person);
                }
                for (int i = 11; i < 16; i++) {
                    person = new Person(profession3, "Genius" + i, "Advanced" + i);
                    generalService.addPerson(person);
                }
            }
        };
    }
}
