package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlingUpdateTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test.csv");
        String content = "name,subject,duedate,completed\n" +
                "First HW,Math,2024-12-20T09:00,false\n" +
                "Second HW,Science,2024-12-21T10:00,false\n" +
                "Third HW,History,2024-12-22T11:00,false";
        Files.writeString(testFile, content);
    }

    @Test
    @DisplayName("Update existing homework marks it as completed")
    void testUpdateCompleted_ExistingHomework() throws IOException {
        CSVHandling.updateCompleted(testFile, "Second HW", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("true", result.rows().get(1).get(3));
    }

    @Test
    @DisplayName("Update non-existent homework leaves file unchanged")
    void testUpdateCompleted_NonExistentHomework() throws IOException {
        String originalContent = Files.readString(testFile);

        CSVHandling.updateCompleted(testFile, "NotFound", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(3, result.rows().size());
        assertEquals("false", result.rows().get(0).get(3));
        assertEquals("false", result.rows().get(1).get(3));
        assertEquals("false", result.rows().get(2).get(3));
    }

    @Test
    @DisplayName("Update first data row correctly")
    void testUpdateCompleted_FirstRow() throws IOException {
        CSVHandling.updateCompleted(testFile, "First HW", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("true", result.rows().get(0).get(3));
        assertEquals("false", result.rows().get(1).get(3));
        assertEquals("false", result.rows().get(2).get(3));
    }

    @Test
    @DisplayName("Update last data row correctly")
    void testUpdateCompleted_LastRow() throws IOException {
        CSVHandling.updateCompleted(testFile, "Third HW", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("false", result.rows().get(0).get(3));
        assertEquals("false", result.rows().get(1).get(3));
        assertEquals("true", result.rows().get(2).get(3));
    }

    @Test
    @DisplayName("Update with duplicate names only updates first match")
    void testUpdateCompleted_DuplicateNames() throws IOException {
        String content = "name,subject,duedate,completed\n" +
                "Duplicate,Math,2024-12-20T09:00,false\n" +
                "Duplicate,Science,2024-12-21T10:00,false";
        Files.writeString(testFile, content);

        CSVHandling.updateCompleted(testFile, "Duplicate", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("true", result.rows().get(0).get(3));
        assertEquals("false", result.rows().get(1).get(3));
    }

    @Test
    @DisplayName("Update preserves other fields unchanged")
    void testUpdateCompleted_PreservesOtherFields() throws IOException {
        CSVHandling.updateCompleted(testFile, "Second HW", true);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("Second HW", result.rows().get(1).get(0));
        assertEquals("Science", result.rows().get(1).get(1));
        assertEquals("2024-12-21T10:00", result.rows().get(1).get(2));
        assertEquals("true", result.rows().get(1).get(3));
    }
}
