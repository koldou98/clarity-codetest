package es.koldo.clarity.codetest.utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class FileUtilities {
    private static Path inputLogsDirectoryPath = Path.of("inputLogs");
    private static Path logFileName;

    private FileUtilities() {
    }

    public static void writeLine(String line) throws IOException {
        Files.writeString(logFileName,
                          line,
                          StandardCharsets.UTF_8,
                          StandardOpenOption.APPEND);
    }

    public static void createLogFileInInputLogsDirectory() throws IOException {
        if (checkInputLogsDirectoryDoesNotExist()) {
            Files.createDirectories(inputLogsDirectoryPath);
        }
        logFileName = inputLogsDirectoryPath.resolve(String.format("log-%s.txt",
                                                                   Instant.now()
                                                                          .toEpochMilli()));
        System.out.println("Log can be found here: " + logFileName.toAbsolutePath());
        Files.createFile(logFileName);
    }

    private static boolean checkInputLogsDirectoryDoesNotExist() {
        return !Files.exists(FileUtilities.inputLogsDirectoryPath);
    }

    public static void setInputLogsDirectoryPath(Path inputLogsDirectoryPath) {
        FileUtilities.inputLogsDirectoryPath = inputLogsDirectoryPath;
    }
}
