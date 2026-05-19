package com.se1020.movierental.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final Path DATA_DIRECTORY = Paths.get("src", "main", "resources", "data");

    public List<String> readAllLines(String fileName) {
        Path filePath = resolveFilePath(fileName);
        try {
            if (Files.notExists(filePath)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read file: " + fileName, exception);
        }
    }

    public void writeAllLines(String fileName, List<String> lines) {
        Path filePath = resolveFilePath(fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write file: " + fileName, exception);
        }
    }

    public void appendLine(String fileName, String line) {
        Path filePath = resolveFilePath(fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(
                    filePath,
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to append file: " + fileName, exception);
        }
    }

    private Path resolveFilePath(String fileName) {
        return DATA_DIRECTORY.resolve(fileName);
    }
}
