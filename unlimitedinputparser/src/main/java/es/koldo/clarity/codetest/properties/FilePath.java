package es.koldo.clarity.codetest.properties;

public class FilePath implements Property {
    private static final String PROPERTY_NAME = "FilePath";

    @Override
    public String getProperty() {
        loadProperties();
        return propertyManager.properties.getProperty(PROPERTY_NAME);
    }

    @Override
    public void setProperty(String filePath) {
        propertyManager.properties.setProperty(PROPERTY_NAME, filePath);
        writeProperty();
    }
}
