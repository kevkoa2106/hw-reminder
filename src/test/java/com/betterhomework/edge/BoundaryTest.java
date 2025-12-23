package com.betterhomework.edge;

import com.betterhomework.models.CsvData;
import com.betterhomework.models.Homework;
import com.betterhomework.service.CSVHandling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Boundary condition tests for the BetterHomework application.
 * Tests extreme values, edge cases, and boundary conditions.
 */
class BoundaryTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("boundary.csv");
    }

    @Test
    @DisplayName("Handles extremely long homework name")
    void testVeryLongName() throws IOException {
        String longName = "A".repeat(10000);
        LocalDateTime dueDate = LocalDateTime.now();

        CSVHandling.writeToCSV(testFile, longName, "Subject", dueDate, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());
        assertEquals(longName, result.rows().get(0).get(0));
        assertEquals(10000, result.rows().get(0).get(0).length());
    }

    @Test
    @DisplayName("Handles extremely long subject name")
    void testVeryLongSubject() throws IOException {
        String longSubject = "B".repeat(10000);
        LocalDateTime dueDate = LocalDateTime.now();

        CSVHandling.writeToCSV(testFile, "Name", longSubject, dueDate, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());
        assertEquals(longSubject, result.rows().get(0).get(1));
        assertEquals(10000, result.rows().get(0).get(1).length());
    }

    @Test
    @DisplayName("Handles due date in the past")
    void testPastDueDate() throws IOException {
        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 10, 0);

        CSVHandling.writeToCSV(testFile, "Past HW", "Subject", pastDate, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());

        Homework hw = new Homework(
                result.rows().get(0).get(0),
                result.rows().get(0).get(1),
                LocalDateTime.parse(result.rows().get(0).get(2)),
                Boolean.parseBoolean(result.rows().get(0).get(3))
        );

        assertEquals(2020, hw.getDueDate().getYear());
        assertTrue(hw.getDueDate().isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Handles due date far in the future")
    void testFarFutureDueDate() throws IOException {
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59);

        CSVHandling.writeToCSV(testFile, "Future HW", "Subject", futureDate, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());

        Homework hw = new Homework(
                result.rows().get(0).get(0),
                result.rows().get(0).get(1),
                LocalDateTime.parse(result.rows().get(0).get(2)),
                Boolean.parseBoolean(result.rows().get(0).get(3))
        );

        assertEquals(2100, hw.getDueDate().getYear());
        assertTrue(hw.getDueDate().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Handles midnight time correctly")
    void testMidnightTime() throws IOException {
        LocalDateTime midnight = LocalDateTime.of(2024, 12, 25, 0, 0, 0);

        CSVHandling.writeToCSV(testFile, "Midnight HW", "Subject", midnight, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        LocalDateTime parsed = LocalDateTime.parse(result.rows().get(0).get(2));

        assertEquals(0, parsed.getHour());
        assertEquals(0, parsed.getMinute());
        assertEquals(0, parsed.getSecond());
    }

    @Test
    @DisplayName("Handles end of day time correctly")
    void testEndOfDayTime() throws IOException {
        LocalDateTime endOfDay = LocalDateTime.of(2024, 12, 25, 23, 59, 59);

        CSVHandling.writeToCSV(testFile, "EndOfDay HW", "Subject", endOfDay, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        LocalDateTime parsed = LocalDateTime.parse(result.rows().get(0).get(2));

        assertEquals(23, parsed.getHour());
        assertEquals(59, parsed.getMinute());
        assertEquals(59, parsed.getSecond());
    }
}
