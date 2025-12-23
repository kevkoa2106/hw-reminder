package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlingWriteTest {

    @TempDir
    Path tempDir;

    private Path testFile;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test.csv");
        testDateTime = LocalDateTime.of(2024, 12, 25, 10, 30, 0);
    }

    @Test
    @DisplayName("Write to non-existent file creates file with headers and row")
    void testWriteToCSV_NewFile() throws IOException {
        CSVHandling.writeToCSV(testFile, "Math HW", "Mathematics", testDateTime, false);

        assertTrue(Files.exists(testFile));
        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertEquals("name", result.headers().get(0));
        assertEquals(1, result.rows().size());
        assertEquals("Math HW", result.rows().get(0).get(0));
    }

    @Test
    @DisplayName("Write to existing file appends row without duplicate headers")
    void testWriteToCSV_ExistingFile() throws IOException {
        String initialContent = "name,subject,duedate,completed\nExisting,Subject,2024-12-20T09:00,false\n";
        Files.writeString(testFile, initialContent);

        CSVHandling.writeToCSV(testFile, "New HW", "New Subject", testDateTime, true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertEquals(2, result.rows().size());
        assertEquals("Existing", result.rows().get(0).get(0));
        assertEquals("New HW", result.rows().get(1).get(0));
    }

    @Test
    @DisplayName("Write to empty existing file adds headers and row")
    void testWriteToCSV_EmptyExistingFile() throws IOException {
        Files.createFile(testFile);

        CSVHandling.writeToCSV(testFile, "Math HW", "Mathematics", testDateTime, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertEquals(1, result.rows().size());
    }

    @Test
    @DisplayName("Write values with commas are properly escaped")
    void testWriteToCSV_SpecialCharacters() throws IOException {
        CSVHandling.writeToCSV(testFile, "Math, Part 1", "Mathematics", testDateTime, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("Math, Part 1", result.rows().get(0).get(0));
    }

    @Test
    @DisplayName("Write with null field writes empty or null string")
    void testWriteToCSV_NullValues() throws IOException {
        CSVHandling.writeToCSV(testFile, null, "Subject", testDateTime, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());
        // FastCSV writes null as empty string
        assertTrue(result.rows().get(0).get(0) == null || result.rows().get(0).get(0).isEmpty());
    }

    @Test
    @DisplayName("DateTime is serialized in ISO format")
    void testWriteToCSV_DateTimeFormat() throws IOException {
        CSVHandling.writeToCSV(testFile, "HW", "Subject", testDateTime, false);

        CsvData result = CSVHandling.readFromCSV(testFile);
        String dateStr = result.rows().get(0).get(2);
        assertEquals("2024-12-25T10:30", dateStr);
    }
}
