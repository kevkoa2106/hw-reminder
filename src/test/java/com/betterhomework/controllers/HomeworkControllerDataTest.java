package com.betterhomework.controllers;

import com.betterhomework.models.Homework;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for data transformation logic used in HomeworkController.
 * These tests verify how form data is converted to Homework objects
 * and how CSV rows are parsed.
 */
class HomeworkControllerDataTest {

    // Helper method that mirrors controller's homework creation logic
    private Homework createHomeworkFromForm(String name, String subject, LocalDate date, LocalTime time) {
        LocalDateTime dueDate = date.atTime(time);
        return new Homework(name, subject, dueDate, false);
    }

    // Helper method that mirrors controller's row parsing logic (from initialize())
    private Homework parseRowToHomework(List<String> row) {
        return new Homework(
                row.get(0),
                row.get(1),
                LocalDateTime.parse(row.get(2)),
                Boolean.parseBoolean(row.get(3))
        );
    }

    @Test
    @DisplayName("Create Homework object from form data")
    void testCreateHomework_FromFormData() {
        String name = "Math HW";
        String subject = "Mathematics";
        LocalDate date = LocalDate.of(2024, 12, 25);
        LocalTime time = LocalTime.of(10, 30);

        Homework homework = createHomeworkFromForm(name, subject, date, time);

        assertEquals("Math HW", homework.getName());
        assertEquals("Mathematics", homework.getSubject());
        assertEquals(LocalDateTime.of(2024, 12, 25, 10, 30), homework.getDueDate());
        assertFalse(homework.isCompleted());
    }

    @Test
    @DisplayName("Date and time are combined correctly into LocalDateTime")
    void testDateTimeCombination() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        LocalTime time = LocalTime.of(14, 45, 30);

        LocalDateTime combined = date.atTime(time);

        assertEquals(2024, combined.getYear());
        assertEquals(6, combined.getMonthValue());
        assertEquals(15, combined.getDayOfMonth());
        assertEquals(14, combined.getHour());
        assertEquals(45, combined.getMinute());
        assertEquals(30, combined.getSecond());
    }

    @Test
    @DisplayName("Parse valid CSV row to Homework object")
    void testRowParsing_ValidRow() {
        List<String> row = Arrays.asList("Math HW", "Mathematics", "2024-12-25T10:30", "false");

        Homework homework = parseRowToHomework(row);

        assertEquals("Math HW", homework.getName());
        assertEquals("Mathematics", homework.getSubject());
        assertEquals(LocalDateTime.of(2024, 12, 25, 10, 30), homework.getDueDate());
        assertFalse(homework.isCompleted());
    }

    @Test
    @DisplayName("Parse row with invalid date throws DateTimeParseException")
    void testRowParsing_InvalidDate() {
        List<String> row = Arrays.asList("Math HW", "Mathematics", "invalid-date", "false");

        assertThrows(DateTimeParseException.class, () -> parseRowToHomework(row));
    }

    @Test
    @DisplayName("Parse row with invalid boolean treats it as false")
    void testRowParsing_InvalidBoolean() {
        List<String> row = Arrays.asList("Math HW", "Mathematics", "2024-12-25T10:30", "notabool");

        Homework homework = parseRowToHomework(row);

        // Boolean.parseBoolean returns false for any non-"true" string
        assertFalse(homework.isCompleted());
    }
}
