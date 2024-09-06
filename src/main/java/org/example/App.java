package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class App {
    private static final String SEARCH_NAMES = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas," +
            "Christopher,Daniel,Paul,Mark,Donald,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew," +
            "Gary,Timothy,Jose,Larry,Jeffrey,Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis," +
            "Walter,Patrick,Peter,Harold,Douglas,Henry,Carl,Arthur,Ryan,Roger";

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        List<String> names = Arrays.asList(SEARCH_NAMES.split(","));

        Path filePath = Paths.get("src/main/resources/big.txt");
        int linesPerPart = 1000;
        int chunkSize = 10000;

        FileProcessor fileProcessor = new FileProcessor(filePath, linesPerPart, chunkSize, names);
        fileProcessor.processFile();
    }
}