package com.connector.qq.rest;

import com.connector.qq.model.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@PropertySource("classpath:application.properties")
public class RestTemplatePush {

    private static final Logger logger = LoggerFactory
            .getLogger(RestTemplatePush.class);

    @Value("${rest.resource}")
    private String baseUrl;
    @Value("${oauth.authorize:http://localhost:8082/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://localhost:8082/oauth/token}")
    private String tokenUrl;


    public void pushQMessage(Message payload){

        RestTemplate restTemplate = new RestTemplate();
        // stuff the message into an object to send over
        MessagePayload messagePayload = new MessagePayload();

        try {
            messagePayload.setMessagePayload(((TextMessage) payload).getText());

            HttpHeaders headers = new HttpHeaders();
            //headers.set("X-COM-LOCATION", "USA");
            //any other needed headers go here

            HttpEntity<MessagePayload> request = new HttpEntity<>(messagePayload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
            logger.info("Response Received:" + response.getBody());

        }catch (Exception ex){
            logger.error("Error posting payload"+ ex.getCause());
            ex.printStackTrace();
        }
    }



}
