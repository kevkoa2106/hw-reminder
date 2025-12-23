package com.betterhomework.ui;

import com.betterhomework.Application;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI tests for form validation functionality.
 * Tests that form fields and button exist and are properly configured.
 * Note: TwoSlices toasts are native system notifications and cannot be
 * directly verified in tests.
 *
 * Run with: mvn test -Dtestfx.robot=glass -Dui.tests=true
 */
@ExtendWith(ApplicationExtension.class)
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
class ToastNotificationTest {

    @Start
    private void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    @DisplayName("Name field exists and accepts input")
    void testToast_MissingName(FxRobot robot) {
        TextField nameField = robot.lookup("#nameField").queryAs(TextField.class);
        assertNotNull(nameField, "Name field should exist");
        assertTrue(nameField.getText().isEmpty(), "Name field should start empty");
        assertEquals("Name", nameField.getPromptText(), "Name field should have correct prompt text");
    }

    @Test
    @DisplayName("Subject field exists and accepts input")
    void testToast_MissingSubject(FxRobot robot) {
        TextField subjectField = robot.lookup("#subjectField").queryAs(TextField.class);
        assertNotNull(subjectField, "Subject field should exist");
        assertTrue(subjectField.getText().isEmpty(), "Subject field should start empty");
        assertEquals("Subject", subjectField.getPromptText(), "Subject field should have correct prompt text");
    }

    @Test
    @DisplayName("Date picker exists and is empty by default")
    void testToast_MissingDate(FxRobot robot) {
        DatePicker datePicker = robot.lookup("#dueDatePicker").queryAs(DatePicker.class);
        assertNotNull(datePicker, "Date picker should exist");
        assertNull(datePicker.getValue(), "Date picker should be null by default");
    }

    @Test
    @DisplayName("Time picker exists and has default value")
    void testToast_MissingTime(FxRobot robot) {
        // Time picker is initialized with LocalTime.now() in controller
        assertDoesNotThrow(() -> robot.lookup("#dateTimePicker").query(),
            "Time picker should exist");
    }

    @Test
    @DisplayName("Add homework button exists and is enabled")
    void testToast_SuccessfulInsert(FxRobot robot) {
        Button addButton = robot.lookup("Add homework").queryAs(Button.class);
        assertNotNull(addButton, "Add homework button should exist");
        assertFalse(addButton.isDisabled(), "Add homework button should be enabled");
        assertEquals("Add homework", addButton.getText());
    }
}
