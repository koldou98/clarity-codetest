package es.koldo.clarity.codetest.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LogManager {

    private static final String LOGS_PATH_NAME = "logs";
    private static final Path LOG_DIRECTORY_PATH = Path.of(LOGS_PATH_NAME);
    private static final String LOG_FILE_NAME = "UnlimitedInputPaser.log";
    private static final Path LOG_FILE_PATH = LOG_DIRECTORY_PATH.resolve(
            LOG_FILE_NAME);
    private static Logger logger;

    private LogManager() throws IOException {
        createLogDirectoryIfItDoesNotExist();
        createLogFileIfItDoesNotExist();
        configureLogger();
    }

    public static Logger getInstance() {
        if (logger == null) {
            try {
                new LogManager();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    private static void configureLogger() throws IOException {
        logger = Logger.getLogger("log");
        logger.setLevel(Level.ALL);
        var fileHandler = createFileHandler();
        logger.addHandler(fileHandler);
    }

    private static FileHandler createFileHandler() throws IOException {
        var fileHandler = new FileHandler(LOG_FILE_PATH.toAbsolutePath()
                                                       .toString(), true);
        var simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        fileHandler.setLevel(Level.INFO);
        fileHandler.setEncoding("utf-8");
        return fileHandler;
    }

    private void createLogFileIfItDoesNotExist() throws IOException {
        if (!Files.exists(LOG_DIRECTORY_PATH.resolve(LOG_FILE_NAME))) {
            Files.createFile(LOG_FILE_PATH);
        }
    }

    private void createLogDirectoryIfItDoesNotExist() throws IOException {
        if (!Files.exists(LOG_DIRECTORY_PATH)) {
            Files.createDirectories(LOG_DIRECTORY_PATH);
        }
    }
}
