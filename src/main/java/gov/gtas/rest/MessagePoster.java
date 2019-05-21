/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.rest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@PropertySource("classpath:application.yml")
public class MessagePoster {

    private final
    JmsTemplate jmsTemplateFile;

    @Autowired
    public MessagePoster(JmsTemplate jmsTemplateFile) {
        this.jmsTemplateFile = jmsTemplateFile;
    }

    public void postQMessage(String messageContent) {
        jmsTemplateFile.send(session -> {
            javax.jms.Message m = session.createObjectMessage(messageContent);
            m.setStringProperty("filename", UUID.randomUUID().toString());
            return m;
        });
    }


    public void postQMessageWithFileName(String messageContent, String fileName) {
        jmsTemplateFile.send(session -> {
            javax.jms.Message m = session.createObjectMessage(messageContent);
            m.setStringProperty("filename", fileName);
            return m;
        });
    }
}
