package ru.somehost.javaexamples.spring.mtrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan("ru.somehost.javaexamples.spring.mtrp.*")
public class MtrpApplication {
    public static void main(String[] args){
        SpringApplication.run(MtrpApplication.class, args);
    }
}
