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

class CSVHandlingDeleteTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test.csv");
        String content = "name,subject,duedate,completed\n" +
                "First HW,Math,2024-12-20T09:00,false\n" +
                "Second HW,Science,2024-12-21T10:00,false\n" +
                "Third HW,History,2024-12-22T11:00,true";
        Files.writeString(testFile, content);
    }

    @Test
    @DisplayName("Delete existing homework removes it from file")
    void testDeleteRow_ExistingHomework() throws IOException {
        CSVHandling.deleteRow(testFile, "Second HW");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(2, result.rows().size());
        assertEquals("First HW", result.rows().get(0).get(0));
        assertEquals("Third HW", result.rows().get(1).get(0));
    }

    @Test
    @DisplayName("Delete first row removes it correctly")
    void testDeleteRow_FirstRow() throws IOException {
        CSVHandling.deleteRow(testFile, "First HW");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(2, result.rows().size());
        assertEquals("Second HW", result.rows().get(0).get(0));
        assertEquals("Third HW", result.rows().get(1).get(0));
    }

    @Test
    @DisplayName("Delete last row removes it correctly")
    void testDeleteRow_LastRow() throws IOException {
        CSVHandling.deleteRow(testFile, "Third HW");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(2, result.rows().size());
        assertEquals("First HW", result.rows().get(0).get(0));
        assertEquals("Second HW", result.rows().get(1).get(0));
    }

    @Test
    @DisplayName("Delete non-existent homework leaves file unchanged")
    void testDeleteRow_NonExistentHomework() throws IOException {
        CSVHandling.deleteRow(testFile, "NonExistent");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(3, result.rows().size());
        assertEquals("First HW", result.rows().get(0).get(0));
        assertEquals("Second HW", result.rows().get(1).get(0));
        assertEquals("Third HW", result.rows().get(2).get(0));
    }

    @Test
    @DisplayName("Delete with duplicate names removes all matches")
    void testDeleteRow_DuplicateNames() throws IOException {
        String content = "name,subject,duedate,completed\n" +
                "Duplicate,Math,2024-12-20T09:00,false\n" +
                "Duplicate,Science,2024-12-21T10:00,true\n" +
                "Other,History,2024-12-22T11:00,false";
        Files.writeString(testFile, content);

        CSVHandling.deleteRow(testFile, "Duplicate");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1, result.rows().size());
        assertEquals("Other", result.rows().get(0).get(0));
        assertEquals("History", result.rows().get(0).get(1));
    }

    @Test
    @DisplayName("Delete all rows leaves only headers")
    void testDeleteRow_AllRows() throws IOException {
        CSVHandling.deleteRow(testFile, "First HW");
        CSVHandling.deleteRow(testFile, "Second HW");
        CSVHandling.deleteRow(testFile, "Third HW");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertTrue(result.rows().isEmpty());
    }

    @Test
    @DisplayName("Delete preserves other fields in remaining rows")
    void testDeleteRow_PreservesOtherFields() throws IOException {
        CSVHandling.deleteRow(testFile, "Second HW");

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals("First HW", result.rows().get(0).get(0));
        assertEquals("Math", result.rows().get(0).get(1));
        assertEquals("2024-12-20T09:00", result.rows().get(0).get(2));
        assertEquals("false", result.rows().get(0).get(3));

        assertEquals("Third HW", result.rows().get(1).get(0));
        assertEquals("History", result.rows().get(1).get(1));
        assertEquals("2024-12-22T11:00", result.rows().get(1).get(2));
        assertEquals("true", result.rows().get(1).get(3));
    }

    @Test
    @DisplayName("Delete on non-existent file throws IOException")
    void testDeleteRow_NonExistentFile() {
        Path nonExistent = tempDir.resolve("does_not_exist.csv");

        assertThrows(IOException.class, () -> CSVHandling.deleteRow(nonExistent, "Any"));
    }
}
