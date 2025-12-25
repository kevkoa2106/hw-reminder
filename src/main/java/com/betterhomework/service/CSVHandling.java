package com.betterhomework.service;

import com.betterhomework.models.CsvData;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVHandling {
    public static CsvData readFromCSV(Path file) throws IOException {
        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try (CsvReader<CsvRecord> csv = CsvReader.builder().ofCsvRecord(file)) {
            Iterator<CsvRecord> it = csv.iterator();

            if (it.hasNext()) {
                headers = new ArrayList<>(it.next().getFields());
            }

            while (it.hasNext()) {
                rows.add(new ArrayList<>(it.next().getFields()));
            }
        }

        return new CsvData(headers, rows);
    }

    public static void writeToCSV(Path file, String val1, String val2, LocalDateTime val3, Boolean val4) throws IOException {
        boolean fileExists = file.toFile().exists() && file.toFile().length() > 0;

        try (CsvWriter csv = CsvWriter.builder()
                .build(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            if (!fileExists) {
                csv.writeRecord("name", "subject", "duedate", "completed");
            }

            csv.writeRecord(val1, val2, val3.toString(), val4.toString());
        }
    }

    public static void updateCompleted(Path file, String homeworkName, boolean completed) throws IOException {
        CsvData data = readFromCSV(file);

        int completedCol = data.headers().indexOf("completed");
        int nameCol = data.headers().indexOf("name");

        for (List<String> row : data.rows()) {
            if (row.get(nameCol).equals(homeworkName)) {
                row.set(completedCol, String.valueOf(completed));
                break;
            }
        }

        writeAll(file, data);
    }

    public static void writeAll(Path file, CsvData data) throws IOException {
        try (CsvWriter writer = CsvWriter.builder().build(file)) {
            writer.writeRecord(data.headers());
            for (List<String> row : data.rows()) {
                writer.writeRecord(row);
            }
        }
    }

    public static void deleteRow(Path file, String homeworkName) throws IOException {
        CsvData data = readFromCSV(file);
        int nameCol = data.headers().indexOf("name");
        data.rows().removeIf(row -> row.get(nameCol).equals(homeworkName));
        writeAll(file, data);
    }
}
