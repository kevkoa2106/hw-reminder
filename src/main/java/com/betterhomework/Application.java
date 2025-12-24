package com.betterhomework;

import atlantafx.base.theme.CupertinoLight;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("homework-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Application.class.getResource("atlantafx.css").toExternalForm());
        stage.setTitle("HW reminder/tracker");
        stage.setScene(scene);
        stage.show();
    }
}
