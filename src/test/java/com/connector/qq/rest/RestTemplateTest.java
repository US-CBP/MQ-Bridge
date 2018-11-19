package com.connector.qq.rest;

import com.connector.qq.model.MessagePayload;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTemplateTest {


    private static final Logger logger = LoggerFactory
            .getLogger(RestTemplateTest.class);

    @Value("${rest.resource}")
    private String baseUrl;
    @Value("${oauth.authorize:http://localhost:8082/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://localhost:8082/oauth/token}")
    private String tokenUrl;


    @Test
    @Ignore
    public void testRestTemplateWithPostPayload() {

        RestTemplate restTemplate = new RestTemplate();
//        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        rt.getMessageConverters().add(new StringHttpMessageConverter());
        MessagePayload messagePayload = new MessagePayload();


//        String plainCreds = "user@awesome.com:sfdfsdf$%&^$%4";
//        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("X-COM-LOCATION", "USA");

        HttpEntity<MessagePayload> request = new HttpEntity<>(messagePayload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);


        logger.info("Account Content:" + response.getBody());

//        LoginForm loginForm = new LoginForm("username", "password");
//        HttpEntity<LoginForm> requestEntity
//                = new HttpEntity<LoginForm>(loginForm);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity<String> responseEntity
//                = restTemplate.postForEntity(
//                "http://httpbin.org/post", requestEntity, String.class
//        );
//
//        assertThat(
//                responseEntity.getStatusCode(),
//                is(equalTo(HttpStatus.OK))
//        );
//        assertThat(
//                responseEntity.getHeaders().get("Foo").get(0),
//                is(equalTo("bar"))
//        );
    }
}
