package es.koldo.clarity.codetest;

import es.koldo.clarity.codetest.exceptions.FileDoesNotExistException;
import es.koldo.clarity.codetest.exceptions.IncorrectTimeWindowException;
import es.koldo.clarity.codetest.parser.LogParserImpl;
import es.koldo.clarity.codetest.utilities.FileUtilities;
import es.koldo.clarity.codetest.utilities.LogManager;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public class ListOfHostnames {

    private static final int NUMBER_OF_ARGUMENTS = 4;
    private static final Logger log = LogManager.getInstance();
    private static final String NEEDED_ARGUMENTS_MESSAGE = "This application needs the following 4 arguments: <FilePath> <init_datetime <end_datetime> <target hostname>";

    public static void main(String[] args) {
        if (args.length < NUMBER_OF_ARGUMENTS) {
            printNeededArguments();
            System.exit(0);
        }
        try {
            var logParser = new LogParserImpl(args[0],
                                              args[1],
                                              args[2],
                                              args[3]);
            Set<String> hostList = logParser.obtainConnectedHostSet();
            FileUtilities.createOutputDirectory();
            var outputFilePath = FileUtilities.createOutputFileToStoreHosts();
            hostList.forEach(host -> FileUtilities.appendLine(outputFilePath,
                                                              host));
        } catch (IncorrectTimeWindowException | IOException | FileDoesNotExistException e) {
            log.severe(e.getMessage());
        }
    }

    private static void printNeededArguments() {
        log.warning(NEEDED_ARGUMENTS_MESSAGE);
    }
}
