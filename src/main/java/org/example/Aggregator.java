package org.example;

import java.util.*;

public class Aggregator {

    private final Map<String, List<Location>> aggregatedResults;
    public Aggregator() {
        this.aggregatedResults = new HashMap<>();
    }

    public void aggregate(Map<String, List<Location>> results) {
        for (Map.Entry<String, List<Location>> entry : results.entrySet()) {
            aggregatedResults.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                    .addAll(entry.getValue());
        }
    }

    public void printResults() {
        for (Map.Entry<String, List<Location>> entry : aggregatedResults.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }
    }
}