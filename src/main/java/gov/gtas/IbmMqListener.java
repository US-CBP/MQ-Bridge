/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.UUID;

@Component
public class IbmMqListener {

    private static final Logger logger = LoggerFactory
            .getLogger(IbmMqListener.class);

    private final JmsTemplate jmsTemplate;
    private final FileReader fileReader;

    @Autowired
    public IbmMqListener(JmsTemplate jmsTemplate, FileReader fileReader) {
        this.jmsTemplate = jmsTemplate;
        this.fileReader = fileReader;
    }

    @JmsListener(destination = "${servers.mq.queue}")
    public void onMessage(Message message) {
        final String contents = getContents(message);
        try {
            logger.info("sending message");
            if (contents != null) {
                jmsTemplate.send(session -> {
                    javax.jms.Message m = session.createObjectMessage(contents);
                    m.setStringProperty("filename", UUID.randomUUID().toString());
                    return m;
                });
            } else {
                logger.error("Unable to process message! Contents is null or message is not text ");
            }
        } catch (Exception io) {
            logger.error("Error posting payload. Will now write message contents to error folder and try again later.", io);
            fileReader.writeErrorFile(contents);
        }
    }

    private String getContents(Message message) {
        String contents = null;
        try {
            contents = getContentsAsString(message);
        } catch (JMSException e) {
            logger.error("Unable to get file contents");
        }
        return contents;
    }

    private String getContentsAsString(Message message) throws JMSException {
        String contents = null;
        if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;
            byte[] data = new byte[(int) bytesMessage.getBodyLength()];
            bytesMessage.readBytes(data);
            bytesMessage.reset();
            contents =  new String(data);
        } else if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            contents = textMessage.getText();
        }
        return contents;
    }

}
