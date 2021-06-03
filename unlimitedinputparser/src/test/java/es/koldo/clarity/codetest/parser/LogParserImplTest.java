package es.koldo.clarity.codetest.parser;

import es.koldo.clarity.codetest.model.LogLine;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class LogParserImplTest {
    static LogParserImpl logParserImpl;
    static LogLine logLine;
    String logLineString = "1565647228897 Heera Eron";

    @BeforeAll
    static void setUp() {
        logParserImpl = new LogParserImpl();
        logLine = mock(LogLine.class);
    }

    @Test
    @DisplayName("parseLogLine should return a LogLine object")
    void parseLogLineShouldReturnALogLineObject() {
        logLine = logParserImpl.parseLogLine(logLineString);

        assertThat(logLine, Matchers.instanceOf(LogLine.class));
    }
}
