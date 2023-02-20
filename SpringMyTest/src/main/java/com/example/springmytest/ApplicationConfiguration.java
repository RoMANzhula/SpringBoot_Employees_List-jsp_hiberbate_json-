package com.example.springmytest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //данный метод при Get-запросах позволяет
        registry
                .addResourceHandler("/static/**") //на end-point - /static/** (слеш-статик-слеш-что угодно(две звездочки
                //означают что угодно) привязать какие=то данные
                .addResourceLocations("/WEB-INF/static/"); //это что угодно (/**) нужно искать в папке /WEB-INF/static/
    }

    @Bean
    public CommandLineRunner demo(final GeneralService generalService) { //описываем Бин, у которого тип CommandLineRunner - в demo
        // инжектим Сервис-ContactService contactService

        return new CommandLineRunner() { //создаем обьект анонимного класса, у которого есть метод run()

            @Override //переопрделяем метод run(), в который
            public void run(String... strings) throws Exception { //передаются все параметры, которые были в main(), данный метод
                //выполняется один раз при старте приложения

                    //пишем сюда код, который уже может использовать Beans

                //тут мы наполняем базу первичными данными (начальный вид приложения в браузере)
                Profession profession1 = new Profession("Java Junior Developer"); //создаем профессию
                Profession profession2 = new Profession("Java Middle Developer"); //создаем профессию
                Profession profession3 = new Profession("Java Senior Developer"); //создаем профессию
                Person person; //создаем ссылку типа еловек

                generalService.addProfession(profession1); //через Сервис добавляем профессию
                generalService.addProfession(profession2);//через Сервис добавляем профессию
                generalService.addProfession(profession3);//через Сервис добавляем профессию

                for (int i = 1; i < 6; i++) { //через цикл создаем 5 человек
                    person = new Person(profession1, "Genius" + i, "Ordinary" + i);
                    generalService.addPerson(person); //через Срвис добавляем их
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
