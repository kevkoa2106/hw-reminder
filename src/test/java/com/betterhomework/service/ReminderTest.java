package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Reminder service logic.
 * Since the Reminder service depends on FXTrayIcon (JavaFX component),
 * these tests focus on the reminder eligibility logic without UI dependencies.
 */
class ReminderTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("reminder_test.csv");
    }

    /**
     * Helper method that mirrors the reminder eligibility check from Reminder.remind()
     */
    private boolean isReminderEligible(LocalDateTime dueDate, Set<String> notifiedHomework, String name) {
        if (notifiedHomework.contains(name)) {
            return false;
        }
        return Duration.between(LocalDateTime.now(), dueDate).toDays() <= 1;
    }

    @Test
    @DisplayName("Homework due within 1 day is eligible for reminder")
    void testReminderEligible_DueWithinOneDay() {
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(20);
        Set<String> notified = new HashSet<>();

        assertTrue(isReminderEligible(tomorrow, notified, "Math HW"));
    }

    @Test
    @DisplayName("Homework due in more than 1 day is not eligible")
    void testReminderNotEligible_DueInMoreThanOneDay() {
        LocalDateTime farFuture = LocalDateTime.now().plusDays(5);
        Set<String> notified = new HashSet<>();

        assertFalse(isReminderEligible(farFuture, notified, "Math HW"));
    }

    @Test
    @DisplayName("Already notified homework is not eligible again")
    void testReminderNotEligible_AlreadyNotified() {
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(20);
        Set<String> notified = new HashSet<>();
        notified.add("Math HW");

        assertFalse(isReminderEligible(tomorrow, notified, "Math HW"));
    }

    @Test
    @DisplayName("Homework due exactly in 1 day is eligible")
    void testReminderEligible_ExactlyOneDay() {
        LocalDateTime exactlyOneDay = LocalDateTime.now().plusDays(1);
        Set<String> notified = new HashSet<>();

        assertTrue(isReminderEligible(exactlyOneDay, notified, "Math HW"));
    }

    @Test
    @DisplayName("Overdue homework (past due date) is eligible")
    void testReminderEligible_Overdue() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Set<String> notified = new HashSet<>();

        // Duration.between returns negative for past dates, so toDays() <= 1 will be true
        assertTrue(isReminderEligible(yesterday, notified, "Overdue HW"));
    }

    @Test
    @DisplayName("Homework due in exactly 2 days is not eligible")
    void testReminderNotEligible_TwoDays() {
        LocalDateTime twoDays = LocalDateTime.now().plusDays(2).plusMinutes(1);
        Set<String> notified = new HashSet<>();

        assertFalse(isReminderEligible(twoDays, notified, "Math HW"));
    }

    @Test
    @DisplayName("Multiple homeworks can be eligible independently")
    void testMultipleHomeworksEligible() {
        LocalDateTime soon = LocalDateTime.now().plusHours(12);
        LocalDateTime later = LocalDateTime.now().plusDays(3);
        Set<String> notified = new HashSet<>();

        assertTrue(isReminderEligible(soon, notified, "Soon HW"));
        assertFalse(isReminderEligible(later, notified, "Later HW"));

        // After notifying the first one
        notified.add("Soon HW");
        assertFalse(isReminderEligible(soon, notified, "Soon HW"));
    }

    @Test
    @DisplayName("CSV file with homework entries can be parsed for reminders")
    void testCSVDataForReminders() throws IOException {
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(20);
        LocalDateTime nextWeek = LocalDateTime.now().plusDays(7);

        String content = "name,subject,duedate,completed\n" +
                "Soon HW,Math," + tomorrow.toString() + ",false\n" +
                "Later HW,Science," + nextWeek.toString() + ",false";
        Files.writeString(testFile, content);

        CsvData data = CSVHandling.readFromCSV(testFile);
        Set<String> notified = new HashSet<>();

        assertEquals(2, data.rows().size());

        // Check first row (due soon)
        String name1 = data.rows().get(0).get(0);
        LocalDateTime dueDate1 = LocalDateTime.parse(data.rows().get(0).get(2));
        assertTrue(isReminderEligible(dueDate1, notified, name1));

        // Check second row (due later)
        String name2 = data.rows().get(1).get(0);
        LocalDateTime dueDate2 = LocalDateTime.parse(data.rows().get(1).get(2));
        assertFalse(isReminderEligible(dueDate2, notified, name2));
    }
}