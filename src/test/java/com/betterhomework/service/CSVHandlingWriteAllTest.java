package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlingWriteAllTest {

    @TempDir
    Path tempDir;

    private Path testFile;
    private List<String> headers;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test.csv");
        headers = Arrays.asList("name", "subject", "duedate", "completed");

        String initialContent = "oldheader1,oldheader2\nold1,old2\nold3,old4";
        Files.writeString(testFile, initialContent);
    }

    @Test
    @DisplayName("WriteAll overwrites entire file content")
    void testWriteAll_OverwritesFile() throws IOException {
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("New HW", "Math", "2024-12-25T10:00", "false"));

        CsvData newData = new CsvData(headers, rows);
        CSVHandling.writeAll(testFile, newData);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertEquals("name", result.headers().get(0));
        assertEquals(1, result.rows().size());
        assertEquals("New HW", result.rows().get(0).get(0));
    }

    @Test
    @DisplayName("WriteAll with empty data writes only headers")
    void testWriteAll_EmptyData() throws IOException {
        CsvData emptyData = new CsvData(headers, new ArrayList<>());
        CSVHandling.writeAll(testFile, emptyData);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(4, result.headers().size());
        assertTrue(result.rows().isEmpty());
    }

    @Test
    @DisplayName("WriteAll preserves row order")
    void testWriteAll_PreservesOrder() throws IOException {
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("First", "Math", "2024-12-25T10:00", "false"));
        rows.add(Arrays.asList("Second", "Science", "2024-12-26T10:00", "false"));
        rows.add(Arrays.asList("Third", "History", "2024-12-27T10:00", "true"));

        CsvData data = new CsvData(headers, rows);
        CSVHandling.writeAll(testFile, data);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(3, result.rows().size());
        assertEquals("First", result.rows().get(0).get(0));
        assertEquals("Second", result.rows().get(1).get(0));
        assertEquals("Third", result.rows().get(2).get(0));
    }

    @Test
    @DisplayName("WriteAll handles large dataset successfully")
    void testWriteAll_LargeDataset() throws IOException {
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            rows.add(Arrays.asList("HW" + i, "Subject" + i, "2024-12-25T10:00", "false"));
        }

        CsvData data = new CsvData(headers, rows);
        CSVHandling.writeAll(testFile, data);

        CsvData result = CSVHandling.readFromCSV(testFile);
        assertEquals(1000, result.rows().size());
        assertEquals("HW0", result.rows().get(0).get(0));
        assertEquals("HW999", result.rows().get(999).get(0));
    }
}
