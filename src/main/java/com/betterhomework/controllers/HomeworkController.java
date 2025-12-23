package com.betterhomework.controllers;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import com.betterhomework.Application;
import com.betterhomework.models.Homework;
import com.betterhomework.service.CSVHandling;
import com.dlsc.gemsfx.TimePicker;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

public class HomeworkController {
    @FXML private TextField nameField;
    @FXML private TextField subjectField;
    @FXML private DatePicker dueDatePicker;
    @FXML private TimePicker dateTimePicker;
    @FXML private TableColumn<Homework, String> cName;
    @FXML private TableColumn<Homework, String> cSubject;
    @FXML private TableColumn<Homework, String> cDueDate;
    @FXML private TableColumn<Homework, Boolean> cCompleted;
    @FXML private TableView<Homework> table;

    final private Path file = Paths.get("/Users/khoa/Documents/code shi/Java/better-homework/src/main/resources/com/betterhomework/test.csv");

    @FXML
    private void btnInsert() throws IOException {
        if (nameField.getText().isEmpty()) {
            Toast.toast(ToastType.INFO, "Missing name", "Please select a time for the homework due date.");
            return;
        }
        if (subjectField.getText().isEmpty()) {
            Toast.toast(ToastType.INFO, "Missing subject", "Please select a time for the homework due date.");
            return;
        }
        if (dueDatePicker.getValue() == null) {
            Toast.toast(ToastType.INFO, "Missing date", "Please select a time for the homework due date.");
            return;
        }
        if (dateTimePicker.getValue() == null) {
            Toast.toast(ToastType.INFO, "Missing time", "Please select a time for the homework due date.");
            return;
        }

        Homework newHomework = new Homework(nameField.getText(), subjectField.getText(), dueDatePicker.getValue().atTime(dateTimePicker.getTime()), false);
        table.getItems().add(newHomework);
        nameField.clear();
        subjectField.clear();
        dueDatePicker.setValue(LocalDate.now());
        dateTimePicker.setValue(LocalTime.now());
        CSVHandling.writeToCSV(file, newHomework.getName(), newHomework.getSubject(), newHomework.getDueDate(), newHomework.isCompleted());

        Toast.toast(ToastType.INFO, "Success", "Homework added successfully.");
    }

    @FXML
    public void switchToLight() {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    @FXML
    public void switchToDark() {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }

    @FXML
    public void initialize() {
        dateTimePicker.setValue(LocalTime.now());
        dateTimePicker.setEditable(true);

        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        cCompleted.setCellValueFactory(new PropertyValueFactory<>("completed"));
    }
}
