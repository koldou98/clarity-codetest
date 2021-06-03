package es.koldo.clarity.codetest.properties;

import es.koldo.clarity.codetest.utilities.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

public final class PropertyManager {

    private static final Logger log = LogManager.getInstance();
    private static PropertyManager propertyManager;
    private final Path propertyDirectory = Path.of("properties");
    protected final Path propertyFile = propertyDirectory.resolve(
            "properties.ini");
    private final Property filePathProperty = new FilePath();
    private final Property destinationHostProperty = new DestinationHostName();
    private final Property originHostProperty = new OriginHostName();
    protected Properties properties;

    private PropertyManager() throws IOException {
        properties = new Properties();
        if (!checkPropertyFileExists()) {
            createPropertyFile();
        }
    }

    public static PropertyManager getInstance() {
        if (propertyManager == null) {
            try {
                propertyManager = new PropertyManager();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        }
        return propertyManager;
    }

    private void createPropertyFile() throws IOException {
        Files.createDirectories(propertyDirectory);
        Files.createFile(propertyFile);
    }

    private boolean checkPropertyFileExists() {
        return Files.exists(propertyFile);
    }

    public void setFilePathProperty(String filePath) {
        filePathProperty.setProperty(filePath);
    }

    public String getDestinationHost() {
        return originHostProperty.getProperty();
    }

    public void setDestinationHost(String hostname) {
        originHostProperty.setProperty(hostname);
    }

    public String getOriginHost() {
        return destinationHostProperty.getProperty();
    }

    public void setOriginHost(String hostname) {
        destinationHostProperty.setProperty(hostname);
    }
}
