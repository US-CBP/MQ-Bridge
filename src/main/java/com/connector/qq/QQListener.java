package com.connector.qq;

import com.connector.qq.rest.RestTemplatePush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class QQListener {

    private static final Logger logger = LoggerFactory
            .getLogger(QQListener.class);

    private final RestTemplatePush restTemplatePush;
    private final FileReader fileReader;

    @Autowired
    public QQListener(RestTemplatePush restTemplatePush, FileReader fileReader) {
        this.restTemplatePush = restTemplatePush;
        this.fileReader = fileReader;
    }

    @JmsListener(destination = "${servers.mq.queue}")
    public void onMessage(Message message) {
        try {
            fileReader.writeFile(message);
        } catch (Exception e) {
            logger.error("***Error writing file to disk. Attempting to post payload anyways...**");
        }
        try{
            restTemplatePush.pushQMessage(message);
        }catch (Exception io){
            logger.error("Error posting payload"+ io.getCause());
        }
    }

}
