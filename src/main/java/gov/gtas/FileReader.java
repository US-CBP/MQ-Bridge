/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas;

import java.io.*;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@PropertySource("classpath:application.yml")
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


    public void archiveFile(Message strContent) {
        logger.debug("************************* FILE Received ************************");
        if (archivingToDisk) {
            logger.debug("ARCHIVING FILE");
            File destFolder = new File(IN_FOLDER);
            writeFileToFolder(destFolder, strContent);
        }
        logger.debug("************************* FILE FINISHED ************************");
    }

    public void archiveFile(String content, String fileName) {
        File destFolder = new File(IN_FOLDER);
        try (BufferedWriter out = getBufferedWriter(destFolder, fileName)) {
            out.write(content);
            out.flush();
        } catch (Exception e) {
            logger.error("Exception saving message file", e);
        }
    }

    void writeErrorFile(Message strContent) {
        logger.debug("In Write Error File");
        if (failLogOn) {
            logger.debug("ARCHIVING FAILURE FILE");
            File destFolder = new File(ERROR_FOLDER);
            writeFileToFolder(destFolder, strContent);
        }
    }

    private void writeFileToFolder(File folder, Message strContent) {
        try (BufferedWriter out = getBufferedWriter(folder, strContent.getJMSMessageID().substring(4))) {
            String messageText = "";
            if (strContent instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) strContent;
                byte[] data = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(data);
                messageText =  new String(data);
            } else if (strContent instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) strContent;
                messageText = textMessage.getText();
            }
            out.write(messageText);
            out.flush();
        } catch (Exception e) {
            logger.error("Exception saving message file", e);
        }
    }

    private BufferedWriter getBufferedWriter(File folder, String strContent) throws IOException {
        String pathToGet = folder.getPath()
                + File.separator
                + strContent;
        FileSystem fileSystem = FileSystems.getDefault();
        Path path = fileSystem.getPath(pathToGet);
        return Files.newBufferedWriter(path);
    }
}
