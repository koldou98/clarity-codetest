package es.koldo.clarity.codetest.parser;

import es.koldo.clarity.codetest.model.LogLine;

public class LogParserImpl implements LogParser {
    @Override
    public LogLine parseLogLine(String logLine) {
        String[] logLineData = obtainDataFieldsFromLogLine(logLine);
        long logLineTimestamp = getTimestampFromString(logLineData[0]);
        String originHost = logLineData[1];
        String destinationHost = logLineData[2];
        return new LogLine(logLineTimestamp, originHost, destinationHost);
    }

    private String[] obtainDataFieldsFromLogLine(String stringLogLine) {
        return stringLogLine.split("\\s+");
    }

    private long getTimestampFromString(String date) {
        return Long.parseLong(date);
    }
}
