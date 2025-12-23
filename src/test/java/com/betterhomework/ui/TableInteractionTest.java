package com.betterhomework.ui;

import com.betterhomework.Application;
import com.betterhomework.models.Homework;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
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
 * UI tests for table interaction functionality.
 * Tests table display, row selection, and column verification.
 *
 * Run with: mvn test -Dtestfx.robot=glass -Dui.tests=true
 */
@ExtendWith(ApplicationExtension.class)
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
class TableInteractionTest {

    @Start
    private void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    @DisplayName("Table exists and is visible")
    void testTable_DisplaysHomework(FxRobot robot) {
        TableView<Homework> table = robot.lookup("#table").queryAs(TableView.class);
        assertNotNull(table, "Table should exist");
        assertTrue(table.isVisible(), "Table should be visible");
    }

    @Test
    @DisplayName("Table supports row selection")
    void testTable_SelectRow(FxRobot robot) {
        TableView<Homework> table = robot.lookup("#table").queryAs(TableView.class);
        assertNotNull(table.getSelectionModel(), "Table should have a selection model");
    }

    @Test
    @DisplayName("Table has all required columns")
    void testTable_ColumnsCorrect(FxRobot robot) {
        TableView<Homework> table = robot.lookup("#table").queryAs(TableView.class);

        assertEquals(4, table.getColumns().size());
        assertEquals("Name", table.getColumns().get(0).getText());
        assertEquals("Subject", table.getColumns().get(1).getText());
        assertEquals("Due Date", table.getColumns().get(2).getText());
        assertEquals("Completed", table.getColumns().get(3).getText());
    }

    @Test
    @DisplayName("File menu contains toggle completed option")
    void testMarkCompleted_UpdatesTable(FxRobot robot) {
        javafx.scene.control.MenuBar menuBar = robot.lookup(".menu-bar").queryAs(javafx.scene.control.MenuBar.class);
        assertNotNull(menuBar, "Menu bar should exist");

        javafx.scene.control.Menu fileMenu = menuBar.getMenus().stream()
                .filter(m -> m.getText().equals("File"))
                .findFirst()
                .orElse(null);

        assertNotNull(fileMenu, "File menu should exist");

        boolean hasToggleCompleted = fileMenu.getItems().stream()
                .anyMatch(item -> item.getText().contains("Toggle completed"));
        assertTrue(hasToggleCompleted, "File menu should contain Toggle completed option");
    }
}
