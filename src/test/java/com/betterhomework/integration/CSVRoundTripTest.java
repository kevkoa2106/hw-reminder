package com.betterhomework.integration;

import com.betterhomework.models.CsvData;
import com.betterhomework.models.Homework;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CSV read/write round-trip operations.
 * Verifies that data written to CSV can be read back correctly.
 */
class CSVRoundTripTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("roundtrip.csv");
    }

    // Helper to convert CSV row back to Homework
    private Homework rowToHomework(List<String> row) {
        return new Homework(
                row.get(0),
                row.get(1),
                LocalDateTime.parse(row.get(2)),
                Boolean.parseBoolean(row.get(3))
        );
    }

    @Test
    @DisplayName("Write single homework and read it back correctly")
    void testWriteThenRead_SingleHomework() throws IOException {
        LocalDateTime dueDate = LocalDateTime.of(2024, 12, 25, 10, 30);
        Homework original = new Homework("Math HW", "Mathematics", dueDate, false);

        // Write
        CSVHandling.writeToCSV(testFile, original.getName(), original.getSubject(), original.getDueDate(), original.isCompleted());

        // Read back
        CsvData data = CSVHandling.readFromCSV(testFile);
        assertEquals(1, data.rows().size());

        Homework readBack = rowToHomework(data.rows().get(0));
        assertEquals(original, readBack);
    }

    @Test
    @DisplayName("Write multiple homeworks and read them all back")
    void testWriteThenRead_MultipleHomeworks() throws IOException {
        LocalDateTime baseDate = LocalDateTime.of(2024, 12, 20, 9, 0);
        List<Homework> originals = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Homework hw = new Homework(
                    "HW" + i,
                    "Subject" + i,
                    baseDate.plusDays(i),
                    i % 2 == 0
            );
            originals.add(hw);
            CSVHandling.writeToCSV(testFile, hw.getName(), hw.getSubject(), hw.getDueDate(), hw.isCompleted());
        }

        // Read back
        CsvData data = CSVHandling.readFromCSV(testFile);
        assertEquals(5, data.rows().size());

        for (int i = 0; i < 5; i++) {
            Homework readBack = rowToHomework(data.rows().get(i));
            assertEquals(originals.get(i), readBack);
        }
    }

    @Test
    @DisplayName("Full CRUD cycle: write, update, read")
    void testWriteUpdateRead_Cycle() throws IOException {
        LocalDateTime dueDate = LocalDateTime.of(2024, 12, 25, 10, 30);

        // Create
        CSVHandling.writeToCSV(testFile, "Math HW", "Mathematics", dueDate, false);

        // Verify initial state
        CsvData initialData = CSVHandling.readFromCSV(testFile);
        assertEquals("false", initialData.rows().get(0).get(3));

        // Update
        CSVHandling.updateCompleted(testFile, "Math HW", true);

        // Read and verify update
        CsvData updatedData = CSVHandling.readFromCSV(testFile);
        assertEquals(1, updatedData.rows().size());
        assertEquals("Math HW", updatedData.rows().get(0).get(0));
        assertEquals("Mathematics", updatedData.rows().get(0).get(1));
        assertEquals("true", updatedData.rows().get(0).get(3));
    }

    @Test
    @DisplayName("Multiple rapid sequential writes all persist")
    void testConcurrentWrites() throws IOException, InterruptedException {
        int numWrites = 20;
        LocalDateTime baseDate = LocalDateTime.of(2024, 12, 20, 9, 0);

        // Perform sequential rapid writes
        for (int i = 0; i < numWrites; i++) {
            CSVHandling.writeToCSV(testFile, "HW" + i, "Subject" + i, baseDate.plusHours(i), false);
        }

        // Verify all writes persisted
        CsvData data = CSVHandling.readFromCSV(testFile);
        assertEquals(numWrites, data.rows().size());

        // Verify order preserved
        for (int i = 0; i < numWrites; i++) {
            assertEquals("HW" + i, data.rows().get(i).get(0));
        }
    }

    @Test
    @DisplayName("Read after partial/corrupted write handles gracefully")
    void testFileCorruptionRecovery() throws IOException {
        // Write valid data first
        CSVHandling.writeToCSV(testFile, "Valid HW", "Subject", LocalDateTime.now(), false);

        // Simulate corruption by appending malformed data
        Files.writeString(testFile, Files.readString(testFile) + "\nincomplete,row");

        // Reading should throw exception due to inconsistent columns
        assertThrows(Exception.class, () -> CSVHandling.readFromCSV(testFile));
    }
}
