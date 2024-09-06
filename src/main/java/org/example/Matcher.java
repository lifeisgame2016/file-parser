package org.example;

import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Emit;

import java.util.*;
import java.util.concurrent.Callable;

public class Matcher implements Callable<Map<String, List<Location>>> {
    private final String text;
    private final List<String> names;
    private final int lineOffset;

    public Matcher(String text, List<String> names, int lineOffset) {
        this.text = text;
        this.names = names;
        this.lineOffset = lineOffset;
    }

    @Override
    public Map<String, List<Location>> call() {
        Map<String, List<Location>> result = new HashMap<>();
        Trie trie = Trie.builder().onlyWholeWords().addKeywords(names).build();
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            Collection<Emit> emits = trie.parseText(lines[i]);
            for (Emit emit : emits) {
                String keyword = emit.getKeyword();
                result.computeIfAbsent(keyword, k -> new ArrayList<>())
                        .add(new Location(lineOffset + i, emit.getStart()));
            }
        }

        return result;
    }
}