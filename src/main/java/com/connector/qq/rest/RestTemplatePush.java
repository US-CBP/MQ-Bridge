package com.connector.qq.rest;

import com.connector.qq.model.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@PropertySource("classpath:application.properties")
public class RestTemplatePush {

    private static final Logger logger = LoggerFactory
            .getLogger(RestTemplatePush.class);

    private static final String API_KEY = "x-api-key";

    @Value("${rest.api.value}")
    private String apiValue;
    @Value("${rest.resource}")
    private String baseUrl;
    @Value("${oauth.authorize:http://localhost:8082/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://localhost:8082/oauth/token}")
    private String tokenUrl;


    public void pushQMessage(Message payload) throws JMSException {
        RestTemplate restTemplate = new RestTemplate();
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setMessagePayload(((TextMessage) payload).getText());
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, apiValue);
        HttpEntity<MessagePayload> request = new HttpEntity<>(messagePayload, headers);
        restTemplate.postForEntity(baseUrl, request, String.class);
    }
}
