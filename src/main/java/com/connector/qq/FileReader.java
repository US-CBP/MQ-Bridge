package com.connector.qq;

import java.io.*;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@PropertySource("classpath:application.properties")
public class FileReader {

    private static final Logger logger = LoggerFactory
            .getLogger(FileReader.class);

    @Value("${archive.folder}")
    private String IN_FOLDER;

    @Value("${archive.on}")
    private Boolean archivingToDisk;


    public void writeFile( Message strContent ) {
        logger.debug("************************* FILE Received ************************");
        if(archivingToDisk) {
            logger.debug("ARCHIVING FILE");
            File destFolder = new File(IN_FOLDER);
            checkAndMoveFiles(destFolder, strContent);
        }
        logger.debug("************************* FILE FINISHED ************************");
    }

    private void checkAndMoveFiles(File folder, Message strContent) {
        BufferedWriter bufferedWriter = null;
        BufferedWriter out = null;
        try {
            FileSystem fromFileSystem = FileSystems.getDefault();
            Path moveTo = fromFileSystem.getPath(folder
                    .getPath()
                    + File.separator
                    + strContent.getJMSMessageID().substring(4)+"APIs");
            //out = Files.newBufferedWriter(moveTo);
            out = new BufferedWriter(new FileWriter(String.valueOf(moveTo)));
            out.write(((TextMessage) strContent).getText());
            out.flush();

        } catch (IOException e) {
        }
        catch (Exception e) {

            logger.info("Exception saving message file"
                    + e.getMessage());
        }
        finally{
            try{
                if(bufferedWriter != null) bufferedWriter.close();
            } catch(Exception ex){

            }
        }
    }
}
