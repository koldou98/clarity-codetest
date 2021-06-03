package es.koldo.clarity.codetest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogLineTest {

    LogLine logLine;

    @BeforeEach
    public void setUp() {
        logLine = new LogLine(123456789, "originHost", "destinationHost");
    }

    @Test
    @DisplayName("LogLine timestamp is between the initial DateTime and the end DateTime")
    void checkLogLineTimestampBetweenRangeAndReturnsTrue() {
        boolean result;

        result = logLine.checkLogLineTimestamp(0, 999999999);

        assertTrue(result);
    }

    @Test
    @DisplayName("LogLine timestamp is before the initial DateTime")
    void checkLogLineTimestampChecksTimestampBeforeInitDateTimeAndReturnsFalse() {
        boolean result;

        result = logLine.checkLogLineTimestamp(123456799, 999999999);

        assertFalse(result);
    }

    @Test
    @DisplayName("LogLine timestamp is before the initial DateTime")
    void checkLogLineTimestampChecksTimestampAfterEndDateTimeAndReturnsFalse() {
        boolean result;

        result = logLine.checkLogLineTimestamp(123456779, 123456788);

        assertFalse(result);
    }

    @Test
    @DisplayName("LogLine destination host is the same as in the object (case do match)")
    void checkDestinationHostWhenCaseDoMatchDestinationHostAndReturnsTrue() {
        boolean result;

        result = logLine.checkDestinationHost("destinationHost");

        assertTrue(result);
    }

    @Test
    @DisplayName("LogLine destination host is the same as in the object (casing do not match)")
    void checkDestinationHostWhenCaseDoNotMatchDestinationHostAndReturnsTrue() {
        boolean result;

        result = logLine.checkDestinationHost("DestinationHOST");

        assertTrue(result);
    }

    @Test
    @DisplayName("LogLine destination host is not the same as in the object")
    void checkDestinationHostWhenDestinationHostIsDifferentAndReturnsFalse() {
        boolean result;

        result = logLine.checkDestinationHost("destination");

        assertFalse(result);
    }

    @Test
    @DisplayName("Get the Origin Host from the Object")
    void getOriginHost() {
        String expectedOriginHost = "originHost";

        String actualOriginHost = logLine.getOriginHost();

        assertEquals(expectedOriginHost, actualOriginHost);
    }
}
