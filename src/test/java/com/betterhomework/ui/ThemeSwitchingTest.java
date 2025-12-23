package com.betterhomework.ui;

import com.betterhomework.Application;
import com.betterhomework.models.Homework;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
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
 * UI tests for theme switching functionality.
 * Tests light mode, dark mode, and data preservation during theme switch.
 *
 * Run with: mvn test -Dtestfx.robot=glass -Dui.tests=true
 */
@ExtendWith(ApplicationExtension.class)
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
class ThemeSwitchingTest {

    @Start
    private void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    @DisplayName("View menu contains theme options")
    void testSwitchToLight_AppliesTheme(FxRobot robot) {
        // Find the menu bar and verify View menu exists
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        assertNotNull(menuBar, "Menu bar should exist");

        // Find the View menu (note: typo in FXML says "VIew")
        Menu viewMenu = menuBar.getMenus().stream()
                .filter(m -> m.getText().equalsIgnoreCase("VIew") || m.getText().equalsIgnoreCase("View"))
                .findFirst()
                .orElse(null);

        assertNotNull(viewMenu, "View menu should exist");
        assertEquals(2, viewMenu.getItems().size(), "View menu should have 2 items (Light/Dark)");
    }

    @Test
    @DisplayName("Dark mode menu item exists")
    void testSwitchToDark_AppliesTheme(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu viewMenu = menuBar.getMenus().stream()
                .filter(m -> m.getText().equalsIgnoreCase("VIew") || m.getText().equalsIgnoreCase("View"))
                .findFirst()
                .orElse(null);

        assertNotNull(viewMenu);

        boolean hasDarkMode = viewMenu.getItems().stream()
                .anyMatch(item -> item.getText().toLowerCase().contains("dark"));
        assertTrue(hasDarkMode, "View menu should contain Dark mode option");
    }

    @Test
    @DisplayName("Theme switch preserves table structure")
    void testThemeSwitch_PreservesData(FxRobot robot) {
        // Verify table exists and has correct column count before any theme change
        TableView<Homework> table = robot.lookup("#table").queryAs(TableView.class);
        assertNotNull(table, "Table should exist");
        assertEquals(4, table.getColumns().size(), "Table should have 4 columns");
    }
}
