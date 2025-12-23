package com.betterhomework.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkTest {

    private LocalDateTime testDateTime;
    private Homework homework;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2024, 12, 25, 10, 30);
        homework = new Homework("Math HW", "Mathematics", testDateTime, false);
    }

    // Constructor Tests

    @Test
    @DisplayName("Constructor sets all fields correctly with valid inputs")
    void testConstructor_ValidInputs() {
        assertEquals("Math HW", homework.getName());
        assertEquals("Mathematics", homework.getSubject());
        assertEquals(testDateTime, homework.getDueDate());
        assertFalse(homework.isCompleted());
    }

    @Test
    @DisplayName("Constructor accepts null name")
    void testConstructor_NullName() {
        Homework hw = new Homework(null, "Subject", testDateTime, false);
        assertNull(hw.getName());
    }

    @Test
    @DisplayName("Constructor accepts empty strings")
    void testConstructor_EmptyStrings() {
        Homework hw = new Homework("", "", testDateTime, false);
        assertEquals("", hw.getName());
        assertEquals("", hw.getSubject());
    }

    // Setter Tests

    @Test
    @DisplayName("setCompleted changes true to false")
    void testSetCompleted_TrueToFalse() {
        Homework hw = new Homework("HW", "Sub", testDateTime, true);
        assertTrue(hw.isCompleted());
        hw.setCompleted(false);
        assertFalse(hw.isCompleted());
    }

    @Test
    @DisplayName("setCompleted changes false to true")
    void testSetCompleted_FalseToTrue() {
        assertFalse(homework.isCompleted());
        homework.setCompleted(true);
        assertTrue(homework.isCompleted());
    }

    // Equals Tests

    @Test
    @DisplayName("Equals returns true for objects with same values")
    void testEquals_SameValues() {
        Homework hw1 = new Homework("HW", "Sub", testDateTime, false);
        Homework hw2 = new Homework("HW", "Sub", testDateTime, false);
        assertEquals(hw1, hw2);
    }

    @Test
    @DisplayName("Equals returns false for different name")
    void testEquals_DifferentName() {
        Homework hw1 = new Homework("HW1", "Sub", testDateTime, false);
        Homework hw2 = new Homework("HW2", "Sub", testDateTime, false);
        assertNotEquals(hw1, hw2);
    }

    @Test
    @DisplayName("Equals returns false for different subject")
    void testEquals_DifferentSubject() {
        Homework hw1 = new Homework("HW", "Math", testDateTime, false);
        Homework hw2 = new Homework("HW", "Science", testDateTime, false);
        assertNotEquals(hw1, hw2);
    }

    @Test
    @DisplayName("Equals returns false for different due date")
    void testEquals_DifferentDueDate() {
        LocalDateTime otherDate = testDateTime.plusDays(1);
        Homework hw1 = new Homework("HW", "Sub", testDateTime, false);
        Homework hw2 = new Homework("HW", "Sub", otherDate, false);
        assertNotEquals(hw1, hw2);
    }

    @Test
    @DisplayName("Equals returns false for different completed status")
    void testEquals_DifferentCompleted() {
        Homework hw1 = new Homework("HW", "Sub", testDateTime, false);
        Homework hw2 = new Homework("HW", "Sub", testDateTime, true);
        assertNotEquals(hw1, hw2);
    }

    @Test
    @DisplayName("Equals returns false when compared to null")
    void testEquals_Null() {
        assertNotEquals(null, homework);
    }

    @Test
    @DisplayName("Equals returns false when compared to different class")
    void testEquals_DifferentClass() {
        assertNotEquals("String object", homework);
    }

    // HashCode Tests

    @Test
    @DisplayName("HashCode is consistent across multiple calls")
    void testHashCode_Consistency() {
        int hash1 = homework.hashCode();
        int hash2 = homework.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Equal objects have same hashCode")
    void testHashCode_EqualObjects() {
        Homework hw1 = new Homework("HW", "Sub", testDateTime, false);
        Homework hw2 = new Homework("HW", "Sub", testDateTime, false);
        assertEquals(hw1.hashCode(), hw2.hashCode());
    }
}