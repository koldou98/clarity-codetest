package es.koldo.clarity.codetest.utilities;

import es.koldo.clarity.codetest.UnlimitedInputParser;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DataStore {

    private static final Logger log = LogManager.getInstance();
    private final Set<String> connectionsFromSet;
    private final Set<String> connectionsToSet;
    private final Map<String, Integer> connectionsByHost;

    public DataStore() {
        this.connectionsFromSet = ConcurrentHashMap.newKeySet();
        this.connectionsToSet = ConcurrentHashMap.newKeySet();
        this.connectionsByHost = new ConcurrentHashMap<>();
    }

    public void addConnectionFromSet(String host) {
        this.connectionsFromSet.add(host);
    }

    public void addConnectionToSet(String host) {
        this.connectionsToSet.add(host);
    }

    public void addConnectionByHost(String host) {
        this.connectionsByHost.merge(host, 1, Integer::sum);
    }

    private void resetSets() {
        this.connectionsFromSet.clear();
        this.connectionsToSet.clear();
        this.connectionsByHost.clear();
    }

    public Runnable printData() {
        return () -> {
            var hostToConnect = UnlimitedInputParser.getHostToConnect();
            var hostConnectedFrom = UnlimitedInputParser.getHostConnectedFrom();

            var output = String.format("Report from the last hour:\n" +
                                               "List of hosts connected to" +
                                               " %s:\n%s\nList of hosts " +
                                               "received connection from " +
                                               "%s:\n%s\nHostname with " +
                                               "most connections:\n%s",
                                       hostToConnect,
                                       connectionsToSet,
                                       hostConnectedFrom,
                                       connectionsFromSet,
                                       getHostWithMostConnections());
            log.info(output);
            UnlimitedInputParser.updateValues();
            resetSets();
        };
    }

    private Map.Entry<String, Integer> getHostWithMostConnections() {
        return connectionsByHost.entrySet()
                                .parallelStream()
                                .max(Map.Entry.comparingByValue())
                                .orElse(new AbstractMap.SimpleEntry<>(
                                        "No connections in the last hour",
                                        0));
    }
}
