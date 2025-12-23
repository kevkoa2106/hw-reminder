package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlingReadTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test.csv");
    }

    @Test
    @DisplayName("Read well-formed CSV with headers and data rows")
    void testReadFromCSV_ValidFile() throws IOException {
        String content = "name,subject,duedate,completed\nMath HW,Math,2024-12-25T10:00,false\nScience HW,Science,2024-12-26T14:00,true";
        Files.writeString(testFile, content);

        CsvData result = CSVHandling.readFromCSV(testFile);

        assertEquals(4, result.headers().size());
        assertEquals("name", result.headers().get(0));
        assertEquals(2, result.rows().size());
        assertEquals("Math HW", result.rows().get(0).get(0));
        assertEquals("Science HW", result.rows().get(1).get(0));
    }

    @Test
    @DisplayName("Read empty CSV file returns empty data")
    void testReadFromCSV_EmptyFile() throws IOException {
        Files.writeString(testFile, "");

        CsvData result = CSVHandling.readFromCSV(testFile);

        assertTrue(result.headers().isEmpty());
        assertTrue(result.rows().isEmpty());
    }

    @Test
    @DisplayName("Read CSV with only headers returns empty rows")
    void testReadFromCSV_HeadersOnly() throws IOException {
        String content = "name,subject,duedate,completed";
        Files.writeString(testFile, content);

        CsvData result = CSVHandling.readFromCSV(testFile);

        assertEquals(4, result.headers().size());
        assertTrue(result.rows().isEmpty());
    }

    @Test
    @DisplayName("Read non-existent file throws IOException")
    void testReadFromCSV_NonExistentFile() {
        Path nonExistent = tempDir.resolve("does_not_exist.csv");

        assertThrows(IOException.class, () -> CSVHandling.readFromCSV(nonExistent));
    }

    @Test
    @DisplayName("Read CSV with special characters in values")
    void testReadFromCSV_SpecialCharacters() throws IOException {
        String content = "name,subject,duedate,completed\n\"Value, with comma\",Subject,2024-12-25T10:00,false";
        Files.writeString(testFile, content);

        CsvData result = CSVHandling.readFromCSV(testFile);

        assertEquals(1, result.rows().size());
        assertEquals("Value, with comma", result.rows().get(0).get(0));
    }

    @Test
    @DisplayName("Read CSV with unicode characters")
    void testReadFromCSV_UnicodeCharacters() throws IOException {
        String content = "name,subject,duedate,completed\n数学作业,数学,2024-12-25T10:00,false";
        Files.writeString(testFile, content);

        CsvData result = CSVHandling.readFromCSV(testFile);

        assertEquals(1, result.rows().size());
        assertEquals("数学作业", result.rows().get(0).get(0));
        assertEquals("数学", result.rows().get(0).get(1));
    }

    @Test
    @DisplayName("Read CSV with inconsistent column count throws CsvParseException")
    void testReadFromCSV_MalformedCSV() throws IOException {
        String content = "name,subject,duedate,completed\nShortRow,Subject\nFullRow,Subject,2024-12-25T10:00,false";
        Files.writeString(testFile, content);

        // FastCSV enforces consistent column counts and throws CsvParseException
        assertThrows(Exception.class, () -> CSVHandling.readFromCSV(testFile));
    }
}
