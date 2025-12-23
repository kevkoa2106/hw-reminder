package com.betterhomework.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for validation logic used in HomeworkController.
 * These tests verify the validation conditions without requiring JavaFX UI components.
 */
class HomeworkControllerValidationTest {

    // Validation helper methods that mirror controller logic
    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }

    private boolean isSubjectValid(String subject) {
        return subject != null && !subject.isEmpty();
    }

    private boolean isDateValid(LocalDate date) {
        return date != null;
    }

    private boolean isTimeValid(LocalTime time) {
        return time != null;
    }

    private boolean isFormValid(String name, String subject, LocalDate date, LocalTime time) {
        return isNameValid(name) && isSubjectValid(subject) && isDateValid(date) && isTimeValid(time);
    }

    @Test
    @DisplayName("Validation fails when name is empty")
    void testValidation_MissingName() {
        String name = "";
        String subject = "Mathematics";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        assertFalse(isNameValid(name));
        assertFalse(isFormValid(name, subject, date, time));
    }

    @Test
    @DisplayName("Validation fails when subject is empty")
    void testValidation_MissingSubject() {
        String name = "Math HW";
        String subject = "";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        assertFalse(isSubjectValid(subject));
        assertFalse(isFormValid(name, subject, date, time));
    }

    @Test
    @DisplayName("Validation fails when date is null")
    void testValidation_MissingDate() {
        String name = "Math HW";
        String subject = "Mathematics";
        LocalDate date = null;
        LocalTime time = LocalTime.now();

        assertFalse(isDateValid(date));
        assertFalse(isFormValid(name, subject, date, time));
    }

    @Test
    @DisplayName("Validation fails when time is null")
    void testValidation_MissingTime() {
        String name = "Math HW";
        String subject = "Mathematics";
        LocalDate date = LocalDate.now();
        LocalTime time = null;

        assertFalse(isTimeValid(time));
        assertFalse(isFormValid(name, subject, date, time));
    }

    @Test
    @DisplayName("Validation passes when all fields are valid")
    void testValidation_AllFieldsValid() {
        String name = "Math HW";
        String subject = "Mathematics";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        assertTrue(isNameValid(name));
        assertTrue(isSubjectValid(subject));
        assertTrue(isDateValid(date));
        assertTrue(isTimeValid(time));
        assertTrue(isFormValid(name, subject, date, time));
    }

    @Test
    @DisplayName("Validation behavior with whitespace-only name")
    void testValidation_WhitespaceOnlyName() {
        String name = "   ";
        String subject = "Mathematics";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Current implementation does not trim - whitespace is considered valid
        // This documents the current behavior
        assertTrue(isNameValid(name));
        assertTrue(isFormValid(name, subject, date, time));
    }
}
