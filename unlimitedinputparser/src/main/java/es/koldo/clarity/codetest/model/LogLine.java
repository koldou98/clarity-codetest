package es.koldo.clarity.codetest.model;

import es.koldo.clarity.codetest.utilities.DataStore;

public class LogLine {
    private final long timestamp;
    private final String originHost;
    private final String destinationHost;

    public LogLine(long timestamp, String originHost, String destinationHost) {
        this.timestamp = timestamp;
        this.originHost = originHost;
        this.destinationHost = destinationHost;
    }

    public boolean checkLineTimestamp(long rangeStart) {
        return this.timestamp > rangeStart;
    }

    public void processLogLine(String hostToConnect, String hostConnectedFrom,
                               DataStore dataStore) {
        if (destinationHost.equalsIgnoreCase(hostToConnect)) {
            dataStore.addConnectionToSet(originHost);
        }
        if (originHost.equalsIgnoreCase(hostConnectedFrom)) {
            dataStore.addConnectionFromSet(destinationHost);
        }
        dataStore.addConnectionByHost(originHost);
    }
}
