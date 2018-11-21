package com.connector.qq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainApp extends SpringBootServletInitializer  {

    /*
     * Uncomment main method and delete SpringBootServletInitializer to package as .jar / stand alone micro-service.
     * */

   /*
    public static void main(String[] args) {

        SpringApplication.run(MainApp.class, args);
    }
    */
}
