package com.connector.qq;

import java.io.*;
import java.nio.file.*;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class FileReader {

    private static final Logger logger = LoggerFactory
            .getLogger(FileReader.class);


    public void writeFile( Message strContent ) {
        logger.info("************************* FILE Received ************************"
                + strContent);
        logger.info("*************************************************************************************");
        Properties properties = getSchedulerProperties();

        if (properties != null) {
            File destFolder = new File(
                    properties.getProperty("dir.dest"));


            checkAndMoveFiles(destFolder, strContent);

            logger.info("*************************************************************************************");
        }
    }

    private void checkAndMoveFiles(File folder, Message strContent) {
        BufferedWriter bufferedWriter = null;
        BufferedWriter out = null;
        try {

            FileSystem fromFileSystem = FileSystems.getDefault();
            Path moveFrom = fromFileSystem.getPath(folder
                    .getPath());
            Path moveTo = fromFileSystem.getPath(folder
                            .getPath()
                    + File.separator
                    + strContent.getJMSMessageID().substring(4)+"APIs");


            //out = Files.newBufferedWriter(moveTo);
            out = new BufferedWriter(new FileWriter(String.valueOf(moveTo)));
            out.write(((TextMessage) strContent).getText());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
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

    public Properties getSchedulerProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(
                    "fileLocation.properties").getFile());
            input = new FileInputStream(file);
            prop.load(input);
        } catch (IOException e1) {
            logger.info("Exception loading fileLocation.properties"
                    + e1.getMessage());
        }
        return prop;
    }

}
