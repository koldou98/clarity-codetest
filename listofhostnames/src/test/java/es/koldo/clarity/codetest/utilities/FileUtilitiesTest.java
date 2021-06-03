package es.koldo.clarity.codetest.utilities;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilitiesTest {

    static final Path OUTPUT_PATH = Path.of("output");

    @BeforeEach
    void setUpEach() throws IOException {
        Files.createDirectories(OUTPUT_PATH);
    }

    @AfterEach
    void cleanUpEach() throws IOException {
        Files.newDirectoryStream(OUTPUT_PATH).forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Files.delete(OUTPUT_PATH);
    }

    @Test
    @DisplayName("Creates the output directory")
    void createOutputDirectoryWhenTheOutputDirectoryDoesNotExistShouldCreateTheDirectory()
            throws IOException {
        Files.delete(OUTPUT_PATH);
        FileUtilities.createOutputDirectory();

        boolean exists = Files.exists(OUTPUT_PATH);

        assertTrue(exists);
    }

    @Test
    @DisplayName("The output directory already exists so it does not create")
    void checkIfOutputDirectoryExistsBeforeCreating() throws IOException {
        FileUtilities.createOutputDirectory();

        boolean exists = Files.exists(OUTPUT_PATH);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Check that the output file exists on the output folder")
    void checkTheCreatedOutputFileExistsAfterCreation() throws IOException {
        Path outputFilePath = FileUtilities.createOutputFileToStoreHosts();

        boolean fileExists = Files.exists(outputFilePath);

        assertTrue(fileExists);
    }

    @Test
    @DisplayName("Check that the created output file has no content")
    void checkCreatedOutputFileIsEmpty() throws IOException {
        Path outputFilePath = FileUtilities.createOutputFileToStoreHosts();

        long fileSize = Files.size(outputFilePath);

        assertEquals(0, fileSize);
    }

    @Test
    @DisplayName("Check if the appended content match on file")
    void checkAppendedContentMatchesHelloWithLineBreak() throws IOException {
        var filePath = OUTPUT_PATH.resolve("test");
        Files.createFile(filePath);
        FileUtilities.appendLine(filePath, "Hello");

        String fileContent = Files.readString(filePath);
        assertThat(fileContent, Matchers.equalToIgnoringCase("Hello\n"));
    }

}
