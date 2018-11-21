package com.connector.qq;

import java.io.*;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@PropertySource("classpath:application.properties")
public class FileReader {

    private static final Logger logger = LoggerFactory
            .getLogger(FileReader.class);

    @Value("${archive.folder}")
    private String IN_FOLDER;

    @Value("${failLog.folder}")
    private String ERROR_FOLDER;

    @Value("${archive.on}")
    private Boolean archivingToDisk;

    @Value("${failLog.on}")
    private Boolean failLogOn;


    void archiveFile(Message strContent) {
        logger.debug("************************* FILE Received ************************");
        if (archivingToDisk) {
            logger.debug("ARCHIVING FILE");
            File destFolder = new File(IN_FOLDER);
            checkAndMoveFiles(destFolder, strContent);
        }
        logger.debug("************************* FILE FINISHED ************************");
    }

    void writeErrorFile(Message strContent) {
        logger.debug("In Write Error File");
        if (failLogOn) {
            logger.debug("ARCHIVING FAILURE FILE");
            File destFolder = new File(ERROR_FOLDER);
            checkAndMoveFiles(destFolder, strContent);
        }
    }

    private void checkAndMoveFiles(File folder, Message strContent) {
        try (FileWriter writer = makeFileWriter(folder, strContent);
             BufferedWriter out = new BufferedWriter(writer)
        ) {
            TextMessage textMessage = (TextMessage) strContent;
            out.write(textMessage.getText());
            out.flush();
        } catch (Exception e) {
            logger.info("Exception saving message file"
                    , e);
        }
    }

    private FileWriter makeFileWriter(File folder, Message strContent) throws JMSException, IOException {
        FileSystem fromFileSystem = FileSystems.getDefault();
        Path path = fromFileSystem.getPath(folder.getPath()
                + File.separator
                + strContent.getJMSMessageID().substring(4) + "APIs");
        String stingValueOfPath = String.valueOf(path);
        return new FileWriter(stingValueOfPath);
    }
}
