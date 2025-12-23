module com.betterhomework {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.gemsfx;
    requires com.sshtools.twoslices;

    opens com.betterhomework to javafx.fxml;
    exports com.betterhomework;
    exports com.betterhomework.controllers;
    opens com.betterhomework.controllers to javafx.fxml;
    opens com.betterhomework.models to javafx.base, javafx.fxml;
}