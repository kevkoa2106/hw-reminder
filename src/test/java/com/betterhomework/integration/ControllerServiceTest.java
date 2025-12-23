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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Controller-Service interactions.
 * Tests the flow of data between controller logic and CSV service.
 */
class ControllerServiceTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("controller_service.csv");
    }

    // Simulates controller's btnInsert logic
    private void simulateInsertHomework(Path file, String name, String subject, LocalDateTime dueDate) throws IOException {
        // Validation (from controller)
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name required");
        if (subject == null || subject.isEmpty()) throw new IllegalArgumentException("Subject required");
        if (dueDate == null) throw new IllegalArgumentException("Due date required");

        // Persist (via service)
        CSVHandling.writeToCSV(file, name, subject, dueDate, false);
    }

    // Simulates controller's changeCompleted logic
    private void simulateMarkCompleted(Path file, Homework homework) throws IOException {
        if (homework != null) {
            CSVHandling.updateCompleted(file, homework.getName(), true);
        }
    }

    // Simulates controller's initialize logic - loads CSV into list
    private List<Homework> simulateLoadHomeworks(Path file) throws IOException {
        List<Homework> homeworks = new ArrayList<>();
        CsvData data = CSVHandling.readFromCSV(file);

        for (List<String> row : data.rows()) {
            Homework hw = new Homework(
                    row.get(0),
                    row.get(1),
                    LocalDateTime.parse(row.get(2)),
                    Boolean.parseBoolean(row.get(3))
            );
            homeworks.add(hw);
        }
        return homeworks;
    }

    @Test
    @DisplayName("Insert homework persists to CSV file")
    void testInsertHomework_PersistsToFile() throws IOException {
        LocalDateTime dueDate = LocalDateTime.of(2024, 12, 25, 10, 30);

        simulateInsertHomework(testFile, "Math HW", "Mathematics", dueDate);

        // Verify file exists and contains data
        assertTrue(Files.exists(testFile));
        CsvData data = CSVHandling.readFromCSV(testFile);
        assertEquals(1, data.rows().size());
        assertEquals("Math HW", data.rows().get(0).get(0));
        assertEquals("Mathematics", data.rows().get(0).get(1));
        assertEquals("false", data.rows().get(0).get(3));
    }

    @Test
    @DisplayName("Mark completed updates CSV file")
    void testMarkCompleted_UpdatesFile() throws IOException {
        LocalDateTime dueDate = LocalDateTime.of(2024, 12, 25, 10, 30);

        // Insert homework
        simulateInsertHomework(testFile, "Science HW", "Science", dueDate);

        // Create homework object (as if selected from table)
        Homework selected = new Homework("Science HW", "Science", dueDate, false);

        // Mark completed
        simulateMarkCompleted(testFile, selected);

        // Verify file reflects the change
        CsvData data = CSVHandling.readFromCSV(testFile);
        assertEquals("true", data.rows().get(0).get(3));
    }

    @Test
    @DisplayName("Initialize loads existing data from CSV")
    void testInitialize_LoadsFromFile() throws IOException {
        // Pre-populate CSV file
        String content = "name,subject,duedate,completed\n" +
                "HW1,Math,2024-12-20T09:00,false\n" +
                "HW2,Science,2024-12-21T10:00,true\n" +
                "HW3,History,2024-12-22T11:00,false";
        Files.writeString(testFile, content);

        // Simulate controller initialization
        List<Homework> homeworks = simulateLoadHomeworks(testFile);

        assertEquals(3, homeworks.size());
        assertEquals("HW1", homeworks.get(0).getName());
        assertEquals("Math", homeworks.get(0).getSubject());
        assertFalse(homeworks.get(0).isCompleted());

        assertEquals("HW2", homeworks.get(1).getName());
        assertTrue(homeworks.get(1).isCompleted());

        assertEquals("HW3", homeworks.get(2).getName());
        assertFalse(homeworks.get(2).isCompleted());
    }

    @Test
    @DisplayName("Initialize handles empty CSV gracefully")
    void testInitialize_EmptyFile() throws IOException {
        // Create empty CSV with only headers
        String content = "name,subject,duedate,completed";
        Files.writeString(testFile, content);

        // Simulate controller initialization
        List<Homework> homeworks = simulateLoadHomeworks(testFile);

        assertTrue(homeworks.isEmpty());
    }
}
