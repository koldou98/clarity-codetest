package es.koldo.clarity.codetest.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.logging.Logger;

public class FileUtilities {
    private static final Path OUTPUT_PATH = Path.of("output");
    private static final Logger log = LogManager.getInstance();
    private static final String LINE_TERMINATION = "\n";

    private FileUtilities() {
    }

    public static void createOutputDirectory() throws IOException {
        if (!Files.exists(OUTPUT_PATH)) {
            Files.createDirectories(OUTPUT_PATH);
        }
    }

    public static Path createOutputFileToStoreHosts() throws IOException {
        var outputFilePath = OUTPUT_PATH.resolve(String.format(
                "List-of-hosts_%d.txt",
                Instant.now().toEpochMilli()));
        Files.createFile(outputFilePath);
        return outputFilePath;
    }

    public static void appendLine(Path outputFilePath, String value) {
        try {
            Files.writeString(outputFilePath,
                              String.format("%s%s", value, LINE_TERMINATION),
                              StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }
}
