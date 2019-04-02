package com.freeco;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.SpringApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(basePackages ="org.adcommon")
//@ComponentScan(value = {"com.duowan.taskReRun.action,com.duowan.taskReRun.service,com.duowan.taskReRun.dao"})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@EnableAutoConfiguration(exclude={SecurityAutoConfiguration.class})
@EnableWebSecurity
public class App {
    public static void main(String[] args) {
//        ConfigurableApplicationContext run = run(App.class, args);
        SpringApplication.run(App.class, args);
//        org.apache.log4j.Logger
    }

}
