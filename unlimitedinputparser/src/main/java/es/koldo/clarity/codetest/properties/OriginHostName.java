package es.koldo.clarity.codetest.properties;

public class OriginHostName implements Property {
    private static final String PROPERTY_NAME = "OriginHostName";

    @Override
    public String getProperty() {
        loadProperties();
        return propertyManager.properties.getProperty(PROPERTY_NAME);
    }

    @Override
    public void setProperty(String hostName) {
        propertyManager.properties.setProperty(PROPERTY_NAME, hostName);
        writeProperty();
    }
}
