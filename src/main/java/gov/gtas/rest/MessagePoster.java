/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.rest;

import gov.gtas.model.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:application.yml")
public class MessagePoster {

    private static final Logger logger = LoggerFactory
            .getLogger(MessagePoster.class);

    private static final String API_KEY = "x-api-key";

    @Value("${rest.api.value}")
    private String apiValue;
    @Value("${rest.resource}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public MessagePoster(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postQMessage(String messageContent) {
        messageContent = stripNewLineAndCarriageReturn(messageContent);
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setMessagePayload(messageContent);
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY, apiValue);
        HttpEntity<MessagePayload> request = new HttpEntity<>(messagePayload, headers);
        restTemplate.postForEntity(baseUrl, request, String.class);
        logger.info("message sent to !" + baseUrl);
    }

    private String stripNewLineAndCarriageReturn(String messageContent) {
        messageContent = messageContent.replaceAll("\\n", "");
        messageContent = messageContent.replaceAll("\\r", "");
        return messageContent;
    }
}
