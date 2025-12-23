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
                headers = it.next().getFields(); // first row = headers
            }

            while (it.hasNext()) {
                rows.add(it.next().getFields()); // each row stays grouped
            }
        }

        return new CsvData(headers, rows);
    }

    public static void writeToCSV(Path file, String val1, String val2, LocalDateTime val3, Boolean val4) throws IOException {
        boolean fileExists = file.toFile().exists() && file.toFile().length() > 0;

        try (CsvWriter csv = CsvWriter.builder()
                .build(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            // Write header only if file is new/empty
            if (!fileExists) {
                csv.writeRecord("name", "subject", "duedate", "completed");
            }

            csv.writeRecord(val1, val2, val3.toString(), val4.toString());
        }
    }
}
