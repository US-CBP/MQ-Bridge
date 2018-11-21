package com.connector.qq.schedular;

import com.connector.qq.rest.RestTemplatePush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Component
@ConditionalOnProperty("failLog.cronjob.on")
public class RetryErrorMessagesTask {

    private Logger logger = LoggerFactory.getLogger(RetryErrorMessagesTask.class);

    @Value("${failLog.folder}")
    private String ERROR_FOLDER;

    @Value("${failLog.retryUntilSent}")
    private Boolean retryUntilSent;

    private final
    RestTemplatePush restTemplatePush;


    @Autowired
    public RetryErrorMessagesTask(RestTemplatePush restTemplatePush) {
        this.restTemplatePush = restTemplatePush;
    }

    @Scheduled(cron = "${failLog.cronjob.task.1}")
    @Scheduled(cron = "${failLog.cronjob.task.2}")
    public void resendErrorMessages() {
        listFilesForFolder();
    }

    private void listFilesForFolder() {
        File folder = new File(ERROR_FOLDER);
        List<File> files = folder.listFiles() == null ?
                Collections.EMPTY_LIST : Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        for (final File errorFile : files) {
            if (!errorFile.isDirectory() && errorFile.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(errorFile.toPath()));
                    restTemplatePush.pushQMessage(content);
                    deleteFile(errorFile);
                } catch (Exception e) {
                    logger.error("Failed to RESEND message " + errorFile.getName() + " !");
                    if (!retryUntilSent) {
                        logger.debug("Deleting failed message.");
                        deleteFile(errorFile);
                    }
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
