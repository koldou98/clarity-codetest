package es.koldo.clarity.codetest;

import es.koldo.clarity.codetest.utilities.FileUtilities;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LogGenerator {
    public static final String LINE_TERMINATION = "\n";
    static final int TIME_MARGIN_ERROR_MINUTES = 5;
    static final int TIME_MARGIN_ERROR_MILLISECONDS = TIME_MARGIN_ERROR_MINUTES * 60 * 1000;
    private static final List<String> HOST_LIST = new ArrayList<>(Arrays.asList(
            "Pablo",
            "Iker",
            "Paula",
            "Itsaso",
            "Asier",
            "I\u00F1igo",
            "Enara",
            "Olatz",
            "Leire",
            "Ainhoa",
            "Andrea",
            "Jon",
            "Erik",
            "Ander",
            "Melvin",
            "Patrick",
            "Nerea",
            "Aitor",
            "Alberto",
            "Andoni",
            "Esti",
            "Mainer",
            "\u00C1lvaro",
            "Egoitz",
            "Jose",
            "Miguel",
            "Idoia"));

    public static void main(String[] args) {

        if (args.length > 0) {
            setPathWhereTheLogIsGoingToBeStored(args[0]);
        }
        try {
            createPathWhereLogWillBeWrittenIn();
            generateInfiniteLog();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private static void setPathWhereTheLogIsGoingToBeStored(String path) {
        FileUtilities.setInputLogsDirectoryPath(Path.of(path));
    }

    private static void createPathWhereLogWillBeWrittenIn() throws IOException {
        FileUtilities.createLogFileInInputLogsDirectory();
    }

    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    private static void generateInfiniteLog() throws IOException, InterruptedException {
        while (true) {
            String line = generateRandomLogLine();
            FileUtilities.writeLine(line);
            Thread.sleep(0, 1);
        }
    }

    private static String generateRandomLogLine() {
        int[] hostsArray = obtainSourceAndTargetHosts();
        long timestampWithError = getTimestampWithError();
        return String.format("%d %s %s%s",
                             timestampWithError,
                             HOST_LIST.get(hostsArray[0]),
                             HOST_LIST.get(hostsArray[1]),
                             LINE_TERMINATION);
    }

    private static int[] obtainSourceAndTargetHosts() {
        return ThreadLocalRandom.current()
                                .ints(0, HOST_LIST.size())
                                .distinct()
                                .limit(2)
                                .toArray();
    }

    private static long getTimestampWithError() {
        var randomNumber = new Random(Instant.now().getNano());
        var error = randomNumber.nextInt(TIME_MARGIN_ERROR_MILLISECONDS);
        return Instant.now().plusMillis(error).toEpochMilli();
    }
}
