/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.schedular;

import gov.gtas.FileReader;
import gov.gtas.rest.MessagePoster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Component
@ConditionalOnProperty("failLog.cronjob.on")
public class RetryErrorMessagesTask {

    private final Logger logger = LoggerFactory.getLogger(RetryErrorMessagesTask.class);
    private final FileReader fileReader;

    @Value("${failLog.folder}")
    private String ERROR_FOLDER;

    @Value("${failLog.retryUntilSent}")
    private Boolean retryUntilSent;

    @Value("${archive.on}")
    private Boolean archivingToDisk;

    @Value("${archive.folder}")
    private String IN_FOLDER;

    private final
    MessagePoster messagePoster;

    @Autowired
    public RetryErrorMessagesTask(MessagePoster messagePoster, FileReader fileReader) {
        this.messagePoster = messagePoster;
        this.fileReader = fileReader;
    }

    @Scheduled(cron = "${failLog.cronjob.task.1}")
    @Scheduled(cron = "${failLog.cronjob.task.2}")
    public void resendErrorMessages() {
        File folder = new File(ERROR_FOLDER);
        List<File> files = folder.listFiles() == null ?
                new ArrayList<>() : Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        for (final File errorFile : files) {
            if (!errorFile.isDirectory() && errorFile.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(errorFile.toPath()), StandardCharsets.UTF_8);
                    messagePoster.postQMessageWithFileName(content, errorFile.getName());
                    if (archivingToDisk) {
                        fileReader.archiveFile(content, errorFile.getName());
                        deleteFile(errorFile);
                    } else {
                        deleteFile(errorFile);
                    }
                } catch (Exception e) {
                    logger.error("Failed to send message ", e);
                }
            }
        }
    }

    private void deleteFile(File erroredFile) {
        if (!erroredFile.delete()) {
            logger.error("Failed to delete file " + erroredFile.getName() + " will send a duplicate next time" +
                    "Scheduled task is run!!");
        }
    }
}
