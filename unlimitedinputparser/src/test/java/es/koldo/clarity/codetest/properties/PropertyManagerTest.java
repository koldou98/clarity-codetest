package es.koldo.clarity.codetest.properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertyManagerTest {

    static Path propertyDirectory = Path.of("properties");
    static Path propertyFile = propertyDirectory.resolve("properties.ini");
    static PropertyManager propertyManager;

    @AfterAll
    static void cleanUp() throws IOException {
        Files.delete(propertyFile);
        Files.delete(propertyDirectory);
    }

    @Test
    @DisplayName("The first instance should create the property file")
    void firstPropertyManagerInstanceShouldCreateThePropertyFile() {
        propertyManager = PropertyManager.getInstance();

        boolean exits = Files.exists(propertyFile);

        assertTrue(exits);
    }

    @Test
    @Nested
    @DisplayName("New PropertyManager instances should use the same instance")
    void newPropertyManagerInstanceShouldUseTheSameInstance() {
        PropertyManager newPropertyManager = PropertyManager.getInstance();

        assertEquals(propertyManager, newPropertyManager);
    }
}
