package com.connector.qq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@ComponentScan("com.connector.qq")
public class MainApp {

    @Autowired
    private JmsTemplate queueTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}
