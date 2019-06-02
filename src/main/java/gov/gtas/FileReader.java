/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

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


    void archiveFile(String strContent) {
        logger.debug("************************* FILE Received ************************");
        if (archivingToDisk) {
            logger.debug("ARCHIVING FILE");
            File destFolder = new File(IN_FOLDER);
            writeFileToFolder(destFolder, strContent);
        }
        logger.debug("************************* FILE FINISHED ************************");
    }

    void writeErrorFile(String strContent) {
        logger.debug("In Write Error File");
        if (failLogOn) {
            logger.debug("ARCHIVING FAILURE FILE");
            File destFolder = new File(ERROR_FOLDER);
            writeFileToFolder(destFolder, strContent);
        }
    }

    private void writeFileToFolder(File folder, String strContent) {
        try (BufferedWriter out = getBufferedWriter(folder)) {
            out.write(strContent);
            out.flush();
        } catch (Exception e) {
            logger.error("Exception saving message file", e);
        }
    }

    private BufferedWriter getBufferedWriter(File folder) throws IOException {
        String pathToGet = folder.getPath()
                + File.separator
                + UUID.randomUUID().toString();
        FileSystem fileSystem = FileSystems.getDefault();
        Path path = fileSystem.getPath(pathToGet);
        return Files.newBufferedWriter(path);
    }
}
