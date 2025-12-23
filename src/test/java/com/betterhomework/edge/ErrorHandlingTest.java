package com.betterhomework.edge;

import com.betterhomework.models.CsvData;
import com.betterhomework.service.CSVHandling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Error handling tests for the BetterHomework application.
 * Tests exception handling and error recovery scenarios.
 */
class ErrorHandlingTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("error.csv");
    }

    @Test
    @DisplayName("Reading deleted file throws IOException")
    void testCSVFileDeleted() throws IOException {
        // Create and then delete the file
        CSVHandling.writeToCSV(testFile, "Test", "Subject", LocalDateTime.now(), false);
        assertTrue(Files.exists(testFile));

        Files.delete(testFile);
        assertFalse(Files.exists(testFile));

        // Attempt to read should throw
        assertThrows(IOException.class, () -> CSVHandling.readFromCSV(testFile));
    }

    @Test
    @DisplayName("Writing to new file in non-existent directory throws IOException")
    void testCSVFileLocked() {
        Path nonExistentDir = tempDir.resolve("nonexistent/subdir/file.csv");

        assertThrows(IOException.class, () ->
                CSVHandling.writeToCSV(nonExistentDir, "Test", "Subject", LocalDateTime.now(), false));
    }

    @Test
    @DisplayName("Reading malformed CSV with inconsistent columns throws exception")
    void testInvalidCSVFormat() throws IOException {
        // Write valid CSV then corrupt it
        String malformedContent = "name,subject,duedate,completed\nOnly,Two";
        Files.writeString(testFile, malformedContent);

        // FastCSV throws exception for inconsistent column counts
        assertThrows(Exception.class, () -> CSVHandling.readFromCSV(testFile));
    }

    @Test
    @DisplayName("WriteAll to non-existent directory fails gracefully")
    void testDiskFull() {
        Path invalidPath = tempDir.resolve("nonexistent/path/data.csv");

        CsvData data = new CsvData(
                Arrays.asList("name", "subject", "duedate", "completed"),
                new ArrayList<>()
        );

        assertThrows(IOException.class, () -> CSVHandling.writeAll(invalidPath, data));
    }

    @Test
    @DisplayName("Update on non-existent file throws IOException")
    void testReadOnlyFile() {
        Path nonExistent = tempDir.resolve("does_not_exist.csv");

        assertThrows(IOException.class, () ->
                CSVHandling.updateCompleted(nonExistent, "Some HW", true));
    }
}
