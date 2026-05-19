package com.se1020.movierental.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FileUtils {

    private FileUtils() {
    }

    public static List<String> readAllLines(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            List<String> filteredLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    filteredLines.add(line);
                }
            }

            return filteredLines;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void writeAllLines(String filePath, List<String> lines) {
        try {
            Files.write(Path.of(filePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String filePath, String line) {
        try {
            Files.write(
                Path.of(filePath),
                List.of(line),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean lineExists(String filePath, String id) {
        List<String> lines = readAllLines(filePath);
        for (String line : lines) {
            if (line.startsWith(id)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteLine(String filePath, String id) {
        List<String> lines = new ArrayList<>(readAllLines(filePath));
        lines.removeIf(line -> line.startsWith(id));
        writeAllLines(filePath, lines);
    }

    public static void updateLine(String filePath, String id, String newLine) {
        List<String> lines = new ArrayList<>(readAllLines(filePath));

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(id)) {
                lines.set(i, newLine);
                break;
            }
        }

        writeAllLines(filePath, lines);
    }
}
