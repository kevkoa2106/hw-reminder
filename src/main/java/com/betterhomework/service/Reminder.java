package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Reminder {
    private static final Path file = Paths.get("src/main/resources/com/betterhomework/data.csv");

    private static final Set<String> notifiedHomework = new HashSet<>();

    static FXTrayIcon trayIcon;
    public static void setTrayIcon(FXTrayIcon icon) {
        trayIcon = icon;
    }

    public static void remind() {
        try {
            CsvData csvData = CSVHandling.readFromCSV(file);

            for (var row : csvData.rows()) {
                String name = row.get(0);
                LocalDateTime dueDate = LocalDateTime.parse(row.get(2));

                if (notifiedHomework.contains(name)) {
                    continue;
                }

                if (Duration.between(LocalDateTime.now(), dueDate).toDays() <= 1) {
                    trayIcon.showWarningMessage("Reminder", "You have " + name + " due soon!");
                    notifiedHomework.add(name);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
