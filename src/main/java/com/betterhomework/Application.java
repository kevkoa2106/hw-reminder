package com.betterhomework;

import atlantafx.base.theme.CupertinoLight;
import com.betterhomework.controllers.HomeworkController;
import com.betterhomework.service.Reminder;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("homework-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Application.class.getResource("atlantafx.css").toExternalForm());
        stage.setTitle("HW reminder/tracker");
        stage.setScene(scene);

        HomeworkController controller = fxmlLoader.getController();

        URL iconURL = Application.class.getResource("icons/aplus.png");

        FXTrayIcon icon = new FXTrayIcon.Builder(stage, iconURL)
                .menuItem("Open window", e -> stage.show())
                .menuItem("Minimize window", e -> stage.hide())
                .addExitMenuItem("Quit")
                .build();

        controller.setTrayIcon(icon);
        Reminder.setTrayIcon(icon);

        icon.show();
        stage.show();
    }
}
