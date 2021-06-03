package es.koldo.clarity.codetest.parser;

import es.koldo.clarity.codetest.model.LogLine;

public interface LogParser {
    LogLine parseLogLine(String logLine);
}
