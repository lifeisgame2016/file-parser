package org.example;

public record Location(int lineOffset, int charOffset) {
    @Override
    public String toString() {
        return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
    }
}