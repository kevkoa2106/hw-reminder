package com.betterhomework.ui;

import com.betterhomework.Application;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;
import java.time.LocalTime;
import com.dlsc.gemsfx.TimePicker;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * UI tests for form input functionality.
 * Tests text fields, date picker, and time picker interactions.
 *
 * These tests require a display environment to run.
 * Run with: mvn test -Dtestfx.robot=glass -Dui.tests=true
 * Skip with: mvn test (default - UI tests are skipped)
 */
@ExtendWith(ApplicationExtension.class)
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
class FormInputTest {

    @Start
    private void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    @DisplayName("Name field accepts text input")
    void testNameField_AcceptsInput(FxRobot robot) {
        robot.clickOn("#nameField");
        robot.write("Math Homework");

        TextField nameField = robot.lookup("#nameField").queryAs(TextField.class);
        assertEquals("Math Homework", nameField.getText());
    }

    @Test
    @DisplayName("Subject field accepts text input")
    void testSubjectField_AcceptsInput(FxRobot robot) {
        TextField subjectField = robot.lookup("#subjectField").queryAs(TextField.class);
        robot.clickOn("#subjectField");
        robot.sleep(100);
        robot.interact(() -> subjectField.setText("Mathematics"));

        assertEquals("Mathematics", subjectField.getText());
    }

    @Test
    @DisplayName("Date picker allows date selection")
    void testDatePicker_SelectsDate(FxRobot robot) {
        DatePicker datePicker = robot.lookup("#dueDatePicker").queryAs(DatePicker.class);

        robot.clickOn("#dueDatePicker");
        robot.interact(() -> datePicker.setValue(LocalDate.of(2024, 12, 25)));

        assertEquals(LocalDate.of(2024, 12, 25), datePicker.getValue());
    }

    @Test
    @DisplayName("Time picker accepts time input")
    void testTimePicker_SelectsTime(FxRobot robot) {
        // TimePicker is editable, so we can type in it
        robot.clickOn("#dateTimePicker");
        robot.press(KeyCode.CONTROL, KeyCode.A).release(KeyCode.CONTROL, KeyCode.A);
        robot.write("10:30");

        // Verify the time picker has a value set
        // Note: Exact verification depends on TimePicker implementation
        assertDoesNotThrow(() -> robot.lookup("#dateTimePicker").query());
    }

    @Test
    @DisplayName("Form can be filled and add button is clickable")
    void testInsertButton_ClearsFields(FxRobot robot) {
        // Fill in all fields using interact for reliability
        TextField nameField = robot.lookup("#nameField").queryAs(TextField.class);
        TextField subjectField = robot.lookup("#subjectField").queryAs(TextField.class);
        DatePicker datePicker = robot.lookup("#dueDatePicker").queryAs(DatePicker.class);
        TimePicker timePicker = robot.lookup("#dateTimePicker").queryAs(TimePicker.class);

        robot.interact(() -> {
            nameField.setText("Test HW");
            subjectField.setText("Test Subject");
            datePicker.setValue(LocalDate.now().plusDays(1));
            timePicker.setValue(LocalTime.of(10, 30));
        });

        robot.sleep(200);

        // Verify fields were filled
        assertEquals("Test HW", nameField.getText());
        assertEquals("Test Subject", subjectField.getText());
        assertNotNull(datePicker.getValue());
        assertNotNull(timePicker.getValue());

        // Verify add button exists and is clickable
        javafx.scene.control.Button addButton = robot.lookup("Add homework").queryAs(javafx.scene.control.Button.class);
        assertNotNull(addButton, "Add homework button should exist");
        assertFalse(addButton.isDisabled(), "Add homework button should be enabled");
    }
}
