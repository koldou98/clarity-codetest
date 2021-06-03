package es.koldo.clarity.codetest.parser;

import es.koldo.clarity.codetest.exceptions.FileDoesNotExistException;
import es.koldo.clarity.codetest.exceptions.IncorrectTimeWindowException;
import es.koldo.clarity.codetest.model.LogLine;
import es.koldo.clarity.codetest.utilities.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParserImpl implements LogParser {
    private final Path filepath;
    private final String targetHostname;
    private final long initDateTime;
    private final long endDateTime;
    private final Logger log = LogManager.getInstance();

    public LogParserImpl(String filepath, String initDateTime, String endDateTime, String targetHostname) throws IncorrectTimeWindowException, FileDoesNotExistException {
        this.filepath = getPathFromString(filepath);
        this.initDateTime = getTimestampFromString(initDateTime);
        this.endDateTime = getTimestampFromString(endDateTime);
        this.targetHostname = targetHostname;
        checkFilePath();
        checkDates();
    }

    private void checkFilePath() throws FileDoesNotExistException {
        if (!Files.exists(filepath)) {
            throw new FileDoesNotExistException("The file does not exits");
        }
    }

    private void checkDates() throws IncorrectTimeWindowException {
        if (initDateTime > endDateTime) {
            throw new IncorrectTimeWindowException("Initial date after end date");
        }
    }

    @Override
    public Set<String> obtainConnectedHostSet() {
        try (Stream<String> hostSet = Files.lines(filepath)
                                           .parallel()
                                           .map(this::createLogLine)
                                           .filter(logLine -> logLine.checkLogLineTimestamp(
                                                   initDateTime,
                                                   endDateTime))
                                           .filter(logLine -> logLine.checkDestinationHost(
                                                   targetHostname))
                                           .map(LogLine::getOriginHost)) {
            return hostSet.collect(Collectors.toSet());
        } catch (IOException e) {
            log.severe(e.getMessage());
            return new HashSet<>();
        }
    }

    private LogLine createLogLine(String actualLogLine) {
        String[] logLineData = obtainDataFieldsFromLogLine(actualLogLine);
        long logLineTimestamp = getTimestampFromString(logLineData[0]);
        String originHost = logLineData[1];
        String destinationHost = logLineData[2];
        return new LogLine(logLineTimestamp, originHost, destinationHost);
    }


    private String[] obtainDataFieldsFromLogLine(String stringLogLine) {
        return stringLogLine.split("\\s+");
    }

    private long getTimestampFromString(String date) {
        long timestamp;
        try {
            timestamp = Long.parseLong(date);
        } catch (Exception e) {
            var localDateTime = LocalDateTime.parse(date,
                                                    DateTimeFormatter.ofPattern(
                                                            "yyyy-MM-dd HH:mm:ss"));
            timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        }
        return timestamp;
    }

    private Path getPathFromString(String filepath) {
        return Path.of(filepath);
    }
}
