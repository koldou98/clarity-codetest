package es.koldo.clarity.codetest.properties;

import es.koldo.clarity.codetest.utilities.LogManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public interface Property {
    PropertyManager propertyManager = PropertyManager.getInstance();
    Logger log = LogManager.getInstance();

    String getProperty();

    void setProperty(String value);

    default void writeProperty() {
        try (var fileOutputStream = new FileOutputStream(propertyManager.propertyFile
                                                                 .toFile())) {
            propertyManager.properties.storeToXML(fileOutputStream, null);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    default void loadProperties() {
        try (var fileInputStream = new FileInputStream(propertyManager.propertyFile
                                                               .toFile())) {
            propertyManager.properties.loadFromXML(fileInputStream);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }
}
