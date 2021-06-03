package es.koldo.clarity.codetest.model;

import es.koldo.clarity.codetest.utilities.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LogLineTest {

    LogLine logLine;
    DataStore dataStore;

    @BeforeEach
    public void setUp() {
        logLine = new LogLine(123456789, "originHost", "destinationHost");
        dataStore = mock(DataStore.class);
    }

    @Test
    @DisplayName("LogLine timestamp is in range")
    void checkLogLineTimestampShouldReturnTrue() {
        boolean result;

        result = logLine.checkLineTimestamp(123456788);

        assertTrue(result);
    }

    @Test
    @DisplayName("LogLine timestamp is not in range")
    void checkLogLineTimestampShouldReturnFalseTimestampIsNotInRange() {
        boolean result;

        result = logLine.checkLineTimestamp(123456798);

        assertFalse(result);
    }

    @DisplayName("DataStore AddConnectionByHost Should be called once with any hostToConnect")
    @ParameterizedTest
    @ValueSource(strings = {"originHost", "hostToConnect", "hostname1"})
    void dataStoreAddConnectionByHostShouldBeCalledOnceWithAnyHostToConnect(
            String hostToConnect) {
        logLine.processLogLine(hostToConnect, "hostConnectedFrom", dataStore);

        Mockito.verify(dataStore, times(1)).addConnectionByHost(any());
    }

    @DisplayName("DataStore AddConnectionByHost Should be called once with any hostConnectedFrom")
    @ParameterizedTest
    @ValueSource(strings = {"destinationHost", "hostConnectedFrom", "hostname1"})
    void dataStoreAddConnectionByHostShouldBeCalledOnceWithAnyHostConnectedFrom(
            String hostConnectedFrom) {
        logLine.processLogLine("hostToConnect", hostConnectedFrom, dataStore);

        Mockito.verify(dataStore, times(1)).addConnectionByHost(any());
    }

    @Test
    @DisplayName("DataStore AddConnectionToSet Should be called once with the correct destinationHost")
    void dataStoreAddConnectionToSetShouldBeCalledOnceWithTheCorrectDestinationHost() {
        logLine.processLogLine("destinationHost", "hostname", dataStore);

        Mockito.verify(dataStore, times(1)).addConnectionToSet(any());
    }

    @Test
    @DisplayName("DataStore AddConnectionToSet is not called with the wrong destinationHost")
    void dataStoreAddConnectionToSetIsNotCalledWithThrWrongDestinationHost() {
        logLine.processLogLine("hostname1", "hostname2", dataStore);

        Mockito.verify(dataStore, never()).addConnectionToSet(any());
    }

    @Test
    @DisplayName("DataStore addConnectionFromSet Should be called once with the correct originHost")
    void dataStoreAddConnectionFromSetShouldBeCalledOnceWithTheCorrectOriginHost() {
        logLine.processLogLine("hostname", "originHost", dataStore);

        Mockito.verify(dataStore, times(1)).addConnectionFromSet(any());
    }

    @Test
    @DisplayName("DataStore addConnectionFromSet is not called with the wrong originHost")
    void dataStoreAddConnectionFromSetIsNotCalledWithThrWrongOriginHost() {
        logLine.processLogLine("hostname1", "hostname2", dataStore);

        Mockito.verify(dataStore, never()).addConnectionFromSet(any());
    }
}
