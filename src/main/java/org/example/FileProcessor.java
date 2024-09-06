package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FileProcessor {

    private static final long SECONDS_TO_WAIT = 30;
    private final Path filePath;
    private final int linesPerPart;
    private final int chunkSize;
    private final List<String> names;

    public FileProcessor(Path filePath, int linesPerPart, int chunkSize, List<String> names) {
        this.filePath = filePath;
        this.linesPerPart = linesPerPart;
        this.chunkSize = chunkSize;
        this.names = names;
    }

    public void processFile() throws IOException, InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Map<String, List<Location>>>> futures = new ArrayList<>();
            Aggregator aggregator = new Aggregator();

            try (Stream<String> lines = Files.lines(filePath)) {
                Iterator<String> iterator = lines.iterator();
                StringBuilder part = new StringBuilder();
                int lineOffset = 1;
                int lineCount = 0;

                while (iterator.hasNext()) {
                    String line = iterator.next();
                    int lineLength = line.length();
                    int processedLength = 0;

                    while (processedLength < lineLength) {
                        int end = Math.min(lineLength, processedLength + chunkSize);
                        part.append(line, processedLength, end).append("\n");
                        processedLength = end;
                        lineCount++;

                        if (lineCount == linesPerPart) {
                            futures.add(executor.submit(new Matcher(part.toString(), names, lineOffset)));
                            part.setLength(0);
                            lineOffset += lineCount;
                            lineCount = 0;
                        }
                    }
                }

                if (!part.isEmpty()) {
                    futures.add(executor.submit(new Matcher(part.toString(), names, lineOffset)));
                }
            }

            for (Future<Map<String, List<Location>>> future : futures) {
                try {
                    aggregator.aggregate(future.get(SECONDS_TO_WAIT, TimeUnit.SECONDS));
                } catch (TimeoutException e) {
                    throw new RuntimeException("Timeout while waiting for the result", e);
                }
            }

            aggregator.printResults();
        }
    }
}