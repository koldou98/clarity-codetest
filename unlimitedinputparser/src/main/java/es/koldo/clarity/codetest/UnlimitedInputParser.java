package es.koldo.clarity.codetest;

import es.koldo.clarity.codetest.parser.LogParserImpl;
import es.koldo.clarity.codetest.properties.PropertyManager;
import es.koldo.clarity.codetest.utilities.DataStore;
import es.koldo.clarity.codetest.utilities.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnlimitedInputParser {
    private static final TimeUnit SCHEDULE_TIME_UNIT = TimeUnit.MINUTES;
    private static final int SCHEDULE_TIME = 60;
    private static final int SCHEDULE_TIME_TO_START = 60;
    private static final PropertyManager propertyManager = PropertyManager.getInstance();
    private static final Logger log = LogManager.getInstance();
    private static boolean isFileLog = false;
    private static String filePath = "";
    private static long timeRangeStart = 0;
    private static String hostToConnect = "";
    private static String hostConnectedFrom = "";

    public static void main(String[] args) {
        timeRangeStart = Instant.now()
                                .minus(60, ChronoUnit.MINUTES)
                                .toEpochMilli();
        var dataStore = new DataStore();
        int argsLength = args.length;
        if (argsLength < 2) {
            printNeededArguments();
            System.exit(0);
        }
        hostToConnect = args[0];
        hostConnectedFrom = args[1];

        propertyManager.setDestinationHost(hostToConnect);
        propertyManager.setOriginHost(hostConnectedFrom);

        if (argsLength > 2) {
            var filePathFromArgument = Path.of(args[2]);
            var fileAbsolutePath = filePathFromArgument.toAbsolutePath()
                                                       .toString();
            propertyManager.setFilePathProperty(fileAbsolutePath);
            filePath = fileAbsolutePath;
            isFileLog = true;
        }

        var scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduleOutput(dataStore, scheduledExecutorService);

        readLog(argsLength, dataStore);
        shutdown(scheduledExecutorService, dataStore);
    }

    private static void shutdown(
            ScheduledExecutorService scheduledExecutorService,
            DataStore dataStore) {
        scheduledExecutorService.execute(dataStore.printData());
        scheduledExecutorService.shutdown();
    }

    private static void scheduleOutput(DataStore dataStore,
                                       ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.scheduleAtFixedRate(dataStore.printData(),
                                                     SCHEDULE_TIME_TO_START,
                                                     SCHEDULE_TIME,
                                                     SCHEDULE_TIME_UNIT);
    }

    public static String getHostToConnect() {
        return hostToConnect;
    }

    public static String getHostConnectedFrom() {
        return hostConnectedFrom;
    }

    private static void readLog(int argsLength, DataStore dataStore) {
        try (var inputStream = getInputStream(argsLength)) {
            createInputStreamReader(inputStream, dataStore);
        } catch (IOException e) {
            log.severe(e.getCause().getMessage());
        } catch (InterruptedException e) {
            log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static void createInputStreamReader(InputStream inputStream,
                                                DataStore dataStore)
            throws IOException, InterruptedException {
        try (var inputStreamReader = new InputStreamReader(inputStream)) {
            createBufferReader(inputStreamReader, dataStore);
        }
    }

    private static void createBufferReader(InputStreamReader inputStreamReader,
                                           DataStore dataStore)
            throws IOException, InterruptedException {

        try (var bufferedReader = new BufferedReader(inputStreamReader)) {
            readLogIndefinitely(bufferedReader, dataStore);
        }
    }

    private static void readLogIndefinitely(BufferedReader bufferedReader,
                                            DataStore dataStore)
            throws IOException, InterruptedException {

        var fileParser = new LogParserImpl();

        if (isFileLog) {
            readFileBuffer(fileParser, bufferedReader, dataStore);
        } else {
            readBuffer(fileParser, bufferedReader, dataStore);
        }
    }

    @SuppressWarnings("BusyWait")
    private static void readFileBuffer(LogParserImpl fileParser,
                                       BufferedReader bufferedReader,
                                       DataStore dataStore)
            throws IOException, InterruptedException {
        long actualFileSize;
        do {
            actualFileSize = getFileSize(filePath);
            readBuffer(fileParser, bufferedReader, dataStore);
            Thread.sleep(50);
        } while (actualFileSize != getFileSize(filePath));
    }

    private static void readBuffer(LogParserImpl fileParser,
                                   BufferedReader bufferedReader,
                                   DataStore dataStore) {
        bufferedReader.lines()
                      .map(fileParser::parseLogLine)
                      .filter(logLine -> logLine.checkLineTimestamp(
                              timeRangeStart))
                      .forEach(logLine -> logLine.processLogLine(hostToConnect,
                                                                 hostConnectedFrom,
                                                                 dataStore));
    }

    private static long getFileSize(String filePath) throws IOException {
        return Files.size(Path.of(filePath));
    }

    private static InputStream getInputStream(int argsLength)
            throws FileNotFoundException {
        return argsLength == 2 ? System.in : new FileInputStream(filePath);
    }

    private static void printNeededArguments() {
        log.warning("This application needs the following arguments: " +
                            "<host to connect> <host connection from> " +
                            "[filePath]\n" +
                            "If filePath is not provided, System standard input " +
                            "will be used.");
    }

    public static void updateValues() {
        timeRangeStart = Instant.now()
                                .minus(60, ChronoUnit.MINUTES)
                                .toEpochMilli();
        hostToConnect = propertyManager.getOriginHost();
        hostConnectedFrom = propertyManager.getDestinationHost();
    }
}
