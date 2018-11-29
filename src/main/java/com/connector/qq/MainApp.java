package com.connector.qq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableOAuth2Sso
public class MainApp extends SpringBootServletInitializer  {

    /*
     * Uncomment main method and delete SpringBootServletInitializer to package as .jar / stand alone micro-service.
     * */

   /*
@EnableOAuth2Sso
public class MainApp {
    public static void main(String[] args) {

        SpringApplication.run(MainApp.class, args);
    }
    */
}
