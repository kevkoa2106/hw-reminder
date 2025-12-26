package com.betterhomework.controllers;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import com.betterhomework.Application;
import com.betterhomework.Launcher;
import com.betterhomework.models.CsvData;
import com.betterhomework.models.Homework;
import com.betterhomework.service.CSVHandling;
import com.betterhomework.service.Reminder;
import com.dlsc.gemsfx.TimePicker;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    final private Path file = Paths.get("src/main/resources/com/betterhomework/data.csv");
    FXTrayIcon trayIcon;

    CsvData csvData;
    {
        try {
            csvData = CSVHandling.readFromCSV(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTrayIcon(FXTrayIcon icon) {
        this.trayIcon = icon;
    }

    @FXML
    public void changeCompleted() throws IOException {
        Homework selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setCompleted(true);
            table.refresh();
            CSVHandling.updateCompleted(file, selected.getName(), true);
        }
    }

    @FXML
    public void deleteSelectedHomework() throws IOException {
        Homework selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            CSVHandling.deleteRow(file, selected.getName());
        }
    }

    @FXML
    public void btnInsert() throws IOException {
        if (nameField.getText().isEmpty()) {
            trayIcon.showErrorMessage("Missing name", "Please type in a name for the homework due date.");
            return;
        }
        if (subjectField.getText().isEmpty()) {
            trayIcon.showErrorMessage("Missing subject", "Please type in a subject for the homework due date.");
            return;
        }
        if (dueDatePicker.getValue() == null) {
            trayIcon.showErrorMessage("Missing due date", "Please select a date for the homework due date.");
            return;
        }
        if (dateTimePicker.getValue() == null) {
            trayIcon.showErrorMessage("Missing time", "Please select a time for the homework due date.");
            return;
        }

        String name = nameField.getText();
        String subject = subjectField.getText();
        LocalDateTime dueDate = dueDatePicker.getValue().atTime(dateTimePicker.getTime());
        boolean completed = false;

        Homework newHomework = new Homework(name, subject, dueDate, completed);
        table.getItems().add(newHomework);
        nameField.clear();
        subjectField.clear();
        dueDatePicker.setValue(LocalDate.now());
        dateTimePicker.setValue(LocalTime.now());
        CSVHandling.writeToCSV(file, newHomework.getName(), newHomework.getSubject(), newHomework.getDueDate(), newHomework.isCompleted());

        trayIcon.showInfoMessage("Success", "Homework added successfully.");
    }

    @FXML
    public void switchToLight() {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    @FXML
    public void switchToDark() {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }

    private List<String> getRow(Integer i) {
        return csvData.rows().get(i);
    }

    @FXML
    public void initialize() throws IOException {
        dateTimePicker.setValue(LocalTime.now());
        dateTimePicker.setEditable(true);

        for (int i = 0; i < csvData.rows().size(); i++) {
            Homework savedHW = new Homework(getRow(i).get(0), getRow(i).get(1), LocalDateTime.parse(getRow(i).get(2)), Boolean.parseBoolean(getRow(i).get(3)));
            table.getItems().add(savedHW);
        }

        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        cCompleted.setCellValueFactory(new PropertyValueFactory<>("completed"));

        // Check for reminders every second
        Timeline reminderTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            Reminder.remind();
        }));
        reminderTimeline.setCycleCount(Timeline.INDEFINITE);
        reminderTimeline.play();
    }
}
