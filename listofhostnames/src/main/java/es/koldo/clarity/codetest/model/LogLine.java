package es.koldo.clarity.codetest.model;

public class LogLine {
    private final long timestamp;
    private final String originHost;
    private final String destinationHost;

    public LogLine(long timestamp, String originHost, String destinationHost) {
        this.timestamp = timestamp;
        this.originHost = originHost;
        this.destinationHost = destinationHost;
    }

    public boolean checkLogLineTimestamp(long initDateTime, long endDateTime) {
        return initDateTime < timestamp && endDateTime > timestamp;
    }

    public boolean checkDestinationHost(String destinationHost) {
        return destinationHost.equalsIgnoreCase(this.destinationHost);
    }

    public String getOriginHost() {
        return originHost;
    }
}
