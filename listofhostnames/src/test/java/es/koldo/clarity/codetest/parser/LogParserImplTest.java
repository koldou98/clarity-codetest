package es.koldo.clarity.codetest.parser;

import es.koldo.clarity.codetest.exceptions.FileDoesNotExistException;
import es.koldo.clarity.codetest.exceptions.IncorrectTimeWindowException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogParserImplTest {

    static LogParser logParser;
    static LogParser logParserWithWrongTargetHostname;
    static URL testFile = LogParserImplTest.class.getClassLoader()
                                                 .getResource("testLog.txt");
    static String testFilePath;

    static {
        assert testFile != null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            testFilePath = testFile.getFile().replaceFirst("/", "");
        }else{
            testFilePath = testFile.getFile();
        }
    }

    @BeforeAll
    static void init()
            throws IncorrectTimeWindowException, FileDoesNotExistException {
        logParser = new LogParserImpl(testFilePath,
                                      "2019-08-13 21:46:00",
                                      "2019-08-13 22:00:00",
                                      "Arieal");
        logParserWithWrongTargetHostname = new LogParserImpl(testFilePath,
                                                             "2019-08-13 21:46:00",
                                                             "2019-08-13 22:00:00",
                                                             "targetHostName");
    }

    @Test
    @DisplayName("Initial date time is after the end date time and throws IncorrectTimeWindowsException")
    void initDateTimeAfterEndDateTimeAndShouldThrowIncorrectTimeWindowsException() {
        assertThrows(IncorrectTimeWindowException.class,
                     () -> new LogParserImpl(testFilePath,
                                             "2021-07-21 12:00:00",
                                             "2021-07-20 12:00:00",
                                             "targetHostName"));
    }

    @Test
    @DisplayName("FilePath does not exists and throws FileDoesNotExistException")
    void filePathDoesNotExitsShouldThrowFileDoesNotExistsException() {
        assertThrows(FileDoesNotExistException.class,
                     () -> new LogParserImpl("testFilePath",
                                             "2019-08-13 21:46:00",
                                             "2019-08-13 21:50:00",
                                             "testTargetHostname"));
    }

    @Test
    @DisplayName("Obtain connected host number should return 1")
    void obtainConnectedHostNumberShouldReturn1() {
        Set<String> connectedHostSet = logParser.obtainConnectedHostSet();

        assertEquals(1, connectedHostSet.size());
    }

    @Test
    @DisplayName("Obtain connected host set with the hosts set")
    void obtainConnectedHostsFromTheSetShouldReturnDebbra() {
        Set<String> connectedHostSet = logParser.obtainConnectedHostSet();

        assertThat("The host set contains Debbra",
                   connectedHostSet,
                   containsInAnyOrder("Debbra"));
    }

    @Test
    @DisplayName("Obtain connected host number should return 0")
    void obtainConnectedHostNumberShouldReturn0BecauseNoHostHasConnected() {
        Set<String> connectedHostSet = logParserWithWrongTargetHostname.obtainConnectedHostSet();

        assertEquals(0, connectedHostSet.size());
    }
}
