/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gtas.gov;

import gtas.gov.rest.MessagePoster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class QQListener {

    private static final Logger logger = LoggerFactory
            .getLogger(QQListener.class);

    private final MessagePoster messagePoster;
    private final FileReader fileReader;

    @Autowired
    public QQListener(MessagePoster messagePoster, FileReader fileReader) {
        this.messagePoster = messagePoster;
        this.fileReader = fileReader;
    }

    @JmsListener(destination = "${servers.mq.queue}")
    public void onMessage(Message message) {
        try {
            fileReader.archiveFile(message);
        } catch (Exception e) {
            logger.error("***Error writing file to disk. Attempting to post payload anyways...**");
        }
        try {
            String messageContent = ((TextMessage) message).getText();
            messagePoster.postQMessage(messageContent);
        } catch (Exception io) {
            logger.error("Error posting payload. Will now write file to error folder...", io);
            fileReader.writeErrorFile(message);
        }
    }

}
