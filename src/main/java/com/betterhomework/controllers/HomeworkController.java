package com.betterhomework.controllers;

import com.betterhomework.models.Homework;
import com.dlsc.gemsfx.TimePicker;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeworkController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField subjectField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TimePicker dateTimePicker;
    @FXML
    private TableColumn<Homework, String> cName;
    @FXML
    private TableColumn<Homework, String> cSubject;
    @FXML
    private TableColumn<Homework, String> cDueDate;
    @FXML
    private TableColumn<Homework, Boolean> cCompleted;
    @FXML
    private TableView<Homework> table;


    @FXML
    private void btnInsert() {
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

        System.out.println(table.getItems().size());
        Toast.toast(ToastType.INFO, "Success", "Homework added successfully.");
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
